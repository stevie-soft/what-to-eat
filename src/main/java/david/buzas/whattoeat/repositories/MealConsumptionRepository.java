package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.itemmanagement.ItemRepository;
import david.buzas.whattoeat.itemmanagement.JsonFileItemManager;

public class MealConsumptionRepository extends ItemRepository<MealConsumption> {
    public MealConsumptionRepository(String filePathRaw) {
        super(MealConsumption.class, new JsonFileItemManager<>(MealConsumption.class, filePathRaw));
    }
}
