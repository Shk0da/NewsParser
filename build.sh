#!/bin/bash
docker rm db;
docker rm newsparser;
./mvnw clean package -DskipTests docker:build &&\
docker build src/main/resources/docker/db -t db  &&\
docker run --name db -h db -p 5432:5432 -d db  &&\
docker run --name newsparser -p 80:80 --link db:db -d ashkondin/newsparser