package br.com.forum_hub.infra.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.SecurityMarker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private FiltroTokenAcesso filtroTokenAcesso;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login","/atualizar-token", "/registrar",
                            "verificar-conta").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/cursos").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/topicos/**").permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/topicos/").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PUT, "/topicos/").hasAnyRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.DELETE, "/topicos/**").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PATCH, "/topicos/**").hasRole("MODERADOR");
                    auth.requestMatchers(HttpMethod.PATCH, "/adicionar-perfil/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PATCH, "/remover-perfil/**").hasRole("ADMIN");

                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(filtroTokenAcesso, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encriptador(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public RoleHierarchy hierarquiaPerfis(){
//        String hierarquia = "ROLE_ADMIN > ROLE_MODERADOR\n" +
//                "ROLE_MODERADOR > ROLE_INSTRUTOR\n" +
//                "ROLE_INSTRUTOR > ROLE_ESTUDANTE";
//        return RoleHierarchyImpl.fromHierarchy(hierarquia);
//    }

    @Bean
    public RoleHierarchy hierarquiaPerfis(){
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("MODERADOR")
                .role("MODERADOR").implies("ESTUDANTE", "INSTRUTOR")
                .build();
    }




}
