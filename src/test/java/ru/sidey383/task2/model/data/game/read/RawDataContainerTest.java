package ru.sidey383.task2.model.data.game.read;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RawDataContainerTest {

    private static final Object[] objects = new Object[]{new Object(), new Object(), new Object(), "TestSrt"};
    private static final String[] names = new String[]{"Obj0", "Obj1", "Obj2", "str"};
    private static final Class<?>[] classes = new Class[]{Object.class, Object.class, Object.class, String.class};

    @Test
    @Order(0)
    public void baseTest() {
        var builder = RawDataContainer.builder();
        String hash = "TestHash";
        builder.withHash(hash)
                .withObject(names[0], objects[0])
                .withObject(names[1], objects[1]);
        RawDataContainer container = builder
                .withObject(names[2], objects[2])
                .withObject(names[3], objects[3])
                .build();
        assertEquals(hash, container.getHash());
        for (int i = 0; i < objects.length; i++)
            assertEquals(Optional.of(objects[i]), container.getData(classes[i], names[i]), "Wrong object, name " + names[i]);
        assertEquals(Optional.empty(), container.getData(String.class, names[0]), "Wrong class test");
        assertEquals(Optional.of(objects[3]), container.getData(Object.class, names[3]));
    }

    @Test
    @Order(1)
    public void nullTest() {
        var builder = RawDataContainer.builder();
        String hash = "TestHash";
        Object obj = new Object();
        RawDataContainer container = builder.withHash(hash)
                .withObject("null1", null)
                .withObject("null2", null)
                .withObject("object", obj)
                .build();
        assertEquals(Optional.empty(), container.getData(Object.class,"null1"), "get Object by name null1");
        assertEquals(Optional.empty(), container.getData(Object.class,"null2"), "get Object by name null2");
        assertEquals(Optional.of(obj), container.getData(Object.class,"object"), "get Object by name object");
    }

    @Test
    @Order(2)
    public void hashNotSetTest() {
        assertThrowsExactly(IllegalStateException.class, () -> RawDataContainer.builder().build(), "Hash not set");
        assertThrowsExactly(IllegalStateException.class, () -> RawDataContainer.builder().withHash(null).build(), "Set null hash");
        assertThrowsExactly(IllegalStateException.class, () -> RawDataContainer.builder().withHash("TestHash").withHash(null).build(), "Hash removed");
    }

}
