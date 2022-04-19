package Pages;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
public class WaitingPage{
	private Pane root;
	public Parent CreateWaitingPage() {
		Label label=new Label("Waiting the host to start¡­¡­");
		root=new Pane();
		label.setLayoutX(180);
		label.setLayoutY(180);
		root.getChildren().addAll(label);
		//waitingPage=new Scene(root,600,400);
		//waitingPage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
		label.getStyleClass().add("labelContent1");
		return root;
	}
}
