package com.example.newsecurity.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Engineers")
public class Engineer extends User{
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "engineer_skills", joinColumns = @JoinColumn(name = "engineer_id"))
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    private Map<String, Integer> skills;
}
