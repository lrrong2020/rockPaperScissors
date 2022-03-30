package rockPaperScissors.rockPaperScissors;


import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
public class StartPage extends Application{
	public void start(Stage stage) {
		Image background=new Image("/rockPaperScissors/rockPaperScissors/media/background.jpg");
		ImageView background1=new ImageView(background);
		Image icon=new Image("/rockPaperScissors/rockPaperScissors/media/icon.gif");
		ImageView icon1=new ImageView(icon);
		Label label1=new Label("Rock Paper Scissors Game");
		Button bt1=new Button("One round");
		Button bt2=new Button("Best two out of three");
		Button bt3=new Button("Best three out of five");
		icon1.setFitHeight(150);
		icon1.setFitWidth(200);
		VBox box=new VBox();
		box.getChildren().add(icon1);
		Pane pane=new Pane();
		bt1.layoutXProperty().bind(pane.widthProperty().divide(1.5));
		bt2.layoutXProperty().bind(bt1.layoutXProperty());
		bt3.layoutXProperty().bind(bt1.layoutXProperty());
		bt1.layoutYProperty().bind(pane.heightProperty().divide(3));
		bt2.layoutYProperty().bind(pane.heightProperty().divide(2));
		bt3.layoutYProperty().bind(pane.heightProperty().divide(1.5));
		label1.layoutXProperty().bind(pane.widthProperty().divide(3));
		box.layoutXProperty().bind(pane.widthProperty().divide(12));
		box.layoutYProperty().bind(pane.heightProperty().divide(1.8));
		background1.fitHeightProperty().bind(pane.heightProperty());
		background1.fitWidthProperty().bind(pane.widthProperty());
		pane.getChildren().addAll(background1,box,label1,bt1,bt2,bt3);
		Scene scene=new Scene(pane,600,400);
		stage.setTitle("Welcome to rockPaperScissors game!!");
		stage.setScene(scene);
		stage.show();
		
	}
	

}
