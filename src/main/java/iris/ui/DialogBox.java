package iris.ui;

import java.io.IOException;

import iris.MainWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * A dialog box for displaying text in the GUI.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    /**
     * Constructor for DialogBox.
     *
     * @param text The text to be displayed in the dialog box.
     */
    public DialogBox(String text, boolean isUser) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        dialog.setText(text);

        if (isUser) {
            this.setAlignment(Pos.TOP_RIGHT);
            this.getChildren().get(0).getStyleClass().add("user-label");
        } else {
            this.setAlignment(Pos.TOP_LEFT);
        }
    }
}
