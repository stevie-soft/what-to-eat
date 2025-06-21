package david.buzas.whattoeat.ui.components;

import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.ui.utils.ObjectStringConverter;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class MealTypeChoiceBox extends ChoiceBox<MealType> {

    public MealTypeChoiceBox() {
        StringConverter<MealType> converter = new ObjectStringConverter<>(MealType::getDisplayName);
        this.setConverter(converter);
    }

}
