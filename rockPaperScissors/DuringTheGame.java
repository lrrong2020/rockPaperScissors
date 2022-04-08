package rockPaperScissors.rockPaperScissors;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
public class DuringTheGame{
	
	private Pane root;
	
	public Parent CreateGamePage() {
		root=new Pane();
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
		
		ImageView whiteboard=new ImageView(new Image("/rockPaperScissors/rockPaperScissors/media/whiteboard.png"));
		
		ImageView clock=new ImageView(new Image("/rockPaperScissors/rockPaperScissors/media/clock.png"));
		clock.setFitHeight(50);
		clock.setFitWidth(50);
		clock.setLayoutX(510);
		clock.setLayoutY(15);
		whiteboard.setFitHeight(280);
		whiteboard.setFitWidth(560);
		whiteboard.setLayoutX(20);
		rock1.setLayoutX(50);
		rock1.setLayoutY(300);
		paper1.setLayoutX(250);
		paper1.setLayoutY(300);
		scissors1.setLayoutX(450);
		scissors1.setLayoutY(300);
		rock1.getStyleClass().add("bt");
		paper1.getStyleClass().add("bt");
		scissors1.getStyleClass().add("bt");
		
		rock1.addEventHandler(MouseEvent.MOUSE_CLICKED, TestClientFx.getEvent().get(0));
		paper1.addEventHandler(MouseEvent.MOUSE_CLICKED, TestClientFx.getEvent().get(1));
		scissors1.addEventHandler(MouseEvent.MOUSE_CLICKED, TestClientFx.getEvent().get(2));
		root.getChildren().addAll(rock1,paper1,scissors1,whiteboard,clock);
		
		return root;
		
	}
	


}
