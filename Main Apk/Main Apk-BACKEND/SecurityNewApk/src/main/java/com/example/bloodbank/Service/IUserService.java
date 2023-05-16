package com.example.bloodbank.Service;

import com.example.bloodbank.Model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserService extends ICRUDService<User>{
    User findByUsername(String username);
    //User save(UserRequest userRequest);
    User getByVerificationCode(String code);
    User activate(User u);
}
