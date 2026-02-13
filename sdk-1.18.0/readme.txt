URCaps SDK

/artifacts/     - contains all released versions of the api and the archetype for creating a URCap
/doc/           - contains the tutorial describing how to develop a URCap. Comes in both a Swing-based and HTML-based UI.
                  Also contains additional documents on how to work with various other aspects of the URCap API version
                  as well a guide on how to convert an existing HTML-based URCap to a Swing-based one.
/samples/       - contains example URCaps, among others the ones referred to in the tutorial
/urtool/        - contains Debian package for cross-compiler for 64-bit systems
install.sh      - The script for installing the api and archetype in your local maven repository. Run this first.
newURCap.sh     - The script for creating a URCap in your current working directory
upgradeURCap.sh - The script for updating an existing URCap (project) to specify which robot series the URCap is compatible with
