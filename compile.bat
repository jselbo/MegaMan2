@echo off

echo Cleaning bin/ directory...
rmdir /s /q bin
mkdir bin

echo Copying resources...
xcopy res\* bin\res /s /i

echo Compiling...
dir /s /B *.java > sources.txt
javac -d bin @sources.txt
del sources.txt
