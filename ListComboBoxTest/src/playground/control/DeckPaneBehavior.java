/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playground.control;

import com.sun.javafx.scene.control.behavior.BehaviorBase;

/**
 *
 * @author jojorabbit
 * Nothing special for now.
 */
class DeckPaneBehavior extends BehaviorBase<DeckPane> {

    public DeckPane deckPane;

    public DeckPaneBehavior(DeckPane deckPane) {
        super(deckPane);
        this.deckPane = deckPane;
    }
}
