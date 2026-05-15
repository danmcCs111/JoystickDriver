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

../joystick_mouse_config/launch_antimicrox-windows.sh&
java -cp "$classpath" JoystickDriver "$@"; ../joystick_mouse_config/antimicrox-3.5.1-PortableWindows-AMD64/bin/antimicrox.exe --profile ../joystick_mouse_config/video-launcher_joystick.gamecontroller

cd -
