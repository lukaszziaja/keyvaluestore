import domain.ValueObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simplestore.KeyValueStore;
import simplestore.SimpleKeyValueStore;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;


class SimpleKeyValueStoreTest {

    private KeyValueStore<String> stringSimpleKeyValueStore;
    private ValueObject valueObject;

    @BeforeEach
    void setUp() {
        stringSimpleKeyValueStore = new SimpleKeyValueStore<>();
        valueObject = new ValueObject();
    }

    @Test
    void putAndGetValuesShouldBeTheSame() throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IOException, InstantiationException {
        stringSimpleKeyValueStore.put("oneNamespace", "myValue", "a");
        stringSimpleKeyValueStore.put("twoNamespace", "myValue", "b");

        Assertions.assertEquals("a", stringSimpleKeyValueStore.get("oneNamespace", "myValue"));
        Assertions.assertEquals("b", stringSimpleKeyValueStore.get("twoNamespace", "myValue"));
    }

    @Test
    void shouldDeleteElementSuccessfully() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InstantiationException {
        stringSimpleKeyValueStore.put("twoNamespace", "myValue", "b");
        boolean deletionResult = stringSimpleKeyValueStore.delete("twoNamespace", "myValue");

        Assertions.assertEquals(true, deletionResult);
        Assertions.assertNull(stringSimpleKeyValueStore.get("twoNamespace", "myValue"));
    }

    @Test
    void shouldUpdateExistingElement() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InstantiationException {
        stringSimpleKeyValueStore.put("oneNamespace", "myValue", "b");
        stringSimpleKeyValueStore.put("oneNamespace", "myValue", "c");

        Assertions.assertEquals("c", stringSimpleKeyValueStore.get("oneNamespace", "myValue"));
    }

    @Test
    void objectValuesShouldBeImmutableAfterSaving() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InstantiationException {
        KeyValueStore<ValueObject> valueObjectKeyValueStore = new SimpleKeyValueStore<>();

        valueObject.setName("Tom");
        valueObjectKeyValueStore.put("namespace", "myValue", valueObject);
        valueObject.setName("Max");

        Assertions.assertEquals("Tom", valueObjectKeyValueStore.get("namespace", "myValue").getName());
    }

    @Test
    void shouldFilterOnlyCertainValues() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InstantiationException {
        stringSimpleKeyValueStore.put("oneNamespace", "1", "b");
        stringSimpleKeyValueStore.put("oneNamespace", "2", "b");
        stringSimpleKeyValueStore.put("oneNamespace", "3", "a");
        stringSimpleKeyValueStore.put("oneNamespace", "4", "b");
        stringSimpleKeyValueStore.put("oneNamespace", "5", "b");
        stringSimpleKeyValueStore.put("oneNamespace", "6", "a");
        stringSimpleKeyValueStore.put("oneNamespace", "7", "b");

        Object[] objects = stringSimpleKeyValueStore.filter("oneNamespace",(x -> x.equals("b"))).toArray();
        Assertions.assertEquals(5, objects.length);
    }

    @Test
    void shouldMakeValuesUpperCaseUsingMap() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InstantiationException {

        stringSimpleKeyValueStore.put("oneNamespace", "1", "Java 8");
        stringSimpleKeyValueStore.put("oneNamespace", "2", "makes");
        stringSimpleKeyValueStore.put("oneNamespace", "3", "tests");
        stringSimpleKeyValueStore.put("oneNamespace", "4", "a lot");
        stringSimpleKeyValueStore.put("oneNamespace", "5", "more fun");
        stringSimpleKeyValueStore.put("oneNamespace", "6", "when you use");
        stringSimpleKeyValueStore.put("oneNamespace", "7", "streams");

        List<String> collectionOfUpperCaseValues = stringSimpleKeyValueStore.map("oneNamespace", String::toUpperCase).collect(Collectors.toList());

        Assertions.assertEquals("MAKES", collectionOfUpperCaseValues.get(1));
    }

    @Test
    void shouldAddAllTheValuesUsingConcat() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InstantiationException {
        KeyValueStore<Integer> integerKeyValueStore = new SimpleKeyValueStore<>();

        integerKeyValueStore.put("oneNamespace", "1", 100);
        integerKeyValueStore.put("oneNamespace", "2", 145);
        integerKeyValueStore.put("oneNamespace", "3", 12);
        integerKeyValueStore.put("oneNamespace", "4", 13);
        integerKeyValueStore.put("oneNamespace", "5", 144);
        integerKeyValueStore.put("oneNamespace", "6", 1323);
        integerKeyValueStore.put("oneNamespace", "7", 144);
        integerKeyValueStore.put("oneNamespace", "8", 15);

        Integer reduceValue = integerKeyValueStore.reduce("oneNamespace",0, (x, y) -> x + y);

        Assertions.assertEquals(Integer.valueOf(1896), reduceValue);
    }
}