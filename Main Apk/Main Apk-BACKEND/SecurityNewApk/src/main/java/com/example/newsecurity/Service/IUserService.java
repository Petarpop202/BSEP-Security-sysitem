package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.UserRequest;
import com.example.newsecurity.Model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserService extends ICRUDService<User>{
    User findByUsername(String username);
    User save(UserRequest userRequest);
    User activate(User u);
    void passwordlessLogin(String token, String mail);
}
