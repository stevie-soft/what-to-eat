package david.buzas.whattoeat.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class MealCategory implements Entity {
    private UUID uuid;
    private String key;
    private String displayName;
}
