package com.example.mainAPK.Service.ServiceImplementation;

import com.example.mainAPK.Model.Address;
import com.example.mainAPK.Repository.IAddressRepository;
import com.example.mainAPK.Service.IAddressService;
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
