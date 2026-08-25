package com.arthur.projeto2.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/admin/*")
public class AuthFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        boolean estaLogado = (session != null && session.getAttribute("usuario") != null);

        if (estaLogado) {
            System.out.println("Usuário Logado");
            chain.doFilter(request, response);
        } else {
            System.out.println("Usuário Não logado");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
