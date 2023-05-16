package com.example.newsecurity.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "RegistrationRequests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegsitrationRequest {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long Id;
    @Column
    private long UserId ;
    @Column
    private boolean Accepted;
    @Column
    private Timestamp ResponseDate;
}
