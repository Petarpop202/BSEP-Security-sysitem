package com.PKISecurity.services.servicesImplementation;

import com.PKISecurity.model.CertificateData;
import com.PKISecurity.repository.ICertificateDataRepository;
import com.PKISecurity.services.ICertificateDataService;
import org.springframework.stereotype.Service;

@Service
public class CerificateDataService implements ICertificateDataService {
    private ICertificateDataRepository _certificateDataRepository;

    CerificateDataService(ICertificateDataRepository certificateDataRepository){this._certificateDataRepository = certificateDataRepository;}
    @Override
    public Iterable getAll() {
        return _certificateDataRepository.findAll();
    }

    @Override
    public CertificateData getById(Long id) {
        return _certificateDataRepository.findById(id).orElseGet(null);
    }

    @Override
    public CertificateData create(CertificateData entity) {
        return _certificateDataRepository.save(entity);
    }

    @Override
    public CertificateData update(CertificateData entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }
}
