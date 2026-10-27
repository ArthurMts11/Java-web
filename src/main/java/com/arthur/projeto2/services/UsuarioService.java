package com.arthur.projeto2.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arthur.projeto2.dtos.UserDTO;
import com.arthur.projeto2.model.TipoUsuario;
import com.arthur.projeto2.model.UsuarioModel;
import com.arthur.projeto2.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioModel> getAllUsers() {
        return repository.findAll();
    }

    public UsuarioModel find(Integer id) {
        Optional<UsuarioModel> usuario = repository.findById(id);
        return usuario.orElse(null);
    }

    public UsuarioModel insert(UsuarioModel usuario) {
        return repository.save(usuario);
    }

    public UsuarioModel update(UsuarioModel usuario) {
        try {
            UsuarioModel existente = find(usuario.getId());
            if (existente == null) {
                return null;
            } else {
                if (usuario.getNome() != null) {
                    existente.setNome(usuario.getNome());
                }
            }
            return repository.save(existente);
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public UsuarioModel insert(UserDTO dto) {
        UsuarioModel model = new UsuarioModel();
        model.setNome(dto.getNome());
        model.setEmail(dto.getEmail());
        model.setSenha(passwordEncoder.encode(dto.getSenha()));
        if (dto.getTipo() != null) {
            model.setTipo(TipoUsuario.valueOf(dto.getTipo()));
        }
        return repository.save(model);
    }

    public UsuarioModel update(UserDTO dto) {
        UsuarioModel model = find(dto.getId());
        try {
            if (model != null) {
                model.setNome(dto.getNome());
                model.setEmail(dto.getEmail());
                if (dto.getTipo() != null) {
                    model.setTipo(TipoUsuario.valueOf(dto.getTipo()));
                }
                return repository.save(model);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
