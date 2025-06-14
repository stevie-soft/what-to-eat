package david.buzas.whattoeat.controllers;

import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.entities.MealType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WhatToEatPageController {

    @FXML
    ChoiceBox<MealType> mealTypeFilterChoiceBox;

    @FXML
    TextField mealConsumptionFrequenceDaysFilterTextField;

    @FXML
    TextField mealMaximumTotalCostTextField;

    @FXML
    Label resultText;

    @FXML
    Button generateButton;

    AppModel model = WhatToEatApplication.model;

    @FXML
    private void initialize() {
        this.model.filterFormModel.mealTypeProperty.bindBidirectional(this.mealTypeFilterChoiceBox.valueProperty());
        this.model.filterFormModel.ateDaysAgoProperty.bindBidirectional(this.mealConsumptionFrequenceDaysFilterTextField.textProperty());
        this.model.filterFormModel.maximumTotalCostForintProperty.bindBidirectional(this.mealMaximumTotalCostTextField.textProperty());
        this.model.generatedResult.bindBidirectional(this.resultText.textProperty());
    }


    @FXML
    public void onGenerate() {
        this.model.generateMeals();
    }
}
