package com.example.newsecurity.DTO;

import com.example.newsecurity.Model.Address;
import com.example.newsecurity.Model.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerReadDTO {
    private Long id;
    private String name;
    private String surname;
    private String mail;
    private String username;
    private String phoneNumber;
    private String jmbg;
    private Address address;
}
