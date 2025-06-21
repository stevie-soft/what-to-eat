package david.buzas.whattoeat.ui.components;

import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.ui.utils.ObjectStringConverter;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class MealCategoryChoiceBox extends ChoiceBox<MealCategory> {

    public MealCategoryChoiceBox() {
        StringConverter<MealCategory> converter = new ObjectStringConverter<>(MealCategory::getDisplayName);
        this.setConverter(converter);
    }

}
