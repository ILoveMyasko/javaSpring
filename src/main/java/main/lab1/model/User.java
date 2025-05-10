package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor //mandatory for SpringJPA
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column
    private long userId;
    @Column (nullable = false, length = 50)
    private @NotBlank String name;
    @Column (length = 50)
    private @Email String email;
}