package rockPaperScissors.rockPaperScissors;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
public class Test1 extends Application{
	Label label1=new Label();
	String s;
	Label label=new Label();
	public void start(Stage stage) {
		
		
		AnchorPane root = new AnchorPane();
		
		stage.setScene(new Scene(root,100,100));
		stage.show();
	}
	public void label1ActionPerformed(java.awt.event.ActionEvent evt) {
	    Timer timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	int k=1;
	        @Override
	        public void run() {
	        	javafx.application.Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                		
	                		
	                		
	                }
	                
	            });
	        }
	        public long scheduledExecutionTime() {
	        	return 5;
	        }
	    }, 10000, 13000);
	}

}
