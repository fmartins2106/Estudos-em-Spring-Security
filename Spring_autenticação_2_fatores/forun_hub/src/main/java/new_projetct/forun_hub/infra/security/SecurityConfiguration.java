package new_projetct.forun_hub.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.POST, "/login", "/atualizar-token", "/registrar", "/verificar-conta").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/verificar-conta").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/curso**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/curso").hasAnyRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PUT, "/curso").hasAnyRole("ADMINISTRADOR");

                    auth.requestMatchers(HttpMethod.GET,"/topicos/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/topicos").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PUT, "/topicos").hasAnyRole("MODERADOR, INSTRUTOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/topicos**").hasRole("ESTUDANTE");

                    auth.requestMatchers(HttpMethod.GET, "/respostas").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/repostas").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PUT, "/repostas").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PATCH, "/repostas**").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.DELETE, "/repostas**").hasRole("ESTUDANTE");

                    auth.requestMatchers(HttpMethod.POST, "/registrar").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/{nomeUsuario}").hasAuthority("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PATCH, "/editar-perfil").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PATCH, "/alterar-senha").hasRole("ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PATCH, "/desativar/{id}").hasAnyRole("ADMINISTRADOR", "ESTUDANTE");
                    auth.requestMatchers(HttpMethod.PATCH, "/reativar-conta/{id}").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PATCH, "/adicionar-perfil/{id}").hasRole("ADMINISTRADOR");
                    auth.requestMatchers("/login/github","/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**",
                            "/webjars/**").permitAll();

                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public RoleHierarchy roleHierarchy(){
        String hierarquia = "ROLE_ADMINISTRADOR > ROLE_MODERADOR\n" +
                "ROLE_MODERADOR > ROLE_INSTRUTOR\n" +
                "ROLE_INSTRUTOR > ROLE_ESTUDANTE";
        return RoleHierarchyImpl.fromHierarchy(hierarquia);
    }



}
