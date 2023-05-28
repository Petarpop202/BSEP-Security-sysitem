package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.RequestResponse;
import com.example.newsecurity.Model.RegistrationRequest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IRegistrationRequestService extends ICRUDService<RegistrationRequest>{
    public RegistrationRequest setResponse(RequestResponse response) throws NoSuchAlgorithmException, InvalidKeyException;

    void Activate(String code) throws NoSuchAlgorithmException, InvalidKeyException;

    public List<RegistrationRequest> getAllUnresponded();
}
