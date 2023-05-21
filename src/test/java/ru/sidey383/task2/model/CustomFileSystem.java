package ru.sidey383.task2.model;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomFileSystem implements BeforeAllCallback, AfterAllCallback {

    private FileSystem fileSystem;

    private Path root;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        fileSystem = MemoryFileSystemBuilder.newEmpty().build("test");
        root = fileSystem.getPath("game");
        Files.createDirectories(root);
    }

    public Path getRoot() {
        return root;
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        fileSystem.close();
    }
}
