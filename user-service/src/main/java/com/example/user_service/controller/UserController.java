package com.example.user_service.controller;

import com.example.user_service.model.User;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "user_id") Long id){
        try{
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("can't get, user with id " + id + " not found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNewUser(@RequestBody User user){
        try {
            int rows = userService.createNewUser(user);

            if(rows > 0)
                return ResponseEntity.status(HttpStatus.CREATED).body("the user created successfully");
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("the user can't be created (SERVER_ERROR)");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable(value = "user_id") Long id, @RequestBody User user){
        user.setUserId(id);
        try{
            int rows = userService.updateUser(user);
            if(rows > 0)
                return ResponseEntity.ok("user with id " + id + " updated successfully");
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("can't update, user with id " + id + " not found");
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id){
        int rows = userService.deleteUser(user_id);

        if(rows > 0)
            return ResponseEntity.ok("the user with id " + user_id + " deleted successfully with all his answers");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("can't delete user, because the id " + user_id + " not found");
    }

    @GetMapping()
    public Boolean isRegisterByUserId(@RequestParam Long user_id){ // get from polls service
        try{
            User user = userService.getUserById(user_id);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
