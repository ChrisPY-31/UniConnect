package com.chris.uniconnect.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailTemplateService {
    public String buildWelcomeEmail(String username, String password) {
        // Lee el template HTML (puedes cargarlo desde un archivo)
        String template = loadTemplateFromFile();

        // Reemplaza las variables
        return template
                .replace("${username}", username)
                .replace("${password}", password);
    }

    private String loadTemplateFromFile() {
        // Carga el template desde resources/templates/email-template.html
        try {
            return new String(Files.readAllBytes(Paths.get("src/main/resources/templates/email-template.html")));
        } catch (IOException e) {
            // Fallback a template básico
            return getBasicTemplate();
        }
    }

    private String getBasicTemplate() {
        return """
                <!DOCTYPE html>
                <html>
                <body>
                    <h2>Bienvenido a uniConnect</h2>
                    <p>Usuario: ${username}</p>
                    <p>Contraseña: ${password}</p>
                </body>
                </html>
                """;
    }
}
