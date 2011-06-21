package playground;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import playground.control.ListComboBox;

public class ListComboBoxTest extends Application {

    ObservableList<Label> items = FXCollections.<Label>observableArrayList();
    int itemCount = 25;

    @Override
    public void start(Stage stage) {
        stage.setTitle("SpinnerTest");

        createItems();

        ListComboBox<Label> ListComboBox = new ListComboBox<Label>();
        ListComboBox.setItems(items);
        ListComboBox.setPrefSize(120, 22);
        StackPane root = new StackPane();


        root.getChildren().addAll(ListComboBox);

        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        scene.getStylesheets().addAll("/playground/listcombobox.css");


        stage.setVisible(true);


    }

    public static void main(String[] args) {
        Application.launch(ListComboBoxTest.class, args);
    }

    private void createItems() {
        for (int i = 0; i < itemCount; i++) {
            Label label = new Label("Item " + (i + 1));
            items.add(label);
        }
    }
}
