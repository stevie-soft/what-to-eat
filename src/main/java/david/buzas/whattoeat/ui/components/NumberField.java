package david.buzas.whattoeat.ui.components;

import javafx.scene.control.TextField;

public class NumberField extends TextField {
    public NumberField() {
        this.textProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                this.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }
}
