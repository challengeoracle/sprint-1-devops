package br.com.fiap.medix_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita a proteção contra CSRF para APIs REST
                .csrf(csrf -> csrf.disable())
                // Garante que a sessão seja Stateless (não armazena estado no servidor)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rotas Públicas (acesso a qualquer um, mesmo não autenticado)
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/avaliacoes/**").permitAll() // IoT (ESP32)
                        // Libera o acesso ao Swagger/OpenAPI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs/swagger-config"
                        ).permitAll()

                        // Rotas de ACESSO GERAL (Qualquer usuário autenticado - Paciente ou Colaborador)
                        // Permite que Pacientes vejam as unidades por lista e por ID
                        .requestMatchers(HttpMethod.GET, "/unidades").authenticated()
                        .requestMatchers(HttpMethod.GET, "/unidades/{id}").authenticated()

                        // Rotas de Agendamento (Todos os autenticados podem usar o fluxo)
                        .requestMatchers("/agendamentos/disponibilidade/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/agendamentos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/agendamentos/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/agendamentos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/agendamentos/**").authenticated()

                        // Rotas de GESTÃO (Apenas Colaborador)
                        // CRUD de Colaboradores (Total)
                        .requestMatchers("/colaboradores/**").hasRole("COLABORADOR")

                        .requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()

                        // CRUD de Pacientes (Total)
                        .requestMatchers("/pacientes/**").hasRole("COLABORADOR")

                        // Rotas de escrita/exclusão de Unidades (GETs liberados acima)
                        .requestMatchers(HttpMethod.POST, "/unidades/**").hasRole("COLABORADOR")
                        .requestMatchers(HttpMethod.PUT, "/unidades/**").hasRole("COLABORADOR")
                        .requestMatchers(HttpMethod.DELETE, "/unidades/**").hasRole("COLABORADOR")

                        // CRUD de Salas (Total)
                        .requestMatchers("/salas/**").hasRole("COLABORADOR")

                        // Todas as outras rotas exigem autenticação
                        .anyRequest().authenticated()
                )
                // Adiciona o filtro JWT para validar o token em todas as requisições
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Desabilita frameOptions para permitir o console H2 em iframes
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .build();
    }


    // Cria o bean para o gerenciador de autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Define o encoder de senhas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}