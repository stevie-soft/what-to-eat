package david.buzas.whattoeat.controllers;

import david.buzas.whattoeat.WhatToEatApplication;
import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.Repository;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppModel {
    public static class MealFormModel {
        Repository<MealCategory> mealCategoryRepository = WhatToEatApplication.mealCategoryRepository;
        Repository<MealType> mealTypeRepository = WhatToEatApplication.mealTypeRepository;

        public SimpleStringProperty titleProperty = new SimpleStringProperty();
        public SimpleObjectProperty<MealCategory> categoryProperty = new SimpleObjectProperty<>();
        public SimpleObjectProperty<MealType> typeProperty = new SimpleObjectProperty<>();
        public SimpleStringProperty consumptionFrequencyDaysProperty = new SimpleStringProperty();
        public SimpleStringProperty averageCostForintProperty = new SimpleStringProperty();

        public Meal buildMeal() {
            Meal meal = new Meal();

            meal.setTitle(this.titleProperty.get());
            meal.setCategoryKey(this.categoryProperty.get().getKey());
            meal.setTypeKey(this.typeProperty.get().getKey());
            meal.setConsumptionFrequencyDays(Integer.parseInt(this.consumptionFrequencyDaysProperty.get()));
            meal.setAverageCostForint(Integer.parseInt(this.averageCostForintProperty.get()));

            return meal;
        }

        public void loadValues(Meal meal) throws Repository.OperationException {
            if (meal == null) {
                return;
            }

            this.titleProperty.set(meal.getTitle());
            MealCategory category = mealCategoryRepository.getBy(MealCategory::getKey, meal.getCategoryKey());
            this.categoryProperty.set(category);
            MealType type = mealTypeRepository.getBy(MealType::getKey, meal.getTypeKey());
            this.typeProperty.set(type);
            this.consumptionFrequencyDaysProperty.set(String.valueOf(meal.getConsumptionFrequencyDays()));
            this.averageCostForintProperty.set(String.valueOf(meal.getAverageCostForint()));
        }

        public void clearValues() {
            this.titleProperty.set(null);
            this.categoryProperty.set(null);
            this.typeProperty.set(null);
            this.consumptionFrequencyDaysProperty.set(null);
            this.averageCostForintProperty.set(null);
        }
    }

    public static class ConsumptionFormModel {
        public SimpleObjectProperty<Meal> mealProperty = new SimpleObjectProperty<>();
        public SimpleObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>(LocalDate.now());

        public MealConsumption buildConsumption() {
            MealConsumption consumption = new MealConsumption();

            consumption.setMealUuid(this.mealProperty.get().getUuid());
            consumption.setDate(this.dateProperty.get());

            return consumption;
        }

        public void loadValues(MealConsumption consumption) throws Repository.OperationException {
            if (consumption == null) {
                return;
            }

            mealProperty.set(consumption.getMeal());
            dateProperty.set(consumption.getDate());
        }

        public void clearValues() {
            mealProperty.set(null);
            dateProperty.set(LocalDate.now());
        }
    }

    Repository<Meal> mealRepository = WhatToEatApplication.mealRepository;
    Repository<MealCategory> mealCategoryRepository = WhatToEatApplication.mealCategoryRepository;
    Repository<MealType> mealTypeRepository = WhatToEatApplication.mealTypeRepository;
    Repository<MealConsumption> consumptionRepository = WhatToEatApplication.mealConsumptionRepository;

    public SimpleObjectProperty<Meal> selectedMealProperty = new SimpleObjectProperty<>();
    public SimpleBooleanProperty editingDisabledProperty = new SimpleBooleanProperty(true);
    public SimpleListProperty<Meal> mealsProperty = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    public SimpleListProperty<MealCategory> mealCategoriesProperty = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    public SimpleListProperty<MealType> mealTypesProperty = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    public MealFormModel mealFormModel = new MealFormModel();

    public SimpleListProperty<MealConsumption> consumptionsProperty = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    public SimpleObjectProperty<MealConsumption> selectedConsumptionProperty = new SimpleObjectProperty<>();
    public SimpleBooleanProperty consumptionEditingDisabledProperty = new SimpleBooleanProperty(true);
    public ConsumptionFormModel consumptionFormModel = new ConsumptionFormModel();

    public void setup() throws Repository.OperationException {
        selectedMealProperty.addListener((_, _, newValue) ->
            editingDisabledProperty.set(newValue == null)
        );
        selectedConsumptionProperty.addListener((_, _, newValue) -> {
            consumptionEditingDisabledProperty.set(newValue == null);
        });

        this.loadMealCategories();
        this.loadMealTypes();
        this.refreshMeals();
    }

    public void loadMealCategories() throws Repository.OperationException {
        List<MealCategory> mealCategories = this.mealCategoryRepository.getAll();
        this.mealCategoriesProperty.set(FXCollections.observableList(mealCategories));
    }

    public void loadMealTypes() throws Repository.OperationException {
        List<MealType> mealTypes = this.mealTypeRepository.getAll();
        this.mealTypesProperty.set(FXCollections.observableList(mealTypes));
    }

    public void refreshMeals() throws Repository.OperationException {
        List<Meal> meals = this.mealRepository.getAll();
        this.mealsProperty.set(FXCollections.observableList(meals));
        this.refreshConsumptions();
    }

    public void addMeal() throws Repository.OperationException {
        Meal meal = this.mealFormModel.buildMeal();
        this.mealRepository.add(meal);
        this.selectedMealProperty.set(meal);
        refreshMeals();
    }

    public void updateMeal() throws Repository.OperationException {
        Meal meal = this.mealFormModel.buildMeal();
        meal.setUuid(this.selectedMealProperty.get().getUuid());
        this.selectedMealProperty.set(meal);
        this.mealRepository.update(meal);
        refreshMeals();
    }

    public void removeMeal() throws Repository.OperationException {
        Meal meal = this.selectedMealProperty.get();
        this.mealRepository.remove(meal);
        this.selectedMealProperty.set(null);
        this.mealFormModel.clearValues();
        this.consumptionRepository.removeManyBy(MealConsumption::getMealUuid, meal.getUuid());
        refreshMeals();
    }

    public void refreshConsumptions() throws Repository.OperationException {
        List<MealConsumption> consumptions = this.consumptionRepository.getAll();
        consumptions.sort(Comparator.comparing(MealConsumption::getDate).reversed());
        this.consumptionsProperty.set(FXCollections.observableList(consumptions));
    }

    public void addConsumption() throws Repository.OperationException {
        MealConsumption consumption = this.consumptionFormModel.buildConsumption();
        this.consumptionRepository.add(consumption);
        this.selectedConsumptionProperty.set(consumption);
        this.refreshConsumptions();
    }

    public void updateConsumption() throws Repository.OperationException {
        MealConsumption consumption = this.consumptionFormModel.buildConsumption();
        consumption.setUuid(this.selectedConsumptionProperty.get().getUuid());
        this.consumptionRepository.update(consumption);
        this.refreshConsumptions();
        this.selectedConsumptionProperty.set(consumption);
    }

    public void removeConsumption() throws Repository.OperationException {
        MealConsumption consumption = this.selectedConsumptionProperty.get();
        this.consumptionRepository.remove(consumption);
        this.selectedConsumptionProperty.set(null);
        this.consumptionFormModel.clearValues();
        this.refreshConsumptions();
    }
}
