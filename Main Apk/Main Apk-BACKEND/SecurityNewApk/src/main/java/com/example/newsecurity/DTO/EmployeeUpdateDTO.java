package com.example.newsecurity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
