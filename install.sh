#!/bin/bash

set -ex; \
cd /; \
curl https://urplus-developer-site.s3.eu-west-1.amazonaws.com/sdk/sdk-1.14.0.zip -o sdk-1.14.0.zip; \
unzip sdk-1.14.0.zip -d /sdk-1.14.0; \
rm sdk-1.14.0.zip; \
cd /sdk-1.14.0; \
no | ./install.sh