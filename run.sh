if [ $# -eq 0 ]; then
    echo "No arguments provided. Please specify the library on which Main is run. i.e application or editor"
    exit 1
fi

./compile.sh

# Execute package main
cd bin
java $1.Main
