package com.example.user_service.service;

import com.example.user_service.client_users_api.UsersSystemService;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements AddableUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersSystemService usersSystemService;

    public User getUserById(Long user_id){
        return userRepository.findUserById(user_id);
    }

    public List<User> getAllUsers(){
        return userRepository.getUsers();
    }

    public int createNewUser(User user){
        if(user.getUserId() <= 0 ||
                user.getFirstName() == null || user.getFirstName().isEmpty() ||
                user.getLastName() == null || user.getLastName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getAge() <= 0 || user.getAddress() == null || user.getJoiningDate() == null
        ) // for not null
            throw new IllegalArgumentException("can't create user, all fields must be given and must be not empty or zero");

        if( !userRepository.userExistsByIdHelper(user.getUserId()).isEmpty() ) // if the id already exist (unique id)
            throw new IllegalArgumentException("can't create the user, the id : " + user.getUserId() + " is already exist");


        if(userRepository.getEmailHelper(user.getEmail()).size() == 1) // for unique
            throw new IllegalArgumentException("can't create user, the email is already exist");

        return userRepository.saveUser(user);
    }

    public int updateUser(User user){
        if(user.getUserId() <= 0) // if id not sent or sent user_id = 0
            throw new IllegalArgumentException("can't update user, user id must be not equal to zero");

        if(user.getUserId() == 0 || // not null
                user.getFirstName() == null || user.getFirstName().isEmpty() ||
                user.getLastName() == null || user.getLastName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getAge() == 0 || user.getAddress() == null || user.getJoiningDate() == null
        )
            throw new IllegalArgumentException("can't update user, fields must be given and must be not empty");

        if(!userRepository.existsEmailAtAnotherRowHelper(user.getUserId(), user.getEmail()).isEmpty()) // for unique email, true if exist in another row
            throw new IllegalArgumentException("can't update user, email already exist");

        return userRepository.updateUser(user);
    }

    public int deleteUser(Long user_id){

        usersSystemService.deleteAnswers(user_id);
        return userRepository.deleteUserById(user_id);
    }

    /// /////////// addable user service ////////////////////

    @Override
    public User isRegisterByUserId(Long user_id){
        return userRepository.isRegisterByUserId(user_id);
    }
}
