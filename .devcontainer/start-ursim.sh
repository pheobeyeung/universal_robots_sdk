#!/bin/bash

set -x

export LD_LIBRARY_PATH=/opt/urtool-3.0/lib

URSIM_ROOT=/ursim

pushd $URSIM_ROOT &>/dev/null

CLASSPATH=$(echo $URSIM_ROOT/lib/*.jar | tr ' ' ':')

./stopurcontrol.sh
./starturcontrol.sh

ROBOT_TYPE="UR5"
if [ "$1" == "UR10" ] ; then
  ROBOT_TYPE="UR10"
elif [ "$1" == "UR3" ] ; then
  ROBOT_TYPE="UR3"
elif [ "$1" == "UR16" ] ; then
  ROBOT_TYPE="UR16"
elif [ "$1" == "UR20" ] ; then
  ROBOT_TYPE="UR20"
elif [ "$1" == "UR30" ] ; then
  ROBOT_TYPE="UR30"
fi

#Setting up the configuration files according to the robot type
#urcontrol.conf
rm -f $URSIM_ROOT/.urcontrol/urcontrol.conf
ln -s $URSIM_ROOT/.urcontrol/urcontrol.conf.$ROBOT_TYPE $URSIM_ROOT/.urcontrol/urcontrol.conf

#ur-serial
rm -f $URSIM_ROOT/ur-serial
ln -s $URSIM_ROOT/ur-serial.$ROBOT_TYPE $URSIM_ROOT/ur-serial

#safety.conf
rm -f $URSIM_ROOT/.urcontrol/safety.conf
ln -s $URSIM_ROOT/.urcontrol/safety.conf.$ROBOT_TYPE $URSIM_ROOT/.urcontrol/safety.conf

#program directory
rm -f $URSIM_ROOT/programs
ln -s $URSIM_ROOT/programs.$ROBOT_TYPE $URSIM_ROOT/programs

cat << EOF > /ursim/.polyscope/controllerinterface.properties
controller.socket.ip=127.0.0.1
EOF

# Robot Root Certificate check
$URSIM_ROOT/ursim-certificate-check.sh

#Start the gui
pushd GUI
HOME=$URSIM_ROOT java -Duser.home=$URSIM_ROOT -Dconfig.path=$URSIM_ROOT/.urcontrol -Xms2048m -Xmx2048m -XX:ThreadStackSize=300 -XX:MaxMetaspaceSize=150m -XX:CompressedClassSpaceSize=25m -Xrs -Xbatch -Dfile.encoding=UTF-8 -Djava.library.path=/usr/lib/jni --add-opens=java.desktop/sun.awt=ALL-UNNAMED -jar bin/*.jar

#HOME=$URSIM_ROOT java -Duser.home=$URSIM_ROOT -Dconfig.path=$URSIM_ROOT/.urcontrol -DmockCybersecurityBackend=true -Djava.library.path=/usr/lib/jni -jar bin/*.jar
popd


#clean up
rm -f $URSIM_ROOT/.urcontrol/urcontrol.conf
rm -f $URSIM_ROOT/ur-serial
rm -f $URSIM_ROOT/.urcontrol/safety.conf
rm -f $URSIM_ROOT/programs

popd &>/dev/null
