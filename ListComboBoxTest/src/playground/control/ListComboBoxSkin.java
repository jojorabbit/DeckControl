package playground.control;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ListCell;
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
    private StackPane cellContainer; // holder for cell
    private StackPane arrowContainer; // holder for arrow shape
    private StackPane line; // holder for line shape
    private Duration animationDuration;
    private MultipleSelectionModel<T> selectionModel;
    private static final String ANIMATION_DURATION_PROPERTY_NAME = "ANIMATION_DURATION";
    // listView transition property
    private DoubleProperty transition = new DoubleProperty(0.0d) {

        @Override
        protected void invalidated() {
            if (listView != null) {
                // refreshing skin -> like call layoutChildren on each invalidation
                ListComboBoxSkin.this.requestLayout();
            }
        }
    };
    ObjectProperty<ListCell<T>> selectedCell = new ObjectProperty<ListCell<T>>();

    public ListComboBoxSkin(ListComboBox<T> listComboBox) {
        super(listComboBox, new ListComboBoxBehavior<T>(listComboBox));
        init();
        registerChangeListener(getSkinnable().animationDurationProperty(), ANIMATION_DURATION_PROPERTY_NAME);
        initListenersAndBinds();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if (string.equals(ANIMATION_DURATION_PROPERTY_NAME)) {
            animationDuration = getSkinnable().getAnimationDuration();
        }
    }

    private void init() {
        listView = getSkinnable().listView;
        selectionModel = listView.getSelectionModel();
        selectionModel.selectFirst();
        animationDuration = getSkinnable().getAnimationDuration();


        StackPane arrow = new StackPane();
        arrow.getStyleClass().add("arrow");

        arrowContainer = new StackPane();
        arrowContainer.getStyleClass().add("arrow-container");
        arrowContainer.getChildren().add(arrow);

        line = new StackPane();
        line.getStyleClass().add("line");

        cellContainer = new StackPane();

        listView.setVisible(false);
        getChildren().addAll(cellContainer, arrowContainer, listView);
    }

    private void initListenersAndBinds() {
        selectedCell.addListener(new InvalidationListener<ListCell<T>>() {

            @Override
            public void invalidated(ObservableValue<? extends ListCell<T>> observable) {
                cellContainer.getChildren().setAll(selectedCell.get().getNode());
//                cellContainer.setStyle("-fx-border-color:red;");
//                System.out.println("selected: " + selectedCell.get()); // debug
                ListComboBoxSkin.this.requestLayout();
            }
        });

        selectedCell.bind(new ObjectBinding<ListCell<T>>() {

            {
                super.bind(selectionModel.selectedIndexProperty());
            }

            @Override
            protected ListCell<T> computeValue() {
                ListCell<T> cell = getBehavior().createCell();
                cell.setId(selectionModel.getSelectedItem().toString());
                getBehavior().setCellIndex(cell, selectionModel.getSelectedIndex());
                cell.getStyleClass().add("combo-box-cell");
                return cell;
            }
        });

        arrowContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {

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
    }

    private void doAnimation() {
        Duration duration;
        if ((this.timeline != null) && (this.timeline.getStatus() != Animation.Status.STOPPED)) {
            duration = this.timeline.getCurrentTime();
            this.timeline.stop();
        } else {
            duration = animationDuration;
        }
//        System.out.println("duration: " + duration);
        timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyFrame keyFrame1;
        KeyFrame keyFrame2;
        //
        if (!getSkinnable().isShowing()) {
            // kf1
            keyFrame1 = new KeyFrame(
                    Duration.ONE,
                    new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            listView.setVisible(true);
//                    listView.setCache(true);
                        }
                    },
                    new KeyValue(transition, 0.0d, Interpolator.LINEAR));
            // kf2
            keyFrame2 = new KeyFrame(
                    duration,
                    new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            getSkinnable().showingProperty().set(true);
//                    listView.setCache(false);
                            listView.requestFocus();
                        }
                    },
                    new KeyValue(transition, 1.0d, Interpolator.EASE_IN));
        } else {
            keyFrame1 = new KeyFrame(
                    Duration.ZERO,
                    new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
//                    listView.setVisible(true);
//                    listView.setCache(true);
                        }
                    },
                    new KeyValue(transition, 1.0d, Interpolator.LINEAR));
            //
            keyFrame2 = new KeyFrame(
                    duration,
                    new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            getSkinnable().showingProperty().set(false);
                            listView.setVisible(false);
//                    listView.setCache(false);
                            getSkinnable().requestFocus();
                        }
                    },
                    new KeyValue(transition, 0.0d, Interpolator.EASE_OUT));
        } // end if-else
        //
        timeline.getKeyFrames().setAll(keyFrame1, keyFrame2);
        timeline.play();
    }

    private double getTransition() {
        return transition.get();
    }

    // ***********************************
    //             overrides
    // ***********************************
    @Override
    protected void layoutChildren() {

        double top = getPadding().getTop();
        double right = getPadding().getRight();
        double bottom = getPadding().getBottom();
        double left = getPadding().getLeft();
        double width = this.getWidth();
        double height = this.getHeight();

        double arrowpw = arrowContainer.prefWidth(-1);

        if (getSkinnable().isArrowOnLeft()) {
            arrowContainer.resize(arrowpw, height);
            positionInArea(arrowContainer, 0.0, 0.0d, arrowpw, height, 0.0d, HPos.CENTER, VPos.CENTER);
            cellContainer.resize(width - left - right - arrowpw, height - top - bottom);
            positionInArea(cellContainer, arrowpw + left, 0.0d, width, height, 0.0d, HPos.LEFT, VPos.CENTER);
        } else {
            cellContainer.resize(width - left - right - arrowpw, height - top - bottom);
            positionInArea(cellContainer, left, 0.0, width, height, 0.0d, HPos.LEFT, VPos.CENTER);
            arrowContainer.resize(arrowpw, height);
            positionInArea(arrowContainer, width - arrowpw, 0.0, arrowpw, height, 0.0d, HPos.CENTER, VPos.CENTER);
        }
//        selectedCell.get().resize(cellContainer.prefWidth(-1), cellContainer.prefHeight(-1));
        double areaHeight = listView.prefHeight(-1) * getTransition(); // multiply with transition value -> range [0.0, 1.0]
        listView.resize(width, areaHeight);

        positionInArea(listView, 0.0d, height + top, width, areaHeight, 0.0d, HPos.CENTER, VPos.CENTER);

    }

    @Override
    protected double computeMaxHeight(double width) {
        return getPadding().getTop() + Math.max(22.0d, getSkinnable().prefHeight(width)) + getPadding().getBottom();
    }

    @Override
    protected double computePrefHeight(double width) {
        return getPadding().getTop() + Math.max(22.0d, cellContainer.prefHeight(-1)) + getPadding().getBottom();
    }

    @Override
    protected double computeMinHeight(double width) {
        return getPadding().getTop() + Math.max(22.0d, cellContainer.minHeight(-1)) + getPadding().getBottom();
    }

    @Override
    protected double computePrefWidth(double height) {
        double labelwp = cellContainer.prefWidth(-1);
        double arrowwp = arrowContainer.prefWidth(-1);
        return getPadding().getLeft() + Math.max(60.0d, labelwp + arrowwp) + getPadding().getRight();
    }

    @Override
    protected double computeMaxWidth(double height) {
        return getPadding().getLeft() + Math.max(60.0d, getSkinnable().prefWidth(height)) + getPadding().getRight();
    }

    @Override
    protected double computeMinWidth(double height) {
        return getPadding().getLeft() + Math.max(60.0d, cellContainer.minWidth(-1)) + getPadding().getRight();
    }
}
