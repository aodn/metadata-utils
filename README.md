

### GeoNetwork XSLT record editor

#### TODO
- maybe change name mest-cli-bulk-editor or mest-stylesheet-editor
- support reading some options from file (eg. connection credentials)
- change artifact name from myartifact.jar

### Build

    mvn clean install

### Examples

#### Get help
```
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -help

usage: Updater
 -all               apply actions to all metadata records
 -context           make additional metadata fields such as record uuid
                    available to stylesheet
 -help              show help
 -pass <arg>        password
 -stdout            dump result to stdout
 -transform <arg>   transform stylesheet xslt file to use
 -update            perform inplace update of the metadata record
 -url <arg>         jdbc connection string, eg.
                    jdbc:postgresql://127.0.0.1/geonetwork
 -user <arg>        user
 -uuid <arg>        apply actions to specific metadata record
 -validate <arg>    validation schema xsd file to use
```


#### Dump original Argo record to stdout
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -stdout
```

#### Validate Argo against mcp-2 schema
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -validate ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/schema.xsd
```

#### Validate all records against mcp-2 schema and generate report
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -all \
  -validate ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/schema.xsd
```


#### Transform record from draft mcp-2 to mcp-2 and validate but do not update
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/from_mcp2_draft.xsl \
  -validate ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/schema.xsd
```


#### Transform record from draft mcp-2 to mcp-2 and perform in-place update
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/from_mcp2_draft.xsl \
  -update
```

#### Transform mcp-2 profile back to iso19139 and validate
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/to19139.xsl \
  -validate ../mcp-2.0/iso19139/schema.xsd
```

#### Older,


#### Port forward vagrant box postgres and dump all records to stdout
```
ssh -L :9000:localhost:5432  10.11.12.13

java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  \
  -url jdbc:postgresql://127.0.0.1:9000/geonetwork \
  -user geonetwork -pass geonetwork -all   -stdout

```


#### Transform all records by stylesheet and dump to stdout
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://127.0.0.1:5432/geonetwork \
  -user geonetwork -pass geonetwork \
  -all -transform scripts/test1.xslt -stdout 2>&1 | less
```

#### Transform all records and perform in-place update
```
java -cp ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://127.0.0.1:5432/geonetwork \
  -user geonetwork -pass geonetwork \
  -all -transform scripts/test1.xslt -update
```

