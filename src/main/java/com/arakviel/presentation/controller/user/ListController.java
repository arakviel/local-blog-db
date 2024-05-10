package com.arakviel.presentation.controller.user;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.User.Role;
import com.arakviel.persistence.entity.filter.UserFilterDto;
import com.arakviel.persistence.repository.contract.UserRepository;
import java.time.LocalDate;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

// Потрібно було використовувати UserService замість UserRepository
@Component
public class ListController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Role> roleComboBox;
    @FXML
    private DatePicker birthdayFromPicker;
    @FXML
    private DatePicker birthdayToPicker;
    @FXML
    private VBox userListContainer;
    @FXML
    private Pagination pagination;

    private final UserRepository userRepository;

    private static final int PAGE_SIZE = 5;
    private String sortColumn = "username";
    private boolean ascending = true;
    private UserFilterDto currentFilters = new UserFilterDto("", "", null, null);

    public ListController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.values());
        roleComboBox.setPromptText("Виберіть роль");

        pagination.setPageCount(getTotalPages());
        updateUserList(0);
    }

    private int getTotalPages() {
        long totalUsers = userRepository.count();
        return (int) Math.ceil((double) totalUsers / PAGE_SIZE);
    }

    private void updateUserList(int pageIndex) {
        int offset = pageIndex * PAGE_SIZE;

        Set<User> users = userRepository.findAll(
            offset, PAGE_SIZE, sortColumn, ascending, currentFilters
        );

        userListContainer.getChildren().clear();
        userListContainer.getChildren().addAll(users.stream().map(this::createUserCard).toList());
    }

    private VBox createUserCard(User user) {
        Label usernameLabel = new Label(user.username());
        usernameLabel.setStyle("-fx-font-weight: bold;");

        Label emailLabel = new Label("Email: " + user.email());
        Label birthdayLabel = new Label("День народження: " + user.birthday().toString());
        Label roleLabel = new Label("Роль: " + user.role().getName());

        VBox userCard = new VBox(usernameLabel, emailLabel, birthdayLabel, roleLabel);
        userCard.setSpacing(5);
        userCard.setStyle(
            "-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1; -fx-background-radius: 5;");

        return userCard;
    }

    @FXML
    private void handlePageChange(int pageIndex) {
        updateUserList(pageIndex);
    }

    @FXML
    public void handleSortAscending(ActionEvent actionEvent) {
        ascending = true;
        updateUserList(pagination.getCurrentPageIndex());
    }

    @FXML
    public void handleSortDescending(ActionEvent actionEvent) {
        ascending = false;
        updateUserList(pagination.getCurrentPageIndex());
    }

    @FXML
    public void handleApplyFilters(ActionEvent actionEvent) {
        String username = searchField.getText().trim();
        Role role = roleComboBox.getValue();
        LocalDate birthdayFrom = birthdayFromPicker.getValue();
        LocalDate birthdayTo = birthdayToPicker.getValue();

        // не дороблено, щоб фільтрувати по діапазону дат (приклад в коментарях є)
        currentFilters = new UserFilterDto(username, "", birthdayFrom, role);

        pagination.setPageCount(getTotalPages());
        updateUserList(0);
    }

}
