package playground.control;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListCell;

/**
 *
 * @author jojorabbit
 */
public class ListComboBoxBehavior<T> extends BehaviorBase<ListComboBox<T>> {

    protected static final List<KeyBinding> LIST_COMBO_BOX_BINDINGS = new ArrayList();

    public ListComboBoxBehavior(ListComboBox<T> listComboBox) {
        super(listComboBox);
    }

    // ***********************************
    //             cell util methods
    // ***********************************
    public ListCell<T> createCell() {
        ListCell<T> tempCell;

        tempCell = getControl().getCellFactory().call(getControl().listView);
        return tempCell;
    }

    public void setCellIndex(ListCell<T> paramCell, int index) {
        int oldIndex = paramCell.getIndex();
//        System.out.println("oldIndex: " + oldIndex + " index: " + index);
        if (oldIndex != index) {
            paramCell.updateIndex(index);
            configCell(paramCell);
        }
    }

    public void configCell(ListCell<T> paramCell) {
        int index = paramCell.getIndex();
        T item = getControl().listView.getItems().get(index);
//        System.out.println("item: " + item);
        paramCell.updateItem(item, false);
    }

    public ListCell<T> getCell(int index) {
        ListCell<T> tempCell = createCell();
        tempCell.updateIndex(index);
        return tempCell;
    }

    @Override
    protected List<KeyBinding> createKeyBindings() {
        return LIST_COMBO_BOX_BINDINGS;
    }

    static {
        LIST_COMBO_BOX_BINDINGS.addAll(TRAVERSAL_BINDINGS);        
    }
}
