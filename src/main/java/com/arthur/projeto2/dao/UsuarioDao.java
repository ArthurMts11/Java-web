package com.arthur.projeto2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.arthur.projeto2.model.UsuarioModel;
import com.arthur.projeto2.util.SenhaUtil;

public class UsuarioDao {

    public void salvar(UsuarioModel u) throws SQLException {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)");
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getSenha());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public List<UsuarioModel> listarTodos() throws SQLException {
        List<UsuarioModel> usuarios = new ArrayList<>();

        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(new UsuarioModel(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return usuarios;
    }

    public UsuarioModel buscarPorId(int id) throws SQLException {
        UsuarioModel usuario = null;

        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = new UsuarioModel(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return usuario;
    }

    /**
     * Autentica um usuário pelo e-mail e senha.
     * Retorna o UsuarioModel encontrado, ou null se as credenciais forem inválidas.
     */
    public UsuarioModel login(String email, String senha) throws SQLException {
        UsuarioModel usuario = null;

        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("SELECT * FROM usuarios WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && SenhaUtil.verificarSenha(senha, rs.getString("senha"))) {
                usuario = new UsuarioModel(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return usuario;
    }

    public void atualizar(UsuarioModel u) throws SQLException {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?");
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getSenha());
            stmt.setInt(4, u.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void deletar(int id) throws SQLException {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
