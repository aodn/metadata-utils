
## MAFIA

CLI tool for Iso19139 Administration


### TODO
- removal of saxon dependency.
    Note need for factory.setNamespaceAware(true) in non-saxon implementation

- support options read from a configuration file (eg. connection credentials)
- factor transform/validation pipeline from database manipulation
- review older examples
- done - remove need to specify the class
- done - rename artifact from myartifact.jar

### Build
    mvn clean install

### Warning

This utility performs no concurrent update checking so it is recommended to 
shutdown the geonetwork instance which needs to be updated to prevent
concurrent updates by the application


### Examples



##### Get help
```
java -jar ./mafia/target/mafia-1.0.0.jar -help

usage: Updater
 -all                   etl applies to all metadata records
 -context               expose additional metadata fields (eg. record uuid) to
                        etl
 -help                  show help
 -pass <arg>            password
 -stdout                dump result to stdout
 -transform <arg>       transform stylesheet (.xslt) file to use
 -update                perform inplace update of the metadata record
 -url <arg>             jdbc connection string, eg.
                        jdbc:postgresql://127.0.0.1/geonetwork
 -user <arg>            user
 -uuid <arg>            etl applies to specific metadata record
- selected <arg>        etl applies to selected metadata record uuids listed in the provided file
- isharvested <arg>     etl applies to harvested or non-harvested metadata records depending upon provided value (y or n)
- validate <arg>        validation schema (.xsd) file to use  or provide schema folder (schema.xsd will be selected depending upon schema type)
- invalids <arg>        provide a name of the file to list all invalid uuids
- xmloutputpath <arg>   provide a folder path to store xml files

```

##### Apply explicit identity transform
```
TODO: Fixme!
java -jar ./validate/target/validate-1.0.0.jar \
  -source ./resources/argo.xml \
  -validate /home/meteo/imos/schema-plugins/iso19139.mcp-2.0/schema.xsd \
  -transform resources/identity.xml \
  | less
```

##### Apply explicit identity transform and validation using schema folder instead of schema (.xsd) file
```
java -jar ./validate/target/validate-1.0.0.jar \
  -source ./resources/argo.xml \
  -validate /home/meteo/imos/schema-plugins \
  -transform resources/identity.xml \
  | less
```


##### Dump untransformed Argo record to stdout
```
java -jar ./mafia/target/mafia-1.0.0.jar \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -stdout
```

##### Validate Argo record against mcp-2 schema
```
java -jar ./mafia/target/mafia-1.0.0.jar \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -validate ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/schema.xsd
```

##### Validate all records against mcp-2 schema and generate report
```
java -jar ./mafia/target/mafia-1.0.0.jar \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -all \
  -validate ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/schema.xsd \
  > report.txt
```


##### Transform Argo record from draft mcp-2 to mcp-2 and validate but do not update
```
java -jar ./mafia/target/mafia-1.0.0.jar \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/from_mcp2_draft.xsl \
  -validate ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/schema.xsd
```


##### Transform Argo record from draft mcp-2 to mcp-2 and perform in-place update
```
java -jar ./mafia/target/mafia-1.0.0.jar \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/from_mcp2_draft.xsl \
  -update
```

##### Transform Argo record from mcp-2 to iso19139 and validate
```
java -jar ./mafia/target/mafia-1.0.0.jar \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/to19139.xsl \
  -validate ../mcp-2.0/iso19139/schema.xsd
```


##### Port forward vagrant box postgres instance and dump all records to stdout
```
Fixme!

ssh -L :9000:localhost:5432  10.11.12.13

java -jar ./mafia/target/mafia-1.0.0.jar \
  -url jdbc:postgresql://127.0.0.1:9000/geonetwork -user geonetwork -pass geonetwork \
  -all -stdout
```

##### List record uuids with schema validation errors
```
java -jar ./mafia/target/mafia-1.0.0.jar  \ 
    -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
    -all \
    -validate /path/to/schema-plugins \
    -invalids invalids.txt 
```

##### Transform to fix validation errors for selected list of uuids and downloads metadata.xml (before and after applying fix)
```
java -jar ./mafia/target/mafia-1.0.0.jar 
    -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
    -selected invalids.txt \
    -validate /path/to/schema-plugins \
    -transform /path/to/fix-mcp-schema-validation.xsl \
    -xmloutputpath /tmp/gn-validate-imos-xml 
```