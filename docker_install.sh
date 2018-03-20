#!/bin/bash

# make jar
mvn package -DskipTests=true

# docker-compose build
docker-compose build

# docker compose the postgres DB
docker-compose up -d db

# sleep to wait for DB to init
sleep 5

# docker compose the vulnmanager container, with the jar made previously
docker-compose up -d vulnmanager

# docker PS to verify everything is running
docker ps

