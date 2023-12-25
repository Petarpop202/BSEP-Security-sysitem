package com.example.newsecurity.Service;

import com.example.newsecurity.Model.Alarm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface IAlarmService extends ICRUDService<Alarm> {
    Alarm update();
    boolean IsAlarmed();
    void checkIp(String ipAddress) throws NoSuchAlgorithmException, InvalidKeyException;
}
