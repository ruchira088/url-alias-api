FROM openjdk:8-jdk-alpine

ENV SBT_VERSION 0.13.15
ENV PATH ${PATH}:/opt/sbt/bin

RUN apk update && \
    apk add wget bash

WORKDIR /opt

RUN wget http://dl.bintray.com/sbt/native-packages/sbt/0.13.15/sbt-0.13.15.tgz && \
    tar -xvf sbt-0.13.15.tgz

WORKDIR /opt/url-alias-api

COPY ./build.sbt .
COPY ./project ./project

RUN sbt compile

COPY . .

RUN sbt compile

ENTRYPOINT ["sbt"]

CMD ["-jvm-debug", "5005", "~run"]
