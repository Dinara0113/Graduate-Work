package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
/**
 * Конфигурация безопасности Spring Security.
 * Настраивает доступ к эндпоинтам, шифрование паролей и механизм аутентификации.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    /**
     * Пути, доступные без аутентификации.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Конфигурация цепочки фильтров безопасности.
     *
     * @param http объект {@link HttpSecurity} для настройки
     * @return настроенная цепочка фильтров {@link SecurityFilterChain}
     * @throws Exception в случае ошибок конфигурации
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/ads/*/image").permitAll()
                        .requestMatchers("/ads/**", "/users/**", "/comments/**").authenticated()
                )
                .cors(withDefaults())
                .formLogin(withDefaults())         // включаем форму входа
                .httpBasic(httpBasic -> httpBasic.disable()); // отключаем Basic Auth

        return http.build();
    }


    /**
     * Бин для шифрования паролей пользователей.
     *
     * @return объект {@link PasswordEncoder} с реализацией BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
