package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.RequestResponse;
import com.example.newsecurity.Model.RegistrationRequest;
import com.example.newsecurity.Repository.IRegistrationRequestRepository;
import com.example.newsecurity.Service.IRegistrationRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegistrationRequestService implements IRegistrationRequestService {
    private IRegistrationRequestRepository _registrationRequestRepository;
    RegistrationRequestService(IRegistrationRequestRepository registrationRequestRepository){_registrationRequestRepository = registrationRequestRepository;}
    @Override
    public Iterable<RegistrationRequest> getAll() {
        return null;
    }

    @Override
    public RegistrationRequest getById(Long id) {
        return null;
    }

    @Override
    public RegistrationRequest create(RegistrationRequest entity) {
        return _registrationRequestRepository.save(entity);
    }

    @Override
    public RegistrationRequest update(RegistrationRequest entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }

    @Override
    public RegistrationRequest setResponse(RequestResponse response) {
        RegistrationRequest registrationRequest = _registrationRequestRepository.getReferenceById(response.getRequestId());
        registrationRequest.setAccepted(response.isResponse());
        registrationRequest.setResponseDate(LocalDate.now());
        return null;
    }
}
