package david.buzas.whattoeat.ui.components;

import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class MealConsumptionListView extends ListView<MealConsumption> {
    public MealConsumptionListView() {
        this.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(MealConsumption mealConsumption, boolean empty) {
                super.updateItem(mealConsumption, empty);

                if (mealConsumption == null) {
                    return;
                }

                Meal meal;

                try {
                    meal = WhatToEatApplication.state.repositories.getMealRepository().getByUuid(mealConsumption.getMealUuid());
                } catch (Repository.OperationException e) {
                    throw new RuntimeException(e);
                }

                String displayName = meal.getTitle() + " - " + mealConsumption.getDate().toString();
                this.setText(empty ? null : displayName);
            }
        });
    }
}
