package Pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Model.Choice.GESTURES;
import Client.*;

public class GameOverPage {
	public static Client client = ClientMain.client;;
	Image rock=new Image("/media/rock.png", 22, 22, false, false);
	Image paper=new Image("/media/paper.png", 22, 22, false, false);
	Image scissors=new Image("/media/scissor.png", 22, 22, false, false);
	private VBox vbox;
	private String result1 = null;
	private int youWinInt = 0;
	private int opponentWinInt = 0;
	private int i = 1;
	private int roundNum = client.getRoundNoInt()-1;
	private Image YourImage;
	private Image OpponentImage;
	private String yourChoice = null;
	private String opponentChoice = null;
	private int winOrNot = 0;

	public GameOverPage() 
	{
		super();
	}

	public GameOverPage(Client client) {
		GameOverPage.client = client;
	}


	public Parent CreateOverPage() {
		result1 = client.rdp.getGameResultText();
		youWinInt = client.rdp.getRoundsYouWinInt();
		opponentWinInt=client.rdp.getRoundsOpponentWinInt();

		vbox = new VBox();
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);

		Label score = new Label(youWinInt + "-" + opponentWinInt); //Total Score
		score.getStyleClass().add("score");
		vbox.getChildren().add(score);

		Label result = new Label(result1);//result: YOU WIN, YOU LOSE, TIE 
		if (result1.equals("YOU WIN")) {
			result.getStyleClass().add("green");
		}
		else if (result1.equals("YOU LOSE")) {
			result.getStyleClass().add("red");
		}
		else {
			result.getStyleClass().add("tie");
		}
		vbox.getChildren().add(result);

		HBox hbox = new HBox(); // Score table for each round
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(20);
		vbox.getChildren().add(hbox);

		VBox v0 = new VBox();
		v0.setSpacing(15);
		Label player = new Label("Player");
		player.getStyleClass().add("blue");
		v0.getChildren().add(player);
		Label yourChoice = new Label("Your Choice");
		yourChoice.getStyleClass().add("blue");
		v0.getChildren().add(yourChoice);
		Label opponentChoice = new Label("Opponent Choice");
		opponentChoice.getStyleClass().add("blue");
		v0.getChildren().add(opponentChoice);
		hbox.getChildren().add(v0);

		for (i = 1; i <= roundNum; i++) {

			VBox v = new VBox();
			v.setSpacing(10);
			Label round = new Label("Round" + i); //Round number
			v.getChildren().add(round);
			Label yourChoice1 = new Label("");//Your choice each round
			this.yourChoice=client.rdp.getResultList().get(i-1).getYourChoice().getChoiseName();
			setYourImage(YourImage);
			yourChoice1.setGraphic(new ImageView(YourImage));
			v.getChildren().add(yourChoice1);
			Label opponentChoice1 = new Label(""); //Opponent choice each round
			this.opponentChoice=client.rdp.getResultList().get(i-1).getOpponentChoice().getChoiseName();
			setOpponentImage(OpponentImage);
			opponentChoice1.setGraphic(new ImageView(OpponentImage));
			v.getChildren().add(opponentChoice1);
			WinOrNot();
			if (winOrNot == 1) {
				round.getStyleClass().add("roundGreen");
			}
			if (winOrNot == -1) {
				round.getStyleClass().add("roundRed");
			}
			if (winOrNot == 0) {
				round.getStyleClass().add("roundTie");
			}
			hbox.getChildren().add(v);
		}

		return vbox;
	}
	public void setYourImage(Image YourImage) {
		if (yourChoice.equals(GESTURES.ROCK)){
			YourImage = rock;
		}
		else if (yourChoice.equals(GESTURES.PAPER)){
			YourImage = paper;
		}
		else if (yourChoice.equals(GESTURES.SCISSORS)) {
			YourImage = scissors;
		}
		this.YourImage = YourImage;
	}

	public void setOpponentImage(Image OpponentImage) {
		if (opponentChoice.equals(GESTURES.ROCK)){
			OpponentImage = rock;
		}
		else if (opponentChoice.equals(GESTURES.PAPER)){
			OpponentImage = paper;
		}
		else if (opponentChoice.equals(GESTURES.SCISSORS)) {
			OpponentImage = scissors;
		}
		this.OpponentImage = OpponentImage;
	}
	public void WinOrNot() {
		if ((yourChoice.equals(GESTURES.PAPER) && opponentChoice.equals(GESTURES.ROCK)) ||
				(yourChoice.equals(GESTURES.ROCK) && opponentChoice.equals(GESTURES.SCISSORS)) ||
				(yourChoice.equals(GESTURES.SCISSORS) && opponentChoice.equals(GESTURES.PAPER))) 
		{
			this.winOrNot = 1;
		}
		else if ((yourChoice.equals(GESTURES.PAPER) && opponentChoice.equals(GESTURES.SCISSORS)) ||
				(yourChoice.equals(GESTURES.ROCK) && opponentChoice.equals(GESTURES.PAPER)) ||
				(yourChoice.equals(GESTURES.SCISSORS) && opponentChoice.equals(GESTURES.ROCK)))
		{
			this.winOrNot = -1;
		}
		else 
		{
			this.winOrNot = 0;
		}
	}
}

