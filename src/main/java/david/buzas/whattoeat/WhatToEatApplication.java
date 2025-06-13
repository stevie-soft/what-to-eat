package david.buzas.whattoeat;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.repositories.MealRepository;
import david.buzas.whattoeat.repositories.Repository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WhatToEatApplication extends Application {
    public static Repository<Meal> mealRepository = new MealRepository();

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