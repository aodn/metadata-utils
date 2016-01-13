
### GeoNetwork XSLT record editor

### Build

    mvn clean install

### Examples

#### Get help
```
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -help
usage: Updater
 -all                   all records
 -help                  show help
 -p <arg>               password
 -stdout                dump raw record to stoud
 -stdout_with_context   dump to stdout with additional context fields
 -title                 output id/uuid to stdout
 -transform <arg>       xslt file for transform
 -u <arg>               user
 -update                actually update the record in db
 -url <arg>             jdbc connection string, eg.
                        jdbc:postgresql://127.0.0.1/geonetwork
 -uuid <arg>            specific metadata record uuid to operate on
 -validate <arg>        xsd file for validation
```

#### Validate record

```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  -url jdbc:postgresql://geonetwork2/geonetwork -u geonetwork -p geonetwork -uuid 4402cb50-e20a-44ee-93e6-4728259250d2 -title -validate  '../schema-plugins/iso19139.mcp-2.0/schema.xsd'
```

#### Port forward vagrant box postgres, and dump records untransformed to stdout
```
ssh -L :9000:localhost:5432  10.11.12.13
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:9000/geonetwork -u geonetwork -p geonetwork -all   -stdout
```

#### Dump specific untransformed record (Argo) to stdout
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:9000/geonetwork -u geonetwork -p geonetwork -uuid 4402cb50-e20a-44ee-93e6-4728259250d2    -stdout   
```

#### Transform all records and dump to stdout
```
java -cp  ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u geonetwork -p geonetwork -all -transform scripts/test1.xslt -stdout 2>&1 | less
```

#### Transform and update all records
```
java -cp ./target/myartifcat-1.0.0.jar au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u geonetwork -p geonetwork -all -transform scripts/test1.xslt -update
```

