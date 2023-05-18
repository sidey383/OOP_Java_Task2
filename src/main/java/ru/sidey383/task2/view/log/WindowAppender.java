package ru.sidey383.task2.view.log;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

@Plugin(
        name = "WindowAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE,
        printObject = true)
public class WindowAppender extends AbstractAppender {

    private Stage dialog = null;

    private VBox scrollBox = null;

    private final Image icon;

    protected WindowAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Image icon) {
        super(name, filter, layout, ignoreExceptions, null);
        this.icon = icon;
    }

    @PluginFactory
    public static WindowAppender createAppender(@PluginAttribute("name") String name,
                                                @PluginElement("Filter") final Filter filter,
                                                @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                @PluginAttribute("icon") String iconPath) {
        if (name == null) {
            LOGGER.error("There is no name provided for MyCustomAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        Image icon = null;
        if (iconPath != null) {
            icon = new Image(iconPath);
        }
        return new WindowAppender(name, filter, layout, false, icon);
    }


    @Override
    public void append(LogEvent event) {
        String message = new String(getLayout().toByteArray(event));
        Platform.runLater(() -> {
            synchronized (this) {
                initStage();
                scrollBox.getChildren().add(new Label(message));
            }

        });
    }

    private void initStage() {
        if (dialog != null && scrollBox != null)
            return;
        scrollBox = new VBox();
        ScrollPane pane = new ScrollPane();
        pane.setMinSize(300, 200);
        pane.setContent(scrollBox);
        dialog = new Stage();
        dialog.getIcons().add(icon);
        dialog.setTitle("Log");
        Scene scene = new Scene(pane);
        dialog.setScene(scene);
        dialog.setResizable(true);
        dialog.show();
        dialog.setOnCloseRequest(ev -> {
            synchronized (this) {
                this.dialog = null;
                this.scrollBox = null;
            }
        });
    }

}
