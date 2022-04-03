package rockPaperScissors.rockPaperScissors;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IPaddress extends Application {

	Stage window;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		
		
		window = primaryStage;
		window.setTitle("IP Address");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(10));
		
		Text enterTxt = new Text("Enter the IP address:");
		enterTxt.setFont(Font.font("Tahoma", FontWeight.LIGHT, 25));
		grid.add(enterTxt, 0, 0);
		
		
		TextField IP = new TextField();
		IP.setUserData("172.28.141.189");
		IP.setPromptText("IP address");
		grid.add(IP, 0, 2);
		
		
		Button enter = new Button("OK");
		grid.add(enter, 0, 3);
		
		enter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String t = IP.getText();
				if (IP.getUserData().equals(t)) {
					System.out.println("Correct IP");
					myWindow w = new myWindow(t);
					primaryStage.close();
				}
				else {
						System.out.println("Wrong IP. Please enter again!");
						IP.setText("");
						Text text = new Text("Wrong IP. Please enter again!");
						text.setFill(Color.RED);
						grid.add(text, 0, 4);
						FadeTransition tst = new FadeTransition();
						tst.setDuration(Duration.seconds(0.2));
						tst.setNode(grid);
						tst.setFromValue(0);
						tst.setToValue(1);
						tst.play();
					
				}
				
			}
			
		});
		Scene scene = new Scene(grid, 500, 300);
		window.setScene(scene);
		window.setResizable(false);
		window.show();
		
		
	}
	
	

}
class myWindow {
	private final Stage stage = new Stage();
	
	public myWindow(String t) {
		Text T = new Text("IP: " + t);
		BorderPane bor = new BorderPane();
		bor.setCenter(T);
		Scene scene = new Scene(bor);
		
		stage.setScene(scene);
		stage.setTitle("Correct IP");
		stage.setWidth(500);
		stage.setHeight(300);
		stage.setResizable(false);
		stage.show();
		
	}
}



