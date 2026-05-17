import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JoystickConsole extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		TITLE = "Joystick Output Console",
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
		this.setVisible(true);
	}
	
	public void addOutput(String out)
	{
//		consoleOutput.setText(out + "\n");//just latest output to save memory.
	}

}
