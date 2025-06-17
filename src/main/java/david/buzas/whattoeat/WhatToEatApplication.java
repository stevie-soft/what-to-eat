package david.buzas.whattoeat;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WhatToEatApplication extends Application {
    public static Repository<Meal> mealRepository = new MealRepository("user-meals.json");
    public static Repository<MealConsumption> mealConsumptionRepository = new MealConsumptionRepository("user-consumptions.json");
    public static Repository<MealCategory> mealCategoryRepository = new MealCategoryRepository("meal-categories.json");
    public static Repository<MealType> mealTypeRepository = new MealTypeRepository("meal-types.json");

    public static WhatToEatModel model = new WhatToEatModel();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WhatToEatApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setFullScreen(false);
        stage.setTitle("Mit együnk ma?");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}