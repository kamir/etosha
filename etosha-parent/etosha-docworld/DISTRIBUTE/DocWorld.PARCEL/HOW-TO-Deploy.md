#
# We need a validator tool
#

git clone https://github.com/cloudera/cm_ext
mvn install




#
# FUSEKI runs on JAVA 8 only !!!
# http://tecadmin.net/install-java-8-on-centos-rhel-and-fedora/#
#


#
# First we validate the CSD
#

java -jar /Volumes/MyExternalDrive/GITHUB/cm_ext/validator/target/validator.jar -s ./src/TTFAQ-1.0/descriptor/service.sdl


#
# After validation we package the CSD in a JAR and deploy to the Gateway. 
#
# To create the CSD-Package on a DEV-machine we use:
#

mvn assembly:single 



#
# On the Gateway Node we stage the Parcels
# (Documentation is here: https://github.com/cloudera/cm_ext)
#
# We copy the artifact to the Gateway
#

scp TTFAQ*.jar mirko.kaempf@training03.sjc.cloudera.com:
ssh mirko.kaempf@training03.sjc.cloudera.com sudo ./deployCSD.sh



#
# Now we restart the CM-Server 
#

ssh mirko.kaempf@training03.sjc.cloudera.com sudo ./restartCSD.sh



#
# Using the CM REST-API we could deploy or update a parcel now.
#


