package com.example.newsecurity.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Addresses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long Id;
    @Column
    private String Country;
    @Column
    private String City;
    @Column
    private String Street;
    @Column
    private String StreetNum;
}