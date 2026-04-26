package com.azevedo.task_scheduler_service.infrastructure.security;


import com.azevedo.task_scheduler_service.business.dto.UsuarioDTO;
import com.azevedo.task_scheduler_service.infrastructure.client.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private UsuarioClient client;


    public UserDetails carregaDadosUsuario(String email, String token) {

        UsuarioDTO usuarioDTO = client.buscaUsuarioPorEmail(email, token);
        return User
                .withUsername(usuarioDTO.getEmail()) // Define o nome de usuário como o e-mail
                // A senha não é usada neste microserviço (autenticação é via JWT Bearer).
                .password("{noop}not-used")
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .build(); // Constrói o objeto UserDetails

    }

}
