package com.arakviel.presentation;

import com.arakviel.persistence.ApplicationConfig;
import com.arakviel.persistence.util.ConnectionManager;
import com.arakviel.persistence.util.DatabaseInitializer;
import com.arakviel.presentation.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner extends Application {

    private static AnnotationConfigApplicationContext springContext;

    @Override
    public void start(Stage stage) throws Exception {
        var fxmlLoader = new SpringFXMLLoader(springContext);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        var mainFxmlResource = Runner.class.getResource("view/main.fxml");
        Scene scene = new Scene((Parent) fxmlLoader.load(mainFxmlResource), 900, 600);
        stage.setTitle("Title");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        springContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        var connectionManager = springContext.getBean(ConnectionManager.class);
        var databaseInitializer = springContext.getBean(DatabaseInitializer.class);

        try {
            databaseInitializer.init();
            launch(args);
        } finally {
            connectionManager.closePool();
        }
    }
}
