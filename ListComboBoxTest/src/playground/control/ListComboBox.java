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
    protected static final String PSEUDO_CLASS_ARROW_ON_LEFT = "arrow-on-left";
    protected ListView<T> listView;
    protected DoubleProperty dropDownHeight = new DoubleProperty(0.0d);
    private BooleanProperty arrowOnLeft = new BooleanProperty(false) {

        @Override
        protected void invalidated() {
            ListComboBox.this.impl_pseudoClassStateChanged(PSEUDO_CLASS_ARROW_ON_LEFT);
        }
    };
    private BooleanProperty showing = new BooleanProperty() {

        @Override
        protected void invalidated() {
            ListComboBox.this.impl_pseudoClassStateChanged(PSEUDO_CLASS_SHOWING);
        }
    };

    public ListComboBox() {
        init();
        initListeners();
        dropDownHeight.set(200.0d); // set default dropDownHeight to 200.0
    }

    public final boolean isArrowOnLeft() {
        return arrowOnLeft.get();
    }

    public final void setArrowOnLeft(boolean value) {
        arrowOnLeft.set(value);
    }

    public final boolean isShowing() {
        return showing.get();
    }

    public BooleanProperty showingProperty() {
        return showing;
    }

    @Override
    public void impl_getPseudoClassState(List<String> list) {
        super.impl_getPseudoClassState(list);
        if (isShowing()) {
            list.add(PSEUDO_CLASS_SHOWING);
        }
        if (isArrowOnLeft()) {
            list.add(PSEUDO_CLASS_ARROW_ON_LEFT);
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
    public void setDropDownHeight(double value) {
        dropDownHeight.set(value);
    }

    /**
     * Add cell factory to listView
     * @param value cellFactory
     */
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        listView.setCellFactory(value);
    }

    public Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return listView.getCellFactory();
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
        dropDownHeight.addListener(new InvalidationListener<Number>() {

            @Override
            public void invalidated(ObservableValue<? extends Number> observable) {
                listView.setPrefHeight(dropDownHeight.get());
                listView.setMinHeight(dropDownHeight.get());
                listView.setMaxHeight(dropDownHeight.get());
            }
        });
    }
}
