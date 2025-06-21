package david.buzas.whattoeat;

import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.AppState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WhatToEatApplication extends Application {

    public static AppState state = new AppState();

    @Override
    public void start(Stage stage) throws IOException, Repository.OperationException {
        WhatToEatApplication.state.setup();

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