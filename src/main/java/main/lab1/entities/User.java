package main.lab1.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data
@AllArgsConstructor
public class User {

    private @NotNull int id;
    private @NotBlank String name; //@NotBlank?
    private @Email String email;

}