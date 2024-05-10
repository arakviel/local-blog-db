package com.arakviel.presentation.controller;

import static com.arakviel.presentation.Runner.springContext;

import com.arakviel.presentation.Runner;
import com.arakviel.presentation.util.SpringFXMLLoader;
import java.io.IOException;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private BorderPane root;

    @FXML
    public void initialize() {
        // Встановлюємо значення за замовчуванням
        ToggleButton initialButton = (ToggleButton) toggleGroup.getToggles().getFirst();
        initialButton.setSelected(true);
    }

    @FXML
    private void handleMenuSelection(ActionEvent actionEvent) {
        ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
        if (selectedButton != null) {
            switch (selectedButton.getText()) {
                case "Авторизація" -> switchPage(Path.of("view", "user", "SignIn.fxml").toString());
                case "Реєстрація" -> switchPage(Path.of("view", "user", "SignUp.fxml").toString());
                case "Список користувачів" ->
                    switchPage(Path.of("view", "user", "List.fxml").toString());
                default -> System.err.println(STR."Unknown selection: \{selectedButton.getText()}");
            }
        }
    }

    private void switchPage(String fxmlFile) {
        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            Pane newPage = (Pane) fxmlLoader.load(Runner.class.getResource(fxmlFile));
            root.setCenter(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
