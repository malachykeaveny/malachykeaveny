


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import net.miginfocom.swing.MigLayout;

public class Timer extends JFrame {
	
	/*  DT354/2
	 *  Malachy Keaveny 
	 * C16775539 */
	
	// Interface components
	
	// Fonts to be used
	Font countdownFont = new Font("Arial", Font.BOLD, 20);
	Font elapsedFont = new Font("Arial", Font.PLAIN, 14);
	
	// Labels and text fields
	JLabel countdownLabel = new JLabel("Seconds remaining:");
	JTextField countdownField = new JTextField(15);
	JLabel elapsedLabel = new JLabel("Time running:");
	JTextField elapsedField = new JTextField(15);
	JButton startButton = new JButton("START");
	JButton pauseButton = new JButton("PAUSE");
	JButton stopButton = new JButton("STOP");
	
	// The text area and the scroll pane in which it resides
	JTextArea display;
	
	JScrollPane myPane;
	
	// These represent the menus
	JMenuItem saveData = new JMenuItem("Save data", KeyEvent.VK_S);
	JMenuItem displayData = new JMenuItem("Display data", KeyEvent.VK_D);
	
	JMenu options = new JMenu("Options");
	
	JMenuBar menuBar = new JMenuBar();
	
	// These booleans are used to indicate whether the START button has been clicked
	boolean started;
	
	// and the state of the timer (paused or running)
	boolean paused;
	
	// Number of seconds
	long totalSeconds = 0;
	long secondsToRun = 0;
	long secondsSinceStart = 0;
	
	// This is the thread that performs the countdown and can be started, paused and stopped
	TimerThread countdownThread;
	
	private JFileChooser jfc;

	// Interface constructed
	Timer() {
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		setTitle("Timer Application");
		
    	MigLayout layout = new MigLayout("fillx");
    	JPanel panel = new JPanel(layout);
    	getContentPane().add(panel);
    	
    	options.add(saveData);
    	options.add(displayData);
    	menuBar.add(options);
    	
    	panel.add(menuBar, "spanx, north, wrap");
    	
    	MigLayout centralLayout = new MigLayout("fillx");
    	
    	JPanel centralPanel = new JPanel(centralLayout);
    	
    	GridLayout timeLayout = new GridLayout(2,2);
    	
    	JPanel timePanel = new JPanel(timeLayout);
    	
    	countdownField.setEditable(false);
    	countdownField.setHorizontalAlignment(JTextField.CENTER);
    	countdownField.setFont(countdownFont);
    	countdownField.setText("00:00:00");
    	
    	timePanel.add(countdownLabel);
    	timePanel.add(countdownField);

    	elapsedField.setEditable(false);
    	elapsedField.setHorizontalAlignment(JTextField.CENTER);
    	elapsedField.setFont(elapsedFont);
    	elapsedField.setText("00:00:00");
    	
    	timePanel.add(elapsedLabel);
    	timePanel.add(elapsedField);

    	centralPanel.add(timePanel, "wrap");
    	
    	GridLayout buttonLayout = new GridLayout(1, 3);
    	
    	JPanel buttonPanel = new JPanel(buttonLayout);
    	
    	buttonPanel.add(startButton);
    	buttonPanel.add(pauseButton, "");
    	buttonPanel.add(stopButton, "");
    	
    	centralPanel.add(buttonPanel, "spanx, growx, wrap");
    	
    	panel.add(centralPanel, "wrap");
    	
    	display = new JTextArea(100,150);
        display.setMargin(new Insets(5,5,5,5));
        display.setEditable(false);
        
        JScrollPane myPane = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.add(myPane, "alignybottom, h 100:320, wrap");
        
        
        // Initial state of system
        paused = false;
        started = false;
        
        // Allowing interface to be displayed
    	setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        // TODO: SAVE: This method should allow the user to specify a file name to which to save the contents of the text area using a 
        // JFileChooser. You should check to see that the file does not already exist in the system.
        saveData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	// launch the J file chooser dialog box
            	int returnVal = jfc.showSaveDialog(Timer.this);
				File selectedFile;
					try {
						selectedFile = jfc.getSelectedFile();
						// check to see if a file with the same name already exists, and if it does then don't allow the user to save and keep them trapped in a loop
						if (selectedFile.exists()) {
							JOptionPane.showMessageDialog(null, "File Already Exists!", "ERROR", JOptionPane.ERROR_MESSAGE);
							int returnVal1 = jfc.showSaveDialog(Timer.this);
							selectedFile = jfc.getSelectedFile();
						}
						//once the file is not a duplicate then allow the user to save it
						else
						writeDataFile(selectedFile);
					} catch (IOException e1) {
						System.out.println("Problem with file");
					}
					catch (NullPointerException e2) {
						System.out.println("Cancelled");
					}
				
            }
        });

        
        // TODO: DISPLAY DATa: This method should retrieve the contents of a file representing a previous report using a JFileChooser.
        // The result should be displayed as the contents of a dialog object.
        displayData.addActionListener(new ActionListener() {
        	/*
        	 * 
        	 * I struggled on this one and couldn't get the display working in the end
        	 * 
        	 */
            public void actionPerformed(ActionEvent arg0) {
            	int returnVal = jfc.showOpenDialog(Timer.this);
    			File selectedFile;
    			
    			try {
    				selectedFile = jfc.getSelectedFile();
    				System.out.println(selectedFile.getAbsolutePath());
    				try {
						readDataFile(selectedFile);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    		} 	catch (IOException e1) {
    				System.out.println("Problem with file");
    			}
    			catch (NullPointerException e2) {
    				 System.out.println("Cancel was selected");
    			}
            }
        });

        
        // TODO: START: This method should check to see if the application is already running, and if not, launch a TimerThread object.
		// If the application is running, you may wish to ask the user if the existing thread should be stopped and a new thread started.
        // It should begin by launching a TimerDialog to get the number of seconds to count down, and then pass that number of seconds along
		// with the seconds since the start (0) to the TimerThread constructor.
		// It can then display a message in the text area stating how long the countdown is for.
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	// If the thread has already been started
            	if (started == true) {
            		// ask the user if they want to restart thread
                	int dialogResult = JOptionPane.showConfirmDialog (Timer.this, "Thread has already been started, would you like to restart it?","Warning", JOptionPane.YES_NO_OPTION);
                	// if they say yes
                	if(dialogResult == JOptionPane.YES_OPTION){
                	// stop the thread that's running
                	countdownThread.stop();
                	// Create a new TimerDialog popup as we need to enter new times
                	TimerDialog td = new TimerDialog(Timer.this, totalSeconds, true);
                	// create a new thread and launch it with the new user defined countdown time
                 	countdownThread = new TimerThread(countdownField, elapsedField, td.getSeconds(), secondsSinceStart);
                 	Thread thrd = new Thread(countdownThread);
     				thrd.start();
     				display.setText("Counting down for " + td.getSeconds() + " seconds");
                	}
                }
                
                else {
                	// create a TimerDialog popup box 
                	TimerDialog td = new TimerDialog(Timer.this, totalSeconds, true);
                	countdownThread = new TimerThread(countdownField, elapsedField, td.getSeconds(), secondsSinceStart);
                	//countdownThread = new TimerThread(countdownField, elapsedField, 100, secondsSinceStart);
                	Thread thrd = new Thread(countdownThread);
    				thrd.start();
    				display.setText("Counting down for " + td.getSeconds() + " seconds");
    				started = true;
                }
            	
            }
        });
        
        // TODO: PAUSE: This method should call the TimerThread object's pause method and display a message in the text area
        // indicating whether this represents pausing or restarting the timer.
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	// if the thread has already been paused
            	if (paused) {
            		// go to the pause method in TimerThread
            		// the functionality in that method will set running back to true and the timer will continue running
            		countdownThread.pause();
            		// set paused to false as we have re-started the thread
            		paused = false;
            		//set the pause button back to "Pause"
            		pauseButton.setText("Pause");
            	}
            	// otherwise if the thread hasn't been paused yet
            	else {
            		// call the pause function on the thread
            		countdownThread.pause();
            		// set paused to true
            		paused = true;
            		// set the paused button to "Resume"
            		pauseButton.setText("Resume");
            		// add a note in the dialog box of when the timer was paused
            		display.append("\nPaused at " + countdownThread.getCountdownSeconds() + " seconds");
            	}
            }
        });
        
        // TODO: STOP: This method should stop the TimerThread object and use appropriate methods to display the stop time
        // and the total amount of time remaining in the countdown (if any).
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	// Stop the thread
            	countdownThread.stop();
            	//set started to false
            	started = false;
            	//display the details
            	displaySeconds();
            	
            }
        });
    	
	}
	
	public synchronized void writeDataFile(File f) throws IOException, FileNotFoundException {
		FileOutputStream fs = null;
		ObjectOutputStream out = null;
		try {
			//openining stream
			fs = new FileOutputStream(f);
			out = new ObjectOutputStream(fs);
			
			//writing to stream
			// write the data in the display text box to the file that we've selected
			out.writeObject(display.getText());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally{
			fs.close();
			out.close();
		}
	}
	

	public synchronized String readDataFile(File f) throws IOException, ClassNotFoundException {
			String result = new String();
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		try {
			fileIn = new FileInputStream(f);
			in = new ObjectInputStream(fileIn);
			display.setText((String) in.readObject());
			//f.in.readObject();
			in.readObject();
			}
		catch(EOFException e) {}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			in.close();
			fileIn.close();
		}
	
		
  
		return result;
	}
	
	// method to be displayed in the display box of this thread, which has info on seconds remaining and seconds elapsed
	public void displaySeconds() {
		display.append(("\nThe timer was stopped at " + countdownThread.getElapsedSeconds() + " seconds"));
		display.append(("\nThere were " + countdownThread.getCountdownSeconds() + " seconds remaining"));
	}

    public static void main(String[] args) {

        Timer timer = new Timer();

    }
    
   
}

