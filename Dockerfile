FROM openjdk:13-alpine AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"

# variables build stage
ARG MORFEU_VERSION=0.6.2
ARG MAVEN_URL=https://apache.brunneis.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
ARG MAVEN_OPTS=
ENV MAVEN_HOME /usr/share/maven

# install dependencies (bash to launch angular build, ncurses for pretty output with tput, git for npm deps)
RUN apk add --no-cache curl bash ncurses git
RUN apk add --no-cache --update nodejs npm
RUN npm install -g @angular/cli typescript ts-node

# install maven
RUN mkdir -p ${MAVEN_HOME}
RUN curl ${MAVEN_URL} | tar zxf - -C ${MAVEN_HOME} --strip-components 1
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# checkout and build morfeu dependency, avoid building client as we do not need it for the java dependency
RUN git clone https://github.com/danigiri/morfeu.git
RUN cd morfeu && git -c advice.detachedHead=false checkout ${MORFEU_VERSION} \
	&& mvn package install -DskipITs -DskipTests=true -Djetty.skip -Dbuild-client=false ${MAVEN_OPTS}

# we add the pom and code
COPY pom.xml pom.xml
COPY src src

# and build (two steps to reuse the lengthy maven download)
RUN /usr/bin/mvn compile ${MAVEN_OPTS}
RUN /usr/bin/mvn test package ${MAVEN_OPTS}


FROM openjdk:13-alpine AS main

# variables run stage
ARG VERSION=0.0.2-SNAPSHOT
ENV RESOURCES_PREFIX=file://${JETTY_HOME}/classes/
ENV PROXY_PREFIX=
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.24.v20191120/jetty-distribution-9.4.24.v20191120.tar.gz
ENV JETTY_HOME /var/lib/jetty
ENV JETTY_BASE /jetty-base

# install bash, typescript, node ts-node to be able to use it to parse JS code
RUN apk add --no-cache --update bash nodejs npm
RUN npm install -g typescript ts-node

RUN apk add --no-cache curl
RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# create jetty-base folder and add the configuration
RUN mkdir -p ${JETTY_BASE}/webapps
COPY --from=build ./target/classes/jetty /jetty-base

# add war
COPY --from=build ./target/snow-package-${VERSION}.war ${JETTY_BASE}/webapps/root.war

# add typescript code
COPY --from=build ./src/main/angular ${JETTY_HOME}/typescript

# add test data
RUN mkdir -p ${JETTY_HOME}/target/test-classes/test-resources
COPY --from=build ./target/test-classes/test-resources ${JETTY_HOME}/target/test-classes/test-resources

# start
WORKDIR ${JETTY_HOME}
ENTRYPOINT java -jar ./start.jar jetty.base=${JETTY_BASE} \
	-D__RESOURCES_PREFIX=${RESOURCES_PREFIX} \
	-D__PROXY_PREFIX=${PROXY_PREFIX} \
	-Dtscode=/typescript/src/app/snow-package.ts