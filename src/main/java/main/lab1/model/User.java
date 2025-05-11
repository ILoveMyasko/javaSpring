package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.ZonedDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
//mandatory for SpringJPA
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long userId;
    @NotBlank @Column (nullable = false, length = 50)
    private String name;
    @Email @Column (nullable = false, length = 50)
    private String email;
    @Setter(AccessLevel.NONE) @Column(nullable = false)
    private ZonedDateTime registeredAt;


    public User(long userId,String name, String email)
    {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.registeredAt = ZonedDateTime.now();
    }

    public User(){
        this.registeredAt = ZonedDateTime.now();
    }
}