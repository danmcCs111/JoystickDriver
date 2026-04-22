#!/bin/bash

cd "$(dirname "$0")"
typeOs=`uname`
jarFiles=$(find target/dependency/ -name *.jar)

if [[ "$typeOs" == "Linux" ]]
then
	classpath=$(echo ${jarFiles[@]} | sed 's/ /:/g')":target/JoystickDriver-0.0.1-SNAPSHOT.jar"
else

	classpath=$(echo ${jarFiles[@]} | sed 's/ /;/g')";target/JoystickDriver-0.0.1-SNAPSHOT.jar"
fi

echo "$@"
echo $classpath


java -cp "$classpath" JoystickDriver "$@"

cd -
