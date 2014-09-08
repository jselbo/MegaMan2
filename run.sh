if [ $# -eq 0 ]; then
    echo "No arguments provided. Please specify the library on which Main is run. i.e application or editor"
    exit 1
fi
mkdir -p bin
cd ./bin/
rm -rf *
cd ..
cp -r res bin
javac -d ./bin **/*.java
cd ./bin/
java $1.Main