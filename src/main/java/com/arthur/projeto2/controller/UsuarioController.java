package com.arthur.projeto2.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.arthur.projeto2.dao.UsuarioDao;
import com.arthur.projeto2.model.UsuarioModel;
import com.arthur.projeto2.util.SenhaUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {

    private final UsuarioDao dao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            action = (action == null) ? "listar" : action;

            switch (action) {
                case "novo":
                    request.getRequestDispatcher("usuario-form.jsp").forward(request, response);
                    break;

                case "editar":
                    int id = Integer.parseInt(request.getParameter("id"));
                    UsuarioModel usuario = dao.buscarPorId(id);
                    request.setAttribute("usuario", usuario);
                    request.getRequestDispatcher("usuario-form.jsp").forward(request, response);
                    break;

                case "excluir":
                    dao.deletar(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect("usuarios");
                    break;

                default:
                    List<UsuarioModel> usuarios = dao.listarTodos();
                    request.setAttribute("usuarios", usuarios);
                    request.getRequestDispatcher("usuarios.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao processar usuário", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String idParam = request.getParameter("id");
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha"); // senha que veio do formulário

            if (idParam == null || idParam.isEmpty()) {
                String senhaCriptografada = SenhaUtil.hashSenha(senha);
                UsuarioModel usuario = new UsuarioModel(nome, email, senhaCriptografada);
                dao.salvar(usuario);

                // Cadastro público (registrar.jsp): manda o novo usuário fazer login.
                response.sendRedirect("login.jsp");
            } else {
                int id = Integer.parseInt(idParam);

                // Campo de senha em branco na edição = mantém a senha atual (já criptografada).
                String senhaCriptografada;
                if (senha == null || senha.isEmpty()) {
                    UsuarioModel existente = dao.buscarPorId(id);
                    senhaCriptografada = existente.getSenha();
                } else {
                    senhaCriptografada = SenhaUtil.hashSenha(senha);
                }

                UsuarioModel usuario = new UsuarioModel(id, nome, email, senhaCriptografada);
                dao.atualizar(usuario);

                // Edição via CRUD administrativo: volta para a listagem.
                response.sendRedirect("usuarios");
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar usuário", e);
        }
    }
}
