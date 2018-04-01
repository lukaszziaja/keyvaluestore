package simplestore;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * KeyValueStore in an interface, which sample implementation is SimpleKeyValueStore
 * ( I designed this contract thinking about relation which Map and e.g. HashMaps have ).
 *
 * When using Stream methods you have to pass an argument which specify on which namespace
 * do you want to do an operation.
 */

public interface KeyValueStore<T>{

    /**
     * saves or updates the value for a namespace/key
     */
    void put(String namespace, String key, T value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException;

    /**
     * returns the value for a namespace/key pair
     */
    T get(String namespace, String key);

    /**
     * deletes the value for a namespace/key. returns true on success, false on failure (e.g. object did not exist)
     */
    boolean delete(String namespace, String key);

    /**
     * implemented filter method
     */
    Stream<T> filter(String namespace, Predicate<? super T> predicate);

    /**
     * implemented map method
     */
    <R> Stream<R> map(String namespace, Function<? super T, ? extends R> mapper);

    /**
     * implemented reduce method
     */
    T reduce(String namespace, T identity, BinaryOperator<T> accumulator);

    /**
     * implemented reduce method
     */
    Optional<T> reduce(String namespace, BinaryOperator<T> accumulator);

    /**
     * implemented reduce method
     */
    <U> U reduce(String namespace, U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
}
