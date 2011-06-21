package playground.control;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

/**
 *
 * @author jojorabbit
 */
public class ListComboBoxSkin<T> extends SkinBase<ListComboBox<T>, ListComboBoxBehavior<T>> {

    private ListView<T> listView;
    private final double shapeWidth = 20.0d;
    private Timeline timeline;
    ListComboBox<T> listComboBox;
//    private ListCell<T> selectedCell;
//    private Label selectedLabel;
    private Button button;
    private static final Duration ANIMATION_DURATION = Duration.valueOf(200.0d);
    private BooleanProperty showing = new BooleanProperty(false);
    private DoubleProperty transition = new DoubleProperty(0.0d) {

        @Override
        protected void invalidated() {
            if (listView != null) {
                // refreshing skin -> like call layoutChildren on each invalidation
                ListComboBoxSkin.this.requestLayout();
            }
        }
    }; // listView transition property
    ObjectProperty<T> selectedItem = new ObjectProperty<T>();
    private MultipleSelectionModel<T> selectionModel;

    public ListComboBoxSkin(ListComboBox<T> listComboBox) {
        super(listComboBox, new ListComboBoxBehavior<T>(listComboBox));
        this.listComboBox = listComboBox;
//        setAlignment(Pos.TOP_CENTER);
        listView = listComboBox.listView;
        selectionModel = listView.getSelectionModel();
        selectionModel.selectFirst();
        System.out.println("selected: " + selectionModel.getSelectedItem());
        System.out.println("selected class: " + selectionModel.getSelectedItem().getClass());


//        selectedLabel = new Label();
        double[] points = new double[]{
            0, shapeWidth * 0.25,
            0, shapeWidth * 0.75,
            shapeWidth * 0.35, shapeWidth * 0.5
        };
        Polygon polygon = new Polygon(points);
        polygon.setRotate(90);
//        polygon.setStyle("-fx-padding: 5;");
        button = new Button("", polygon);
        button.textProperty().bind(new ObjectBinding<String>() {

            {
                bind(selectionModel.selectedItemProperty());
            }

            @Override
            protected String computeValue() {
                T item = selectionModel.getSelectedItem();
                String returnString = "";

                if (item instanceof Label) {
                    returnString = ((Label) item).getText();
                } else {
                    System.out.println("Items in list are instanceof: " + item.getClass());
                    returnString = item.toString();
                }
                return returnString;
            }
        });


        button.setPrefWidth(150); // bind to height of control
        button.setPrefHeight(25); // bind to height of control



        button.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                doAnimation();
            }
        });


        listView.setMinSize(150, 150);
        listView.setPrefSize(150, 150);
        listView.setMaxSize(150, 150);

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                doAnimation();
            }
        });


        listView.setVisible(false);
        getChildren().addAll(button, listView);

    }

    private boolean isShowing() {
        return showing.get();
    }

    private void doAnimation() {
        timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyFrame keyFrame1;
        KeyFrame keyFrame2;
        //
        if (!isShowing()) {
            keyFrame1 = new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
//                    System.out.println("timeline started");
                    listView.setVisible(true);
//                listView.setCache(true);
                }
            }, new KeyValue(transition, 0.0d, Interpolator.LINEAR));
            //
            keyFrame2 = new KeyFrame(ANIMATION_DURATION, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    showing.set(true);
                    listView.requestFocus();
                }
            }, new KeyValue(transition, 1.0d, Interpolator.EASE_IN));
        } else {
            keyFrame1 = new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
//                    System.out.println("timeline started");
//                    listView.setVisible(true);
//                listView.setCache(true);
                }
            }, new KeyValue(transition, 1.0d, Interpolator.LINEAR));
            //
            keyFrame2 = new KeyFrame(ANIMATION_DURATION, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    showing.set(false);
                    listView.setVisible(false);
                    button.requestFocus();
                }
            }, new KeyValue(transition, 0.0d, Interpolator.EASE_OUT));
        }
        //
        timeline.getKeyFrames().setAll(keyFrame1, keyFrame2);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
//                System.out.println("timeline finished.");
            }
        });
        timeline.play();

    }

    private double getTransition() {
        return transition.get();
    }

    private void showList() {
//     
    }

    // ***********************************
    //             overrides
    // ***********************************
    @Override
    protected void layoutChildren() {
        double width = this.getWidth();
        double height = this.getHeight();
//        System.out.println("w: " + getWidth() + " h: " + getHeight());
        button.resize(width, height);
        positionInArea(button, 0.0, 0.0, width, height, 0.0d, HPos.CENTER, VPos.CENTER);
//        if (listView.isVisible()) {
//        System.out.println("transition: " + getTransition());
        double areaHeight = listView.prefHeight(-1) * getTransition();
//        System.out.println("areah: " + areaHeight);
//        System.out.println("btn bip: " + button.getBoundsInLocal());
        listView.resize(width, areaHeight);
//        System.out.println("lv - w: " + listView.getWidth() + " h: " + listView.getHeight());
//        System.out.println("lv - w: " + listView.getMaxWidth() + " h: " + listView.getMaxHeight());
        positionInArea(listView, 0.0, height + 2.0, width, areaHeight, 0.0d, HPos.CENTER, VPos.CENTER);
//        System.out.println("lv - w: " + listView.getWidth() + " h: " + listView.getHeight());
//        System.out.println("lv - w: " + listView.getMaxWidth() + " h: " + listView.getMaxHeight());
//        }
    }
//    
//    

    @Override
    protected double computeMaxHeight(double width) {
        return Math.max(22.0d, listComboBox.getPrefHeight());
    }

    @Override
    protected double computePrefHeight(double width) {
        return Math.max(22.0d, listComboBox.getPrefHeight());
    }

    @Override
    protected double computeMinHeight(double width) {
        return Math.max(22.0d, listComboBox.getPrefHeight());
    }

    @Override
    protected double computePrefWidth(double height) {
        return Math.max(40.0d, listComboBox.getPrefWidth());
    }

    @Override
    protected double computeMaxWidth(double height) {
        return Math.max(40.0d, listComboBox.getPrefWidth());
    }

    @Override
    protected double computeMinWidth(double height) {
        return Math.max(40.0d, listComboBox.getPrefWidth());
    }
}
