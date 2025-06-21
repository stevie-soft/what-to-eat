package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;


class MockMealRepository implements Repository<Meal> {
    private final List<Meal> meals;

    public MockMealRepository() {
        this.meals = new ArrayList<>();
    }

    @Override
    public List<Meal> getAll() throws OperationException {
        return this.meals;
    }

    @Override
    public Meal getByUuid(UUID uuid) throws OperationException {
        return null;
    }

    @Override
    public Optional<Meal> findByUuid(UUID uuid) throws OperationException {
        return Optional.empty();
    }

    @Override
    public <TReturnType> Meal getBy(Function<Meal, TReturnType> getter, TReturnType value) throws OperationException {
        return null;
    }

    @Override
    public <TReturnType> Optional<Meal> findBy(Function<Meal, TReturnType> getter, TReturnType value) throws OperationException {
        return Optional.empty();
    }

    @Override
    public <TReturnType> List<Meal> findManyBy(Function<Meal, TReturnType> getter, TReturnType value) throws OperationException {
        return List.of();
    }

    @Override
    public void add(Meal entity) throws OperationException {
        this.meals.add(entity);
    }

    @Override
    public void update(Meal entity) throws OperationException {

    }

    @Override
    public void remove(Meal entity) throws OperationException {

    }

    @Override
    public <TReturnType> void removeManyBy(Function<Meal, TReturnType> getter, TReturnType value) throws OperationException {

    }
}

class MockMealConsumptionRepository implements Repository<MealConsumption> {
    private final List<MealConsumption> mealConsumptions;

    public MockMealConsumptionRepository() {
        this.mealConsumptions = new ArrayList<>();
    }

    @Override
    public List<MealConsumption> getAll() throws OperationException {
        return this.mealConsumptions;
    }

    @Override
    public MealConsumption getByUuid(UUID uuid) throws OperationException {
        return null;
    }

    @Override
    public Optional<MealConsumption> findByUuid(UUID uuid) throws OperationException {
        return Optional.empty();
    }

    @Override
    public <TReturnType> MealConsumption getBy(Function<MealConsumption, TReturnType> getter, TReturnType value) throws OperationException {
        return null;
    }

    @Override
    public <TReturnType> Optional<MealConsumption> findBy(Function<MealConsumption, TReturnType> getter, TReturnType value) throws OperationException {
        return Optional.empty();
    }

    @Override
    public <TReturnType> List<MealConsumption> findManyBy(Function<MealConsumption, TReturnType> getter, TReturnType value) throws OperationException {
        return this.mealConsumptions.stream().filter((mealConsumption -> getter.apply(mealConsumption).equals(value))).toList();
    }

    @Override
    public void add(MealConsumption entity) throws OperationException {
        this.mealConsumptions.add(entity);
    }

    @Override
    public void update(MealConsumption entity) throws OperationException {

    }

    @Override
    public void remove(MealConsumption entity) throws OperationException {

    }

    @Override
    public <TReturnType> void removeManyBy(Function<MealConsumption, TReturnType> getter, TReturnType value) throws OperationException {

    }
}

class GenerateMealStateTest {
    private final AppState appState;

    private final Meal meal1;
    private final Meal meal2;
    private final Meal meal3;
    private final Meal meal4;

    private final MealConsumption consumption1;
    private final MealConsumption consumption2;
    private final MealConsumption consumption3;
    private final MealConsumption consumption4;

    public GenerateMealStateTest() throws Repository.OperationException {
        this.appState = new AppState();
        Repository<Meal> mockMealRepository = new MockMealRepository();
        Repository<MealConsumption> mockMealConsumptionRepository = new MockMealConsumptionRepository();

        this.meal1 = new Meal();
        this.meal1.setUuid(UUID.fromString("b0131fc5-0505-4bb7-a769-53a1e3cface4"));
        this.meal1.setTitle("Tarhonyáshús");
        this.meal1.setCategoryKey("main-course");
        this.meal1.setAverageCostForint(1800);
        this.meal1.setConsumptionFrequencyDays(10);

        this.meal2 = new Meal();
        this.meal2.setUuid(UUID.fromString("e6ed63a1-2e3e-4b5e-91b6-82d7314ea090"));
        this.meal2.setTitle("Lebbencsleves");
        this.meal2.setCategoryKey("soup");
        this.meal2.setAverageCostForint(1200);
        this.meal2.setConsumptionFrequencyDays(14);

        this.meal3 = new Meal();
        this.meal3.setUuid(UUID.fromString("687d78a6-9e0b-4d3d-a370-9be993259c1d"));
        this.meal3.setTitle("Tükörtojás");
        this.meal3.setCategoryKey("side-dish");
        this.meal3.setAverageCostForint(850);
        this.meal3.setConsumptionFrequencyDays(2);

        this.meal4 = new Meal();
        this.meal4.setUuid(UUID.fromString("8af68cf7-34d2-4866-8b32-15d7c2b4ced0"));
        this.meal4.setTitle("Uborkasaláta");
        this.meal4.setCategoryKey("other");
        this.meal4.setAverageCostForint(1100);
        this.meal4.setConsumptionFrequencyDays(5);

        mockMealRepository.add(this.meal1);
        mockMealRepository.add(this.meal2);
        mockMealRepository.add(this.meal3);
        mockMealRepository.add(this.meal4);

        this.consumption1 = new MealConsumption();
        this.consumption1.setUuid(UUID.fromString("35389716-1f63-4cf5-ba59-357e0655ed08"));
        this.consumption1.setMealUuid(meal1.getUuid());

        this.consumption2 = new MealConsumption();
        this.consumption2.setUuid(UUID.fromString("3bbeb69d-8f49-466b-aaf0-e9a3fe577f37"));
        this.consumption2.setMealUuid(meal2.getUuid());

        this.consumption3 = new MealConsumption();
        this.consumption3.setUuid(UUID.fromString("797f59ff-93b1-4699-9912-0b6b5d4e74dd"));
        this.consumption3.setMealUuid(meal3.getUuid());

        this.consumption4 = new MealConsumption();
        this.consumption4.setUuid(UUID.fromString("2644634d-5530-4fa4-9861-072509ac1884"));
        this.consumption4.setMealUuid(meal4.getUuid());

        mockMealConsumptionRepository.add(this.consumption1);
        mockMealConsumptionRepository.add(this.consumption2);
        mockMealConsumptionRepository.add(this.consumption3);
        mockMealConsumptionRepository.add(this.consumption4);

        this.appState.repositories.setMealRepository(mockMealRepository);
        this.appState.repositories.setMealConsumptionRepository(mockMealConsumptionRepository);
    }

    @Test
    void testUnableToGenerateForAnyCategories() throws Repository.OperationException {
        LocalDate now = LocalDate.now();
        this.consumption1.setDate(now);
        this.consumption2.setDate(now);
        this.consumption3.setDate(now);
        this.consumption4.setDate(now);

        this.appState.generateMealState.generate();

        GenerateMealState state = this.appState.generateMealState;

        assert state.soup.get() == null
                && state.mainCourse.get() == null
                && state.sideDish.get() == null
                && state.extraDish.get() == null;
    }

    @Test
    void testCanGenerateForAllCategories() throws Repository.OperationException {
        LocalDate now = LocalDate.now();
        this.consumption1.setDate(now.minusDays(11));
        this.consumption2.setDate(now.minusDays(15));
        this.consumption3.setDate(now.minusDays(3));
        this.consumption4.setDate(now.minusDays(6));

        this.appState.generateMealState.generate();

        GenerateMealState state = this.appState.generateMealState;

        assert state.soup.get() != null
                && state.mainCourse.get() != null
                && state.sideDish.get() != null
                && state.extraDish.get() != null;
    }

    @Test
    void testCanOnlyPartiallyGenerateForCategories() throws Repository.OperationException {
        LocalDate now = LocalDate.now();
        this.consumption1.setDate(now);
        this.consumption2.setDate(now);
        this.consumption3.setDate(now.minusDays(3));
        this.consumption4.setDate(now.minusDays(6));

        this.appState.generateMealState.generate();

        GenerateMealState state = this.appState.generateMealState;

        assert state.soup.get() == null
                && state.mainCourse.get() == null
                && state.sideDish.get() != null
                && state.extraDish.get() != null;
    }
}