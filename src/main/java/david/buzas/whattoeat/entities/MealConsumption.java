package david.buzas.whattoeat.entities;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MealConsumption implements Entity {
    private UUID uuid;
    private UUID mealUuid;
    private LocalDate date;
}
