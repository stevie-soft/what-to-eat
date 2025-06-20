package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.util.*;

public class GenerateMealState extends PartialAppState {
    public ObjectProperty<String> soup;
    public ObjectProperty<String> mainCourse;
    public ObjectProperty<String> sideDish;
    public ObjectProperty<String> extraDish;
    public ObjectProperty<String> totalCostForint;

    Repository<Meal> mealRepository;
    Repository<MealConsumption> mealConsumptionRepository;

    public GenerateMealState(AppState appState) {
        super(appState);
        this.soup = new SimpleObjectProperty<>();
        this.mainCourse = new SimpleObjectProperty<>();
        this.sideDish = new SimpleObjectProperty<>();
        this.extraDish = new SimpleObjectProperty<>();
        this.mealRepository = this.appState.repositories.getMealRepository();
        this.mealConsumptionRepository = this.appState.repositories.getMealConsumptionRepository();
    }

    public void generate() throws Repository.OperationException {
        List<Meal> validMeals = this.getValidMeals();

        List<Meal> validSoups = this.extractMealsByTypeKey(validMeals, "soup");
        List<Meal> validMainCourses = this.extractMealsByTypeKey(validMeals, "main-course");
        List<Meal> validSideDishes = this.extractMealsByTypeKey(validMeals, "side-dish");
        List<Meal> validExtraDishes = this.extractMealsByTypeKey(validMeals, "other");

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

        this.soup.set(generatedSoupName);
        this.mainCourse.set(generatedMainCourseName);
        this.sideDish.set(generatedSideDishName);
        this.extraDish.set(generatedExtraDishName);
        this.totalCostForint.set(String.valueOf(totalCost));
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

    private List<Meal> extractMealsByTypeKey(List<Meal> meals, String mealTypeKey) {
        return meals.stream()
                .filter((meal) -> meal.getTypeKey().equals(mealTypeKey))
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
