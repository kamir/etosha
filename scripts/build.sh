 #!/bin/sh

#-------------------------------------------------------------------------------
#  Prep the target environment
#-------------------------------------------------------------------------------
WSHOST=127.0.0.1
USER=cloudera

#-------------------------------------------------------------------------------
# Build the Etosha toolbox ...
#-------------------------------------------------------------------------------
cd ..
cd etosha-parent
 
JAVA_HOME=/opt/jdk1.8.0_101
export JAVA_HOME
 
mvn clean compile install package assembly:single

cp ./../etc/smw-site.xml /home/$USER/etc/etosha/smw-site.xml
cp ./../etc/smw-site.xml /etc/etosha/smw-site.xml

cd /usr/sbin
sudo ln -s /home/cloudera/workspace/etosha/etosha-parent/bin/ecl.sh ecl
sudo chmod 777 ecl

#-------------------------------------------------------------------------------
# deploy the Etosha CLI ...
#-------------------------------------------------------------------------------
#ssh $USER@WSHOST sudo mkdir /home/$USER/etc
#ssh $USER@WSHOST sudo mkdir /home/$USER/etc/etosha 
#ssh $USER@WSHOST sudo mkdir /etc/etosha

echo "=> replace the password to login to SMW."
echo "=> set JAVA_HOME to Java 8."






