package david.buzas.whattoeat;

import lombok.Data;

import java.util.UUID;

@Data
public class Meal implements Entity {
    private UUID uuid;
    private String title;
    private String categoryKey;
    private String typeKey;
    private int consumptionFrequencyDays;
    private int averageCostForint;
}
