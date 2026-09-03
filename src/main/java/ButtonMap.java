import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
	private static final ControllerAxisToButton 
		TOGGLE_MOUSE_SIM = new ControllerAxisToButton(ControllerAxis.TRIGGERRIGHT),
		LEFTX = new ControllerAxisToButton(ControllerAxis.LEFTX),
		LEFTY = new ControllerAxisToButton(ControllerAxis.LEFTY),
		RIGHTX = new ControllerAxisToButton(ControllerAxis.RIGHTX),
		RIGHTY = new ControllerAxisToButton(ControllerAxis.RIGHTY);
	private static final ControllerAxisToButton [] 
		axis = new ControllerAxisToButton[] 
		{
			new ControllerAxisToButton(ControllerAxis.TRIGGERLEFT),
			TOGGLE_MOUSE_SIM,
			LEFTX,
			LEFTY,
			RIGHTX,
			RIGHTY
		};
	private static final float
		THRESHOLD_POSITIVE = .6f,
		THRESHOLD_NEGATIVE = -.6f;
	private static final String [] []
			keyCombo = new String [] []
	{
		{
			ControllerButton.START.toString(),
			ControllerButton.LEFTBUMPER.toString()
		},
		{
			ControllerButton.START.toString(),
			ControllerButton.RIGHTBUMPER.toString()
		}
	};
	private static int
		CURSOR_MOVE_AMT = 25,
		COMBO_HAULT_DETECT = 1000,
		MOUSE_SIM_TOGGLE_WAIT = 1000,
		SLEEP_INTERVAL = 25;
	private static boolean 
		mouseMove = false,
		hault = false;
	
	private JoystickConsole
		js;
	
	public ButtonMap(JoystickConsole js)
	{
		this.js = js;
	}
	
	public ArrayList<ControllerButton> getControllerButtonsJustPressed(ControllerIndex controller)
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
	
	public ArrayList<ControllerButton> getControllerButtonsPressed(ControllerIndex controller)
	{
		ArrayList<ControllerButton> pressedButtons = new ArrayList<ControllerButton>();
		
		for(ControllerButton cb : controllerButtons)
		{
			try {
				if(controller.isButtonPressed(cb))
				{
					pressedButtons.add(cb);
				}
			} catch (ControllerUnpluggedException e) {
				e.printStackTrace();
			}
		}
		
		return pressedButtons;
	}
	
	private boolean detectComboPress(ArrayList<ControllerButton> cbs)
	{
		int [] count = new int [keyCombo.length];
		for(ControllerButton cb : cbs)
		{
			for(int i = 0; i < keyCombo.length; i++)
			{
				for(String det : keyCombo[i])
				{
					if(cb.toString().equals(det))
					{
						count[i]++;
					}
				}
			}
		}
		
		for(int i = 0; i < keyCombo.length; i++)
		{
			if(count[i] == keyCombo[i].length)
				return true;
		}
		
		return false;
	}
	
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
					ControllerIndex 
						currController = controllers.getControllerIndex(0);
					ArrayList<ControllerButton> 
						cbs = getControllerButtonsJustPressed(currController),
						cbsP = getControllerButtonsPressed(currController);
					
					boolean combo = detectComboPress(cbsP);
					if(combo)
					{
						setHault(!hault);//toggle
						try {
							Thread.sleep(COMBO_HAULT_DETECT);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					
					if(!hault)
					{
						for(ControllerButton cb : cbs)
						{
							if(!mouseMove)
							{
								sendButtonPress(cb.toString());
							}
							else
							{
								switch(cb)
								{
								case DPAD_UP:
									MouseSim.robotKeyPress(KeyEvent.VK_UP);
									break;
								case DPAD_DOWN:
									MouseSim.robotKeyPress(KeyEvent.VK_DOWN);
									break;
								case DPAD_LEFT:
									MouseSim.robotKeyPress(KeyEvent.VK_LEFT);
									break;
								case DPAD_RIGHT:
									MouseSim.robotKeyPress(KeyEvent.VK_RIGHT);
									break;
								case A:
									MouseSim.robotKeyPress(KeyEvent.VK_ENTER);
									break;
								case B:
									MouseSim.robotKeyPress(KeyEvent.VK_ESCAPE);
									break;
								case LEFTSTICK:
									MouseSim.robotKeyPress(KeyEvent.VK_SPACE);
									break;
								case RIGHTSTICK:
									MouseSim.robotKeyPress(KeyEvent.VK_F);
									break;
								case X:
									break;
								case Y:
									break;
								case LEFTBUMPER:
									MouseSim.robotCursorClick(InputEvent.getMaskForButton(MouseEvent.BUTTON1));
									break;
								case RIGHTBUMPER:
									MouseSim.robotCursorClick(InputEvent.getMaskForButton(MouseEvent.BUTTON3));
									break;
								default:
									break;
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
	
	public void startAxisToButtonThread(ControllerManager controllers)
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run() 
			{
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
									if(axis[i] == TOGGLE_MOUSE_SIM)
									{
										mouseMove = !mouseMove;
										js.setText("Mouse Move: " + mouseMove);
										try {
											Thread.sleep(MOUSE_SIM_TOGGLE_WAIT);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
									
									if(mouseMove)
									{
										if(axis[i] == LEFTX)
										{
											MouseSim.robotCursorMove(
												axis[i].isPositiveFloat() ? CURSOR_MOVE_AMT : -CURSOR_MOVE_AMT, 
												0
											);
										}
										else if(axis[i] == LEFTY)
										{
											MouseSim.robotCursorMove(
												0,
												axis[i].isPositiveFloat() ? -CURSOR_MOVE_AMT : CURSOR_MOVE_AMT 
											);
										}
										else if(axis[i] == RIGHTX)
										{
											
										}
										else if(axis[i] == RIGHTY)
										{
											MouseSim.robotScrollWheelMove(!axis[i].isPositiveFloat());
										}
										axis[i].setPressed(false);
									}
									
									else
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
	
	public void toggleMouseSim()
	{
		
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
