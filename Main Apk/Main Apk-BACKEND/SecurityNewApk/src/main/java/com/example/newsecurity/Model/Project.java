package com.example.newsecurity.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "project_employee", joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
                inverseJoinColumns =  @JoinColumn(name = "employee_id", referencedColumnName = "id"))
    private List<Employee> employees;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;
}
