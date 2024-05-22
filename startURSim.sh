#!/bin/bash

(xdg-open 'http://localhost:6080/vnc.html?host=localhost&port=6080')&
(cd /app && ./entrypoint.sh)&
(cd /ursim && ./start-ursim.sh) 





