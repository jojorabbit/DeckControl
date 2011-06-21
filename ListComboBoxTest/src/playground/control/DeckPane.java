package playground.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.util.Duration;

/**
 *
 * @author jojorabbit
 */
public class DeckPane extends Control {

    private ObservableList<Node> contentNodes = FXCollections.<Node>observableArrayList();
    private StringProperty visibleNodeID = new StringProperty(null);
    private ObjectProperty<AnimationMode> animationMode;
    private ObjectProperty<Duration> animationDuration;

    public DeckPane() {
        init();
    }

    private void init() {
        getStyleClass().setAll("deck-pane");
    }

    public ObjectProperty<Duration> animationDurationProperty() {
        if (animationDuration == null) {
            animationDuration = new ObjectProperty<Duration>(Duration.valueOf(500.0d));
        }
        return animationDuration;
    }

    public Duration getAnimationDuration() {
        return animationDurationProperty().get();
    }

    public void setAnimationDuration(Duration value) {
        animationDurationProperty().set(value);
    }

    public AnimationMode getAnimationMode() {
        return animationModeProperty().get();
    }

    public void setAnimationMode(AnimationMode mode) {
        animationModeProperty().set(mode);
    }

    public ObjectProperty<AnimationMode> animationModeProperty() {
        if (animationMode == null) {
            animationMode = new ObjectProperty<AnimationMode>(AnimationMode.FADE);
        }
        return animationMode;
    }

    public StringProperty visibleNodeIDProperty() {
        if (visibleNodeID == null) {
            visibleNodeID = new StringProperty(null);
        }
        return visibleNodeID;
    }

    public final String getVisibleNodeID() {
        return visibleNodeID == null ? null : visibleNodeID.get();
    }

    public final void setVisibleNodeID(String nodeId) {
        System.out.println("setting visible to: " + nodeId);
        visibleNodeIDProperty().set(nodeId);
    }

    public ObservableList<Node> getContentNodes() {
        return contentNodes;
    }

    // public enum
    public enum AnimationMode {

        FADE,
        SCALE,
        PERSPECTIVE
    }
}
