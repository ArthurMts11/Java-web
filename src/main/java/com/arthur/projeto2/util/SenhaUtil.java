package com.arthur.projeto2.util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    public static String hashSenha(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    public static boolean verificarSenha(String senhaPura, String senhaCriptografada) {
        return BCrypt.checkpw(senhaPura, senhaCriptografada);
    }
}
