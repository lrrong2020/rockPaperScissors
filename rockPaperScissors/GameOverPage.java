package rockPaperScissors.rockPaperScissors;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameOverPage extends Application{
	
	public int i = 0;
	public int j = 5;
	
	Image rock=new Image("/rockPaperScissors/rockPaperScissors/media/rock.png", 20, 20, false, false);
	Image paper=new Image("/rockPaperScissors/rockPaperScissors/media/paper.png", 20, 20, false, false);
	Image scissors=new Image("/rockPaperScissors/rockPaperScissors/media/scissor.png", 20, 20, false, false);
	
	
	
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Game Over");
	
		VBox vbox = new VBox();
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);
		
		Label score = new Label("3 - 2");
		score.setFont(Font.font("Amble CN", FontWeight.BOLD, 24));
		vbox.getChildren().add(score);

		Label result = new Label("You Win!");
		result.setFont(Font.font("Amble CN", FontWeight.BOLD, 24));
		vbox.getChildren().add(result);
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(20);
		vbox.getChildren().add(hbox);
		

		  VBox v0 = new VBox();
		  v0.setSpacing(15);
		  Label player = new Label("Player");
		  v0.getChildren().add(player);
		  Label yourChoice = new Label("Your Choice");
		  v0.getChildren().add(yourChoice);
		  Label opponentChoice = new Label("Opponent Choice");
		  v0.getChildren().add(opponentChoice);
		hbox.getChildren().add(v0);
	 
	for (i = 0; i < j; i++) {
		
		VBox v1 = new VBox();
		  v1.setSpacing(10);
		  Label round1 = new Label("Round 1");
		  v1.getChildren().add(round1);
		  Label yourChoice1 = new Label("");
		  yourChoice1.setGraphic(new ImageView(rock));
		  v1.getChildren().add(yourChoice1);
		  Label opponentChoice1 = new Label("");
		  opponentChoice1.setGraphic(new ImageView(rock));
		  v1.getChildren().add(opponentChoice1);
		hbox.getChildren().add(v1);
	}
		  
		 
		
	Platform.setImplicitExit(false);
	primaryStage.setOnCloseRequest(event ->{
		event.consume();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText(null);
		alert.setContentText("Do you want to exit?");
		Optional<ButtonType> r = alert.showAndWait();
		if(r.get() == ButtonType.OK) {
			Platform.exit();
		}
	});
		Scene scene = new Scene(vbox, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

