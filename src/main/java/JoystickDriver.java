import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;

/* https://sourceforge.net/projects/libusb/files/libusb-1.0/libusb-1.0.30-rc1/libusb-1.0.30-rc1.7z/download
 * For 64-bit applications: Copy libusb-1.0.dll from the MS64\dll\ folder to C:\Windows\System32.
 * For 32-bit applications: Copy libusb-1.0.dll from the MS32\dll\ folder to C:\Windows\SysWOW64.
 */
public class JoystickDriver 
{
	public static final String
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		REQUEST_FUNCTION = "Joystick_Button";
	public static int 
		PORT_NUMBER = 9090;
	
	private static ControllerManager 
		controllers;
	private static ControllerIndex 
		currController;
	private static ButtonMap 
		bm;
	
	public JoystickDriver()
	{
		JoystickConsole js = new JoystickConsole();
		controllers = new ControllerManager();
		controllers.initSDLGamepad();
		currController = controllers.getControllerIndex(0);
		bm = new ButtonMap(js);
	}
	
	public void pollController()
	{
		bm.startAxisToButtonThread(controllers, currController);
		bm.startButtonThread(controllers, currController);
		while(true)
		{
			try {
				Thread.sleep(5000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String [] args)
	{
		JoystickDriver jd = new JoystickDriver();
		jd.pollController();
	}
}
