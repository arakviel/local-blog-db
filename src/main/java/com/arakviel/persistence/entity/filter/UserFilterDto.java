package com.arakviel.persistence.entity.filter;

import com.arakviel.persistence.entity.User;
import java.time.LocalDate;

public record UserFilterDto(String username, String email, LocalDate birthday, User.Role role) { }
