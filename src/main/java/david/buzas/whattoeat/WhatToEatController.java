package david.buzas.whattoeat;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.Repository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WhatToEatController {
    @FXML
    VBox rootContainer;

    @FXML
    ChoiceBox<MealCategory> mealCategoryChoiceBox;

    @FXML
    TextField mealTitleTextField;

    @FXML
    TextField mealConsuptionFrequencyDaysTextField;

    @FXML
    TextField mealAverageCostForintTextField;

    @FXML
    ListView<Meal> favoriteMealsListView;

    @FXML
    ChoiceBox<MealType> mealTypeChoiceBox;

    @FXML
    Button addButton;

    @FXML
    Button updateButton;

    @FXML
    Button removeButton;

    Repository<Meal> mealRepository = WhatToEatApplication.mealRepository;
    Repository<MealCategory> mealCategoryRepository = WhatToEatApplication.mealCategoryRepository;
    Repository<MealType> mealTypeRepository = WhatToEatApplication.mealTypeRepository;

    Meal selectedMeal;

    @FXML
    private void initialize() {
        this.selectedMeal = null;
        this.updateButton.setDisable(true);
        this.removeButton.setDisable(true);
        this.initializeFavoriteMealsList();
        this.initializeMealCategoryChoiceBox();
        this.initializeMealTypeChoiceBox();
    }

    private void initializeFavoriteMealsList() {
        this.favoriteMealsListView.setCellFactory(mealListView -> new ListCell<>() {
            @Override
            protected void updateItem(Meal meal, boolean empty) {
                super.updateItem(meal, empty);
                this.setText(empty || meal == null ? null : meal.getTitle());
            }
        });

        this.favoriteMealsListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedMeal = newValue;
                    loadMeal(selectedMeal);
                    this.updateButton.setDisable(selectedMeal == null);
                    this.removeButton.setDisable(selectedMeal == null);
                }
        );

        this.syncMeals();
    }

    private void loadMeal(Meal meal) {
        if (meal == null) {
            return;
        }

        this.mealTitleTextField.setText(meal.getTitle());

        MealCategory mealCategory = null;

        try {
            mealCategory = this.mealCategoryRepository.getBy(MealCategory::getKey, meal.getCategoryKey());
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }

        if (mealCategory != null) {
            this.mealCategoryChoiceBox.setValue(mealCategory);
        }

        MealType mealType = null;

        try {
            mealType = this.mealTypeRepository.getBy(MealType::getKey, meal.getTypeKey());
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }

        if (mealType != null) {
            this.mealTypeChoiceBox.setValue(mealType);
        }

        int mealConsumptionFrequencyDays = meal.getConsumptionFrequencyDays();
        this.mealConsuptionFrequencyDaysTextField.setText(String.valueOf(mealConsumptionFrequencyDays));

        int averageCostForint = meal.getAverageCostForint();
        this.mealAverageCostForintTextField.setText(String.valueOf(averageCostForint));
    }

    private void syncMeals() {
        List<Meal> meals = new ArrayList<>();

        try {
            meals = this.mealRepository.getAll();
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }

        this.favoriteMealsListView.setItems(FXCollections.observableList(meals));
    }

    private void initializeMealCategoryChoiceBox() {
        this.mealCategoryChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MealCategory mealCategory) {
                return mealCategory != null ? mealCategory.getDisplayName() : "Válassz egyet...";
            }

            @Override
            public MealCategory fromString(String s) {
                return null;
            }
        });

        List<MealCategory> mealCategories = new ArrayList<>();

        try {
            mealCategories = this.mealCategoryRepository.getAll();
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }

        this.mealCategoryChoiceBox.setItems(FXCollections.observableList(mealCategories));
    }

    private void initializeMealTypeChoiceBox() {
        this.mealTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MealType mealType) {
                return mealType != null ? mealType.getDisplayName() : "Válassz egyet...";
            }

            @Override
            public MealType fromString(String s) {
                return null;
            }
        });

        List<MealType> mealTypes = new ArrayList<>();

        try {
            mealTypes = this.mealTypeRepository.getAll();
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }

        this.mealTypeChoiceBox.setItems(FXCollections.observableList(mealTypes));
    }

    @FXML
    private void onAddMeal()  {
        Meal meal = buildMealByFormValues();

        try {
            this.mealRepository.add(meal);
            this.syncMeals();
            this.favoriteMealsListView.getSelectionModel().select(meal);
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }
    }

    @FXML
    private void onUpdateMeal()  {
        Meal meal = buildMealByFormValues();
        meal.setUuid(selectedMeal.getUuid());

        try {
            this.mealRepository.update(meal);
            this.syncMeals();
            this.favoriteMealsListView.getSelectionModel().select(meal);
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }
    }

    @FXML
    private void onRemoveMeal() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Törlés");
        alert.setHeaderText("Visszavonhatatlan műveletre készülsz!");
        alert.setContentText(String.format(
                "Biztosan ki szeretnéd törölni a következő elemet: '%s'?",
                selectedMeal.getTitle()
        ));

        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.isEmpty()) {
            return;
        }

        if (buttonType.get() == ButtonType.OK) {
            try {
                this.mealRepository.remove(selectedMeal);
                this.syncMeals();
                this.favoriteMealsListView.getSelectionModel().select(null);
                clearForm();
                selectedMeal = null;
            } catch (Repository.OperationException e) {
                this.fatal(e.getMessage());
            }
        }
    }

    private Meal buildMealByFormValues() {
        Meal meal = new Meal();
        meal.setTitle(this.mealTitleTextField.getText());
        meal.setCategoryKey(this.mealCategoryChoiceBox.getValue().getKey());
        meal.setTypeKey(this.mealTypeChoiceBox.getValue().getKey());
        meal.setConsumptionFrequencyDays(Integer.parseInt(this.mealConsuptionFrequencyDaysTextField.getText()));
        meal.setAverageCostForint(Integer.parseInt(this.mealAverageCostForintTextField.getText()));
        return meal;
    }

    private void clearForm() {
        this.mealTitleTextField.setText(null);
        this.mealCategoryChoiceBox.setValue(null);
        this.mealTypeChoiceBox.setValue(null);
        this.mealConsuptionFrequencyDaysTextField.setText(null);
        this.mealAverageCostForintTextField.setText(null);
    }

    private void fatal(String message) {
        this.rootContainer.getChildren().clear();
        this.rootContainer.getChildren().add(new Label("Végzetes hiba: " + message));
    }

}