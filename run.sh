#! /bin/bash

echo "building liquid-democracy-api" 
docker rm -f $(docker ps -aq)
docker run -ti --rm -v ~/.m2:/root/.m2 -v $(pwd)/liquid-democracy-api:/usr/src/app -w /usr/src/app -v /var/run/docker.sock:/var/run/docker.sock maven:3.2-jdk-8 mvn clean package docker:build

cd liquid-democracy-api

docker-compose up
