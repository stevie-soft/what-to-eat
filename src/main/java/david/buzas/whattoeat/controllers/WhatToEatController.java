package david.buzas.whattoeat.controllers;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.Repository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.Optional;

public class WhatToEatController {
    @FXML
    public VBox rootContainer;

    @FXML
    public ChoiceBox<MealCategory> mealCategoryChoiceBox;

    @FXML
    public TextField mealTitleTextField;

    @FXML
    public TextField mealConsuptionFrequencyDaysTextField;

    @FXML
    public TextField mealAverageCostForintTextField;

    @FXML
    public ListView<Meal> favoriteMealsListView;

    @FXML
    public ChoiceBox<MealType> mealTypeChoiceBox;

    @FXML
    public Button addButton;

    @FXML
    public Button updateButton;

    @FXML
    public Button removeButton;


    WhatToEatModel model;


    @FXML
    private void initialize() {
        this.applyCustomFieldConfigurations();
        this.model = new WhatToEatModel();

        this.favoriteMealsListView.itemsProperty().bind(this.model.mealsProperty);
        this.model.selectedMealProperty.bind(this.favoriteMealsListView.getSelectionModel().selectedItemProperty());
        this.model.selectedMealProperty.addListener((observable, oldValue, newValue) -> {
            this.favoriteMealsListView.getSelectionModel().select(newValue);

            try {
                this.model.mealFormModel.loadValues(newValue);
            } catch (Repository.OperationException e) {
                this.fatal(e.getMessage());
            }
        });
        this.mealCategoryChoiceBox.itemsProperty().bind(this.model.mealCategoriesProperty);
        this.mealTypeChoiceBox.itemsProperty().bind(this.model.mealTypesProperty);
        this.updateButton.disableProperty().bind(this.model.editingDisabledProperty);
        this.removeButton.disableProperty().bind(this.model.editingDisabledProperty);

        this.mealTitleTextField.textProperty().bindBidirectional(this.model.mealFormModel.titleProperty);
        this.model.mealFormModel.categoryProperty.bindBidirectional(this.mealCategoryChoiceBox.valueProperty());
        this.model.mealFormModel.typeProperty.bindBidirectional(this.mealTypeChoiceBox.valueProperty());
        this.model.mealFormModel.consumptionFrequencyDaysProperty.bindBidirectional(this.mealConsuptionFrequencyDaysTextField.textProperty());
        this.model.mealFormModel.averageCostForintProperty.bindBidirectional(this.mealAverageCostForintTextField.textProperty());


        try {
            this.model.setup();
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }
    }

    private void applyCustomFieldConfigurations() {
        this.favoriteMealsListView.setCellFactory(mealListView -> new ListCell<>() {
            @Override
            protected void updateItem(Meal meal, boolean empty) {
                super.updateItem(meal, empty);
                this.setText(empty || meal == null ? null : meal.getTitle());
            }
        });

        this.mealCategoryChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MealCategory mealCategory) {
                return mealCategory != null ? mealCategory.getDisplayName() : "Válassz kategóriát...";
            }

            @Override
            public MealCategory fromString(String s) {
                return null;
            }
        });

        this.mealTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MealType mealType) {
                return mealType != null ? mealType.getDisplayName() : "Válassz típust...";
            }

            @Override
            public MealType fromString(String s) {
                return null;
            }
        });
    }


    @FXML
    private void onAddMeal()  {
        Meal meal = this.model.mealFormModel.buildMeal();

        try {
            this.model.addMeal(meal);
            this.favoriteMealsListView.getSelectionModel().select(meal);
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }
    }

    @FXML
    private void onUpdateMeal()  {
        Meal meal = this.model.mealFormModel.buildMeal();
        meal.setUuid(this.model.selectedMealProperty.get().getUuid());

        try {
            this.model.updateMeal(meal);
            this.favoriteMealsListView.getSelectionModel().select(meal);
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }
    }

    @FXML
    private void onRemoveMeal() {
        Alert dialog = this.buildDeleteConfirmationDialog();
        Optional<ButtonType> userSelection = dialog.showAndWait();

        if (userSelection.isEmpty()) {
            return;
        }

        if (userSelection.get() == ButtonType.OK) {
            try {
                this.model.removeMeal(this.model.selectedMealProperty.get());
                this.favoriteMealsListView.getSelectionModel().select(null);
                this.model.mealFormModel.clearValues();
            } catch (Repository.OperationException e) {
                this.fatal(e.getMessage());
            }
        }
    }

    private Alert buildDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Törlés");
        alert.setHeaderText("Visszavonhatatlan műveletre készülsz!");
        alert.setContentText(String.format(
                "Biztosan ki szeretnéd törölni a következő elemet: '%s'?",
                this.model.selectedMealProperty.get().getTitle()
        ));

        return alert;
    }

    private void fatal(String message) {
        this.rootContainer.getChildren().clear();
        this.rootContainer.getChildren().add(new Label("Végzetes hiba: " + message));
    }

}