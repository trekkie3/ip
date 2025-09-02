package iris;

import java.io.IOException;

import iris.ui.DialogBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main class to start the Iris application GUI.
 */
public class Main extends Application {
    private final Iris iris = new Iris();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane anchor = loader.load();
            Scene scene = new Scene(anchor);
            stage.setScene(scene);

            MainWindow controller = loader.getController();
            controller.setIris(iris);

            DialogBox welcomeMessage = new DialogBox(iris.getPreamble(), false);
            String loadResult = iris.load("data.txt");
            DialogBox loadMessage = new DialogBox(loadResult, false);
            controller.getChatContainer().getChildren().addAll(welcomeMessage, loadMessage);

            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
