package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Entity
@Table(name = "users") //User is reserved name so gotta change to something else
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private @NotBlank String name;
    private @Email String email;
}