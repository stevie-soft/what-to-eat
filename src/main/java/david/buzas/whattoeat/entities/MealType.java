package david.buzas.whattoeat.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class MealType implements Entity {
    private UUID uuid;
    private String key;
    private String displayName;
}
