FROM openjdk:13-alpine AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"
# docker build -t morfeu-webapp:latest --build-arg PROXY='http://192.168.1.30:3128/' --build-arg PROXY_HOST=192.168.1.30 --build-arg PROXY_PORT=3128 .

# variables build stage
ARG MORFEU_VERSION=0.7.0
ARG MAVEN_URL=https://apache.brunneis.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
ARG MAVEN_OPTS=
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
RUN echo 'Using maven options ${MAVEN_OPTS}'
RUN git clone https://github.com/danigiri/morfeu.git && git -c advice.detachedHead=false checkout ${MORFEU_VERSION}
RUN cd morfeu && mkdir -p target/dist && \
	/usr/bin/mvn compile war:war package install \
	-DarchiveClasses=true -DattachClasses=true -DskipITs -Djetty.skip -Dskip-build-client=true ${MAVEN_OPTS}

# we add the pom and code
COPY pom.xml pom.xml
RUN /usr/bin/mvn dependency:go-offline ${MAVEN_OPTS}
COPY src src

# and build (two steps to reuse the lengthy maven download)
RUN echo 'Using maven options ${MAVEN_OPTS}'
RUN /usr/bin/mvn compile ${MAVEN_OPTS}
RUN cd src/main/angular && npm install
RUN /usr/bin/mvn test war:war package ${MAVEN_OPTS}
RUN echo 'build finished'


FROM openjdk:13-alpine AS main

# variables run stage
ARG VERSION=0.0.2-SNAPSHOT
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.24.v20191120/jetty-distribution-9.4.24.v20191120.tar.gz
ENV JETTY_HOME /var/lib/jetty
ARG JETTY_BASE=/jetty-base

# install bash, typescript, node ts-node to be able to use it to parse JS code
# notice that ts-node is not installed in usr/local/bin, so we make an alias as this is where snowpackage expects it
RUN apk add --no-cache --update bash nodejs npm
RUN npm install -g typescript ts-node
RUN ln -s /usr/bin/ts-node /usr/local/bin/ts-node

RUN apk add --no-cache curl
RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# create jetty-base folder and add the configuration
RUN mkdir -p ${JETTY_BASE}/webapps ${JETTY_BASE}/logs
COPY --from=build ./morfeu/target/classes/jetty /jetty-base

# add war
COPY --from=build ./target/snow-package-app-${VERSION}.war ${JETTY_BASE}/webapps/root.war

# add typescript code
RUN mkdir -p ${JETTY_HOME}/src/main/angular
COPY --from=build ./src/main/angular ${JETTY_HOME}/src/main/angular

# add test data
RUN mkdir -p ${JETTY_HOME}/target/test-classes/test-resources
COPY --from=build ./target/test-classes/test-resources ${JETTY_HOME}/target/test-classes/test-resources

# start (notice we override the default port from morfeu)
WORKDIR ${JETTY_HOME}
ENTRYPOINT ["java", "-jar", "./start.jar", "jetty.base=/jetty-base", "--module=http", "jetty.http.port=8990"]
