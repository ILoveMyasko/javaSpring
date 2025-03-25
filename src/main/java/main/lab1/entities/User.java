package main.lab1.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private @Positive int id;
    private @NotBlank String name;
    private @Email String email;
}