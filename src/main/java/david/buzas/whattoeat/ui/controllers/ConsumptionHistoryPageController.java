package david.buzas.whattoeat.ui.controllers;

import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.AppState;
import david.buzas.whattoeat.states.MealConsumptionFormState;
import david.buzas.whattoeat.ui.components.DeleteConfirmationDialog;
import david.buzas.whattoeat.ui.components.ErrorAlert;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.Optional;
import java.util.UUID;

public class ConsumptionHistoryPageController {

    @FXML
    public ChoiceBox<Meal> mealChoiceBox;

    @FXML
    public DatePicker consumptionDatePicker;

    @FXML
    public Button addButton;

    @FXML
    public Button updateButton;

    @FXML
    public Button removeButton;

    @FXML
    public ListView<MealConsumption> consumptionHistoryListView;

    AppState state = WhatToEatApplication.state;
    MealConsumptionFormState formState = WhatToEatApplication.state.mealConsumptionFormState;

    @FXML
    private void initialize() {
        this.mealChoiceBox.itemsProperty().bind(this.state.mealsState.property);
        this.mealChoiceBox.valueProperty().bindBidirectional(this.formState.mealProperty);

        this.consumptionDatePicker.valueProperty().bindBidirectional(this.formState.dateProperty);

        this.consumptionHistoryListView.itemsProperty().bind(this.state.mealConsumptionsState.property);
        this.consumptionHistoryListView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) ->
                this.formState.selectedMealConsumptionProperty.set(newValue)
        );
        this.formState.selectedMealConsumptionProperty.addListener((_, _, mealConsumption) -> {
            this.consumptionHistoryListView.getSelectionModel().select(mealConsumption);
        });

        this.updateButton.disableProperty().bind(this.formState.editDisabledProperty);
        this.removeButton.disableProperty().bind(this.formState.editDisabledProperty);

        this.applyCustomFieldConfigurations();
    }

    private void applyCustomFieldConfigurations() {
        this.mealChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Meal meal) {
                return meal != null ? meal.getTitle() : "Válassz ételt...";
            }

            @Override
            public Meal fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    private void onAddConsumption() {
        try {
            this.formState.addNewMealConsumption();
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onUpdateConsumption() {
        try {
            this.formState.updateSelectedMealConsumption();
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onRemoveConsumption() {
        UUID mealUuid = this.formState.selectedMealConsumptionProperty.get().getMealUuid();
        Meal meal;

        try {
            meal = this.state.repositories.getMealRepository().getByUuid(mealUuid);
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
            return;
        }

        String resourceName = meal.getTitle() + " - " + this.formState.selectedMealConsumptionProperty.get().getDate();
        Alert dialog = new DeleteConfirmationDialog(resourceName);
        Optional<ButtonType> userSelection = dialog.showAndWait();

        if (userSelection.isEmpty()) {
            return;
        }

        if (userSelection.get() != ButtonType.OK) {
            return;
        }

        try {
            this.formState.removeSelectedMealConsumption();
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
        }
    }
}
