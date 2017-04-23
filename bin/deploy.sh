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


cp /GITHUB/SparkShellUtilities/out/artifacts/s2u_jar/s2u.jar release
cp /GITHUB/claerity-cloudera/XWARE42/shop-crawl/out/artifacts/any23_extractor_tool_jar/any23-extractor-tool.jar release
cp /GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-profiler/out/artifacts/etosha_profiler_jar/etosha-profiler.jar release

cp /sparkws/bin/gephi-toolkit-0.9.2-20170113.202843-77-all.jar extlibs

zip -d release/any23-extractor-tool.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
zip -d release/etosha-profiler.jar	 META-INF/*.RSA META-INF/*.DSA META-INF/*.SF

scp -r extlibs root@62.75.210.156:/srv/www/htdocs/repo/etosha
scp -r release root@62.75.210.156:/srv/www/htdocs/repo/etosha

