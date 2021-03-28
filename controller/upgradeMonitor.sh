#!/bin/bash
./mvnw clean package -DskipTests
docker build -f ./src/main/docker/Dockerfile.jvm --tag johara/controller:1.0.1.Final .
docker stop electricity-monitor
docker container rm  electricity-monitor
docker run -d --rm --name electricity-monitor -p 8281:8080 johara/controller:1.0.1.Final
