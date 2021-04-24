package objects;

import ExceptionsType.ExpType;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface CommandAnswer<T, S> {  //  extends ExpEnum extends Dto
    T getObject();
    S getExpMessage();
    ExpType getType();
    boolean isSuccessful();

    T ifSuccessful(UnaryOperator<T> operator);

    void isPresent(Consumer<? super T> consumer);
    void ifFailure(Consumer<? super S> consumer);

    void doAction(Consumer<? super T> objConsumer, Consumer<? super S> expConsumer);
}
