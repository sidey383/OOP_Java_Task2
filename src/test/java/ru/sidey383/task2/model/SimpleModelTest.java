package ru.sidey383.task2.model;

import org.junit.jupiter.api.Test;
import ru.sidey383.task2.model.exception.ModelException;

import java.nio.file.Path;

public class SimpleModelTest {

    @Test
    public void modelCreateTest() throws ModelException {
        ModelInterface model = RootModel.createModel(Path.of("."));
    }

}
