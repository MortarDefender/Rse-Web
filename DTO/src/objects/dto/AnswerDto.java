package objects.dto;

import ExceptionsType.ExpType;
import objects.interfaces.CommandAnswer;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
* Data Transfer Object that includes Exceptions
* is Successful -> give a true or false if the object is an Exception or not
* Get Type -> return the type of exception or that there was no exception
* get Message -> return the DTO object or a primitive type
 */

public class AnswerDto<T, S> implements CommandAnswer<T, S> {  // extends Dto
    private final T dtoObj;
    private final S expObj;
    private final ExpType type;

    public AnswerDto() { this(null, null, ExpType.Successful); }

    public AnswerDto(T dtoObj, S expMessage) {
        this(dtoObj, expMessage, ExpType.Successful);
    }

    public AnswerDto(T dtoObj, S expMessage, ExpType expType) {
        this.dtoObj = dtoObj;
        this.type = expType;
        this.expObj = expMessage;
    }

    @Override
    public ExpType getType() { return this.type; }

    @Override
    public T getObject() { return this.dtoObj; }

    @Override
    public S getExpMessage() { return this.expObj; }

    @Override
    public boolean isSuccessful() { return this.type == ExpType.Successful; }


    @Override
    public T ifSuccessful(UnaryOperator<T> operator) {
        if (this.type == ExpType.Successful)
            return operator.apply(dtoObj);
        return null;
    }

    @Override
    public void isPresent(Consumer<? super T> consumer) {
        if (this.type == ExpType.Successful) {
            if (dtoObj != null)  // needed ??
                consumer.accept(dtoObj);
        }
    }

    @Override
    public void ifFailure(Consumer<? super S> consumer) {
        if (this.type != ExpType.Successful) {
            if (expObj != null)  // needed ??
                consumer.accept(expObj);
        }
    }

    @Override
    public void doAction(Consumer<? super T> objConsumer, Consumer<? super S> expConsumer) {
        if (this.type == ExpType.Successful)
            objConsumer.accept(dtoObj);
        else
            expConsumer.accept(expObj);
    }
}
