package com.arthur.projeto2.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.arthur.projeto2.dao.ProdutoDao;
import com.arthur.projeto2.model.ProdutoModel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/produtos")
public class ProdutoController extends HttpServlet {

    private final ProdutoDao dao = new ProdutoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            action = (action == null) ? "listar" : action;

            switch (action) {
                case "novo":
                    request.getRequestDispatcher("produto-form.jsp").forward(request, response);
                    break;

                case "editar":
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProdutoModel produto = dao.buscarPorId(id);
                    request.setAttribute("produto", produto);
                    request.getRequestDispatcher("produto-form.jsp").forward(request, response);
                    break;

                case "excluir":
                    dao.deletar(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect("produtos");
                    break;

                default:
                    List<ProdutoModel> produtos = dao.listarTodos();
                    request.setAttribute("produtos", produtos);
                    request.getRequestDispatcher("produtos.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao processar produto", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String idParam = request.getParameter("id");
            String nome = request.getParameter("nome");
            double preco = Double.parseDouble(request.getParameter("preco"));
            int quantidade = Integer.parseInt(request.getParameter("quantidade"));

            if (idParam == null || idParam.isEmpty()) {
                ProdutoModel produto = new ProdutoModel(nome, preco, quantidade);
                dao.salvar(produto);
            } else {
                ProdutoModel produto = new ProdutoModel(Integer.parseInt(idParam), nome, preco, quantidade);
                dao.atualizar(produto);
            }

            response.sendRedirect("produtos");
        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar produto", e);
        }
    }
}
