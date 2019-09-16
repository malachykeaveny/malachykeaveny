import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TimerThread implements Runnable {
	
	/*  DT354/2
	 *  Malachy Keaveny 
	 * C16775539 */
	
	// booleans indicating whether the timer is running, and whether the pause button has been pressed.
	boolean running;
	boolean paused;
	
	
	// text fields to be updated.
	JTextField countdownField;
	JTextField elapsedField;
	
	// counters of seconds
	Long elapsedSeconds;
	Long countdownSeconds;
	
	// The constructor:
	// This should set up initial values for the text fields, booleans and start time.
	// It initialises the text fields to be updated along with countdownSeconds and elapsedSeconds.
	// It can then start the thread.
	TimerThread(JTextField countdownField, JTextField elapsedField, long secondsToRun, long secondsSinceStart) {
		this.countdownField = countdownField;
		this.elapsedField = elapsedField;
		this.countdownSeconds = secondsToRun;
		this.elapsedSeconds = secondsSinceStart;
		
		running = true;
		paused = false;
		

	}

	
	// This needs to update the text fields for countdown and elapsed seconds every second.
	// The thread should run until countdownSeconds is 0. If the pause button is activated, the number of elapsed
	// seconds will increase by 1 every second, whereas if pause is not activated, elapsed seconds will be increased
	// by 1 each second, while the countdown seconds should similarly decrease by 1. The text fields should be updated
	// each second. When countdownSeconds reaches 0, a message should be displayed saying that time is up.
	
	public void run() {
		//while the clock is counting down and the application is running
		while (countdownSeconds > 0 && running) {
			
			//increment countdown and elapsed time by 1
			countdownSeconds--;
			elapsedSeconds++;
			
		try {
			// sleep for 1000 ms
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
			//update the fields in the GUI to the live countdown numbers
			countdownField.setText(convertToHMSString(countdownSeconds));
			elapsedField.setText(convertToHMSString(elapsedSeconds));
				} // end while				
		
		 // once the countdown ends I display a message and set running to false
		if (countdownSeconds == 0) {
			// I'm trying to get the dialog message here to display over the display area but for some reason it displays at the bottom
			Timer t = new Timer();
			JOptionPane.showMessageDialog(t.display, "Times up!");
			running = false;
		} 
		
		
		
				
	}
	
	// This should pause or resume the application depending on the current value of paused.
	public void pause() {
		// if the application is running and pause is called then running will be set to false
		if (running) 
			running = false;
		// otherwise if the application isn't running and resume is clicked then the thread will be started again
		else 
			running = true;
	}
	
	// This should stop the application.
	public void stop() {
		if (countdownSeconds > 0) {
		// stop the countdown timer
		running = false;
		// initialize the countdown fields
		countdownField.setText("00:00:00");
    	elapsedField.setText("00:00:00");
		}

	}
	
	// These methods can be used by the Timer class to get the values of elapsed and countdown seconds.
	public long getElapsedSeconds() {
		return elapsedSeconds;
	}
	
	public long getCountdownSeconds() {
		return countdownSeconds;
	}
	
	// This method is to convert a long integer representing the number of seconds for which a thread has been running
	// into a display.
	// You could also use DateFormat.
	public String convertToHMSString(long seconds) {
		long secs, mins, hrs;
		// String to be displayed
		String returnString = "";
		
		// Split time into its components
		secs = seconds % 60;
		mins = (seconds / 60) % 60;
		hrs = (seconds / 60) / 60;
		
		// Insert 0 to ensure each component has two digits
		if (hrs < 10) {
			returnString = returnString + "0" + hrs;
		}
		else returnString = returnString + hrs;
		returnString = returnString + ":";
		
		if (mins < 10) {
			returnString = returnString + "0" + mins;		
		}
		else returnString = returnString + mins;
		returnString = returnString + ":";
		
		if (secs < 10) {
			returnString = returnString + "0" + secs;		
		}
		else returnString = returnString + secs;
		
		return returnString;
		
	}

}

