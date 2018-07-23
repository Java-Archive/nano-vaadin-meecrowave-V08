#!/usr/bin/env bash

docker run --rm --name compile -v "$(pwd)/_data/nexus/settings.xml":/root/.m2/settings.xml -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven svenruppert/maven-3.5-jdk-oracle-08 mvn clean install
