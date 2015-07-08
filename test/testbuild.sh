#!/bin/bash

#compile all jdc test files

for file in *.jdc
do
	filejava="${file%.*}"
	filejava="$filejava.java"
	echo "Compiling $file"
	java -cp ../target/classes/ parser.JDCParser $file
	echo "-----"
	diff -b -s $filejava expected/$filejava
	echo "-------------------"
done
for file in exceptions/*.jdc
do
	echo "Exception testing: compiling $file"
	java -cp ../target/classes/ parser.JDCParser $file
	echo "-------------------"
done
echo "Finished!"
