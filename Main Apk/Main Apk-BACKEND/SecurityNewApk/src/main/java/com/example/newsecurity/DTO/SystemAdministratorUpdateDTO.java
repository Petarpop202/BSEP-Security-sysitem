package com.example.newsecurity.DTO;

import com.example.newsecurity.Model.Address;
import com.example.newsecurity.Model.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SystemAdministratorUpdateDTO {
    private Long id;
    private String name;
    private String surname;
    private String mail;
    private String username;
    private String password;
    private String phoneNumber;
    private String jmbg;
    private GenderEnum gender;
    private Address address;
}
