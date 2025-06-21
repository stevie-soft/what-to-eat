package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;

import java.time.LocalDate;
import java.util.*;

public class GenerateMealState extends PartialAppState {
    public StringProperty soupName;
    public StringProperty mainCourseName;
    public StringProperty sideDishName;
    public StringProperty extraDishName;
    public StringProperty totalCostForintText;

    public ObjectProperty<Meal> soup;
    public ObjectProperty<Meal> mainCourse;
    public ObjectProperty<Meal> sideDish;
    public ObjectProperty<Meal> extraDish;
    public IntegerProperty totalCostForint;

    Repository<Meal> mealRepository;
    Repository<MealConsumption> mealConsumptionRepository;

    public GenerateMealState(AppState appState) {
        super(appState);

        this.mealRepository = this.appState.repositories.getMealRepository();
        this.mealConsumptionRepository = this.appState.repositories.getMealConsumptionRepository();

        this.soupName = new SimpleStringProperty();
        this.mainCourseName = new SimpleStringProperty();
        this.sideDishName = new SimpleStringProperty();
        this.extraDishName = new SimpleStringProperty();
        this.totalCostForintText = new SimpleStringProperty();

        this.soup = new SimpleObjectProperty<>(null);
        this.mainCourse = new SimpleObjectProperty<>(null);
        this.sideDish = new SimpleObjectProperty<>(null);
        this.extraDish = new SimpleObjectProperty<>(null);
        this.totalCostForint = new SimpleIntegerProperty(0);

        this.soup.addListener(this.onSoupChange());
        this.mainCourse.addListener(this.onMainCourseChange());
        this.sideDish.addListener(this.onSideDishChange());
        this.extraDish.addListener(this.onExtraDishChange());
        this.totalCostForint.addListener(this.onTotalCostChange());

        this.applySoup(null);
        this.applyMainCourse(null);
        this.applySideDish(null);
        this.applyExtraDish(null);
        this.applyTotalCost(0);
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

        this.soup.set(generatedSoup);
        this.mainCourse.set(generatedMainCourse);
        this.sideDish.set(generatedSideDish);
        this.extraDish.set(generatedExtraDish);

        int totalCost = 0;
        totalCost += generatedSoup != null ? generatedSoup.getAverageCostForint() : 0;
        totalCost += generatedMainCourse != null ? generatedMainCourse.getAverageCostForint() : 0;
        totalCost += generatedSideDish != null ? generatedSideDish.getAverageCostForint() : 0;
        totalCost += generatedExtraDish != null ? generatedExtraDish.getAverageCostForint() : 0;
        this.totalCostForint.set(totalCost);
    }

    private Repository<Meal> getMealRepository() {
        return this.appState.repositories.getMealRepository();
    }

    private Repository<MealConsumption> getMealConsumptionRepository() {
        return this.appState.repositories.getMealConsumptionRepository();
    }

    private List<Meal> getValidMeals() throws Repository.OperationException {
        List<Meal> meals = this.getMealRepository().getAll();
        return meals.stream().filter(this::isMealValid).toList();
    }

    private boolean isMealValid(Meal meal) {
        LocalDate today = LocalDate.now();

        List<MealConsumption> consumptions;

        try {
            consumptions = this.getMealConsumptionRepository().findManyBy(
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

    private void applySoup(Meal soup) {
        if (soup == null) {
            this.soupName.set("Nincs megfelelő leves.");
        } else {
            this.soupName.set("Leves: " + soup.getTitle());
        }
    }

    private void applyMainCourse(Meal mainCourse) {
        if (mainCourse == null) {
            this.mainCourseName.set("Nincs megfelelő főétel.");
        } else {
            this.mainCourseName.set("Főétel: " + mainCourse.getTitle());
        }
    }

    private void applySideDish(Meal sideDish) {
        if (sideDish == null) {
            this.sideDishName.set("Nincs megfelelő köret.");
        } else {
            this.sideDishName.set("Köret: " + sideDish.getTitle());
        }
    }

    private void applyExtraDish(Meal extraDish) {
        if (extraDish == null) {
            this.extraDishName.set("Nincs megfelelő kiegészítő.");
        } else {
            this.extraDishName.set("Kiegészítő: " + extraDish.getTitle());
        }
    }

    private void applyTotalCost(Number totalCost) {
        this.totalCostForintText.set("Összköltség (Ft): " + totalCost);
    }

    private ChangeListener<Meal> onSoupChange() {
        return (_, _, soup) -> {
           this.applySoup(soup);
        };
    }

    private ChangeListener<Meal> onMainCourseChange() {
        return (_, _, mainCourse) -> {
            this.applyMainCourse(mainCourse);
        };
    }

    private ChangeListener<Meal> onSideDishChange() {
        return (_, _, sideDish) -> {
            this.applySideDish(sideDish);
        };
    }

    private ChangeListener<Meal> onExtraDishChange() {
        return (_, _, extraDish) -> {
            this.applyExtraDish(extraDish);
        };
    }

    private ChangeListener<Number> onTotalCostChange() {
        return (_, _, totalCostForint) -> {
            this.applyTotalCost(totalCostForint);
        };
    }
}
