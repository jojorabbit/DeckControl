package playground.control;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.util.Callback;

/**
 *
 * @author jojorabbit
 * 
 */
public class ListComboBox<T> extends Control {

    private static final String LIST_COMBO_BOX_STYLE_CLASS = "list-combo-box";
    protected static final String PSEUDO_CLASS_SHOWING = "showing";
    protected ListView<T> listView;
    protected DoubleProperty visibleHeight = new DoubleProperty(0.0d);
    private BooleanProperty showing = new BooleanProperty() {

        @Override
        protected void invalidated() {
            ListComboBox.this.impl_pseudoClassStateChanged(PSEUDO_CLASS_SHOWING);
        }
    };

    public ListComboBox() {
        init();
        initListeners();
        visibleHeight.set(200.0d); // set default height to 200.0
    }

    public boolean isShowing() {
        return showing.get();
    }

    public BooleanProperty showingProperty() {
        return showing;
    }

    @Override
    public void impl_getPseudoClassState(List<String> list) {
        super.impl_getPseudoClassState(list);
        if(isShowing()) {
            list.add(PSEUDO_CLASS_SHOWING);
        }
    }

    
    
    private void init() {
        getStyleClass().add(LIST_COMBO_BOX_STYLE_CLASS);
        listView = new ListView<T>();
        listView.getStyleClass().add("list");
    }

    public void setItems(ObservableList<T> items) {
        listView.setItems(items);
    }

    /**
     * Sets visible size of wrapped ListView    
     * @param value
     */
    public void setVisibleHeight(double value) {
        visibleHeight.set(value);
    }

    /**
     * Add cell factory to listView
     * @param value cellFactory
     */
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        listView.setCellFactory(value);
    }

    /**
     * Get listView selections Model
     * @return selection model
     */
    public MultipleSelectionModel<T> getSelectionModel() {
        return listView.getSelectionModel();
    }

    /**
     * Get selected item
     * @return selected item 
     */
    public T getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }

    /**
     * Get selected index
     * @return selected index
     */
    public int getSelectedIndex() {
        return listView.getSelectionModel().getSelectedIndex();
    }

    private void initListeners() {
        visibleHeight.addListener(new InvalidationListener<Number>() {

            @Override
            public void invalidated(ObservableValue<? extends Number> observable) {
                listView.setPrefHeight(visibleHeight.get());
                listView.setMinHeight(visibleHeight.get());
                listView.setMaxHeight(visibleHeight.get());
            }
        });
    }
}
