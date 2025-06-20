package david.buzas.whattoeat.ui.components;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class RemoveButton extends Button {
    public RemoveButton() {
        this.setText("🗑 Töröl");
        this.setTextFill(Color.RED);
    }
}
