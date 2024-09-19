FROM eclipse-temurin:20 AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"
# docker build -t morfeu-webapp:latest --build-arg PROXY='http://192.168.1.30:3128/' --build-arg PROXY_HOST=192.168.1.30 --build-arg PROXY_PORT=3128 .

# variables build stage
ARG MORFEU_VERSION=v0.8.20
ARG MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
ENV MORFEU_VERSION=${MORFEU_VERSION}
ENV MAVEN_HOME /usr/share/maven

# install dependencies (bash to launch angular build, ncurses for pretty output with tput, git for npm deps)
# notice that ts-node is not installed in usr/local/bin, so we make an alias as this is where snowpackage expects it
RUN apk add --no-cache curl bash ncurses git
RUN apk add --no-cache --update nodejs npm
RUN npm install -g @angular/cli typescript ts-node
RUN ln -s /usr/bin/ts-node /usr/local/bin/ts-node

# install maven
RUN mkdir -p ${MAVEN_HOME}
RUN curl ${MAVEN_URL} | tar zxf - -C ${MAVEN_HOME} --strip-components 1
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# checkout and build morfeu dependency, avoid building client as we do not need it for the java dependency
#RUN echo 'Using maven options ${MAVEN_OPTS}'
RUN git clone https://github.com/danigiri/morfeu.git && \
	cd morfeu && \
	git -c advice.detachedHead=false checkout ${MORFEU_VERSION} && \
	mkdir -p target/dist && \
	/usr/bin/mvn compile war:war package install \
	-DarchiveClasses=true -DattachClasses=true -DskipITs -Djetty.skip -Dskip-build-client=true

# we add the pom and code
COPY pom.xml pom.xml
RUN /usr/bin/mvn dependency:go-offline

# cache some node stuff to speed up builds
COPY src/main/angular/*.json /cache/
COPY src/main/angular/*.js /cache/
RUN cd /cache/ && npm install

# add code
COPY src src

# and build (two steps to reuse the lengthy maven download)
RUN /usr/bin/mvn compile 
RUN cp -r /cache/node_modules /src/main/angular/node_modules
RUN /usr/bin/mvn test war:war package
#RUN echo 'build finished'


FROM eclipse-temurin:20 AS main

# arguments and variables run stage
ENV JETTY_HOME /var/lib/jetty
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-home/12.0.11/jetty-home-12.0.11.tar.gz
ARG JETTY_BASE=/jetty-base

# install bash, typescript, node ts-node to be able to use it to parse JS code
RUN apk add --no-cache --update bash curl
# this installs node and npm (using the 'n' package manager)
RUN curl -fsSL https://raw.githubusercontent.com/tj/n/master/bin/n | bash -s lts
# we are using the built-in ts-node now
# notice that ts-node is not installed in usr/local/bin, so we make an alias as this is where snowpackage expects it
# RUN npm install -g typescript ts-node
# RUN ln -s /usr/bin/ts-node /usr/local/bin/ts-node

RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# create jetty-base folder and add the jetty configuration and folder structure
COPY --from=build ./target/classes/jetty ${JETTY_BASE}
RUN mkdir -p ${JETTY_BASE}/webapps ${JETTY_BASE}/resources ${JETTY_BASE}/lib/ext
COPY --from=build ./target/classes/jetty-logging.properties /${JETTY_BASE}/resources
# uncomment to create logs folder if we want to persist them (also enable the module, renaming it from .disabled)
# RUN mkdir -p ${JETTY_BASE}/logs


# add war
COPY --from=build ./target/snow-package-*.war ${JETTY_BASE}/webapps/root.war

# add typescript code
RUN mkdir -p ${JETTY_HOME}/src/main/angular
COPY --from=build ./src/main/angular ${JETTY_HOME}/src/main/angular

# add test data
RUN mkdir -p ${JETTY_HOME}/target/test-classes/test-resources
COPY --from=build ./target/test-classes/test-resources ${JETTY_HOME}/target/test-classes/test-resources

# start jetty from its base folder (uncomment the scan interval when testing), this way of starting it means
# we do not do a fork of the java process to run jetty, and also means ENV vars (like __RESOURCES_PREFIX) will be
# received
WORKDIR ${JETTY_BASE}
ENTRYPOINT sh -c "$(java -jar ${JETTY_HOME}/start.jar jetty.deploy.scanInterval=1 --dry-run)"
