package david.buzas.whattoeat.ui.components;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.ui.utils.ObjectStringConverter;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class MealChoiceBox extends ChoiceBox<Meal> {

    public MealChoiceBox() {
        StringConverter<Meal> converter = new ObjectStringConverter<>(Meal::getTitle);
        this.setConverter(converter);
    }

}
