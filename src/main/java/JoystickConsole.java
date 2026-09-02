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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JoystickConsole extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE = "Joystick Driver",
		ABOUT_TITLE_DIALOG = "Controller Mapping",
		ABOUT_MENU_ITEM = "About",
		ABOUT_CLI_NEWLINE_ARG=";n;",
		ABOUT_DIALOG_MESSAGE = 
			"Left/Right Triggers - Gain focus back to 'Video Launcher' \n" + 
			"Left/Right Bumper Buttons - Restore/Minimize Open Videos / Video Channels \n" + 
			"A - Enter/Select \n" +
			"B - Escape/Cancel \n" + 
			"X - Close Open Videos / Video Channels OR Launch homepage \n" + 
			"Y - Open VideoBookmarks Dialog to Open Videos / Video Channels OR Update Video Channel \n" +
			"Start - Minimize / Restore 'Video Launcher' Window \n" +
			"Back - Close Video Playing \n" + 
			"Left Stick Button - Pause / Play \n" +
			"Right Stick Button - Fullscreen / Restore \n",
		CLOSE_BTN_TEXT = "Close",
		IMG_ICON_LOCATION = System.getProperty("user.dir")+"/images/joystick-sm.png",
		ENABLED_TEXT = "Enabled.",
		DISABLED_TEXT = "Disabled.",
		TOGGLE_HAULT_TEXT = "Pause",
		TOGGLE_ENABLE_TEXT = "Enable";
	private static final Dimension
		MIN_DIMENSION = new Dimension(350, 150);
	private static final Color
		ENABLE_COLOR = Color.DARK_GRAY,
		DISABLE_COLOR = Color.RED;
	
	private JMenuItem
		about;
	private JLabel
		textLabel = new JLabel(),
		statusLabel = new JLabel();
	private JButton
		toggleHaultButton,
		closeButton;
	private ButtonMap
		bm;
	private Image 
		img = null;
	
	public JoystickConsole()
	{
		buildWidgets();
	}
	
	public static void setAboutDialogText(String dialogText)
	{
		dialogText = dialogText.replaceAll(ABOUT_CLI_NEWLINE_ARG, System.lineSeparator());
		ABOUT_DIALOG_MESSAGE = dialogText;
	}
	
	public void setButtonMap(ButtonMap bm)
	{
		this.bm = bm;
		setStatus(bm.getHault());
	}
	
	private void buildWidgets()
	{
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
		
		JMenuBar jmb = new JMenuBar();
		about = new JMenuItem();
		about.setText(ABOUT_MENU_ITEM);
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(
					null, 
					ABOUT_DIALOG_MESSAGE,
					ABOUT_TITLE_DIALOG,
					JOptionPane.PLAIN_MESSAGE,
					new ImageIcon(img)
				);
			}
		});
		
		jmb.add(about);
		
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
		controlPanel.add(textLabel);
		
		connectPanel.add(closeButton);
		
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(MIN_DIMENSION);
		
		this.setJMenuBar(jmb);
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
	}
	
	public void setError(String error)
	{
		textLabel.setText(" " + error);//TODO.
	}
	
	public void setText(String output)
	{
		textLabel.setText(" " + output);
	}
	
	public void addOutput(String output)
	{
//		textLabel.setText(output);
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
