-- Script de criação do banco usado pela classe Conexao (com.arthur.projeto2.dao.Conexao)
-- Execute este script no MySQL antes de rodar a aplicação.

CREATE DATABASE IF NOT EXISTS pw4
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE pw4;

CREATE TABLE IF NOT EXISTS produtos (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    preco      DOUBLE       NOT NULL,
    quantidade INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);
