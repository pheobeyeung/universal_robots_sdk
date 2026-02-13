#!/bin/bash

export LD_LIBRARY_PATH=/opt/urtool-3.0/lib

URSIM_ROOT=$(dirname $(readlink -f $0))

pushd $URSIM_ROOT &>/dev/null

CLASSPATH=$(echo $URSIM_ROOT/lib/*.jar | tr ' ' ':')

./stopurcontrol.sh
./starturcontrol.sh

ROBOT_TYPE="UR5"
ORIGINAL_ROBOT_TYPE="UR5"
if [ "$1" == "UR10" ] ; then
  ROBOT_TYPE="UR10"
  ORIGINAL_ROBOT_TYPE="UR10"
elif [ "$1" == "UR12e" ] ; then
  ROBOT_TYPE="UR10"
  ORIGINAL_ROBOT_TYPE="UR12e"
elif [ "$1" == "UR7e" ] ; then
  ROBOT_TYPE="UR5"
  ORIGINAL_ROBOT_TYPE="UR7e"
elif [ "$1" == "UR3" ] ; then
  ROBOT_TYPE="UR3"
  ORIGINAL_ROBOT_TYPE="UR3"
elif [ "$1" == "UR8LONG" ] ; then
  ROBOT_TYPE="UR8LONG"
  ORIGINAL_ROBOT_TYPE="UR8LONG"
elif [ "$1" == "UR15" ] ; then
  ROBOT_TYPE="UR15"
  ORIGINAL_ROBOT_TYPE="UR15"
elif [ "$1" == "UR16" ] ; then
  ROBOT_TYPE="UR16"
  ORIGINAL_ROBOT_TYPE="UR16"
elif [ "$1" == "UR18" ] ; then
  ROBOT_TYPE="UR18"
  ORIGINAL_ROBOT_TYPE="UR18"
elif [ "$1" == "UR20" ] ; then
  ROBOT_TYPE="UR20"
  ORIGINAL_ROBOT_TYPE="UR20"
elif [ "$1" == "UR30" ] ; then
  ROBOT_TYPE="UR30"
  ORIGINAL_ROBOT_TYPE="UR30"
fi

#Setting up the configuration files according to the robot type
#urcontrol.conf
rm -f $URSIM_ROOT/.urcontrol/urcontrol.conf
ln -s $URSIM_ROOT/.urcontrol/urcontrol.conf.$ROBOT_TYPE $URSIM_ROOT/.urcontrol/urcontrol.conf

#ur-serial
rm -f $URSIM_ROOT/ur-serial
ln -s $URSIM_ROOT/ur-serial.$ORIGINAL_ROBOT_TYPE $URSIM_ROOT/ur-serial

#safety.conf
rm -f $URSIM_ROOT/.urcontrol/safety.conf
ln -s $URSIM_ROOT/.urcontrol/safety.conf.$ORIGINAL_ROBOT_TYPE $URSIM_ROOT/.urcontrol/safety.conf

#program directory
rm -f $URSIM_ROOT/programs
ln -s $URSIM_ROOT/programs.$ORIGINAL_ROBOT_TYPE $URSIM_ROOT/programs

# Robot Root Certificate check
$URSIM_ROOT/ursim-certificate-check.sh

#Start the gui
pushd GUI
HOME=$URSIM_ROOT java -Duser.home=$URSIM_ROOT -Dconfig.path=$URSIM_ROOT/.urcontrol -DmockCybersecurityBackend=true -Djava.library.path=/usr/lib/jni -jar bin/*.jar
popd


#clean up
rm -f $URSIM_ROOT/.urcontrol/urcontrol.conf
rm -f $URSIM_ROOT/ur-serial
rm -f $URSIM_ROOT/.urcontrol/safety.conf
rm -f $URSIM_ROOT/programs

popd &>/dev/null

