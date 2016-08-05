#!/bin/bash

# example script to migrate templates from imosmest to catalogue-imos
# uses xml.services and session cookies to manage imosmest authentication

etl=../../etl/target/etl-1.0.0.jar
# curl_options='-k -v'
curl_options='-k'


import() {

  local url="$1"; shift;
  local user="$1"; shift;
  local pass="$1"; shift;
  local lang="$1"; shift;
  local uuid="$1"; shift;
  local meffile="$1"; shift;

  curl $curl_options \
    -u "$user:$pass" \
    -F uuid=$uuid  \
    -F format=full \
    -F version=true \
    -F uuidAction=overwrite \
    -F assign=on \
    -F mefFile=@$meffile \
    "$url/srv/$lang/mef.import" \
    || { echo 'import failed'; exit 123; }
}


session_export() {

  local url="$1"; shift;
  local lang="$1"; shift;
  local uuid="$1"; shift;
  local meffile="$1"; shift;

  cookies='--cookie cookies.txt --cookie-jar cookies.txt'
  local args="format=full&version=true&uuid=$uuid"

  curl $curl_options \
    $cookies \
    "$url/srv/$lang/mef.export?$args" > "$meffile" \
    || { echo 'export failed'; exit 123; }
}


session_search() {

  local url="$1"; shift;
  local lang="$1"; shift;
  local args="$1"; shift;

  cookies='--cookie cookies.txt --cookie-jar cookies.txt'

  curl $curl_options \
    $cookies \
    -X GET "$url/srv/$lang/xml.search?$args" \
    || { echo 'search failed'; exit 123; }
}


session_login() {
  # uses cookie mechanism and xml.login for session management, used by older geonetwork

  local url="$1"; shift;
  local lang="$1"; shift;
  local user="$1"; shift;
  local pass="$1"; shift;

  cookies='--cookie cookies.txt --cookie-jar cookies.txt'
  service='xml.user.login'
  args="username=$user&password=$pass"

  rm -f ./cookies.txt
  curl $curl_options  $cookies -v "$url/srv/$lang/$service?$args"
}



[ -d ./tmp ] || mkdir tmp

session_login 'http://imosmest.aodn.org.au/geonetwork' 'en' 'admin' 'xxxxxx' 

uuids=$( session_search 'http://imosmest.aodn.org.au/geonetwork' 'en' 'template=y' \
  | java -jar $etl -text -transform  ./extract-uuids.xsl  )


for uuid in $uuids; do
  echo "processing, $uuid"

  meffile='./tmp/out.zip'
  rm -f "$meffile"
  session_export 'http://imosmest.aodn.org.au/geonetwork' 'en' "$uuid" "$meffile"

  # import  'http://geonetwork2.localnet/geonetwork' 'admin' 'admin' 'eng' "$uuid" "$meffile"
  import  'https://catalogue-imos.aodn.org.au/geonetwork' 'admin' 'xxxxxxx' 'eng' "$uuid" "$meffile"
done


