package david.buzas.whattoeat.ui.controllers;

import david.buzas.whattoeat.ui.components.ErrorAlert;

public class Controller {
    protected void showErrorMessage(String message) {
        new ErrorAlert(message).showAndWait();
    }

    protected void showError(Exception exception) {
        this.showErrorMessage(exception.getMessage());
    }
}
