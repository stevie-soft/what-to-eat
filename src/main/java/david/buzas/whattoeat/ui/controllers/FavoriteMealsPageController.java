package david.buzas.whattoeat.ui.controllers;

import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.AppState;
import david.buzas.whattoeat.states.MealFormState;
import david.buzas.whattoeat.ui.components.DeleteConfirmationDialog;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.Optional;

public class FavoriteMealsPageController extends Controller {
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

    AppState state = WhatToEatApplication.state;
    MealFormState formState = WhatToEatApplication.state.mealFormState;

    @FXML
    private void initialize() {
        this.applyCustomFieldConfigurations();

        this.favoriteMealsListView.itemsProperty().bind(this.state.mealsState.property);
        this.favoriteMealsListView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) ->
                this.formState.selectedMealProperty.set(newValue)
        );
        this.formState.selectedMealProperty.addListener((_, _, newValue) -> {
            this.favoriteMealsListView.getSelectionModel().select(newValue);
        });

        this.mealTitleTextField.textProperty().bindBidirectional(this.formState.mealTitleProperty);

        this.mealCategoryChoiceBox.itemsProperty().bind(this.state.mealCategoriesState.property);
        this.mealCategoryChoiceBox.valueProperty().bindBidirectional(this.formState.mealCategoryProperty);

        this.mealTypeChoiceBox.itemsProperty().bind(this.state.mealTypesState.property);
        this.mealTypeChoiceBox.valueProperty().bindBidirectional(this.formState.mealTypeProperty);

        this.updateButton.disableProperty().bind(this.formState.editDisabledProperty);
        this.removeButton.disableProperty().bind(this.formState.editDisabledProperty);

        this.mealConsuptionFrequencyDaysTextField.textProperty().bindBidirectional(this.formState.consumptionFrequencyDaysProperty);
        this.mealAverageCostForintTextField.textProperty().bindBidirectional(this.formState.averageCostForintProperty);
    }

    private void applyCustomFieldConfigurations() {
        this.favoriteMealsListView.setCellFactory(_ -> new ListCell<>() {
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

        this.mealConsuptionFrequencyDaysTextField.textProperty().addListener((_, _, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                mealConsuptionFrequencyDaysTextField.setText(newValue.replaceAll("\\D", ""));
            }
        });

        this.mealAverageCostForintTextField.textProperty().addListener((_, _, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                mealAverageCostForintTextField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    @FXML
    private void onAddMeal()  {
        try {
            this.formState.addNewMeal();
        } catch (Repository.OperationException e) {
            this.showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdateMeal()  {
        try {
            this.formState.updateSelectedMeal();
        } catch (Repository.OperationException e) {
            this.showError(e.getMessage());
        }
    }

    @FXML
    private void onRemoveMeal() {
        Alert dialog = new DeleteConfirmationDialog(this.formState.selectedMealProperty.get().getTitle());
        Optional<ButtonType> userSelection = dialog.showAndWait();

        if (userSelection.isEmpty()) {
            return;
        }

        if (userSelection.get() != ButtonType.OK) {
            return;
        }

        try {
            this.formState.removeSelectedMeal();
        } catch (Repository.OperationException e) {
            this.showError(e.getMessage());
        }
    }
}