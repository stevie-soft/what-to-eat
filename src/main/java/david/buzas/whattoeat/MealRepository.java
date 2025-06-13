package david.buzas.whattoeat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MealRepository {
    private final ItemManager<Meal> mealsManager;
    private List<Meal> meals;

    public static class OperationException extends Exception {
        public OperationException(String operation, String reason) {
            super(
                    String.format(
                            "Cannot %s resource '%s': %s",
                            operation,
                            Meal.class.getSimpleName(),
                            reason
                    )
            );
        }
    }

    public static class ReadOperationException extends OperationException {
        public ReadOperationException(String reason) {
            super("read", reason);
        }
    }

    public static class AddOperationException extends OperationException {
        public AddOperationException(String reason) {
            super("add", reason);
        }
    }

    public static class UpdateOperationException extends OperationException {
        public UpdateOperationException(String reason) {
            super("update", reason);
        }
    }

    public static class RemoveOperationException extends OperationException {
        public RemoveOperationException(String reason) {
            super("remove", reason);
        }
    }

    public static class NotFoundByUuidException extends OperationException {
        public NotFoundByUuidException(UUID uuid) {
            super("find", String.format("non-existent UUID '%s'", uuid.toString()));
        }
    }

    public MealRepository() {
        this.meals = new ArrayList<>();
        this.mealsManager = new JsonFileItemManager<>(Meal.class, "user-meals.json");
    }

    public List<Meal> getAll() throws ReadOperationException {
        try {
            this.meals = this.mealsManager.readItems();
        } catch (IOException e) {
            throw new ReadOperationException(e.getMessage());
        }

        return this.meals;
    }

    public Meal getByUuid(UUID uuid) throws NotFoundByUuidException, ReadOperationException {
        Optional<Meal> meal = this.findByUuid(uuid);

        if (meal.isEmpty()) {
            throw new NotFoundByUuidException(uuid);
        }

        return meal.get();
    }

    public Optional<Meal> findByUuid(UUID uuid) throws ReadOperationException {
        return this.getAll().stream()
                .filter(meal -> meal.getUuid().equals(uuid))
                .findFirst();
    }

    public void add(Meal meal) throws AddOperationException {
        meal.setUuid(UUID.randomUUID());
        this.meals.add(meal);

        try {
            this.persist();
        } catch (IOException e) {
            throw new AddOperationException(e.getMessage());
        }
    }

    public void update(Meal meal) throws NotFoundByUuidException, ReadOperationException, UpdateOperationException {
        Meal mealToUpdate = this.getByUuid(meal.getUuid());
        int targetIndex = this.meals.indexOf(mealToUpdate);
        this.meals.set(targetIndex, meal);

        try {
            this.persist();
        } catch (IOException e) {
            throw new UpdateOperationException(e.getMessage());
        }
    }

    public void remove(Meal meal) throws NotFoundByUuidException, ReadOperationException, RemoveOperationException {
        Meal mealToDelete = this.getByUuid(meal.getUuid());
        this.meals.remove(mealToDelete);

        try {
            this.persist();
        } catch (IOException e) {
            throw new RemoveOperationException(e.getMessage());
        }
    }

    private void persist() throws IOException {
        this.mealsManager.writeItems(this.meals);
    }
}
