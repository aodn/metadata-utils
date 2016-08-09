#!/bin/bash

# example script to run a batch xslt against a set of records
# GN 2.10.x uses http authentication, but requires session cookie for persisting search/select/batch state across url requests

etl=../../etl/target/etl-1.0.0.jar
curl_options='-k -v'


session_login() {

  local url="$1"; shift;
  local lang="$1"; shift;
  local user="$1"; shift;
  local pass="$1"; shift;

  local cookies='--cookie cookies.txt --cookie-jar cookies.txt'

  rm -f ./cookies.txt
  curl $curl_options $cookies -v -u "$user:$pass" "$url" 
}


search_all() {

  local url="$1"; shift;
  local lang="$1"; shift;
  local user="$1"; shift;
  local pass="$1"; shift;

  local cookies='--cookie cookies.txt --cookie-jar cookies.txt'

  curl $curl_options \
    -u "$user:$pass" \
    $cookies \
    "$url/srv/$lang/xml.search?" \
    || { echo 'failed'; exit 123; }
}


select_all() {

  local url="$1"; shift;
  local lang="$1"; shift;
  local user="$1"; shift;
  local pass="$1"; shift;

  local cookies='--cookie cookies.txt --cookie-jar cookies.txt'

  curl $curl_options \
    -u "$user:$pass" \
    $cookies \
    "$url/srv/$lang/metadata.select?selected=add-all" \
    || { echo 'failed'; exit 123; }
}


batch_process() {

  local url="$1"; shift;
  local lang="$1"; shift;
  local user="$1"; shift;
  local pass="$1"; shift;
  local save="$1"; shift;
  local process="$1"; shift;

  local cookies='--cookie cookies.txt --cookie-jar cookies.txt'

  curl $curl_options \
    $cookies \
    -u "$user:$pass" \
    "$url/srv/$lang/xml.metadata.batch.processing?save=$save&process=$process" \
    || { echo 'failed'; exit 123; }
}


url='http://geonetwork2.localnet/geonetwork'
user='admin'
pass='admin'


session_login   "$url" 'eng' "$user" "$pass"
search_all      "$url" 'eng' "$user" "$pass"
select_all      "$url" 'eng' "$user" "$pass"
batch_process   "$url" 'eng' "$user" "$pass" 0 apply-fixups


