package mx.ipn.escom.Recomendaciones.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    /**
     * Muestra la página de inicio de sesión.
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        // Agregar mensajes de error o logout al modelo
        if (error != null) {
            model.addAttribute("error", "Correo electrónico o contraseña incorrectos.");
        }

        if (logout != null) {
            model.addAttribute("logout", "Has cerrado sesión correctamente.");
        }

        return "login"; // Nombre de la plantilla Thymeleaf (login.html)
    }

    /**
     * Maneja el inicio de sesión exitoso y redirige según el rol del usuario.
     */
    @PostMapping("/login")
    public String loginSuccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin"; // Redirige al dashboard de administrador
        } else {
            return "redirect:/home"; // Redirige al home para usuarios normales
        }
    }

    /**
     * Muestra la página de inicio.
     */
    @GetMapping("/home")
    public String home() {
        return "home"; // Nombre de la plantilla Thymeleaf (home.html)
    }

    /**
     * Muestra la página de acceso denegado.
     */
    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied"; // Nombre de la plantilla Thymeleaf (accessDenied.html)
    }
}
