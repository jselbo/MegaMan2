#!/bin/bash

echo "Cleaning bin/ directory..."
# Create and clean bin/
mkdir -p bin
rm -rf bin/*

echo "Copying resources..."
# Copy resources to bin/
cp -r res bin

echo "Compiling..."
# Compile all Java files in src/
find src -name "*.java" | xargs javac -d bin -cp ".;lib/gson-2.3.1.jar"
