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

    Repository<Meal> mealRepository = WhatToEatApplication.mealRepository;
    Repository<MealCategory> mealCategoryRepository = WhatToEatApplication.mealCategoryRepository;
    Repository<MealType> mealTypeRepository = WhatToEatApplication.mealTypeRepository;

    @FXML
    private void initialize() {
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

        this.syncMeals();
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
    private void saveMeal()  {
        Meal meal = new Meal();
        meal.setTitle(this.mealTitleTextField.getText());
        meal.setCategoryKey(this.mealCategoryChoiceBox.getValue().getKey());
        meal.setTypeKey(this.mealTypeChoiceBox.getValue().getKey());
        meal.setConsumptionFrequencyDays(Integer.parseInt(this.mealConsuptionFrequencyDaysTextField.getText()));
        meal.setAverageCostForint(Integer.parseInt(this.mealAverageCostForintTextField.getText()));

        try {
            this.mealRepository.add(meal);
            this.syncMeals();
        } catch (Repository.OperationException e) {
            this.fatal(e.getMessage());
        }
    }

    private void fatal(String message) {
        this.rootContainer.getChildren().clear();
        this.rootContainer.getChildren().add(new Label("Végzetes hiba: " + message));
    }

}