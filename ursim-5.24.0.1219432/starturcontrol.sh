#!/bin/bash
SCRIPT_DIR=$(dirname $(readlink -f $0))
# note: for UR20 on SW5 we need this workaround to get the marketing version
PRODUCT_VERSION=$(sed -n 's/.*marketingVersion.* "\([0-9\.]*\)".*;/\1/p' $SCRIPT_DIR/metadata.n3)
if [ -z "$PRODUCT_VERSION" ]; then
  PRODUCT_VERSION="5.14.0"
fi
HOME=$SCRIPT_DIR $SCRIPT_DIR/URControl -m $PRODUCT_VERSION -r &>$SCRIPT_DIR/URControl.log &
