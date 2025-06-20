package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.*;

public class GenerateMealState extends PartialAppState {
    public StringProperty soupName;
    public StringProperty mainCourseName;
    public StringProperty sideDishName;
    public StringProperty extraDishName;
    public StringProperty totalCostForint;

    Repository<Meal> mealRepository;
    Repository<MealConsumption> mealConsumptionRepository;

    public GenerateMealState(AppState appState) {
        super(appState);
        this.soupName = new SimpleStringProperty();
        this.mainCourseName = new SimpleStringProperty();
        this.sideDishName = new SimpleStringProperty();
        this.extraDishName = new SimpleStringProperty();
        this.totalCostForint = new SimpleStringProperty();
        this.mealRepository = this.appState.repositories.getMealRepository();
        this.mealConsumptionRepository = this.appState.repositories.getMealConsumptionRepository();
    }

    public void generate() throws Repository.OperationException {
        List<Meal> validMeals = this.getValidMeals();

        List<Meal> validSoups = this.extractMealsByCategoryKey(validMeals, "soup");
        List<Meal> validMainCourses = this.extractMealsByCategoryKey(validMeals, "main-course");
        List<Meal> validSideDishes = this.extractMealsByCategoryKey(validMeals, "side-dish");
        List<Meal> validExtraDishes = this.extractMealsByCategoryKey(validMeals, "other");

        Meal generatedSoup = this.extractRandomMeal(validSoups);
        Meal generatedMainCourse = this.extractRandomMeal(validMainCourses);
        Meal generatedSideDish = this.extractRandomMeal(validSideDishes);
        Meal generatedExtraDish = this.extractRandomMeal(validExtraDishes);

        String generatedSoupName;
        String generatedMainCourseName;
        String generatedSideDishName;
        String generatedExtraDishName;

        int totalCost = 0;

        if (generatedSoup != null) {
            generatedSoupName = generatedSoup.getTitle();
            totalCost += generatedSoup.getAverageCostForint();
        } else {
            generatedSoupName = "Nincs megfelelő leves.";
        }

        if (generatedMainCourse != null) {
            generatedMainCourseName = generatedMainCourse.getTitle();
            totalCost += generatedMainCourse.getAverageCostForint();
        } else {
            generatedMainCourseName = "Nincs megfelelő főétel.";
        }

        if (generatedSideDish != null) {
            generatedSideDishName = generatedSideDish.getTitle();
            totalCost += generatedSideDish.getAverageCostForint();
        } else {
            generatedSideDishName = "Nincs megfelelő köret.";
        }

        if (generatedExtraDish != null) {
            generatedExtraDishName = generatedExtraDish.getTitle();
            totalCost += generatedExtraDish.getAverageCostForint();
        } else {
            generatedExtraDishName = "Nincs megfelelő egyéb.";
        }

        this.soupName.set("Leves: " + generatedSoupName);
        this.mainCourseName.set("Főétel: " + generatedMainCourseName);
        this.sideDishName.set("Köret: " + generatedSideDishName);
        this.extraDishName.set("Egyéb: " + generatedExtraDishName);
        this.totalCostForint.set("Összköltség (Ft): " + totalCost);
    }

    private List<Meal> getValidMeals() throws Repository.OperationException {
        List<Meal> meals = this.mealRepository.getAll();
        return meals.stream().filter(this::isMealValid).toList();
    }

    private boolean isMealValid(Meal meal) {
        LocalDate today = LocalDate.now();

        List<MealConsumption> consumptions;

        try {
            consumptions = this.mealConsumptionRepository.findManyBy(
                    MealConsumption::getMealUuid,
                    meal.getUuid()
            );
        } catch (Repository.OperationException e) {
            return false;
        }

        Optional<MealConsumption> lastConsumption = consumptions
                .stream()
                .max(Comparator.comparing(MealConsumption::getDate));

        if (lastConsumption.isEmpty()) {
            return true;
        }

        if (lastConsumption.get().getDate().isBefore(today.minusDays(meal.getConsumptionFrequencyDays()))) {
            return true;
        }

        return false;
    }

    private List<Meal> extractMealsByCategoryKey(List<Meal> meals, String mealCategoryKey) {
        return meals.stream()
                .filter((meal) -> meal.getCategoryKey().equals(mealCategoryKey))
                .toList();
    }

    private Meal extractRandomMeal(List<Meal> meals) {
        if (meals.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int mealIndex = random.nextInt(meals.size());
        return meals.get(mealIndex);
    }
}
