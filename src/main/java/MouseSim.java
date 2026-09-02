import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class MouseSim 
{
	private static int 
		MOUSE_WHEEL_MOVE_AMOUNT = 15;
	private static Robot 
		robot;
	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public static void robotKeyPress(int keyCode)
	{
		robot.keyPress(keyCode);
		robot.keyRelease(keyCode);
	}
	public static void robotCursorMove(int xAmt, int yAmt)
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		robot.mouseMove(p.x + xAmt, p.y + yAmt);
	}
	public static void robotScrollWheelMove(boolean isPositive)
	{
		robot.mouseWheel(isPositive?MOUSE_WHEEL_MOVE_AMOUNT:-MOUSE_WHEEL_MOVE_AMOUNT);
	}
	public static void robotCursorClick(int mouseButton)
	{
		robot.mousePress(mouseButton);
		robot.mouseRelease(mouseButton);
	}
}
