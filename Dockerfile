FROM openjdk:13-alpine AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"

# variables build stage
ENV MAVEN_URL https://apache.brunneis.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
ENV MAVEN_HOME /usr/share/maven

# install dependencies (bash to launch angular build, ncurses for pretty output with tput, git for npm deps)
RUN apk add --no-cache curl bash ncurses git
RUN apk add --no-cache --update nodejs npm
RUN npm install -g @angular/cli

# install maven
RUN mkdir -p ${MAVEN_HOME}
RUN curl ${MAVEN_URL} | tar zxf - -C ${MAVEN_HOME} --strip-components 1
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# we add the pom and validate the project (does nothing), but some of the downloads will be cached
COPY pom.xml pom.xml
RUN /usr/bin/mvn validate

# add code
COPY src src

# and build (two steps to reuse the lengthy maven download)
RUN /usr/bin/mvn compile
RUN /usr/bin/mvn test package

