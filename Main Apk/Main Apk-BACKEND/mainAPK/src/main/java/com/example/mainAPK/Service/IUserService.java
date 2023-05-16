package com.example.mainAPK.Service;

import com.example.mainAPK.Model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserService extends ICRUDService<User>{
    User findByUsername(String username);
    //User save(UserRequest userRequest);
    User getByVerificationCode(String code);
    User activate(User u);
}
