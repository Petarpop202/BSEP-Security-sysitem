package com.PKISecurity.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Subject")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubjectData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private String id;
    @Column
    public String commonName;
    @Column
    public String surname;
    @Column
    public String givenName;
    @Column
    public String organization;
    @Column
    public String organizationalUnitName;
    @Column
    public String country;
    @Column
    public String email;
    @Column
    public List<String> certificateAlias;
}
