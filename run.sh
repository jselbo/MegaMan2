mkdir -p bin
cd ./bin/
rm -rf *
cd ..
cp -r res bin
javac -d ./bin **/*.java
cd ./bin/
java $1.Main