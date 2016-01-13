


### GeoNetwork XSLT record editor

#### TODO
- maybe change name mest-cli-bulk-editor or mest-stylesheet-editor
- support reading partial options (eg. Gn instance from file )
- change artifact name from myartifact.jar

### Build

    mvn clean install

### Examples

#### Get help
```
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -help

usage: Updater
 -all                   apply actions on all metadata records
 -help                  show help
 -pass <arg>            password
 -stdout                dump raw metadata record to stdout
 -stdout_with_context   dump metadata record with additional context
                        fields to stdout
 -title                 output id/uuid of the metadata record to stdout
 -transform <arg>       stylesheet xslt file to use for transform
 -update                perform inplace update of the metadata record
 -url <arg>             jdbc connection string, eg.
                        jdbc:postgresql://127.0.0.1/geonetwork
 -user <arg>            user
 -uuid <arg>            apply action to specific metadata record
 -validate <arg>        schema xsd file to use for validation

```

#### Dump Argo to stdout
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -title \
  -stdout

```

#### Validate Argo against schema xsd
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -title \
  -validate  '../schema-plugins/iso19139.mcp-2.0/schema.xsd'

```

#### Transform record from draft to mcp 2 and validate but do not update
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -title \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/from_mcp2_draft.xsl \
  -validate  '../schema-plugins/iso19139.mcp-2.0/schema.xsd'

```


#### Transform record from draft to mcp 2 and perform in-place update
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://geonetwork2/geonetwork -user geonetwork -pass geonetwork \
  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 \
  -title \
  -transform ../mcp-2.0/schema-plugins/iso19139.mcp-2.0/convert/from_mcp2_draft.xsl \
  -update

```


#### Port forward vagrant box postgres and dump all records untransformed to stdout
```
ssh -L :9000:localhost:5432  10.11.12.13

java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  \
  -url jdbc:postgresql://127.0.0.1:9000/geonetwork \
  -user geonetwork -pass geonetwork -all   -stdout

```


#### Transform records and dump to stdout
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://127.0.0.1:5432/geonetwork \
  -user geonetwork -pass geonetwork \
  -all -transform scripts/test1.xslt -stdout 2>&1 | less
```

#### Transform records and in-place update
```
java -cp ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor \
  -url jdbc:postgresql://127.0.0.1:5432/geonetwork \
  -user geonetwork -pass geonetwork \
  -all -transform scripts/test1.xslt -update
```

