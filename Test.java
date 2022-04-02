import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Test extends Application {
public static void main(String[] args ) {
	launch(args);
}
public void start(Stage stage) {
stage.setTitle ("Hello World!");
Group root = new Group();
Scene scene = new Scene(root, 640, 480);
Button btn = new Button("Hello World!");
btn.setLayoutX(100);
btn.setLayoutY(80);
btn.setOnAction((ActionEvent event) ->{
	System.out.println("Hello World!");
});
root.getChildren().add(btn);
stage.setScene(scene);
stage.show();
}

}