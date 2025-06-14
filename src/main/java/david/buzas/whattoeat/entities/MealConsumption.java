package david.buzas.whattoeat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.repositories.Repository;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MealConsumption implements Entity {
    private UUID uuid;
    private UUID mealUuid;
    private LocalDate date;

    @JsonIgnore
    public Meal getMeal() throws Repository.OperationException {
        return WhatToEatApplication.mealRepository.getByUuid(this.mealUuid);
    }

    @Override
    public String toString() {
        try {
            return String.format("%s - %s", this.getMeal().getTitle(), this.date.toString());
        } catch (Repository.OperationException e) {
            return String.format("unknown meal - %s", this.date.toString());
        }
    }
}
