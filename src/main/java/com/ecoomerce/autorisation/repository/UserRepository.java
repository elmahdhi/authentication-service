package com.ecoomerce.autorisation.repository;

import com.ecoomerce.autorisation.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository de l'entité Utilisateur.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    String USERS_BY_GID_CACHE = "usersByGid";
    String USERS_BY_GID_AND_ACTIVE_CACHE = "usersByGidAndActive";

    /**
     * Supprimer User par gid
     * @param gid grdf id
     */
    void deleteByGid(String gid);

    /**
     * Récupérer user par gid
     * @param gid grdf id
     * @return user récupéré
     */
    @Cacheable(value = USERS_BY_GID_CACHE)
    Optional<User> findByGid(String gid);

    /**
     * Récupérer user actif par gid
     * @param gid grdf id
     * @return user récupéré
     */
    @Cacheable(value = USERS_BY_GID_AND_ACTIVE_CACHE)
    Optional<User> findByGidAndActiveTrue(String gid);


}
