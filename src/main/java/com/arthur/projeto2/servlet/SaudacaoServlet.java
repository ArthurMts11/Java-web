package com.arthur.projeto2.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/saudacao")
public class SaudacaoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String mensagem = request.getParameter("mensagem");
        String saudacao = "Olá, " + nome + "! " + mensagem;

        request.setAttribute("saudacao", saudacao);

        RequestDispatcher rd = request.getRequestDispatcher("saudacao.jsp");
        rd.forward(request, response);
    }
}
