package com.example.bloodbank.Service.ServiceImplementation;

import com.example.bloodbank.Model.Address;
import com.example.bloodbank.Repository.IAddressRepository;
import com.example.bloodbank.Service.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressService {

    private IAddressRepository _addressRepository;

    @Override
    public Iterable<Address> getAll() {
        return null;
    }

    @Override
    public Address getById(Long id) {
        return null;
    }

    @Override
    public Address create(Address entity) {
        return null;
    }

    @Override
    public Address update(Address entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }
}
