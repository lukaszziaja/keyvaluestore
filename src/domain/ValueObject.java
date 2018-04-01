package domain;

import java.io.Serializable;

/**
 * ValueObject class is a simple class which only purpose was to test
 * if objects will remain immutable after inserting it into KeyValueStore.
 *
 *
 * Class has to provide copy constructor!!!!
 **/

public class ValueObject implements Serializable{
    private String name;

    public ValueObject() {
    }

    public ValueObject(ValueObject valueObject) {
        this.name = valueObject.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}