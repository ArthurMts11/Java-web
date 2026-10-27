package com.arthur.projeto2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arthur.projeto2.model.ImovelModel;

public interface ImovelRepository extends JpaRepository<ImovelModel, Integer> {

}
