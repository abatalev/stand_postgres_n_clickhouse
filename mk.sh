#!/bin/sh

I_GROUP="abatalev"
I_SERVICE="${I_GROUP}/pgch-service"
V_SERVICE="2022-06-16"

I_ARCHIVER="${I_GROUP}/pgch-archiver"
V_ARCHIVER="2022-06-16"

I_INITDB="${I_GROUP}/pgch-initdb"
V_INITDB="2022-06-16"

I_INITARCHIVE="${I_GROUP}/pgch-initarchive"
V_INITARCHIVE="2022-06-16"

I_REPORTER="${I_GROUP}/pgch-reporter"
V_REPORTER="2022-06-16"

I_GENERATOR="${I_GROUP}/pgch-generator"
V_GENERATOR="2022-06-16"

function image_exist() {
    docker image list | grep ${1} | awk ' {print $2;}' | grep ${2} | wc -l
}

function image_build() {
    B_PRJ=$1
    I_PRJ=$2
    V_PRJ=$3
    D_PRJ=$4
    N_PRJ=$5
    if [ ${B_PRJ} == 0 ]; then
        echo "### build $N_PRJ"
        docker build -t $I_PRJ:$V_PRJ -f $D_PRJ .
        if [  $? -ne 0  ]; then exit 1; fi
    else 
        echo "### Build $N_PRJ skipped. Image $I_PRJ:$V_PRJ already exist"    
    fi
}

if [ -f "prj2hash" ]; then
    V_SERVICE=$(./prj2hash -short apps/service)
    V_ARCHIVER=$(./prj2hash -short apps/archiver)
    V_REPORTER=$(./prj2hash -short apps/reporter)
    V_GENERATOR=$(./prj2hash -short apps/generator)
    V_INITDB=$(./prj2hash -short apps/initdb)
    V_INITARCHIVE=$(./prj2hash -short apps/initarchive)

    B_SERVICE=$(image_exist $I_SERVICE $V_SERVICE)
    B_ARCHIVER=$(image_exist $I_ARCHIVER $V_ARCHIVER)
    B_REPORTER=$(image_exist $I_REPORTER $V_REPORTER)
    B_GENERATOR=$(image_exist $I_GENERATOR $V_GENERATOR)
    B_INITDB=$(image_exist $I_INITDB $V_INITDB)
    B_INITARCHIVE=$(image_exist $I_INITARCHIVE $V_INITARCHIVE)
fi

CDIR=$(pwd)
cd "${CDIR}"
image_build $B_SERVICE $I_SERVICE $V_SERVICE Dockerfile.service "service"
image_build $B_ARCHIVER $I_ARCHIVER $V_ARCHIVER Dockerfile.archiver "archiver"
image_build $B_GENERATOR $I_GENERATOR $V_GENERATOR Dockerfile.generator "generator"
image_build $B_INITDB $I_INITDB $V_INITDB Dockerfile.initdb "initdb"
image_build $B_INITARCHIVE $I_INITARCHIVE $V_INITARCHIVE Dockerfile.initarchive "initarchive"
image_build $B_REPORTER $I_REPORTER $V_REPORTER Dockerfile.reporter "reporter"

cd "${CDIR}"
echo "### create docker-compose"
cp docker-compose.tmpl docker-compose.yaml
IMAGE_VERSION=$I_SERVICE:$V_SERVICE yq e -i '.services.service.image = strenv(IMAGE_VERSION)' docker-compose.yaml
IMAGE_VERSION=$I_INITDB:$V_INITDB yq e -i '.services.initdb.image = strenv(IMAGE_VERSION)' docker-compose.yaml
IMAGE_VERSION=$I_INITARCHIVE:$V_INITARCHIVE yq e -i '.services.initarchive.image = strenv(IMAGE_VERSION)' docker-compose.yaml
IMAGE_VERSION=$I_REPORTER:$V_REPORTER yq e -i '.services.reporter.image = strenv(IMAGE_VERSION)' docker-compose.yaml
IMAGE_VERSION=$I_ARCHIVER:$V_ARCHIVER yq e -i '.services.archiver.image = strenv(IMAGE_VERSION)' docker-compose.yaml
IMAGE_VERSION=$I_GENERATOR:$V_GENERATOR yq e -i '.services.generator.image = strenv(IMAGE_VERSION)' docker-compose.yaml

echo "### launch docker-compose"
docker-compose up 