package com.PKISecurity.services.servicesImplementation;

import com.PKISecurity.model.SubjectData;
import com.PKISecurity.repository.ISubjectDataRepository;
import com.PKISecurity.services.ISubjectDataService;
import org.springframework.stereotype.Service;

@Service
public class SubjectDataService implements ISubjectDataService {
    private ISubjectDataRepository _subjectDataRepository;

    SubjectDataService(ISubjectDataRepository subjectDataRepository){this._subjectDataRepository = subjectDataRepository;}
    @Override
    public Iterable<SubjectData> getAll() {
        return _subjectDataRepository.findAll();
    }

    @Override
    public SubjectData getById(Long id) {
        return _subjectDataRepository.findById(id).orElseGet(null);
    }

    @Override
    public SubjectData create(SubjectData entity) {
        return _subjectDataRepository.save(entity);
    }

    @Override
    public SubjectData update(SubjectData entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }
}
