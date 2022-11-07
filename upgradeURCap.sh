#!/bin/bash
set -e
########################################################################################################
# Bash script to upgrade the existing URCaps to support compatibility flags
# Usage: ./upgradeURCap.sh <path> [compatibilityCB3=<value>] [compatibilityESeries=<value>] [-help/-h]
#         where <path> is the path to the pom file
#               <value> is the value for compatibility flags. It could either be true or false
#         All arguments except <path> are optional
# For example: ./upgradeURCap.sh . compatibilityCB3=false compatibilityESeries=true
#########################################################################################################

PATH_TO_POM_PREFIX="."
PATH_TO_POM="pom.xml"

COMPATIBILITY_CB3_FLAG_NAME="compatibilityCB3"
COMPATIBILITY_ESERIES_FLAG_NAME="compatibilityESeries"

COMPATIBILITY_CB3_FLAG_VALUE=true
COMPATIBILITY_ESERIES_FLAG_VALUE=true

function show_help() {
echo "Bash script for upgrading existing URCaps to support compatibility flags.

Usage: ./upgradeURCap.sh <path> [compatibilityCB3=<value>] [compatibilityESeries=<value>] [-help/-h]
        where <path>  is the path to the pom.xml file.
              <value> is the value for compatibility flags. It could either be 'true' or 'false'.
        All arguments except <path> are optional.
        If the compatibilityCB3 and/or compatibilityESeries argument(s) are not specified, the
        compatibility flag(s) are assigned the value 'true' by default.

Examples:
  ./upgradeURCap.sh . compatibilityCB3=false compatibilityESeries=true
  ./upgradeURCap.sh ../urcaps/myURCap compatibilityCB3=false compatibilityESeries=true"
}

function show_reference_to_help() {
  echo "Try './upgradeURCap.sh -help' for more information."
}

function set_as_path() {
  if [[ $1 != "." ]] ; then
   PATH_TO_POM_PREFIX=$1
  fi
}

function validate_flag_values() {
  flagvalue=$1
  if [[ $flagvalue != 'true' ]] && [[ $flagvalue != 'false' ]] ; then
   echo "Error: Expected value for compatibility flag arguments is 'true' or 'false'"
   show_reference_to_help
   exit 1
  fi
}

function check_position_for_compatibility_arguments() {
  flagposition=$*
  if [[ flagposition -eq 1 ]] ; then
    echo "Error: Missing value for <path>. First argument must be the path to the URCap project's pom.xml file or -help"
    show_reference_to_help
    exit 1
  fi
}

function show_unexpected_argument_if_not_a_path() {
  position=$1
  path=$2
  if [[ $position -ne 1 ]]; then
    echo "Error: Unexpected argument : $path "
    show_reference_to_help
    exit 1
  else
    set_as_path $path
  fi
}

function show_no_value_specified_error_if_not_a_path() {
  position=$1
  arg=$2
  if [[ position -ne 1 ]]; then
    echo "Error: Missing value for the compatibility flag $arg"
    show_reference_to_help
    exit 1;
  else
    set_as_path $arg
  fi
}

function display_help_and_ignore_others() {
  number_of_args=$1
  if [[ ${number_of_args} -gt 1 ]]; then
        echo "Info: Displaying help and exiting."
  fi
  show_help
  exit 0
}

function parse_compatibility_flag_with_value() {
  pos=$1
  value=$2
  check_position_for_compatibility_arguments $pos
  validate_flag_values $value
}

function parse_and_validate_arguments() {
  if [[  $# -lt 1 ]] ; then
   echo "Error: Missing value for <path>"
   show_reference_to_help
   exit 1
  fi

   params=($*)
   pos=0
   for i in "${params[@]}"; do
      pos=$((pos+1))
      case $i in
      ${COMPATIBILITY_CB3_FLAG_NAME}|${COMPATIBILITY_ESERIES_FLAG_NAME})
      show_no_value_specified_error_if_not_a_path $pos $i
      shift
      ;;
      ${COMPATIBILITY_CB3_FLAG_NAME}=*)
      value="${i#*=}"
      parse_compatibility_flag_with_value $pos $value
      COMPATIBILITY_CB3_FLAG_VALUE=$value
      shift
      ;;
      ${COMPATIBILITY_ESERIES_FLAG_NAME}=*)
      value="${i#*=}"
      parse_compatibility_flag_with_value $pos $value
      COMPATIBILITY_ESERIES_FLAG_VALUE=$value
      shift
      ;;
      -help | -h)
      display_help_and_ignore_others ${#params[@]}
      shift
      ;;
      *)
      show_unexpected_argument_if_not_a_path $pos $i
      shift
      ;;
     esac
   done
}

function warn_if_both_compatibility_flags_are_false() {
  if [[ ${COMPATIBILITY_CB3_FLAG_VALUE} == 'false' ]] && [[ ${COMPATIBILITY_ESERIES_FLAG_VALUE} == 'false' ]]; then
    echo "Warning: URCap will not be compatible with any existing Robot Series."
  fi
}

function check_file_exists_and_is_writable() {
  pom_file=$PATH_TO_POM
  if [[ $PATH_TO_POM_PREFIX != "." ]]; then
    pom_file=$1
  fi
  if [[ ! -e ${pom_file} ]]; then
    echo "Error: File does not exists : ${pom_file}"
    exit 1
  fi
  if [[ ! -w ${pom_file} ]]; then
    echo "Error: File does not have write permissions : ${pom_file}"
    exit 1
  fi
}

function find_matching_line_and_append_text_in_new_line_in_pom() {
  search_strings=$1
  cb3_text_to_insert=$2
  eseries_text_to_insert=$3
  found_search_string=false
  for str in "${search_strings[@]}" ; do
   if  grep -q $str ${POM_FILE}; then
     found_search_string=true
     line_match=$(grep -i $str ${POM_FILE})
     indent_spaces="${line_match%%<*}"
     sed -i "/$str/a \\${indent_spaces}${cb3_text_to_insert}\n${indent_spaces}${eseries_text_to_insert} " ${POM_FILE}
     break;
   fi
  done
  if [[ $found_search_string == false ]]; then
    echo "Cancelling upgrade, it is not a valid URCap."
    exit 1
  fi
}

function update_properties_section() {
 cb3_search_text_in_properties="<urcap.compatibility.CB3>"
 eseries_search_text_in_properties="<urcap.compatibility.eSeries>"

 cb3_text_to_insert="<urcap.compatibility.CB3>${COMPATIBILITY_CB3_FLAG_VALUE}</urcap.compatibility.CB3>"
 eseries_text_to_insert="<urcap.compatibility.eSeries>${COMPATIBILITY_ESERIES_FLAG_VALUE}</urcap.compatibility.eSeries>"

 #delete if it already exists
 sed -i "/${cb3_search_text_in_properties}/d;/${eseries_search_text_in_properties}/d" ${POM_FILE}

 search_strings=('<urcap.licenseType>' '<urcap.description>' '<urcap.copyright>' '<urcap.contactAddress>'
                  '<urcap.vendor>' '<urcap.symbolicname>')
 find_matching_line_and_append_text_in_new_line_in_pom $search_strings $cb3_text_to_insert $eseries_text_to_insert
}

function update_instructions_section() {
 cb3_search_text_in_instructions="<URCapCompatibility-CB3>"
 eseries_search_text_in_instructions="<URCapCompatibility-eSeries>"
 cb3_text_to_insert='<URCapCompatibility-CB3>${urcap.compatibility.CB3}</URCapCompatibility-CB3>'
 eseries_text_to_insert='<URCapCompatibility-eSeries>${urcap.compatibility.eSeries}</URCapCompatibility-eSeries>'

 #delete if it already exists
 sed -i "/${cb3_search_text_in_instructions}/d;/${eseries_search_text_in_instructions}/d" ${POM_FILE}

 search_strings=('<Bundle-Description>' '<Bundle-LicenseType>' '<Bundle-Copyright>' '<Bundle-ContactAddress>'
                  '<Bundle-Vendor>' '<Bundle-Activator>' '<Bundle-Category>')
 find_matching_line_and_append_text_in_new_line_in_pom $search_strings $cb3_text_to_insert $eseries_text_to_insert
}

 parse_and_validate_arguments $*
 warn_if_both_compatibility_flags_are_false

 POM_FILE="${PATH_TO_POM_PREFIX}/${PATH_TO_POM}"

 check_file_exists_and_is_writable ${POM_FILE}
 update_properties_section
 update_instructions_section

 echo "URCap upgraded successfully."
