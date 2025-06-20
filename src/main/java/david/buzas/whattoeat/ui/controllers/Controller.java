package david.buzas.whattoeat.ui.controllers;

import david.buzas.whattoeat.ui.components.ErrorAlert;

public class Controller {
    protected void showError(String message) {
        new ErrorAlert(message).showAndWait();
    }

    private void showError(Exception exception) {
        this.showError(exception.getMessage());
    }
}
