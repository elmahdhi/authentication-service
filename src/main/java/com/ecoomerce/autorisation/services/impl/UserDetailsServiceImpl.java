package com.ecoomerce.autorisation.services.impl;

import com.ecoomerce.autorisation.mappers.UserMapper;
import com.ecoomerce.autorisation.models.Authority;
import com.ecoomerce.autorisation.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.ecoomerce.autorisation.config.auth.AuthoritiesConstants.ANONYMOUS;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;
    private final MaiaUserService maiaUserService;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String gid) throws UsernameNotFoundException {
        var existingUser = accountService.findByGidAndActiveTrue(gid);
        if(existingUser.isPresent()){
            var userDto = userMapper.toDto(existingUser.get());
            log.info("Utilisateur présent dans la base {}", userDto);
        } else {
            log.info("Création d'un nouvel utilisateur automatiquement");
            var newUser = maiaUserService.findByGidAndStatusActif(gid);
            if(newUser.isPresent()){
                var user = userMapper.fromMaiaUserToUser(newUser.get());
                user.setAuthorities(Set.of(Authority.builder().name(ANONYMOUS).build()));

                var userDto = userMapper.toDto(user);
                accountService.save(user);
                log.info("Un nouvel utilisateur a été ajouté dans la base {}", userDto);
            } else {
                log.error("Aucun utilisateur disponible dans la table MAIA dont l'id est {} " + gid);
                return null;
            }
        }
        return new User(existingUser.get().getGid(),
            existingUser.get().getPassword(), existingUser.get().getAuthorities());
    }
}
