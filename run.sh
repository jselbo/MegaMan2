if [ $# -eq 0 ]; then
    echo "Usage: run.sh {application|editor}"
    exit 1
fi

./compile.sh

# Execute package main
cd bin
java $1.Main
