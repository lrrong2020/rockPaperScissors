package rockPaperScissors.Pages;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class WelcomePage {
	private Pane root; 
	public Button bt1=new Button("One round");
	public Button bt2=new Button("Best two out of three");
	public Button bt3=new Button("Best three out of five");
	public Parent getWelcomePage() {
		root=new Pane();
		
		Image icon=new Image("/rockPaperScissors/media/icon.gif");
		ImageView icon1=new ImageView(icon);
		icon1.setFitHeight(150);
		icon1.setFitWidth(200);
		icon1.setLayoutX(50);
		icon1.setLayoutY(200);
		
		Label label1=new Label("Rock Paper Scissors Game");
		label1.getStyleClass().add("labelContent");
	
		label1.setLayoutX(150);
		
		bt1.setLayoutX(400);
		bt1.setLayoutY(150);
		
		bt2.setLayoutY(200);
		bt3.setLayoutY(250);
	
		bt2.layoutXProperty().bind(bt1.layoutXProperty());
		bt3.layoutXProperty().bind(bt1.layoutXProperty());
		root.getChildren().addAll(label1,icon1,bt1,bt2,bt3);
		
		return root;
	
	}
	
}
