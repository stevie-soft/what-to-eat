package david.buzas.whattoeat.ui.components;

import david.buzas.whattoeat.entities.Meal;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class MealsListView extends ListView<Meal> {
    public MealsListView() {
        this.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Meal meal, boolean empty) {
                super.updateItem(meal, empty);
                this.setText(empty || meal == null ? null : meal.getTitle());
            }
        });
    }
}
