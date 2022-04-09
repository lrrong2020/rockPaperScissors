package rockPaperScissors.rockPaperScissors;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rockPaperScissors.rockPaperScissors.ConcurrentModel.Model;
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
		
		
		
		Label clockContent=new Label();
		final AtomicInteger count = new AtomicInteger(-1);
		final NumberFormat formatter = NumberFormat.getIntegerInstance();
		formatter.setGroupingUsed(true);
		
		TestClientFx.client.getCountDown().intProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(final ObservableValue<? extends Number> observable,
					final Number oldValue, final Number newValue) {
				if (count.getAndSet(newValue.intValue()) == -1) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							long value = count.getAndSet(-1);
							clockContent.setText(formatter.format(value));
						}
					});          
				}

			}
		});
		
		
		StackPane stack=new StackPane(clockContent);
		clock.setFitHeight(50);
		clock.setFitWidth(50);
		clock.setLayoutX(510);
		clock.setLayoutY(15);
		stack.setLayoutX(520);
		stack.setLayoutY(30);
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
		clockContent.getStyleClass().add("labelContent");
		
		rock1.addEventHandler(MouseEvent.MOUSE_CLICKED, TestClientFx.getEvent().get(0));
		paper1.addEventHandler(MouseEvent.MOUSE_CLICKED, TestClientFx.getEvent().get(1));
		scissors1.addEventHandler(MouseEvent.MOUSE_CLICKED, TestClientFx.getEvent().get(2));
		root.getChildren().addAll(rock1,paper1,scissors1,whiteboard,clock,stack);
		
		return root;
		
	}
}
		
	
			



