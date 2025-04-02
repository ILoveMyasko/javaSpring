package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data //not recommended?
@AllArgsConstructor
@NoArgsConstructor //mandatory for SpringJPA
@Entity
@Table(name = "users") //User is reserved name so gotta change to something else
public class User {
    @Id @GeneratedValue(strategy = IDENTITY) // why from this idea cant understand that it needs to be not bigint but at least serial?
    @Column
    private long userId;
    @Column (nullable = false, length = 50)
    private @NotBlank String name;
    @Column (length = 50)
    private @Email String email;
}