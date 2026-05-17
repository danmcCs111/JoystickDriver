import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JoystickConsole extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		TITLE = "Joystick Output Console";
	private static final Dimension
		MIN_DIMENSION = new Dimension(500, 400);
	private JScrollPane
		scrollPane = new JScrollPane();
	private JTextArea
		consoleOutput = new JTextArea();
	
	public JoystickConsole()
	{
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		scrollPane.setViewportView(consoleOutput);
		
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(MIN_DIMENSION);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public void addOutput(String out)
	{
		consoleOutput.append(out + "\n");
		consoleOutput.setCaretPosition(consoleOutput.getText().length()-1);
	}

}
