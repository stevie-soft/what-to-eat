package david.buzas.whattoeat.controllers;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert {
    public ErrorAlert(String message) {
        super(AlertType.ERROR);

        this.setTitle("Hiba!");
        this.setHeaderText("Váratlan probléma lépett fel :(");
        this.setContentText(message);
    }
}
