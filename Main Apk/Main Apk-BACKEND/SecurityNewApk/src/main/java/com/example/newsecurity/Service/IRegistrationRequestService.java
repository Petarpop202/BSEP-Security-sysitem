package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.RequestResponse;
import com.example.newsecurity.Model.RegistrationRequest;

public interface IRegistrationRequestService extends ICRUDService<RegistrationRequest>{
    public RegistrationRequest setResponse(RequestResponse response);
}
