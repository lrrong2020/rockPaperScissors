package rockPaperScissors.rockPaperScissors;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
public class DuringTheGame {
	private int width;
	private int height;
	private Pane root1;
	private Scene GamePage;
	public Parent CreateGamePage() {
		root1=new Pane();
		ImageView rock=new ImageView(new Image("/rockPaperScissors/rockPaperScissors/media/rock.png"));
		rock.setFitHeight(50);
		rock.setFitWidth(50);
		
		HBox displayBoard=new HBox();
		
		ImageView paper=new ImageView(new Image("/rockPaperScissors/rockPaperScissors/media/paper.png"));
		paper.setFitHeight(50);
		paper.setFitWidth(50);

		ImageView scissors=new ImageView(new Image("/rockPaperScissors/rockPaperScissors/media/scissor.png"));
		scissors.setFitHeight(50);
		scissors.setFitWidth(50);
		
		Button rock1 = new Button("Rock",rock);//create new button instance
		Button paper1= new Button("Paper",paper);//create new button instance
		Button scissors1 = new Button("Scissors",scissors);
		
		rock1.setLayoutX(50);
		rock1.setLayoutY(300);
		paper1.setLayoutX(250);
		paper1.setLayoutY(300);
		scissors1.setLayoutX(450);
		scissors1.setLayoutY(300);
		
		GamePage=new Scene(root1,600,400);
		GamePage.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
		rock1.getStyleClass().add("bt");
		scissors1.getStyleClass().add("bt");
		paper1.getStyleClass().add("bt");
		root1.getChildren().addAll(rock1,paper1,scissors1);
		return root1;
	}
	
	public void start(Stage stage){
		CreateGamePage();
		stage.setScene(GamePage);
		stage.show();	
	}


}