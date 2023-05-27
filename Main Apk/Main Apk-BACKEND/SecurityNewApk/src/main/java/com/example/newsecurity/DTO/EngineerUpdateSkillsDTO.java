package com.example.newsecurity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineerUpdateSkillsDTO {
    private Long id;
    private HashMap<String, Integer> skills;
}
