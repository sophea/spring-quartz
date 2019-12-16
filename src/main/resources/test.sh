#!/usr/bin/env bash
# -------------------------------------------------------------------------
# This is the Unix startup script for the application
# Author : Mak Sophea
# Version : 1.0
# -------------------------------------------------------------------------

if [ $# -eq 1 ]; then
    echo "The deployment version $1 "
else
    echo "Failed to deploy $# . The usage is ./deployment.sh <version>"
    exit 1;
fi

VERSION=$1


echo "****deployment version $VERSION successfully ****"