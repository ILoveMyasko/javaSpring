package main.lab1.controllers;

import jakarta.validation.Valid;
import main.lab1.entities.User;
import main.lab1.exceptions.UserAlreadyExistsException;
import main.lab1.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.lab1.services.UserService;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    //@Autowired // not necessary if 1 constructor since spring version 3.3+?
    private final UserService userService; //dependency injection soliD

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int uId) {
        //this is the best way to do it? using exception UserNotFound
        return ResponseEntity.ok(userService.getUserById(uId));

        //functional way:
//         return userService.getUserById(uId)
//        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
//        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void createUser(@RequestBody @Valid User user) { //request body build User object through json?
        userService.createUser(user);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
