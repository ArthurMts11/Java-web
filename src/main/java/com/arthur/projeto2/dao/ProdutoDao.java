package com.arthur.projeto2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.arthur.projeto2.model.ProdutoModel;

public class ProdutoDao {

    public void salvar(ProdutoModel p) throws SQLException {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)");
            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQuantidade());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public List<ProdutoModel> listarTodos() throws SQLException {
        List<ProdutoModel> produtos = new ArrayList<>();

        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("SELECT * FROM produtos");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                produtos.add(new ProdutoModel(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return produtos;
    }

    public ProdutoModel buscarPorId(int id) throws SQLException {
        ProdutoModel produto = null;

        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("SELECT * FROM produtos WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                produto = new ProdutoModel(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade")
                );
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return produto;
    }

    public void atualizar(ProdutoModel p) throws SQLException {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?");
            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQuantidade());
            stmt.setInt(4, p.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void deletar(int id) throws SQLException {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement stmt =
                conn.prepareStatement("DELETE FROM produtos WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
