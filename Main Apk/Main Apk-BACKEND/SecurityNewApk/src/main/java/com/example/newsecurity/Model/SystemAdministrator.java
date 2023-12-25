package com.example.newsecurity.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SystemAdministrators")
@AllArgsConstructor
@Getter
@Setter
public class SystemAdministrator extends User{

}
