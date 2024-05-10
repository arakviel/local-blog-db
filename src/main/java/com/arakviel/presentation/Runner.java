package com.arakviel.presentation;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.arakviel.persistence.ApplicationConfig;
import com.arakviel.persistence.util.ConnectionManager;
import com.arakviel.persistence.util.DatabaseInitializer;
import com.arakviel.presentation.util.SpringFXMLLoader;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner extends Application {

    public static AnnotationConfigApplicationContext springContext;

    @Override
    public void start(Stage stage) throws Exception {
        var fxmlLoader = new SpringFXMLLoader(springContext);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        var mainFxmlResource = Runner.class.getResource(Path.of("view", "Main.fxml").toString());
        Parent parent = (Parent) fxmlLoader.load(mainFxmlResource);
        Scene scene = new Scene(parent, 900, 600);
        stage.setTitle("Локальний блог");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        springContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        var connectionManager = springContext.getBean(ConnectionManager.class);
        var databaseInitializer = springContext.getBean(DatabaseInitializer.class);

        // Підключення atlantaFX
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        try {
            databaseInitializer.init();
            launch(args);
        } finally {
            connectionManager.closePool();
        }
    }
}
