/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Callback;
import playground.control.Spinner;
import playground.control.SpinnerCell;

/**
 *
 * @author jojorabbit
 */
public class CellFactoryUtils {

    public static <T> Callback<Spinner<T>, SpinnerCell<T>> createSpinnerFontCellFactory() {
        return new Callback<Spinner<T>, SpinnerCell<T>>() {

            @Override
            public SpinnerCell<T> call(Spinner<T> param) {
                final Label label = new Label();
                final SpinnerCell<T> cell = new SpinnerCell<T>() {

                    @Override
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            label.setText(item.toString());
                            label.setFont(new Font(item.toString(), 12));
                            setNode(label);
                        }
                    }
                };
                return cell;
            }
        };
    }
}
