package iris;

import iris.ui.DialogBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * JavaFx GUI interface for Iris.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private TextField userInput;
    @FXML
    private VBox chatContainer;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button sendButton;

    private Iris iris;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(chatContainer.heightProperty());
    }

    public void setIris(Iris iris) {
        this.iris = iris;
    }

    public VBox getChatContainer() {
        return chatContainer;
    }

    /**
     * Handles user input from the text field and generates appropriate responses.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (!input.isBlank()) {
            DialogBox userDialog = new DialogBox(input, true);


            String response = iris.processCommand(input);
            DialogBox botDialog = new DialogBox(response, false);

            chatContainer.getChildren().addAll(userDialog, botDialog);
            userInput.clear();

            if (input.equals("bye")) {
                iris.save("data.txt");
                Platform.exit();
                System.exit(0);
            }
        }
    }
}
