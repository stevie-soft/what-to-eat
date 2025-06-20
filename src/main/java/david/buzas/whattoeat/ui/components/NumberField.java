package david.buzas.whattoeat.ui.components;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

public class NumberField extends TextField {
    public NumberField() {
        int defaultValue = 0;
        StringConverter<Integer> converter = new IntegerStringConverter();
        TextFormatter<Integer> formatter = new TextFormatter<>(converter, defaultValue);
        this.setTextFormatter(formatter);
    }
}
