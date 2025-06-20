package david.buzas.whattoeat.states;

import lombok.Data;

@Data
public class AppConfigState {
    private String mealsFilePath;
    private String mealConsumptionsFilePath;
    private String mealCategoriesFilePath;
    private String mealTypesFilePath;
}
