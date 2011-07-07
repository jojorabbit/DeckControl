package playground;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Callback;
import playground.control.ListComboBox;

public class ListComboBoxTest extends Application {

    ObservableList<Label> items = FXCollections.<Label>observableArrayList();
    int itemCount = 25;

    @Override
    public void start(Stage stage) {
        stage.setTitle("ListComboBoxTest");

        createItems();
        ObservableList<String> data = FXCollections.observableArrayList(
                "chocolate", "salmon", "gold", "coral",
                "darkorchid", "darkgoldenrod", "lightsalmon",
                "black", "rosybrown", "blue", "blueviolet",
                "brown");
        ListComboBox<String> listComboBox = new ListComboBox<String>();
        listComboBox.setItems(data);
        listComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {
                final Rectangle rect = new Rectangle(190, 20);
                final StackPane stack = new StackPane();
                final ListCell<String> cell = new ListCell<String>() {

                    @Override
                    public void updateItem(String item,
                            boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            rect.setFill(Color.web(item));
//                            setNode(rect);
                            stack.setStyle("-fx-background-color: " + item + ";"
                                    + "-fx-background-insets: 1;"
                                    + "");


                            setNode(stack);
                        }
                    }
                };
                return cell;
            }
        });

        ListComboBox<String> fontComboBox = new ListComboBox<String>();
        ObservableList<String> fontlist = FXCollections.observableArrayList(Font.getFamilies());
        System.out.println("fonts: " + fontlist.size());
        fontComboBox.setItems(fontlist);
        Tooltip fontTooltip = new Tooltip("Choose Font");
        fontComboBox.setArrowOnLeft(true);
        fontComboBox.setTooltip(fontTooltip);
        fontComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                final Label label = new Label();
                final ListCell<String> cell = new ListCell<String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            label.setText(item);
                            label.setFont(new Font(item, 14));
                            setNode(label);
                        }
                    }
                };
                return cell;
            }
        });

        listComboBox.setPrefSize(120, 85);
        listComboBox.setDropDownHeight(150);
        fontComboBox.setPrefWidth(140);

        listComboBox.setTranslateX(10);
        listComboBox.setTranslateY(50);
        fontComboBox.setTranslateX(20);
        fontComboBox.setTranslateY(50);

        ChoiceBox<String> cb = new ChoiceBox<String>(FXCollections.observableArrayList(Font.getFamilies()));
        cb.setPrefWidth(140);
        HBox root = new HBox(20);


        root.getChildren().addAll(listComboBox, fontComboBox, cb);

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
