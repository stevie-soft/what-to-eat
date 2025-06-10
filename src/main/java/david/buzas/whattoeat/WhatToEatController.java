package david.buzas.whattoeat;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class WhatToEatController {
    @FXML
    ChoiceBox<MealCategory> mealCategoryChoiceBox;

    @FXML
    VBox rootContainer;

    @FXML
    private void initialize() {
        URL resourceUrl = WhatToEatApplication.class.getResource("meal-categories.json");
        MealCategoryRepository mealCategoryRepository;

        try {
            if (resourceUrl == null) {
                throw new IOException("Cannot read meal categories: Invalid resources URL");
            }

            mealCategoryRepository = new MealCategoryRepository(resourceUrl);
        } catch (URISyntaxException | IOException e) {
            this.fail(e.getMessage());
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

    private void fail(String message) {
        this.rootContainer.getChildren().clear();
        this.rootContainer.getChildren().add(new Label("Hiba: " + message));
    }
}