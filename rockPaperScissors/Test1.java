package rockPaperScissors.rockPaperScissors;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
public class Test1 extends Application{
	String s;
	Label label=new Label();
	public void start(Stage stage) {
		StartEndChecker s1=new StartEndChecker();
		s1.start();
		AnchorPane root = new AnchorPane(label);
		stage.setScene(new Scene(root,100,100));
		stage.show();
	}
	public class StartEndChecker extends AnimationTimer 
	{	int i=1;
		
		@Override
		public void handle(long arg0)
		{
			if(i==11) {
				stop();
			}
			if(i==1) {
				label.setText(text(i));
				i++;
			}
			else {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				label.setText(text(i));
				i++;
			}
			
		}
		public String text(int k) {
			return String.valueOf(k);
		}
	}

}
