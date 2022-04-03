package rockPaperScissors.rockPaperScissors;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class WelcomePage {
	private Scene welcomePage;
	
	public Scene getWelcomePage() {
		Pane root=new Pane();
		welcomePage=new Scene(root,600,400);
		welcomePage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
		Image icon=new Image("/rockPaperScissors/rockPaperScissors/media/icon.gif");
		ImageView icon1=new ImageView(icon);
		icon1.setFitHeight(150);
		icon1.setFitWidth(200);
		icon1.setLayoutX(50);
		icon1.setLayoutY(200);
		
		Label label1=new Label("Rock Paper Scissors Game");
		label1.getStyleClass().add("labelContent");
	
		label1.setLayoutX(150);
		Button bt1=new Button("One round");
		Button bt2=new Button("Best two out of three");
		Button bt3=new Button("Best three out of five");
		
		bt1.setLayoutX(400);
		bt1.setLayoutY(150);
		
		bt2.setLayoutY(200);
		bt3.setLayoutY(250);
	
		bt2.layoutXProperty().bind(bt1.layoutXProperty());
		bt3.layoutXProperty().bind(bt1.layoutXProperty());
		
		return welcomePage;
	
	}
}
