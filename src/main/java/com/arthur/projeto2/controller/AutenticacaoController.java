package com.arthur.projeto2.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.arthur.projeto2.dao.UsuarioDao;
import com.arthur.projeto2.model.UsuarioModel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class AutenticacaoController extends HttpServlet {

    private final UsuarioDao dao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getServletPath();

        if (acao.equals("/logout")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("login.jsp");
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getServletPath();

        if (acao.equals("/login")) {
            try {
                request.setCharacterEncoding("UTF-8");

                String email = request.getParameter("email");
                String senha = request.getParameter("senha");

                UsuarioModel usuario = dao.login(email, senha);

                if (usuario != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuario);
                    response.sendRedirect("admin/");
                } else {
                    response.sendRedirect("login.jsp?erro=1");
                }
            } catch (SQLException e) {
                throw new ServletException("Erro ao autenticar usuário", e);
            }
        }
    }
}
