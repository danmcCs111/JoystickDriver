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
//		ControllerButton.DPAD_DOWN,
//		ControllerButton.DPAD_LEFT,
//		ControllerButton.DPAD_RIGHT,
//		ControllerButton.DPAD_UP,
		ControllerButton.GUIDE
	};
	private static final float
		THRESHOLD_POSITIVE = .6f,
		THRESHOLD_NEGATIVE = -.6f;
	
	public ButtonMap()
	{
		
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
	
	public void startButtonThread(ControllerManager controllers, ControllerIndex currController)
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run() 
			{
				while(true) 
				{
					controllers.update(); 
					ArrayList<ControllerButton> cbs = getControllerButtonsPressed(currController);
					
					for(ControllerButton cb : cbs)
					{
						sendButtonPress(cb.toString());
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void startAxisToButtonThread(ControllerManager controllers, ControllerIndex currController)
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run() 
			{
				ControllerAxisToButton [] axis = new ControllerAxisToButton[] {
//					new ControllerAxisToButton(ControllerAxis.LEFTX),
//					new ControllerAxisToButton(ControllerAxis.LEFTY),
					new ControllerAxisToButton(ControllerAxis.RIGHTX),
					new ControllerAxisToButton(ControllerAxis.RIGHTY)
				};
				
				while(true)
				{
					controllers.update();
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
					try {
						Thread.sleep(10L);
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
		System.out.println(axis);
		HttpRequestJoy.executePutRequest(
				JoystickDriver.ENDPOINT,
				JoystickDriver.PORT_NUMBER,
				axis,
				JoystickDriver.REQUEST_TYPE_HEADER_KEY,
				JoystickDriver.REQUEST_FUNCTION
		);
	}
	
	public void sendButtonPress(String btn)
	{
		System.out.println(btn);
		HttpRequestJoy.executePutRequest(
				JoystickDriver.ENDPOINT,
				JoystickDriver.PORT_NUMBER,
				btn,
				JoystickDriver.REQUEST_TYPE_HEADER_KEY,
				JoystickDriver.REQUEST_FUNCTION
		);
	}
}
