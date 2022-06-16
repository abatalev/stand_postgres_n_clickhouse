#!/bin/sh

docker build -f Dockerfile.service -t abatalev/pgch-service .
docker build -f Dockerfile.archiver -t abatalev/pgch-archiver .
docker build -f Dockerfile.initdb -t abatalev/pgch-initdb .
docker build -f Dockerfile.initarchive -t abatalev/pgch-initarchive .
docker build -f Dockerfile.reporter -t abatalev/pgch-reporter .
docker build -f Dockerfile.generator -t abatalev/pgch-generator .

cp docker-compose.tmpl docker-compose.yaml
docker-compose up 