package mx.ipn.escom.Recomendaciones.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Desactiva CSRF (solo si es una API RESTful o aplicación móvil)
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/auth/**").permitAll()  // Rutas públicas para la API
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // Recursos estáticos
                .requestMatchers("/register", "/login").permitAll()  // Páginas públicas
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")  // Solo admins pueden acceder a /admin/
                .requestMatchers("/perfil", "/home").authenticated()  // Rutas protegidas
                .anyRequest().authenticated()  // El resto de rutas requieren autenticación
            )
            .formLogin((form) -> form
                .loginPage("/login")  // Página de inicio de sesión personalizada
                .defaultSuccessUrl("/home")  // Redirige a /home tras login exitoso
                .failureUrl("/login?error=true")  // Redirige aquí si hay un error de autenticación
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  // URL para cerrar sesión
                .logoutSuccessUrl("/login?logout=true")  // Redirige aquí después de cerrar sesión
                .permitAll()
            )
            .exceptionHandling((exception) -> exception
                .accessDeniedPage("/accessDenied")  // Página de acceso denegado
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}