#!/bin/bash

# example script to migrate records from imosmest to catalogue-imos
# no authentication for imosmest, and basic http authentication for catalogue-imos

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


export_() {

  local url="$1"; shift;
  # local user="$1"; shift;
  # local pass="$1"; shift;
  local lang="$1"; shift;
  local uuid="$1"; shift;
  local meffile="$1"; shift;

  local args="format=full&version=true&uuid=$uuid"

  curl $curl_options \
    "$url/srv/$lang/mef.export?$args" > "$meffile" \
    || { echo 'export failed'; exit 123; }
}


search() {
  local url="$1"; shift;
  # local user="$1"; shift;
  # local pass="$1"; shift;
  local lang="$1"; shift;
  local args="$1"; shift;

  curl $curl_options \
    -X GET "$url/srv/$lang/xml.search?$args" \
    || { echo 'search failed'; exit 123; }
}

[ -d ./tmp ] || mkdir tmp

uuids=$( search 'http://imosmest.aodn.org.au/geonetwork' 'en' 'siteId=2e5b03a0-fbd5-4215-a650-7d121ffb6635' \
  | java -jar $etl -text -transform  ./extract-uuids.xsl  )


for uuid in $uuids; do

  echo "processing, $uuid"
  meffile='./tmp/out.zip'
  rm -f "$meffile"
  export_ 'http://imosmest.aodn.org.au/geonetwork' 'en' "$uuid" "$meffile"
  # import  'http://geonetwork2.localnet/geonetwork' 'admin' 'admin' 'eng' "$uuid" "$meffile"
  import  'https://catalogue-imos.aodn.org.au/geonetwork' 'admin' 'xxxxxxx' 'eng' "$uuid" "$meffile"
done

