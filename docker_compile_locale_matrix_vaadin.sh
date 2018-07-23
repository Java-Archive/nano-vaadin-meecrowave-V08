#!/usr/bin/env bash

for vaadin in 8.4.5 \
            8.4.4 \
            8.4.3 \
            8.4.2 \
            8.3.3 \
            8.2.0
  do
    docker run --rm --name compile -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven svenruppert/maven-3.5-jdk-oracle-08 mvn -Dvaadin.version=$vaadin clean install
  done


