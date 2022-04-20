package jsonFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;

public class CollectionsTypeFactory {
    public static JavaType listOf(Class clazz) {
        return TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
    }
}
