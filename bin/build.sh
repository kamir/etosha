 #!/bin/sh

#-------------------------------------------------------------------------------
#  Prep the target environment
#
#  We assume, that the project is cloned into:
#     /home/$USER/workspace
#
#-------------------------------------------------------------------------------
WSHOST=127.0.0.1
USER=cloudera

#-------------------------------------------------------------------------------
# Build the Etosha toolbox ...
#-------------------------------------------------------------------------------

cd ..
 
JAVA_HOME=/opt/jdk1.8.0_101
export JAVA_HOME
 
mvn clean compile install package assembly:single

#mkdir /home/$USER/etc
#mkdir /home/$USER/etc/etosha
 
#cp etc/smw-site.xml /home/$USER/etc/etosha/smw-site.xml
#cp etc/smw-site.xml /etc/etosha/smw-site.xml

cd /usr/sbin
sudo ln -s /home/$USER/workspace/etosha/etosha-parent/bin/ecl.sh ecl
sudo chmod 777 ecl

echo "=> Please replace the password in file:   /home/$USER/etc/etosha/smw-site.xml   to login to SMW."
echo "=> Set JAVA_HOME to Java 8."

echo $JAVA_HOME





git add .
git commit -m "autobuild"
git push

