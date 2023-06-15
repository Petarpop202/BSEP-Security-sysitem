package com.example.newsecurity.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Alarms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Alarm {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long Id;
    @Column
    private String Message;
    @Column
    private boolean IsAlarmed;
}
