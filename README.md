# Java-web: API da imobiliária com JWT

Projeto da disciplina de Programação Web IV (IFFar, Campus Panambi). É uma API em Spring Boot para uma imobiliária, com autenticação e autorização usando JWT.

As respostas das perguntas da atividade estão em [Respostas_Atividade_JWT_ArthurFogacaMartins.pdf](Respostas_Atividade_JWT_ArthurFogacaMartins.pdf).

## Tecnologias

- Java 25 e Spring Boot 4
- Spring Security e `com.auth0:java-jwt`
- MySQL (MAMP)

## Como rodar

1. Inicie o MySQL (MAMP, porta 8889, usuário e senha `root`).
2. Importe o arquivo `dump.sql`, que cria o banco `pw4` com os dados.
3. Na pasta do projeto, rode:
   ```
   mvn spring-boot:run
   ```
4. A API fica em `http://localhost:8080`.

Opcionalmente, defina a variável `JWT_SECRET` com pelo menos 32 caracteres. Sem ela o sistema cria uma chave temporária e os tokens deixam de valer quando ele reinicia.

## Usuários de teste

| E-mail | Senha | Papel |
|---|---|---|
| admin.jwt@teste.com | Senha@123 | ADMIN |
| cliente.jwt@teste.com | Senha@123 | CLIENTE |

## Rotas

| Ação | Rota | Quem acessa |
|---|---|---|
| Login | `POST /public/login` | Todos |
| Listar imóveis / um imóvel | `GET /imoveis`, `GET /imoveis/{id}` | Todos |
| Cadastrar, alterar e remover imóveis | `POST`, `PUT`, `DELETE /imoveis` | ADMIN |
| Cadastrar e gerenciar usuários | `/api/usuarios` | ADMIN |
| Alterar status de imóvel | `PATCH /imoveis/{id}/status` | Qualquer usuário logado |

O login devolve um token, que deve ser enviado nas outras requisições no cabeçalho `Authorization: Bearer <token>`.

- Sem token, ou com token inválido ou expirado, a API retorna **401**.
- Com um token válido de um usuário sem permissão (por exemplo, CLIENTE cadastrando imóvel), retorna **403**.

A pasta `postman/` tem a coleção "Imobiliária - JWT" com todos esses testes.
