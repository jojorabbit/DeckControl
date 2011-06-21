package playground.control;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

/**
 *
 * @author jojorabbit
 * TODO: maybe custom singleselectionModel
 */
public class ListComboBox<T> extends Control {

    private static final String SPINNER_STYLE_CLASS = "spinner";
    protected ListView<T> listView;

    public ListComboBox() {
        init();
    }

    private void init() {
        getStyleClass().add(SPINNER_STYLE_CLASS);
//        items = FXCollections.<T>observableArrayList();
        listView = new ListView<T>();
        listView.getStyleClass().add("list");
//        listView.setItems(items);
//        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE_SELECTION);        
    }

    public void setItems(ObservableList<T> items) {
        listView.setItems(items);
    }

//    public ObservableList<T> getItems() {
//        return listView.getItems();
//    }
    public MultipleSelectionModel<T> getSelectionModel() {
        return listView.getSelectionModel();
    }

    public T getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }

    public int getSelectedIndex() {
        return listView.getSelectionModel().getSelectedIndex();
    }
}
