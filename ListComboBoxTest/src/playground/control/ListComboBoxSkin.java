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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author jojorabbit
 *
 */
public class ListComboBoxSkin<T> extends SkinBase<ListComboBox<T>, ListComboBoxBehavior<T>> {

    private ListView<T> listView;
    private Timeline timeline;
    private Label selectedLabel;
    private StackPane arrow; // holder for arrow shape
    private StackPane line; // holder for line shape
    private static final Duration ANIMATION_DURATION = Duration.valueOf(300.0d);
//    private BooleanProperty showing = new BooleanProperty(false); // make as pseudo class
    private MultipleSelectionModel<T> selectionModel;
    private ListComboBox<T> listComboBox;
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

    public ListComboBoxSkin(ListComboBox<T> listComboBox) {
        super(listComboBox, new ListComboBoxBehavior<T>(listComboBox));
        this.listComboBox = listComboBox;
        listView = listComboBox.listView;
        selectionModel = listView.getSelectionModel();
        selectionModel.selectFirst();

        selectedLabel = new Label();

        arrow = new StackPane();
        arrow.getStyleClass().add("arrow");

        line = new StackPane();
        line.getStyleClass().add("line");

        arrow.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("arrow clicked.");
            }
        });

        selectedLabel.textProperty().bind(new ObjectBinding<String>() {

            {
                bind(selectionModel.selectedItemProperty());
            }

            @Override
            protected String computeValue() {
                T item = selectionModel.getSelectedItem();
//                String returnString = "";
//
//                if (item instanceof Label) {
//                    returnString = ((Label) item).getText();
//                } else {
//                    System.out.println("Items in list are instanceof: " + item.getClass());
//                    returnString = item.toString();
//                }
                return item.toString();
            }
        });

        setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                doAnimation();
            }
        });



        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                doAnimation();
            }
        });


        listView.setVisible(false);
        getChildren().addAll(selectedLabel, line, arrow, listView);

    }

 
    private void doAnimation() {
        timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyFrame keyFrame1;
        KeyFrame keyFrame2;
        //
        if (!getSkinnable().isShowing()) {
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
                    getSkinnable().showingProperty().set(true);
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
                    getSkinnable().showingProperty().set(false);
                    listView.setVisible(false);
//                    requestFocus();
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
        // TODO: add padding to calcualting positions
        double top = getPadding().getTop();
        double right = getPadding().getRight();
        double bottom = getPadding().getBottom();
        double left = getPadding().getLeft();
//        System.out.println("top: " + top + " right: " + right + " bottom: " + bottom + " left: " + left);
        double width = this.getWidth();
        double height = this.getHeight();
        double arrowpw = arrow.prefWidth(-1);
        double arrowph = arrow.prefHeight(-1);
        double lineph = line.prefHeight(-1);
        double linepw = line.prefWidth(-1);
        //
//        System.out.println("w: " + getWidth() + " h: " + getHeight());
//        System.out.println("line- w: " + line.getWidth() + " h: " + line.getHeight());
//        System.out.println("arrow-pw: " + arrow.prefWidth(-1));
//        System.out.println("arrow-ph: " + arrow.prefHeight(-1));
//        System.out.println("line-pw: " + line.prefWidth(-1));
//        System.out.println("line-ph: " + line.prefHeight(-1));
//
        selectedLabel.resize(width, height);
        positionInArea(selectedLabel, left, 0.0, width, height, 0.0d, HPos.CENTER, VPos.CENTER);
        arrow.resize(arrowpw, arrowph);
        positionInArea(arrow, width - arrowpw - right, top, arrowpw, height - top - bottom, 0.0d, HPos.CENTER, VPos.CENTER);
        line.resize(linepw, height - top - bottom);
        positionInArea(line, width - arrowpw - linepw - right - line.getPadding().getRight(), top, linepw, height - top - bottom, 0.0d, HPos.CENTER, VPos.CENTER);
        double areaHeight = listView.prefHeight(-1) * getTransition();
        listView.resize(width, areaHeight);
        positionInArea(listView, 0.0d, height + top, width, areaHeight, 0.0d, HPos.CENTER, VPos.CENTER);

    }

    @Override
    protected double computeMaxHeight(double width) {
        return getSkinnable().prefHeight(width);
    }

    @Override
    protected double computePrefHeight(double width) {
        return getPadding().getTop() + Math.max(22.0d, selectedLabel.prefHeight(-1)) + getPadding().getBottom();
    }

    @Override
    protected double computeMinHeight(double width) {
        return getPadding().getTop() + Math.max(22.0d, selectedLabel.prefHeight(-1)) + getPadding().getBottom();
    }

    @Override
    protected double computePrefWidth(double height) {
        double labelwp = selectedLabel.prefWidth(-1);
        double linewp = line.getPadding().getLeft() + line.prefWidth(-1) + line.getPadding().getRight();
        double arrowwp = arrow.getPadding().getLeft() + arrow.prefWidth(-1) + arrow.getPadding().getRight();
        return getPadding().getLeft() + Math.max(200.0d, labelwp + linewp + arrowwp) + getPadding().getRight();
    }

    @Override
    protected double computeMaxWidth(double height) {
        return getSkinnable().prefWidth(height);
    }

    @Override
    protected double computeMinWidth(double height) {
        double labelwp = selectedLabel.prefWidth(-1);
        double linewp = line.getPadding().getLeft() + line.prefWidth(-1) + line.getPadding().getRight();
        double arrowwp = arrow.getPadding().getLeft() + arrow.prefWidth(-1) + arrow.getPadding().getRight();
        return getPadding().getLeft() + Math.max(100.0d, labelwp + linewp + arrowwp) + getPadding().getRight();
    }
}
