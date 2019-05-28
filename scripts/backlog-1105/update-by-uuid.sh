#! /bin/bash

# We are given a xml file and extract the geonetwork uuids from it to find records for updating

# $1 : show, trial, update

for uuid in $(grep "<identifier type=\"global\">" geoserver.imos.org.au_geoserver_wms.xml | cut -c39-74)
  do
    case "$1" in
      show)
        # Display the records to be changed
        java -jar ../../mafia/target/mafia-1.0.0.jar \
            -url jdbc:postgresql://172.28.128.3/geonetwork_aodn \
            -user geonetwork_aodn -pass fzYp6XMZ0fAk2wp9CMKmUqGF \
            -uuid $uuid \
            -stdout
        ;;
      trial)
        # Display changes to records without updating
        java -jar ../../mafia/target/mafia-1.0.0.jar \
            -url jdbc:postgresql://172.28.128.3/geonetwork_aodn \
            -user geonetwork_aodn -pass fzYp6XMZ0fAk2wp9CMKmUqGF \
            -uuid $uuid \
    	    -transform backlog-1105.xslt \
            -stdout
        ;;
      update)
        # Update
        java -jar ../../mafia/target/mafia-1.0.0.jar \
            -url jdbc:postgresql://172.28.128.3/geonetwork_aodn \
            -user geonetwork_aodn -pass fzYp6XMZ0fAk2wp9CMKmUqGF \
            -uuid $uuid \
            -transform backlog-1105.xslt \
            -update
        ;;
    esac
  done
