package com.example.mainAPK.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "RegistrationRequests")
@AllArgsConstructor
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
