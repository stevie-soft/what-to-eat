package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.Entity;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Repository<TEntity extends Entity> {
    class OperationException extends Exception {
        public OperationException(Class<?> entityClass, String operation, String reason) {
            super(
                    String.format(
                            "Cannot %s resource '%s': %s",
                            operation,
                            entityClass.getSimpleName(),
                            reason
                    )
            );
        }
    }
    class ReadOperationException extends OperationException {
        public ReadOperationException(Class<?> entityClass, String reason) {
            super(entityClass, "read", reason);
        }
    }

   class AddOperationException extends OperationException {
        public AddOperationException(Class<?> entityClass, String reason) {
            super(entityClass, "add", reason);
        }
    }

    class UpdateOperationException extends OperationException {
        public UpdateOperationException(Class<?> entityClass, String reason) {
            super(entityClass, "update", reason);
        }
    }

    class RemoveOperationException extends OperationException {
        public RemoveOperationException(Class<?> entityClass, String reason) {
            super(entityClass, "remove", reason);
        }
    }

    class NotFoundByUuidException extends OperationException {
        public NotFoundByUuidException(Class<?> entityClass, UUID uuid) {
            super(entityClass, "find", String.format("non-existent UUID '%s'", uuid.toString()));
        }
    }

    List<TEntity> getAll() throws OperationException;
    TEntity getByUuid(UUID uuid) throws OperationException;
    Optional<TEntity> findByUuid(UUID uuid) throws OperationException;
    <TReturnType> TEntity getBy(Function<TEntity, TReturnType> getter, TReturnType value) throws OperationException;
    <TReturnType> Optional<TEntity> findBy(Function<TEntity, TReturnType> getter, TReturnType value) throws OperationException;
    void add(TEntity entity) throws OperationException;
    void update(TEntity entity) throws OperationException;
    void remove(TEntity entity) throws OperationException;
}
