package com.ecoomerce.autorisation.services;

import com.ecoomerce.autorisation.dto.UserDto;
import com.ecoomerce.autorisation.models.Authority;
import com.ecoomerce.autorisation.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    /**
     * Cette méthode permet de lister toutes les authorities
     *
     * @return liste des authorities
     */
    public List<Authority> findAll();

    /**
     * Cette méthode permet de sauvegarder un utilisateur
     *
     * @param user utilisateur à sauvegarder
     * @return objet userDto avec l'id généré
     */
    public User save(User user);

    /**
     * Cette méthode permet de récupérer un user en fonction de son identifiant
     *
     * @param gid gaia id de l'utilisateur
     * @return user récupéré
     */
    public Optional<User> findByGidAndActiveTrue(String gid);


    /**
     * Cette méthode permet de sauvegarder/mettre à jour un utilisateur
     *
     * @param userDto utilisateur à sauvegarder
     * @return objet userDto créé
     */
    public UserDto save(UserDto userDto);


    /**
     * Cette méthode permet de récupérer tout les utilisateurs
     *
     * @param pageable paramétrage de la pagination
     * @return Page de UserDto
     */
    public Page<UserDto> findAll(Pageable pageable);


    /**
     * Cette méthode permet de récupérer une USER en fonction de son identifiant
     *
     * @param gid grdf id de l'utilisateur
     * @return userDto récupéré
     */
    public Optional<UserDto> findById(String gid);


    /**
     * Cette méthode permet de supprimer un utilisateur en fonction de son gid
     *
     * @param gid grdf id de l'utilisateur
     */
    public void delete(String gid);


    /**
     * Cette méthode permet de supprimer tout les utilisateurs
     */
    public void deleteAll();


    /**
     * Cette méthode permet d'ajouter une liste d'utilisateurs  comme utilisateurs de l'application blanche
     *
     * @param userDtoList la liste des gid des utilisateurs à ajouter
     * @return Liste des utilisateurs crées
     */
    public List<UserDto> addImportedUsers(List<UserDto> userDtoList);
}
