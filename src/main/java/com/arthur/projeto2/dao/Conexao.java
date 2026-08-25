package com.arthur.projeto2.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://127.0.0.1:8889/pw4?useUnicode=true&characterEncoding=UTF-8";
    private final String USUARIO = "root";
    private final String SENHA = "root";

    public Connection conectar() throws SQLException, ClassNotFoundException {
        try {
            Connection conexao = null;
            Class.forName(driver);
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conexao;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
