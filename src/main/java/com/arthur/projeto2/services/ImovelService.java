package com.arthur.projeto2.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.arthur.projeto2.model.ImovelModel;
import com.arthur.projeto2.model.StatusImovel;
import com.arthur.projeto2.repository.ImovelRepository;

@Service
public class ImovelService {

    private final ImovelRepository repository;

    public ImovelService(ImovelRepository repository) {
        this.repository = repository;
    }

    public List<ImovelModel> getAll() {
        return repository.findAll();
    }

    public Page<ImovelModel> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public ImovelModel find(Integer id) {
        Optional<ImovelModel> model = repository.findById(id);
        return model.orElse(null);
    }

    public ImovelModel insert(ImovelModel model) {
        return repository.save(model);
    }

    public ImovelModel update(ImovelModel model) {
        try {
            if (find(model.getId()) != null) {
                return repository.save(model);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public ImovelModel alterarStatus(Integer id, StatusImovel status) {
        ImovelModel model = find(id);
        if (model == null) {
            return null;
        }
        model.setStatus(status);
        return repository.save(model);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

}
