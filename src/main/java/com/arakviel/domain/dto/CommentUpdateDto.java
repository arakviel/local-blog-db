package com.arakviel.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;

public record CommentUpdateDto(
    @NotNull(message = "Відсутній іденитфікатор коментаря")
    UUID id,
    @Length(min = 11, max = 1028, message = "Коментар повинен відповідати діапазону символів від 11 до 1028")
    String body,
    UUID userId,
    UUID postId
) {

}
