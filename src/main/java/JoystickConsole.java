import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JoystickConsole extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		TITLE = "Joystick Driver",
		CLOSE_BTN_TEXT = "Close",
		IMG_ICON_LOCATION = System.getProperty("user.dir")+"/images/joystick-sm.png",
		ENABLED_TEXT = "Enabled.",
		DISABLED_TEXT = "Disabled.",
		TOGGLE_HAULT_TEXT = "Pause",
		TOGGLE_ENABLE_TEXT = "Enable";
	private static final Dimension
//		ENABLE_DISABLE_FRAME_SIZE = new Dimension(325,50),
		MIN_DIMENSION = new Dimension(350, 125);
	private static final Color
		ENABLE_COLOR = Color.DARK_GRAY,
		DISABLE_COLOR = Color.RED;
//	private static final int
//		ENABLE_DISABLE_NOTIFY_DISPLAYTIME = 1000;//millis
//	private JScrollPane
//		scrollPane = new JScrollPane();
//	private JTextArea
//		consoleOutput = new JTextArea();
	private JLabel
		statusLabel = new JLabel();
	private JButton
		toggleHaultButton,
		closeButton;
	private ButtonMap
		bm;
	
	public JoystickConsole()
	{
		buildWidgets();
	}
	
	public void setButtonMap(ButtonMap bm)
	{
		this.bm = bm;
		setStatus(bm.getHault());
	}
	
	private void buildWidgets()
	{
		Image img;
		try {
			img = ImageIO.read(new File(IMG_ICON_LOCATION));
			this.setIconImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		scrollPane.setViewportView(consoleOutput);
		JPanel 
			controlPanel = new JPanel(),
			connectPanel = new JPanel();
		
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
		controlPanel.setLayout(fl);
		FlowLayout f2 = new FlowLayout(FlowLayout.RIGHT);
		connectPanel.setLayout(f2);
		
		closeButton = new JButton(CLOSE_BTN_TEXT);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		toggleHaultButton = new JButton();
		toggleHaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean hlt = bm.getHault();
				bm.setHault(!hlt);
				setStatus(!hlt);
			}
		});
		
		controlPanel.add(toggleHaultButton);
		controlPanel.add(statusLabel);
		
		connectPanel.add(closeButton);
		
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(MIN_DIMENSION);
		
//		this.add(scrollPane, BorderLayout.CENTER);
		this.add(new JLabel("Start + (Left / Right) Bumper to Enable/Disable"), BorderLayout.NORTH);
		this.add(controlPanel, BorderLayout.CENTER);
		this.add(connectPanel, BorderLayout.SOUTH);
		centerOnScreen(this);
		this.setVisible(true);
	}
	
	public void setStatus(boolean hault)
	{
		statusLabel.setText(hault?DISABLED_TEXT:ENABLED_TEXT);
		statusLabel.setForeground(hault?DISABLE_COLOR:ENABLE_COLOR);
		
		toggleHaultButton.setText(hault?TOGGLE_ENABLE_TEXT:TOGGLE_HAULT_TEXT);
//		showNotification(hault);
	}
	
//	public void showNotification(boolean hault)
//	{
//		Runnable r = new Runnable() {
//			@Override
//			public void run() {
//				JFrame f = new JFrame();
//				f.setTitle(hault?"Controller Disabled":"Controller Enabled");
//				f.setMinimumSize(ENABLE_DISABLE_FRAME_SIZE);
//				f.setResizable(false);
//				centerOnScreen(f);
//				f.setVisible(true);
//				f.toFront();
//				try {
//					Thread.sleep(ENABLE_DISABLE_NOTIFY_DISPLAYTIME);
//					f.dispose();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		Thread t = new Thread(r);
//		t.start();
//	}
	
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
