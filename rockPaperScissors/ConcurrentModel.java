package rockPaperScissors.rockPaperScissors;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConcurrentModel extends Application {

	@Override
	public void start(Stage primaryStage) {

		final AtomicInteger count = new AtomicInteger(-1);

		final AnchorPane root = new AnchorPane();
		final Label label = new Label();
		final Model model = new Model();
		final NumberFormat formatter = NumberFormat.getIntegerInstance();
		formatter.setGroupingUsed(true);
		model.intProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(final ObservableValue<? extends Number> observable,
					final Number oldValue, final Number newValue) {
				if (count.getAndSet(newValue.intValue()) == -1) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							long value = count.getAndSet(-1);
							label.setText(formatter.format(value));
						}
					});          
				}

			}
		});
		final Button startButton = new Button("Start");
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				model.start();
			}
		});

		AnchorPane.setTopAnchor(label, 10.0);
		AnchorPane.setLeftAnchor(label, 10.0);
		AnchorPane.setBottomAnchor(startButton, 10.0);
		AnchorPane.setLeftAnchor(startButton, 10.0);
		root.getChildren().addAll(label, startButton);

		Scene scene = new Scene(root, 100, 100);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public class Model extends Thread {
		private IntegerProperty intProperty;

		public Model() {
			intProperty = new SimpleIntegerProperty(this, "int", 0);
			setDaemon(true);
		}

		public int getInt() {
			return intProperty.get();
		}

		public IntegerProperty intProperty() {
			return intProperty;
		}

		@Override
		public void run() {
			while (true) {
				intProperty.set(intProperty.get() + 1);
			}
		}
	}
}
