package com.arakviel.presentation.controller.user;

import com.arakviel.domain.exception.AuthenticationException;
import com.arakviel.domain.exception.UserAlreadyAuthenticatedException;
import com.arakviel.domain.service.impl.AuthenticationService;
import com.arakviel.persistence.entity.User.Role;
import com.arakviel.presentation.viewmodel.UserViewModel;
import java.time.LocalDate;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import org.springframework.stereotype.Component;

@Component
public class SignInController {

    private final AuthenticationService authenticationService;
    @FXML
    public PasswordField passwordField;
    @FXML
    public CheckBox rememberMeCheckBox;
    @FXML
    private TextField usernameField;

    public SignInController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckBox.isSelected();

        try {
            boolean authenticated = authenticationService.authenticate(username, password);

            if (authenticated) {
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Вхід виконано успішно!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Невірний логін або пароль.");
            }
        } catch (UserAlreadyAuthenticatedException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", e.getMessage());
        } catch (AuthenticationException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Невірний логін або пароль.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
