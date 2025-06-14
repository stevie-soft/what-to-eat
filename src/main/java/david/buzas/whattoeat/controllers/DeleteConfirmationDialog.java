package david.buzas.whattoeat.controllers;

import javafx.scene.control.Alert;

public class DeleteConfirmationDialog extends Alert {
    public DeleteConfirmationDialog(String resourceName) {
        super(AlertType.CONFIRMATION);

        this.setTitle("Törlés");
        this.setHeaderText("Visszavonhatatlan műveletre készülsz!");
        this.setContentText(String.format(
                "Biztosan ki szeretnéd törölni a következő elemet: '%s'?",
                resourceName
        ));
    }
}
