package david.buzas.whattoeat;

public class MealRepository extends ItemRepository<Meal> {

    public MealRepository() {
        super(Meal.class, new JsonFileItemManager<>(Meal.class, "user-meals.json"));
    }
}
