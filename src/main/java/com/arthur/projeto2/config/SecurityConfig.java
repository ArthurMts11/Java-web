package com.arthur.projeto2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import com.arthur.projeto2.security.JsonErroSeguranca;
import com.arthur.projeto2.security.SecurityFilter;
import com.arthur.projeto2.security.TokenService;

/**
 * Duas cadeias de segurança independentes:
 * 1) API REST (JSON): stateless, autenticada por JWT no header Authorization.
 * 2) Telas JSP: login por formulário e sessão, como já era antes.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] ROTAS_API = {
        "/public/**", "/imoveis/**", "/api/**", "/bairros/**", "/tipos-imoveis/**", "/fotos/**"
    };

    private static final String[] CADASTROS_PUBLICOS = {
        "/imoveis/**", "/bairros/**", "/tipos-imoveis/**", "/fotos/**"
    };

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, JsonErroSeguranca erros,
            TokenService tokenService) throws Exception {
        http
            .securityMatcher(ROTAS_API)
            .authorizeHttpRequests(auth -> auth
                // Público: login, consultas e o "saber mais" do cliente
                .requestMatchers(HttpMethod.POST, "/public/login").permitAll()
                .requestMatchers(HttpMethod.GET, CADASTROS_PUBLICOS).permitAll()
                .requestMatchers(HttpMethod.POST, "/imoveis/sabermais/*").permitAll()
                // Qualquer usuário logado (ADMIN ou CLIENTE)
                .requestMatchers(HttpMethod.PATCH, "/imoveis/*/status").authenticated()
                // Todo o resto (cadastrar/alterar/remover imóveis, gerenciar usuários...)
                .anyRequest().hasRole("ADMIN")
            )
            // Lê e valida o JWT antes das regras acima serem aplicadas
            .addFilterBefore(new SecurityFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(erros)
                .accessDeniedHandler(erros)
            )
            // Sem sessão: cada requisição se identifica sozinha pelo token.
            // Sem cookie de sessão também não há CSRF a proteger.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http, AuthenticationSuccessHandler loginSuccessHandler)
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            // Requisições JSON (ex.: Thunder Client) não têm o token CSRF que
            // as JSPs mandam no campo oculto. Um <form> HTML comum não
            // consegue forjar Content-Type: application/json, então isentar
            // só esse tipo de requisição do CSRF não abre brecha pros forms.
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(request -> "application/json".equalsIgnoreCase(request.getContentType()))
            )
            .formLogin(form -> form
                .loginPage("/login.jsp")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("senha")
                .successHandler(loginSuccessHandler)
                .failureUrl("/login.jsp?erro=1")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/logout"))
                .logoutSuccessUrl("/login.jsp")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
