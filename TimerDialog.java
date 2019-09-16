import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class TimerDialog extends JDialog {
	
	/*  DT354/2
	 *  Malachy Keaveny 
	 * C16775539 */
	
	// Represents the number of seconds that the countdown will be performed for.
	private long seconds;
	
	// Menu components.
	JTextField hourField, minField, secField;
	JLabel hourLabel, minLabel, secLabel;
	JButton startButton = new JButton("START");

	public TimerDialog(Frame owner, long seconds, boolean modality) {
		super(owner, modality);
		this.seconds = seconds;
		initComponents();
	}
	
	// Sets up display.
	private void initComponents() {
		setTitle("Initialise Timer");	
		setLayout(new BorderLayout());
		
		Font displayFont = new Font("Arial", Font.BOLD, 16);
		Font labelFont = new Font("Arial", Font.BOLD, 12);
		
		JPanel displayPanel = new JPanel(new GridLayout(1,3));
				
		JPanel hourPanel = new JPanel(new BorderLayout());
		hourField = new JTextField(5);
		hourField.setHorizontalAlignment(JTextField.CENTER);
		hourField.setFont(displayFont);
		hourField.setText("00");
		hourLabel = new JLabel("Hours");
		hourLabel.setHorizontalAlignment(JTextField.CENTER);
		hourLabel.setFont(labelFont);
		hourPanel.add(hourField, BorderLayout.CENTER);
		hourPanel.add(hourLabel, BorderLayout.SOUTH);
		
		displayPanel.add(hourPanel);
		
		JPanel minPanel = new JPanel(new BorderLayout());
		minField = new JTextField(5);
		minField.setHorizontalAlignment(JTextField.CENTER);
		minField.setFont(displayFont);
		minField.setText("00");
		minLabel = new JLabel("Minutes");
		minLabel.setHorizontalAlignment(JTextField.CENTER);
		minLabel.setFont(labelFont);
		minPanel.add(minField, BorderLayout.CENTER);
		minPanel.add(minLabel, BorderLayout.SOUTH);
		
		displayPanel.add(minPanel);
		
		JPanel secPanel = new JPanel(new BorderLayout());
		secField = new JTextField(5);
		secField.setHorizontalAlignment(JTextField.CENTER);
		secField.setFont(displayFont);
		secField.setText("00");
		secLabel = new JLabel("Seconds");
		secLabel.setHorizontalAlignment(JTextField.CENTER);
		secLabel.setFont(labelFont);
		secPanel.add(secField, BorderLayout.CENTER);
		secPanel.add(secLabel, BorderLayout.SOUTH);
		
		displayPanel.add(secPanel);
		
		add(displayPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.add(startButton);
		
		// TODO: This start action listener will be invoked when the start button is clicked.
		// It should take the values from the three text fields and try to convert them into integer values, and then check for NumberFormatExceptions 
		// and for the minute and second values between 0 and 59.
		startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	int hour;
            	int mins;
            	int secs;
                try {
                	// the fields for hours, numbers and seconds are Strings so they need to be converted to ints so we can work with them
                	hour = Integer.parseInt(hourField.getText());
                	mins = Integer.parseInt(minField.getText());
                	secs = Integer.parseInt(secField.getText());
                	// multiply hours and minutes as necessary to convert them to seconds
                	seconds = (hour*60*60) + (mins * 60) + secs;
                }
                	// trap the user as long as they enter a character that isn't a number
                	catch (NumberFormatException e)
                    {
                	// display error message
                	JOptionPane.showMessageDialog(null, "Only use numbers!", "ERROR", JOptionPane.ERROR_MESSAGE);
                      return;   
                    }
                
                // also trap the user of they enter a number or minute greater than 59
                if (mins > 59 || secs > 59) {
                	//display error message
            		JOptionPane.showMessageDialog(null, "Minutes and Seconds must be less than 59!", "ERROR", JOptionPane.ERROR_MESSAGE);
            	}
                else 
                dispose();
            }
        });
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		setSize(300, 150);
		setVisible(true);
		
	}
	
	public long getSeconds() {
		return (long)seconds;
	}

}

