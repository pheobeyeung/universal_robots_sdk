#!/bin/bash

import_certificate() {
    $( sudo keytool -import -trustcacerts -keystore $CACERTS -storepass changeit -noprompt \
    -alias ur_robot_root_certificate \
    -file ${URSIM_ROOT}/.certificate/URRobotRoot.crt )
}

delete_certificate() {
    $( sudo keytool -delete -keystore $CACERTS -storepass changeit \
    -alias ur_robot_root_certificate )
}

URSIM_ROOT=$(dirname $(readlink -f $0))

echo "Checking Robot Root Certificate"
CACERTS=$(readlink -e $(dirname $(readlink -e $(which keytool)))/../lib/security/cacerts)
if [ -f ${CACERTS} ] ; then
    if [ $( keytool -list -keystore $CACERTS -storepass changeit | grep ur_robot_root_certificate -c) == 0 ]  ; then
        echo "Installing ur_robot_root_certificate into Java $CACERTS keystore"
        import_certificate
    else
        CERTSHA256=$(openssl x509 -in ${URSIM_ROOT}/.certificate/URRobotRoot.crt -noout -sha256 -fingerprint | awk -F"=" '{print $2'})
        KEYSTORESHA256=$(keytool -v -keystore $CACERTS -list -alias ur_robot_root_certificate -storepass changeit | grep "SHA256: " | awk '{print $2}')
        if [ "$CERTSHA256" != "$KEYSTORESHA256" ] ; then
            echo "Replace certificate in keystore"
            delete_certificate
            import_certificate
        fi
    fi
else
    echo 'Can not find cacerts file.' >&2
fi
