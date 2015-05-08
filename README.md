
## Build

mvn clean install

## Examples


    
    # Get help
    java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -help
    usage: Updater
     -all          do all records
     -help         show help
     -p <arg>      password
     -stdout       dump with context fields to stdout
     -stdout2      dump to stdout
     -t <arg>      xslt file for transform
     -u <arg>      user
     -update       actually update the record in db
     -url <arg>    jdbc connection string, eg.
                   jdbc:postgresql://127.0.0.1/geonetwork
     -uuid <arg>   metadata record uuid

    
    # Dump to stdout
    java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u geonetwork -p     geonetwork -all    -t scripts/test1.xslt -stdout 2>&1 | less

    # Options


------

java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:5432/imosmest -u imosmest -p 1MOSM3ST   -stdout




rsync -avzP  ./geonetwork_updater   imosmest.aodn.org.au 

$ sudo -u postgres pg_dump -Fc geonetwork > geonetwork.dump
$ git clone http://github.com/julian1/geonetwork_updater .
rsync -avzP  target/myartifcat-1.0.0.jar  catalogue-123.aodn.org.au:~/
mkdir target
mv myupdater target

# test
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u geonetwork -p     geonetwork   -uuid 4402cb50-e20a-44ee-93e6-4728259250d2   -stdout

# do the update
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u geonetwork -p geonetwork -all -t trans-2.0.xslt  -update

-----

java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.WebEditor

# 
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://10.11.12.13:5432/geonetwork -u geonetwork -p geonetwork   -uuid 4402cb50-e20a-44ee-93e6-4728259250d2  -t trans-2.0.xslt   -stdout

# 
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://10.11.12.13:5432/imosmest -u imosmest -p imosmest   -uuid b53339be-94b6-4b70-a4a3-c77f15e557f0  -t trans-1.4.xslt     -stdout  | less 

 
-----
get xml,
https://catalogue-123.aodn.org.au/geonetwork/srv/eng/xml.metadata.get?uuid=4402cb50-e20a-44ee-93e6-4728259250d2


----
java -cp  ./myartifcat-1.0.0.jar  au.org.emii.Updater -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u geonetwork -p geonetwork   -uuid 4402cb50-e20a-44ee-93e6-4728259250d2  -t trans-2.0.xslt   -stdout | less

java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.Updater -url jdbc:postgresql://10.11.12.13:5432/geonetwork -u geonetwork -p geonetwork   -uuid 4402cb50-e20a-44ee-93e6-4728259250d2  -t trans-2.0.xslt   -update


need to specify the actual xslt transform to use ... 
- an option to spit to stdout might be nice as well,
- and an option to limit to a particular uuid 



# dump record to stdout

# perform the update in geonetwork
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.Updater -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u meteo -p meteo  -uuid 4402cb50-e20a-44ee-93e6-4728259250d2  -t trans.xslt  -update


java -cp ./target/myartifcat-1.0.0.jar au.org.emii.Updater -help 
java -cp ./target/myartifcat-1.0.0.jar au.org.emii.Updater -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u meteo -p meteo

java -cp ./target/myartifcat-1.0.0.jar au.org.emii.Updater -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u meteo -p meteo -uuid 4402cb50-e20a-44ee-93e6-4728259250d2

-----
mvn clean install
mvn exec:java -Dexec.mainClass="au.org.emii.Updater"

sudo -u postgres psql -d geonetwork -c "select data from metadata where uuid=  '4402cb50-e20a-44ee-93e6-4728259250d2'" 

java -jar saxon9he.jar  ./argo.xml   trans.xslt  | less




---

# copy

ssh catalogue-123.aodn.org.au
sudo -u postgres pg_dump -Fc geonetwork > geonetwork.dump


# local
rsync -avzP catalogue-123.aodn.org.au:~/geonetwork/geonetwork.dump .
---
sudo /etc/init.d/tomcat7_geonetwork_123  stop

sudo -u postgres psql  -c 'drop database geonetwork '
sudo -u postgres pg_restore /vagrant/geonetwork.dump  -C  -d postgres

# or if production,
sudo -u postgres pg_restore  -O -x ./geonetwork.dump  -C  -d postgres
sudo -u postgres psql -d geonetwork -c 'grant all on database geonetwork to meteo ' 
sudo -u postgres psql -d geonetwork -c 'grant all on schema public  to meteo ' 
sudo -u postgres psql -d geonetwork -c 'grant all on all tables in schema public to meteo ' 


--
# copy

ssh  imosmest.aodn.org.au
sudo -u postgres pg_dump -Fc imosmest | less > imosmest.dump
rsync -avzP  imosmest.aodn.org.au:~/imosmest.dump .


# restore

sudo -u postgres psql -c "create role imosmest password 'imosmest' "
# sudo -u postgres pg_restore imosmest.dump -C  -d postgres

sudo -u postgres psql -c "drop database imosmest "
sudo -u postgres psql -c "create database imosmest "
sudo -u postgres psql -d imosmest  -c "create extension postgis "
sudo -u postgres pg_restore /vagrant/imosmest.dump | less
sudo -u postgres pg_restore /vagrant/imosmest.dump -d imosmest

lots of errors but we get the metadata column. eg.,

sudo -u postgres psql -d imosmest -c 'select data from metadata limit 1 '

sudo -u postgres psql -d imosmest -c "select data from metadata where uuid = 'b53339be-94b6-4b70-a4a3-c77f15e557f0'"

sudo -u postgres psql -d imosmest -c "alter role imosmest login "


java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://10.11.12.13:5432/imosmest -u imosmest -p imosmest   -uuid b53339be-94b6-4b70-a4a3-c77f15e557f0      -stdout  | less

