#!/bin/bash

##############################################################################
# Bash script that makes it simple to:
#  1) create a urcap project in the _parent directory of the working directory_
#  2) and bundles it as a OSGi bundle directly ready for import in Universal
#     Robots GUI PolyScope
##############################################################################

function setReleaseNumber() {
case $1 in
1)
	myapiversion=1.14.0
	myReleaseBuildNumber=1.14.0
;;
2)
	myapiversion=1.13.0
	myReleaseBuildNumber=1.13.0
;;
3)
	myapiversion=1.12.0
	myReleaseBuildNumber=1.12.0
;;
4)
  myapiversion=1.11.0
	myReleaseBuildNumber=1.11.0
;;
5)
	myapiversion=1.10.0
	myReleaseBuildNumber=1.10.0
;;
6)
	myapiversion=1.9.0
	myReleaseBuildNumber=1.9.0
;;
7)
	myapiversion=1.8.0
	myReleaseBuildNumber=1.8.0
;;
8)
	myapiversion=1.7.0
	myReleaseBuildNumber=1.7.0
;;
9)
	myapiversion=1.6.0
	myReleaseBuildNumber=1.6.0
;;
10)
	myapiversion=1.5.0
	myReleaseBuildNumber=1.5.0
;;
11)
	myapiversion=1.4.0
	myReleaseBuildNumber=1.4.0
;;
12)
	myapiversion=1.3.0
	myReleaseBuildNumber=1.3.0
;;
13)
	myapiversion=1.2.56
	myReleaseBuildNumber=1.2.56
;;
14)
	myapiversion=1.1.0
	myReleaseBuildNumber=1.1.0-69
;;
15)
	myapiversion=1.0.0
	myReleaseBuildNumber=1.0.0.30
;;
esac
}

# Don't update this function, CB3 is supported till 1.12.0.
function setReleaseNumberCB3() {
case $1 in
1)
	myapiversion=1.12.0
	myReleaseBuildNumber=1.12.0
;;
2)
  myapiversion=1.11.0
	myReleaseBuildNumber=1.11.0
;;
3)
	myapiversion=1.10.0
	myReleaseBuildNumber=1.10.0
;;
4)
	myapiversion=1.9.0
	myReleaseBuildNumber=1.9.0
;;
5)
	myapiversion=1.8.0
	myReleaseBuildNumber=1.8.0
;;
6)
	myapiversion=1.7.0
	myReleaseBuildNumber=1.7.0
;;
7)
	myapiversion=1.6.0
	myReleaseBuildNumber=1.6.0
;;
8)
	myapiversion=1.5.0
	myReleaseBuildNumber=1.5.0
;;
9)
	myapiversion=1.4.0
	myReleaseBuildNumber=1.4.0
;;
10)
	myapiversion=1.3.0
	myReleaseBuildNumber=1.3.0
;;
11)
	myapiversion=1.2.56
	myReleaseBuildNumber=1.2.56
;;
12)
	myapiversion=1.1.0
	myReleaseBuildNumber=1.1.0-69
;;
13)
	myapiversion=1.0.0
	myReleaseBuildNumber=1.0.0.30
;;
esac
}

function setCompatibilityInfo() {
case $1 in
1)
    compatibilitytext="Only compatible with CB3"
    cb3compatible=true
    eseriescompatible=false
    API_VERSIONS+=( 1 "1.12.0 (PolyScope SW 3.15.0)" )
    API_VERSIONS+=( 2 "1.11.0 (PolyScope SW 3.14.0 or newer required)" )
    API_VERSIONS+=( 3 "1.10.0 (PolyScope SW 3.13.0 or newer required)" )
    API_VERSIONS+=( 4 "1.9.0 (PolyScope SW 3.12.0 or newer required)")
    API_VERSIONS+=( 5 "1.8.0 (PolyScope SW 3.11.0 or newer required)" )
    API_VERSIONS+=( 6 "1.7.0 (PolyScope SW 3.10.0 or newer required)" )
    API_VERSIONS+=( 7 "1.6.0 (PolyScope SW 3.9.0 or newer required)" )
    API_VERSIONS+=( 8 "1.5.0 (PolyScope SW 3.8.0 or newer required)" )
    API_VERSIONS+=( 9 "1.4.0 (PolyScope SW 3.7.0 or newer required)" )
    API_VERSIONS+=(10 "1.3.0 (PolyScope SW 3.6.0 or newer required)" )
    API_VERSIONS+=(11 "1.2.56 (PolyScope SW 3.5.0 or newer required)" )
    API_VERSIONS+=(12 "1.1.0 (PolyScope SW 3.4.0 or newer required)" )
    API_VERSIONS+=(13 "1.0.0 (PolyScope SW 3.3.0 or newer required)" )
;;
2)
    softwareVersion=5.15.0
    compatibilitytext="Only compatible with e-Series"
    cb3compatible=false
    eseriescompatible=true
    API_VERSIONS+=( 1 "1.14.0 (PolyScope SW ${softwareVersion} or newer required)" )
    API_VERSIONS+=( 2 "1.13.0 (PolyScope SW 5.11.0 or newer required)" )
    API_VERSIONS+=( 3 "1.12.0 (PolyScope SW 5.10.0 or newer required)" )
    API_VERSIONS+=( 4 "1.11.0 (PolyScope SW 5.9.0 or newer required)" )
    API_VERSIONS+=( 5 "1.10.0 (PolyScope SW 5.8.0 or newer required)" )
    API_VERSIONS+=( 6 "1.9.0 (PolyScope SW 5.6.0 or newer required)")
    API_VERSIONS+=( 7 "1.8.0 (PolyScope SW 5.5.0 or newer required)" )
    API_VERSIONS+=( 8 "1.7.0 (PolyScope SW 5.4.0 or newer required)" )
    API_VERSIONS+=( 9 "1.6.0 (PolyScope SW 5.3.0 or newer required)" )
    API_VERSIONS+=( 10 "1.5.0 (PolyScope SW 5.2.0 or newer required)" )
    API_VERSIONS+=( 11 "1.4.0 (PolyScope SW 5.1.0 or newer required)" )
    API_VERSIONS+=(12 "1.3.0 (PolyScope SW 5.0.0 or newer required)" )
    API_VERSIONS+=(13 "1.2.56 (PolyScope SW 5.0.0 or newer required)" )
    API_VERSIONS+=(14 "1.1.0 (PolyScope SW 5.0.0 or newer required)" )
    API_VERSIONS+=(15 "1.0.0 (PolyScope SW 5.0.0 or newer required)" )
;;
3)
    compatibilitytext="Compatible with CB3 and e-Series"
    cb3compatible=true
    eseriescompatible=true
    API_VERSIONS+=( 1 "1.12.0 (PolyScope SW 5.10.0 or newer/3.15.0 required)" )
    API_VERSIONS+=( 2 "1.11.0 (PolyScope SW 3.14.0/5.9.0 or newer required)" )
    API_VERSIONS+=( 3 "1.10.0 (PolyScope SW 3.13.0/5.8.0 or newer required)" )
    API_VERSIONS+=( 4 "1.9.0 (PolyScope SW 3.12.0/5.6.0 or newer required)" )
    API_VERSIONS+=( 5 "1.8.0 (PolyScope SW 3.11.0/5.5.0 or newer required)" )
    API_VERSIONS+=( 6 "1.7.0 (PolyScope SW 3.10.0/5.4.0 or newer required)" )
    API_VERSIONS+=( 7 "1.6.0 (PolyScope SW 3.9.0/5.3.0 or newer required)" )
    API_VERSIONS+=( 8 "1.5.0 (PolyScope SW 3.8.0/5.2.0 or newer required)" )
    API_VERSIONS+=( 9 "1.4.0 (PolyScope SW 3.7.0/5.1.0 or newer required)" )
    API_VERSIONS+=(10 "1.3.0 (PolyScope SW 3.6.0/5.0.0 or newer required)" )
    API_VERSIONS+=(11 "1.2.56 (PolyScope SW 3.5.0/5.0.0 or newer required)" )
    API_VERSIONS+=(12 "1.1.0 (PolyScope SW 3.4.0/5.0.0 or newer required)" )
    API_VERSIONS+=(13 "1.0.0 (PolyScope SW 3.3.0/5.0.0 or newer required)" )
;;
esac
}

mygroupid="com.yourcompany"
myartifactid="thenewapp"
myapiversion="1.14.0"

myReleaseBuildNumber=1.14.0

compatibilitytext="Compatible with CB3 and e-Series"
compatibilitytag=""
cb3compatible=true
eseriescompatible=true

shell=""
groups=""
home=""

COMPATIBILITY_OPTIONS=(1 "Only compatible with CB3" off \
                       2 "Only compatible with e-Series" off \
                       3 "Compatible with CB3 and e-Series" off )

if [[ -z $1 ]] || [[ "$1" != "-t" ]]; then
	# open fd
	exec 3>&1
	# Store data to $VALUES variable
	VALUES=$(dialog --ok-label "Ok" \
		  --separator ";" \
		  --backtitle "URCap Project Creator" \
		  --title "Project Configuration" \
		  --form "Create a new project" \
	15 60 0 \
		"GroupId:"     1 1	"$mygroupid" 		1 13 80 0 \
		"ArtifactID:"  2 1	"$myartifactid"  	2 13 80 0 \
	2>&1 1>&3)

    # close fd
	exec 3>&-

    if [ -z "$VALUES" ]; then
		echo "Operation cancelled, nothing done..."
		exit 1
	fi

	IFS=';' read -ra VAL_ARRAY1 <<< "$VALUES"

	mygroupid=${VAL_ARRAY1[0]}
	myartifactid=${VAL_ARRAY1[1]}

    while [[  -z ${compatibilitytag} ]] ; do
		exec 3>&1
        COMPATIBILITY_VALUE=$(dialog --backtitle "URCap Project Creator" \
		                             --title "Project Configuration" \
                                     --radiolist "URCap Compatibility: " 13 60 0 "${COMPATIBILITY_OPTIONS[@]}" 2>&1 1>&3)

        if [ $? = 1 ] ; then
            echo "Operation cancelled, nothing done..."
		    exit 1
        fi
        exec 3>&-

	    compatibilitytag=${COMPATIBILITY_VALUE}

	    if [[  -z ${compatibilitytag} ]] ; then
	        dialog --infobox "Choose a compatibility option to continue or press Cancel to exit" 5 60 ; sleep 3
	    fi
	done

    API_VERSIONS=()
    setCompatibilityInfo ${compatibilitytag}

    exec 3>&1
    RELEASE_NUMBER_VALUE=$(dialog --menu "API version:"  13 64 0  "${API_VERSIONS[@]}" 2>&1 1>&3)
	exec 3>&-

	# Inserting blank lines
	echo ""
	echo ""

	if [[ -z "$RELEASE_NUMBER_VALUE" ]]; then
		echo "Operation cancelled, nothing done..."
		exit 1
	fi

  if [ $COMPATIBILITY_VALUE -eq 1 ] || [ $COMPATIBILITY_VALUE -eq 3 ]; then
    setReleaseNumberCB3 ${RELEASE_NUMBER_VALUE}
	else
	  setReleaseNumber ${RELEASE_NUMBER_VALUE}
	fi

fi

mypackage+=$mygroupid
mypackage+="."
mypackage+="$myartifactid"


echo 'Building project with parameters:'
echo "  package:        $mypackage"
echo "  GroupId:        $mygroupid"
echo "  ArtifactID:     $myartifactid"
echo "  API:            $myapiversion"
echo "  Compatibility:  $compatibilitytext"


mvn archetype:generate \
  -DinteractiveMode=false \
  -DarchetypeGroupId=com.ur.urcap \
  -DarchetypeArtifactId=archetype \
  -DarchetypeVersion=1.14.0 \
  "-Dpackage=$mypackage.impl" \
  "-DgroupId=$mygroupid" \
  "-DartifactId=$myartifactid" \
  "-Dapiversion=$myapiversion" \
  "-DapiversionRelease=$myReleaseBuildNumber" \
  "-Durcapcompatibility-cb3=$cb3compatible" \
  "-Durcapcompatibility-eseries=$eseriescompatible"

mv $myartifactid $mypackage
