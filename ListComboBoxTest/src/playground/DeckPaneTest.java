package playground;

import playground.control.DeckPane;
import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DeckPaneTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        HBox hbox = new HBox(20);

        hbox.setAlignment(Pos.CENTER);
        final DeckPane deck = new DeckPane();
        deck.setId("mainDeck");
        deck.setAnimationDuration(Duration.valueOf(800.0d));
        final Button button1 = new Button("blue");
        final Button button2 = new Button("red");
        button1.setId("button1");
        button1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                deck.setVisibleNodeID("blue");
                button1.setDisable(true);
                button2.setDisable(false);
            }
        });


        button2.setDisable(true);
        button2.setId("button2");
        button2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                deck.setVisibleNodeID("red");
                button2.setDisable(true);
                button1.setDisable(false);
            }
        });


        Button red = new Button("Am red.");
        red.setId("red");
        red.setPrefSize(200, 200);
        red.setStyle("-fx-color: red;");
        Button blue = new Button("And am blue.");
        blue.setId("blue");
        blue.setPrefSize(200, 200);
        blue.setStyle("-fx-color: blue;");
        deck.getContentNodes().addAll(red, blue);
        deck.visibleNodeIDProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("->newValue: " + newValue + " oldValue: " + oldValue);
            }
        });
        hbox.getChildren().addAll(button1, button2);
        BorderPane root = new BorderPane();
        root.setTop(hbox);
        root.setCenter(deck);
//        BorderPane.setAlignment(red, Pos.TOP_LEFT);

        final ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (group.getSelectedToggle() != null) {
                    System.out.println("toggle changed: " + newValue.getUserData());
                    System.out.println("toggle changed: " + group.getSelectedToggle().getUserData());
                    DeckPane.AnimationMode mode = (DeckPane.AnimationMode) group.getSelectedToggle().getUserData();
                    deck.setAnimationMode(mode);
                }
            }
        });
        RadioButton rb1 = new RadioButton("FADE");
        rb1.setUserData(DeckPane.AnimationMode.FADE);
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("SCALE");
        rb2.setUserData(DeckPane.AnimationMode.SCALE);
        rb2.setToggleGroup(group);

        RadioButton rb3 = new RadioButton("PERSPECTIVE");
        rb3.setUserData(DeckPane.AnimationMode.PERSPECTIVE);
        rb3.setToggleGroup(group);

        VBox vb = new VBox(10);
        vb.getChildren().addAll(rb1, rb2, rb3);
        root.setLeft(vb);
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        scene.getStylesheets().addAll("/playground/deck.css");

        deck.setVisibleNodeID("red");
        primaryStage.setVisible(true);


    }

    public static void main(String[] args) {
        Application.launch(DeckPaneTest.class, args);
    }
}
