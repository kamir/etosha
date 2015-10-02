#!/bin/bash
#
# First, wee need a validator tool
#
#
# git clone https://github.com/cloudera/cm_ext
# mvn install

#
# Now we validate the CSD
#
# java -jar /Volumes/MyExternalDrive/GITHUB/cm_ext/validator/target/validator.jar -s /Volumes/MyExternalDrive/GITHUB/DocWorld.PARCEL/src/TTFAQ-1.0/descriptor/service.sdl


#
# After validation we package the CSD in a JAR and deploy to the Gateway. 
#
# To create the CSD-Package on a DEV-machine we use:
# 
# mvn assembly:single 



#
# On the Gateway Node we stage the Parcels
# (Documentation is here: https://github.com/cloudera/cm_ext)
#
# We copy the artifact to the Gateway
#
# scp r*.sh mirko.kaempf@training03.sjc.cloudera.com:
# scp ./../target/TTFAQ*.jar mirko.kaempf@training03.sjc.cloudera.com:
#
# COPY TO TC
# scp /GITHUB/DocWorld/dist/DocWorld.war mirko.kaempf@training03.sjc.cloudera.com:/var/run/cloudera-scm-agent/process/10401-ttfaq-TTFAQ_SERVER/scripts/tools
# scp /GITHUB/DocWorld/dist/DocWorld.war mirko.kaempf@training03.sjc.cloudera.com:runtime

#
# COPY WAR-file to TC
#
scp /GITHUB/DocWorld/dist/DocWorld.war mirko.kaempf@training07.sjc.cloudera.com:runtime

#
# restart the CM-Server
#
ssh mirko.kaempf@training07.sjc.cloudera.com sudo ./runtime/controlTC.sh

#
# Now we restart the CM-Server 
#
# ssh mirko.kaempf@training03.sjc.cloudera.com sudo ./rrestartCMS.sh



#
# Using the CM REST-API we could deploy or update a parcel now.
#



#
# Finally we open the Browser and login into CM
#
open -a /Applications/Firefox.app http://training07.mtv.cloudera.com:9090/DocWorld
