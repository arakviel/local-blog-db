package com.arakviel.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;

public record CommentStoreDto(
    @NotBlank(message = "Коментар не може бути пустим")
    @Length(min = 11, max = 1028, message = "Коментар повинен відповідати діапазону символів від 11 до 1028")
    String body,
    @NotNull(message = "Задайте автора коментаря")
    UUID userId,
    @NotNull(message = "Задайте пост, під яким хочете залишити коментар")
    UUID postId
) {

}
