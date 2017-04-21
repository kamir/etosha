cd ..
mvn clean compile package install 
git add .
git commit -m "autobuild"
git push

