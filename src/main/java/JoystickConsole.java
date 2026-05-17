import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JoystickConsole extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		TITLE = "Joystick Driver",
		CLOSE_BTN_TEXT = "Close";
	private static final Dimension
		MIN_DIMENSION = new Dimension(350, 100);
//	private JScrollPane
//		scrollPane = new JScrollPane();
//	private JTextArea
//		consoleOutput = new JTextArea();
	private JButton
		closeButton;
	
	public JoystickConsole()
	{
		buildWidgets();
	}
	
	private void buildWidgets()
	{
//		scrollPane.setViewportView(consoleOutput);
		
		closeButton = new JButton(CLOSE_BTN_TEXT);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.CENTER);
		this.setLayout(fl);
		this.setMinimumSize(MIN_DIMENSION);
		
//		this.add(scrollPane, BorderLayout.CENTER);
		this.add(closeButton);
		centerOnScreen(this);
		this.setVisible(true);
	}
	
	public void addOutput(String out)
	{
//		consoleOutput.setText(out + "\n");//just latest output to save memory.
	}
	
	public static void centerOnScreen(Component comp)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point loc = new Point(0, 0);
		
		double 
			rw = screenSize.getWidth(),
			rh = screenSize.getHeight(),
			w = comp.getWidth(),
			h = comp.getHeight();
		int
			x = loc.x + (int)((rw/2.0) - (w / 2.0)),
			y = loc.y + (int)((rh/2.0) - (h/2.0));
		y=(y<0)?0:y;
		
		comp.setLocation(x, y);
	}

}
