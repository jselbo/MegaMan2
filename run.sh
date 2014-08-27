cd ./bin/
rm -rf *
cd ..
cp -r res bin
javac -d ./bin **/*.java
cd ./bin/
java application.Main