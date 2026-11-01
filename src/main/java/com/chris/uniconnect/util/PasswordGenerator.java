package com.chris.uniconnect.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//contrasenas autogeneradas para las cuentas que crea el sistema (empresa y reclutadores)
@Component
public class PasswordGenerator {

    private static final String UPPER = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijkmnopqrstuvwxyz";
    private static final String DIGITS = "23456789";
    private static final String SYMBOLS = "!@#$%*?";
    private static final int PASSWORD_LENGTH = 12;

    private final SecureRandom random = new SecureRandom();

    //garantiza al menos una mayuscula, minuscula, digito y simbolo
    public String generate() {
        String all = UPPER + LOWER + DIGITS + SYMBOLS;
        List<Character> chars = new ArrayList<>();
        chars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        chars.add(LOWER.charAt(random.nextInt(LOWER.length())));
        chars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        chars.add(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        while (chars.size() < PASSWORD_LENGTH) {
            chars.add(all.charAt(random.nextInt(all.length())));
        }
        Collections.shuffle(chars, random);
        StringBuilder password = new StringBuilder();
        chars.forEach(password::append);
        return password.toString();
    }
}
