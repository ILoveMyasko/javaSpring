package main.lab1.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private @Positive long userId;
    private @NotBlank String name;
    private @Email String email;
}