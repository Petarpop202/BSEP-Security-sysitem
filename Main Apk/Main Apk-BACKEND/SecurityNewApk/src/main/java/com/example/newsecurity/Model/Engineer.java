package com.example.newsecurity.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Engineers")
public class Engineer extends User{
    private HashMap<String, Integer> skills;
}
