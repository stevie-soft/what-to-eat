package david.buzas.whattoeat.controllers;

import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.Optional;

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

    AppModel model = WhatToEatApplication.model;

    @FXML
    private void initialize() {
        this.mealChoiceBox.itemsProperty().bind(this.model.mealsProperty);
        this.mealChoiceBox.valueProperty().bindBidirectional(this.model.consumptionFormModel.mealProperty);
        this.consumptionDatePicker.valueProperty().bindBidirectional(this.model.consumptionFormModel.dateProperty);
        this.consumptionHistoryListView.itemsProperty().bind(this.model.consumptionsProperty);
        this.consumptionHistoryListView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) ->
                this.model.selectedConsumptionProperty.set(newValue)
        );
        this.model.selectedConsumptionProperty.addListener((_, _, newValue) -> {
            this.consumptionHistoryListView.getSelectionModel().select(newValue);

            try {
                this.model.consumptionFormModel.loadValues(newValue);
            } catch (Repository.OperationException e) {
                new ErrorAlert(e.getMessage()).showAndWait();
            }
        });
        this.updateButton.disableProperty().bind(this.model.consumptionEditingDisabledProperty);
        this.removeButton.disableProperty().bind(this.model.consumptionEditingDisabledProperty);

        this.applyCustomFieldConfigurations();

        try {
            this.model.setup();
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
        }
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
            this.model.addConsumption();
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onUpdateConsumption() {
        try {
            this.model.updateConsumption();
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onRemoveConsumption() {
        Alert dialog = new DeleteConfirmationDialog(this.model.selectedConsumptionProperty.get().toString());
        Optional<ButtonType> userSelection = dialog.showAndWait();

        if (userSelection.isEmpty()) {
            return;
        }

        if (userSelection.get() != ButtonType.OK) {
            return;
        }

        try {
            this.model.removeConsumption();
        } catch (Repository.OperationException e) {
            new ErrorAlert(e.getMessage()).showAndWait();
        }
    }
}
