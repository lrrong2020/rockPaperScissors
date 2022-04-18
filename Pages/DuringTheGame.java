package rockPaperScissors.Pages;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import rockPaperScissors.Client.*;

public class DuringTheGame{

	private Pane root;
	public Label label=new Label();
	public Label label1=new Label();
	public Label label2=new Label();
	public Label label3=new Label();
	public Label label4=new Label();
	private int i = 10;

	public Parent CreateGamePage() {
		root=new Pane();
		ImageView rock=new ImageView(new Image("/rockPaperScissors/media/rock.png"));
		rock.setFitHeight(50);
		rock.setFitWidth(50);

		HBox displayBoard=new HBox();

		ImageView paper=new ImageView(new Image("/rockPaperScissors/media/paper.png"));
		paper.setFitHeight(50);
		paper.setFitWidth(50);

		ImageView scissors=new ImageView(new Image("/rockPaperScissors/media/scissor.png"));
		scissors.setFitHeight(50);
		scissors.setFitWidth(50);

		Button rock1 = new Button("Rock",rock);//create new button instance
		Button paper1= new Button("Paper",paper);//create new button instance
		Button scissors1 = new Button("Scissors",scissors);

		ImageView whiteboard=new ImageView(new Image("/rockPaperScissors/media/whiteboard.png"));

		ImageView clock=new ImageView(new Image("/rockPaperScissors/media/clock.png"));

		label3.setLayoutX(80);
		label3.setLayoutY(60);
		label4.setLayoutX(80);
		label4.setLayoutY(80);
		label1.setLayoutX(80);
		label1.setLayoutY(80);
		label2.setLayoutX(80);
		label2.setLayoutY(100);

		clock.setFitHeight(50);
		clock.setFitWidth(50);
		clock.setLayoutX(510);
		clock.setLayoutY(15);

		label.setLayoutX(530);
		label.setLayoutY(25);
		label.getStyleClass().add("labelContent");
		label1.getStyleClass().add("labelContent");
		label2.getStyleClass().add("labelContent");
		label3.getStyleClass().add("labelContent");
		label4.getStyleClass().add("labelContent");

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

		rock1.addEventHandler(MouseEvent.MOUSE_CLICKED, ClientMain.getEvent().get(0));
		paper1.addEventHandler(MouseEvent.MOUSE_CLICKED, ClientMain.getEvent().get(1));
		scissors1.addEventHandler(MouseEvent.MOUSE_CLICKED, ClientMain.getEvent().get(2));

		root.getChildren().addAll(rock1,paper1,scissors1,whiteboard,clock,label,label1,label2,label3,label4);

		return root;
	}
}
