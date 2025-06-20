package david.buzas.whattoeat.ui.controllers;

import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GenerateMealPageController extends Controller {

    @FXML
    Label soupNameLabel;

    @FXML
    Label mainCourseNameLabel;

    @FXML
    Label sideDishNameLabel;

    @FXML
    Label extraDishNameLabel;

    @FXML
    Label totalCostForintLabel;

    @FXML
    Button generateButton;


    AppState state = WhatToEatApplication.state;

    @FXML
    private void initialize() {
        this.soupNameLabel.textProperty().bind(this.state.generateMealState.soupName);
        this.mainCourseNameLabel.textProperty().bind(this.state.generateMealState.mainCourseName);
        this.sideDishNameLabel.textProperty().bind(this.state.generateMealState.sideDishName);
        this.extraDishNameLabel.textProperty().bind(this.state.generateMealState.extraDishName);
        this.totalCostForintLabel.textProperty().bind(this.state.generateMealState.totalCostForint);
    }

    @FXML
    public void onGenerate() {
        try {
            this.state.generateMealState.generate();
        } catch (Repository.OperationException e) {
            this.showError(e);
        }
    }
}
