package rockPaperScissors.rockPaperScissors;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import java.awt.event.ActionEvent;
public class Test1 extends Application{
	Label label=new Label();
	Semaphore s=new Semaphore(1);
	private int i = 10;
	public void start(Stage stage) throws InterruptedException {
		Pane pane=new Pane(label);
		
		labelActionPerformed(new ActionEvent(pane, 0, null));
	
		
		
		System.out.print(s.availablePermits());
		stage.setScene(new Scene(pane,100,100));
		stage.show();
	
	
	
	}
	private void labelActionPerformed(java.awt.event.ActionEvent evt) {
	    Timer timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	int k=1;
	        @Override
	        public void run() {
	        	javafx.application.Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                		if(i==0) {
	                			label.setText(Integer.toString(i));
	                			k++;
	                			
	                			try {
									TimeUnit.SECONDS.sleep(3);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	                			i=12;
	                		}
	            
	                		else {
	                			label.setText(Integer.toString(i--));
	                		}
	                		if(k>3) {
	                			cancel();
	                			
	                		}
	                }
	                
	            });
	        }
	        public long scheduledExecutionTime() {
	        	return 3;
	        }
	    }, 3000, 1000);
	    
	    }

}
