import java.util.ArrayList;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;

public class ButtonMap 
{
	private static final ControllerButton []
		controllerButtons = new ControllerButton []
	{
		ControllerButton.A,
		ControllerButton.B,
		ControllerButton.X,
		ControllerButton.Y,
		ControllerButton.START,
		ControllerButton.BACK,
		ControllerButton.LEFTBUMPER,
		ControllerButton.RIGHTBUMPER,
		ControllerButton.LEFTSTICK,
		ControllerButton.RIGHTSTICK,
		ControllerButton.DPAD_DOWN,
		ControllerButton.DPAD_LEFT,
		ControllerButton.DPAD_RIGHT,
		ControllerButton.DPAD_UP,
		ControllerButton.GUIDE
	};
	private static final float
		THRESHOLD_POSITIVE = .6f,
		THRESHOLD_NEGATIVE = -.6f;
	
	private JoystickConsole
		js;
//	private static final String []
//		keyCombo = new String [] {
//				ControllerButton.START.toString(),
//				ControllerButton.BACK.toString()
//		};
	private static boolean 
		hault = false;
	private static int
		SLEEP_INTERVAL = 25;
	
	public ButtonMap(JoystickConsole js)
	{
		this.js = js;
	}
	
	public ArrayList<ControllerButton> getControllerButtonsPressed(ControllerIndex controller)
	{
		ArrayList<ControllerButton> pressedButtons = new ArrayList<ControllerButton>();
		
		for(ControllerButton cb : controllerButtons)
		{
			try {
				if(controller.isButtonJustPressed(cb))
				{
					pressedButtons.add(cb);
				}
			} catch (ControllerUnpluggedException e) {
				e.printStackTrace();
			}
		}
		
		return pressedButtons;
	}
	
//	private boolean detectComboPress(ArrayList<ControllerButton> cbs)
//	{
//		String [] detect = new String [keyCombo.length];
//		int count = 0;
//		for(ControllerButton cb : cbs)
//		{
//			for(String det : detect)
//			{
//				if(cb.toString().equals(det))
//				{
//					count++;
//				}
//			}
//		}
//		return (count == detect.length);
//	}
	
	public boolean getHault()
	{
		return hault;
	}
	public void setHault(boolean isHault)
	{
		hault = isHault;
		this.js.setStatus(hault);
	}
	
	public void startButtonThread(ControllerManager controllers)
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run() 
			{
				while(true) 
				{
					controllers.update();
					ControllerIndex currController = controllers.getControllerIndex(0);
					ArrayList<ControllerButton> cbs = getControllerButtonsPressed(currController);
					
					if(!hault)
					{
						for(ControllerButton cb : cbs)
						{
							sendButtonPress(cb.toString());
						}
					}
					
//					boolean combo = detectComboPress(cbs);
//					if(combo)
//					{
//						setHault(!hault);//toggle
//					}
					
					try {
						Thread.sleep(SLEEP_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void startAxisToButtonThread(ControllerManager controllers)
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run() 
			{
				ControllerAxisToButton [] axis = new ControllerAxisToButton[] {
					new ControllerAxisToButton(ControllerAxis.TRIGGERLEFT),
					new ControllerAxisToButton(ControllerAxis.TRIGGERRIGHT),
					new ControllerAxisToButton(ControllerAxis.LEFTX),
					new ControllerAxisToButton(ControllerAxis.LEFTY),
					new ControllerAxisToButton(ControllerAxis.RIGHTX),
					new ControllerAxisToButton(ControllerAxis.RIGHTY)
				};
				
				while(true)
				{
					if(!hault)
					{
						controllers.update();
						ControllerIndex currController = controllers.getControllerIndex(0);
						for(int i = 0; i < axis.length; i++)
						{
							try {
								axis[i].val = currController.getAxisState(axis[i].getControllerAxis());
							} catch (ControllerUnpluggedException e) {
								e.printStackTrace();
							}
							float val = axis[i].val;
							
							if(val >= THRESHOLD_POSITIVE || val <= THRESHOLD_NEGATIVE)
							{
								boolean press = axis[i].getPressed();
								if(press)
								{
									sendJoyPress(
											axis[i].getControllerAxis().toString() + " " +
													axis[i].isPositiveFloat()
											);
									axis[i].setPressed(false);
								}
							}
						}
					}
					try {
						Thread.sleep(SLEEP_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void sendJoyPress(String axis)
	{
		js.addOutput(axis);
		String resp = HttpRequestJoy.executePutRequest(
				JoystickDriver.ENDPOINT,
				JoystickDriver.PORT_NUMBER,
				axis,
				JoystickDriver.REQUEST_TYPE_HEADER_KEY,
				JoystickDriver.REQUEST_FUNCTION
		);
		js.addOutput(resp);
	}
	
	public void sendButtonPress(String btn)
	{
		js.addOutput(btn);
		String resp = HttpRequestJoy.executePutRequest(
				JoystickDriver.ENDPOINT,
				JoystickDriver.PORT_NUMBER,
				btn,
				JoystickDriver.REQUEST_TYPE_HEADER_KEY,
				JoystickDriver.REQUEST_FUNCTION
		);
		js.addOutput(resp);
	}
}
