import java.util.Calendar;

import com.studiohartman.jamepad.ControllerAxis;

public class ControllerAxisToButton 
{
	public static long 
		waitMillis = 350,
		waitMillisFast = 20,
		waitReleaseMillis = 350;
	
	public float 
		val = 0f;
	private boolean 
		pressedPos = false,
		pressedNeg = false;
	private long 
		startTimePos = 0,
		startTimeNeg = 0;
	private ControllerAxis 
		axis;
	private boolean
		slowPressPos = true,
		slowPressNeg = true;
	
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
		if(isPositiveFloat())
		{
			this.pressedPos = pressed;
			startTimePos = Calendar.getInstance().getTimeInMillis();
		}
		else
		{
			this.pressedNeg = pressed;
			startTimeNeg = Calendar.getInstance().getTimeInMillis();
		}
	}
	
	public boolean getPressed()
	{
		boolean 
			pressed = isPositiveFloat() ? pressedPos : pressedNeg,
			slowPress = isPositiveFloat() ? slowPressPos : slowPressNeg;
		long startTime = isPositiveFloat() ? startTimePos : startTimeNeg;
		
		if(!pressed)
		{
			long 
				curTime = Calendar.getInstance().getTimeInMillis(),
				waitDiff = (curTime - startTime);
			
			if(waitDiff < waitMillis 
					&& waitDiff >= waitMillisFast
					&& !slowPress)
			{
				setPressed(true);
			}
			else if (waitDiff > waitMillis)
			{
				setSlowPress(!slowPress);
				setPressed(true);
			}
		}
		return pressed;
	}
	
	public void setSlowPress(boolean slowPress)
	{
		if(isPositiveFloat())
		{
			this.slowPressPos = slowPress;
		}
		else {
			this.slowPressNeg = slowPress;
		}
	}
	
	public boolean isPositiveFloat()
	{
		return (val > 0);
	}
}
