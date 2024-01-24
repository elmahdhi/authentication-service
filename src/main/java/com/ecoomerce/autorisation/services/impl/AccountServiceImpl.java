package com.ecoomerce.autorisation.services.impl;

import com.ecoomerce.autorisation.dto.UserDto;
import com.ecoomerce.autorisation.mappers.UserMapper;
import com.ecoomerce.autorisation.models.Authority;
import com.ecoomerce.autorisation.models.User;
import com.ecoomerce.autorisation.repository.AuthorityRepository;
import com.ecoomerce.autorisation.repository.UserRepository;
import com.ecoomerce.autorisation.rest.error.ErrorConstants;
import com.ecoomerce.autorisation.rest.error.exception.InternalErrorException;
import com.ecoomerce.autorisation.services.AccountService;
import com.ecoomerce.autorisation.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



/**
 * Service pour gérer l'entité {@link User}.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CacheManager cacheManager;
    private final MessageUtil messages;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional(readOnly = true)
    public List<Authority> findAll() {
        log.debug("Récupération de toutes les authorities");
        return authorityRepository.findAll();
    }

    public User save(User user) {
        log.debug("Enregistrement de l'utilisateur {} ", user);
        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public Optional<User> findByGidAndActiveTrue(String gid) {
        log.debug("Récupération du User connecté {} ", gid);
        return userRepository.findByGidAndActiveTrue(gid);
    }


    public UserDto save(UserDto userDto) {
        encryptPassword(userDto);
        var user= userMapper.toEntity(userDto);
        try {
            log.debug("Enregistrement d'un User : {}", userDto);
            user = userRepository.save(user);
            clearCaches();
        } catch (Exception ex) {
            if (userDto.getGid() != null) {
                log.error(messages.getMessage(ErrorConstants.ERR_F_USER_UPDATE, userDto.getGid()));
                throw new InternalErrorException(ErrorConstants.ERR_F_USER_UPDATE,
                    messages.getMessage(ErrorConstants.ERR_F_USER_UPDATE));
            }
            log.error(messages.getMessage(ErrorConstants.ERR_F_USER_CREATE, userDto.getGid()));
            throw new InternalErrorException(ErrorConstants.ERR_F_USER_CREATE,
                messages.getMessage(ErrorConstants.ERR_F_USER_CREATE));
        }
        return userMapper.toDto(user);
    }

    private void encryptPassword(UserDto userDto) {
        if(userDto.getPassword() != null){
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        }
    }

    /**
     * Cette méthode permet de récupérer tout les utilisateurs
     *
     * @param pageable paramétrage de la pagination
     * @return Page de UserDto
     */
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        log.debug("Récupération des tous les Users");
        return userRepository.findAll(pageable)
            .map(userMapper::toDto);
    }

    /**
     * Cette méthode permet de récupérer une USER en fonction de son identifiant
     *
     * @param gid grdf id de l'utilisateur
     * @return userDto récupéré
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(String gid) {
        log.debug("Récupération du User : {}", gid);
        var user = userRepository.findByGid(gid);
        if (user.isEmpty()) {
            log.warn(messages.getMessage(ErrorConstants.ERR_F_USER_NOT_EXIST, gid));
            throw new NotFoundException(ErrorConstants.ERR_F_USER_NOT_EXIST,
                messages.getMessage(ErrorConstants.ERR_F_USER_NOT_EXIST, gid));
        }
        return user.map(userMapper::toDto);

    }

    /**
     * Cette méthode permet de supprimer un utilisateur en fonction de son gid
     *
     * @param gid grdf id de l'utilisateur
     */
    @Transactional
    public void delete(String gid) {
        try {
            log.debug("Suppression de User : {}", gid);
            userRepository.deleteByGid(gid);
        } catch (Exception ex) {
            log.error(messages.getMessage(ErrorConstants.ERR_F_USER_DELETE, gid));
            throw new InternalErrorException(ErrorConstants.ERR_F_USER_DELETE,
                messages.getMessage(ErrorConstants.ERR_F_USER_DELETE, gid));
        }
    }

    /**
     * Cette méthode permet de supprimer tout les utilisateurs
     */
    public void deleteAll() {
        log.debug("Suppression de tous les Users");
        userRepository.deleteAll();
    }


    /**
     * Cette méthode permet d'ajouter une liste d'utilisateurs  comme utilisateurs de l'application blanche
     *
     * @param userDtoList la liste des gid des utilisateurs à ajouter
     * @return Liste des utilisateurs crées
     */
    public List<UserDto> addImportedUsers(List<UserDto> userDtoList) {
        userDtoList.forEach(userDto -> {
            if (userDto.getPassword() != null){
                encryptPassword(userDto);
            }
        });
        return userRepository.saveAll(userDtoList.stream()
                .map(userMapper::toEntity)
                .collect(Collectors.toList()))
            .stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
    }
}
