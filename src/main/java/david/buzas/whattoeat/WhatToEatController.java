package david.buzas.whattoeat;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.MealCategoryRepository;
import david.buzas.whattoeat.repositories.MealTypeRepository;
import david.buzas.whattoeat.repositories.Repository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
        String fileNameRaw = "meal-categories.json";
        URL resourceUrl = WhatToEatApplication.class.getResource(fileNameRaw);
        MealCategoryRepository mealCategoryRepository;

        try {
            if (resourceUrl == null) {
                throw new IOException(String.format("Cannot read meal categories: Invalid resource '%s'", fileNameRaw));
            }

            mealCategoryRepository = new MealCategoryRepository(resourceUrl);
        } catch (URISyntaxException | IOException e) {
            this.fatal(e.getMessage());
            return;
        }

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
        this.mealCategoryChoiceBox.setItems(FXCollections.observableList(mealCategoryRepository.getAll()));
    }

    private void initializeMealTypeChoiceBox() {
        String fileNameRaw = "meal-types.json";
        URL resourceUrl = WhatToEatApplication.class.getResource(fileNameRaw);
        MealTypeRepository mealTypeRepository;

        try {
            if (resourceUrl == null) {
                throw new IOException(String.format("Cannot read meal types: Invalid resource '%s'", fileNameRaw));
            }

            mealTypeRepository = new MealTypeRepository(resourceUrl);
        } catch (URISyntaxException | IOException e) {
            this.fatal(e.getMessage());
            return;
        }

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
        this.mealTypeChoiceBox.setItems(FXCollections.observableList(mealTypeRepository.getAll()));
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