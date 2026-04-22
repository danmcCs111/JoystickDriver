import java.util.Calendar;

import com.studiohartman.jamepad.ControllerAxis;

public class ControllerAxisToButton 
{
	public static int 
		waitMillis = 350;
	
	public int 
		wait = waitMillis;
	public float 
		val = 0f;
	private boolean 
		pressed = false;
	private long 
		startTime = 0;
	private ControllerAxis 
		axis;
	
	public ControllerAxisToButton(ControllerAxis axis)
	{
		this.axis = axis;
	}
	
	public ControllerAxis getControllerAxis()
	{
		return axis;
	}
	
	public void setPressed(boolean pressed)
	{
		this.pressed = pressed;
		startTime = Calendar.getInstance().getTimeInMillis();
	}
	
	public boolean getPressed()
	{
		if(pressed)
		{
			long curTime = Calendar.getInstance().getTimeInMillis();
			if(curTime - startTime > waitMillis)
			{
				pressed = false;
			}
		}
		return pressed;
	}
	
	public boolean isPositiveFloat()
	{
		return (val > 0);
	}
}
