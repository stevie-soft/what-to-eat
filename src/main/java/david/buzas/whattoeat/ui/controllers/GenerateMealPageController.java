package david.buzas.whattoeat.ui.controllers;

import david.buzas.whattoeat.entities.MealType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GenerateMealPageController {

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


    @FXML
    private void initialize() {

    }


    @FXML
    public void onGenerate() {

    }
}
