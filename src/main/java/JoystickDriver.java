import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerUnpluggedException;

public class JoystickDriver 
{
	/* https://sourceforge.net/projects/libusb/files/libusb-1.0/libusb-1.0.30-rc1/libusb-1.0.30-rc1.7z/download
	 * For 64-bit applications: Copy libusb-1.0.dll from the MS64\dll\ folder to C:\Windows\System32.
	 * For 32-bit applications: Copy libusb-1.0.dll from the MS32\dll\ folder to C:\Windows\SysWOW64.
	*/
	public static void main(String [] args)
	{
		ControllerManager controllers = new ControllerManager();
		controllers.initSDLGamepad();
		//Print a message when the "A" button is pressed. Exit if the "B" button is pressed 
		//or the controller disconnects.
		ControllerIndex currController = controllers.getControllerIndex(0);

		while(true) {
		  controllers.update(); //If using ControllerIndex, you should call update() to check if a new controller
		                        //was plugged in or unplugged at this index.
		  try {
		    if(currController.isButtonPressed(ControllerButton.A)) {
		      System.out.println("\"A\" on \"" + currController.getName() + "\" is pressed");
		    }
		    if(currController.isButtonPressed(ControllerButton.B)) {
		      break;
		    }
		  } catch (ControllerUnpluggedException e) {   
		    break;
		  }
		}
	}
}
