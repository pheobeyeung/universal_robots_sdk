#!/bin/bash

cd /
./entrypoint.sh &

cd /ursim 
./start-ursim.sh &

xdg-open http://localhost:6080/vnc.html?host=localhost&port=6080
