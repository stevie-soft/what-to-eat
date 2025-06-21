package david.buzas.whattoeat.states.utils;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

public class ArrayListProperty<TItem> extends SimpleListProperty<TItem> {
    public ArrayListProperty() {
        super(FXCollections.observableList(new ArrayList<>()));
    }

    public ArrayListProperty(List<TItem> items) {
        super(FXCollections.observableList(items));
    }
}
