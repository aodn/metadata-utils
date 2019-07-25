#! /bin/bash

# Given a list of uuids update records in geonetwork

# $1 : show, trial, update, reindex (required)
# $2 : file containing list of uuids (required)
# $3 : host and database (eg 6-aws-syd.emii.org.au/geonetwork_aodn) OR geonetwork host (required)
# $4 : database user
# $5 : database password
# $6 : transform xsl file

action="$1";

for uuid in $(cat "$2")
  do
    case "$action" in
      show)
        # Display the records to be changed
        java -jar ../../mafia/target/mafia-1.0.0.jar \
            -url jdbc:postgresql://"$3" \
            -user "$4" -pass "$5" \
            -uuid $uuid \
            -stdout
        ;;
      trial)
        # Display changes to records without updating
        java -jar ../../mafia/target/mafia-1.0.0.jar \
            -url jdbc:postgresql://"$3" \
            -user "$4" -pass "$5" \
            -uuid $uuid \
    	    -transform "$6" \
            -stdout
        ;;
      update)
        # Update
        java -jar ../../mafia/target/mafia-1.0.0.jar \
            -url jdbc:postgresql://"$3" \
            -user "$4" -pass "$5" \
            -uuid $uuid \
            -transform "$6" \
            -update
        ;;
      reindex)
        # Reindex records by http
        geonetwork_host="$3";
        curl -X GET https://"$geonetwork_host"/geonetwork/srv/eng/metadata.show?uuid="$uuid" -o /dev/null
    esac
  done
