/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playground.control;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import static java.lang.Math.*;

/**
 *
 * @author jojorabbit
 */
public class DeckPaneSkin extends SkinBase<DeckPane, DeckPaneBehavior> {

    private Node visibleNodeRef = null;
    // add it with CSS
    private Duration animationDuration;
    private Timeline timeline;
    private StackPane contentStack;
    private boolean removeNode = true;
    private DoubleProperty angle;
    private DeckPane.AnimationMode mode;
    private Animation animation;

    public DeckPaneSkin(DeckPane deckPane) {
        super(deckPane, new DeckPaneBehavior(deckPane));
        System.out.println("skin init");
        contentStack = new StackPane();
        registerChangeListener(deckPane.visibleNodeIDProperty(), "VISIBLE_NODE");
        registerChangeListener(deckPane.animationModeProperty(), "ANIMATION_MODE");
        registerChangeListener(deckPane.animationDurationProperty(), "ANIMATION_DURATION");

        findVisibleNode();
        System.out.println("deck: " + deckPane);
        getChildren().setAll(contentStack);
        animationDuration = getSkinnable().getAnimationDuration();
        if (visibleNodeRef != null) {
            contentStack.getChildren().add(visibleNodeRef);

        }
        System.out.println("skin init done");

    }

    private DoubleProperty angleProperty() {
        if (angle == null) {
            angle = new DoubleProperty(90.0d);
        }
        return angle;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        System.out.println("handleProp");
        super.handleControlPropertyChanged(string);
        if (string.equals("VISIBLE_NODE")) {
            doAnimationTransition();
        } else if (string.equals("ANIMATION_MODE")) {
            mode = getSkinnable().getAnimationMode();
        } else if (string.equals("ANIMATION_DURATION")) {
            animationDuration = getSkinnable().getAnimationDuration();
        }
    }

    private void doAnimationTransition() {
        if (timeline != null) {

            System.out.println("----> timeline status: " + timeline.getStatus());
            if (timeline.getStatus() == Status.RUNNING) {
                timeline.stop();
            }
        }

        if (animation != null) {
//            System.out.println("----> animation status: " + animation.getStatus());
            if (animation.getStatus() == Status.RUNNING) {
                animation.stop();
//                System.out.println("----> animation status: " + animation.getStatus());
            }
        }

        System.out.println("doAnimation");
        timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().setAll(generateKeyFrames());

        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("-- TIME LINE FINISHED -- ");
            }
        });
        timeline.playFromStart();
    }

    private ObservableList<KeyFrame> generateKeyFrames() {
        ObservableList<KeyFrame> frames = FXCollections.<KeyFrame>observableArrayList();


        if (removeNode) {
            KeyFrame keyFrame1 = new KeyFrame(Duration.ONE, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("handle fade out");
                    if (mode == DeckPane.AnimationMode.PERSPECTIVE) {
                        contentStack.effectProperty().bind(new ObjectBinding<Effect>() {

                            {
                                super.bind(angleProperty());
                            }

                            @Override
                            protected Effect computeValue() {
                                return getTransform(contentStack.getLayoutBounds().getWidth(), contentStack.getLayoutBounds().getHeight());
                            }
                        });
                        angle.setValue(90.0d);
                    }
                    animation = createOutAnimation();

                    animation.playFromStart();
                }
            });

            frames.setAll(keyFrame1);
        } else {
            KeyFrame keyFrame1 = new KeyFrame(Duration.ONE, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("handle fadein");
                    removeNode = true;
                    findVisibleNode();
                    if (mode == DeckPane.AnimationMode.PERSPECTIVE) {
                        contentStack.effectProperty().bind(new ObjectBinding<Effect>() {

                            {
                                super.bind(angleProperty());
                            }

                            @Override
                            protected Effect computeValue() {
                                return getTransform(contentStack.getLayoutBounds().getWidth(), contentStack.getLayoutBounds().getHeight());
                            }
                        });
                        angle.set(0.0d);
                    }
                    contentStack.setCache(true);
                    contentStack.getChildren().setAll(visibleNodeRef);
                    animation = createInAnimation();
                    animation.playFromStart();
                    contentStack.getParent().impl_transformsChanged();
                }
            });
            frames.setAll(keyFrame1);

        }
        return frames;
    }

    private void findVisibleNode() {
        System.out.println("findVisibleNode");
        visibleNodeRef = null;
        if (getSkinnable().getVisibleNodeID() == null) {
            System.err.println("visibleNodeId is not set.");
            return;
        }
        for (Node node : getSkinnable().getContentNodes()) {
            if (node.getId().equals(getSkinnable().getVisibleNodeID())) {
                removeNode = true;
                visibleNodeRef = node;
                System.out.println("visibleNodeRef found: " + visibleNodeRef);
                break;
            }
        }
        if (visibleNodeRef == null) {
            System.err.println("Could not find node with ID: " + getSkinnable().getVisibleNodeID());
        }
    }

    private Animation createOutAnimation() {
        // finish handler
        EventHandler<ActionEvent> onFinishedHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                contentStack.effectProperty().unbind();
                System.out.println("finished: + " + angleProperty().get());
                removeNode = false;
                doAnimationTransition();
            }
        };

        //
        if (mode == DeckPane.AnimationMode.SCALE) {
            ScaleTransition animation = new ScaleTransition(animationDuration, contentStack);
            animation.setFromX(1.0);
            animation.setFromY(1.0);
            animation.setToX(0.0);
            animation.setToY(0.0);
            animation.setInterpolator(Interpolator.EASE_OUT);
            animation.setCycleCount(1);
            animation.setOnFinished(onFinishedHandler);
            return animation;
        } else if (mode == DeckPane.AnimationMode.PERSPECTIVE) {
            Timeline perspectiveTimeline = new Timeline();
            perspectiveTimeline.setCycleCount(1);
            perspectiveTimeline.getKeyFrames().addAll(
                    new KeyFrame(animationDuration,
                    onFinishedHandler,
                    new KeyValue(angleProperty(), 180.0d, Interpolator.EASE_OUT)));
            return perspectiveTimeline;
        } else {
            FadeTransition animation = new FadeTransition(animationDuration, contentStack);
            animation.setFromValue(1.0);
            animation.setToValue(0.0);
            animation.setInterpolator(Interpolator.EASE_OUT);
            animation.setCycleCount(1);
            animation.setOnFinished(onFinishedHandler);
            return animation;
        }
    }

    private Animation createInAnimation() {
        if (mode == DeckPane.AnimationMode.SCALE) {
            ScaleTransition animation = new ScaleTransition(animationDuration, contentStack);
            animation.setFromX(0.0);
            animation.setFromY(0.0);
            animation.setToX(1.0);
            animation.setToY(1.0);
            animation.setInterpolator(Interpolator.EASE_IN);
            animation.setCycleCount(1);
            return animation;
        } else if (mode == DeckPane.AnimationMode.PERSPECTIVE) {
            Timeline perspectiveTimeline = new Timeline();
            perspectiveTimeline.setCycleCount(1);
            perspectiveTimeline.getKeyFrames().addAll(
                    new KeyFrame(animationDuration,
                    new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            contentStack.effectProperty().unbind();
                        }
                    },
                    new KeyValue(angleProperty(), 90.0d, Interpolator.EASE_IN)));
            return perspectiveTimeline;
        } else {
            System.out.println("creating FadeIn.");
            FadeTransition animation = new FadeTransition(animationDuration, contentStack);
            animation.setFromValue(0.0);
            animation.setToValue(1.0);
            animation.setInterpolator(Interpolator.EASE_IN);
            animation.setCycleCount(1);
            return animation;
        }
    }

    private PerspectiveTransform getTransform(double width, double height) {
        PerspectiveTransform transform = new PerspectiveTransform();
        double ox = 0;
        double oy = 0;
        double pw = width;
        double ph = height;
        double radius = pw / 2;
        double back = ph / 10;
        double t = toRadians(angle.get());

        transform.setUlx(ox + radius - sin(t) * radius);
        transform.setUly(oy + 0 - cos(t) * back);
        transform.setUrx(ox + radius + sin(t) * radius);
        transform.setUry(oy + 0 + cos(t) * back);
        transform.setLrx(ox + radius + sin(t) * radius);
        transform.setLry(oy + ph - cos(t) * back);
        transform.setLlx(ox + radius - sin(t) * radius);
        transform.setLly(oy + ph + cos(t) * back);
        return transform;
    }
}