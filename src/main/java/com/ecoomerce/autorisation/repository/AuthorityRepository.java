package com.ecoomerce.autorisation.repository;

import com.ecoomerce.autorisation.models.Authority;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository de l'entité Authority.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    String ALL_AUTHORITIES = "allAuthorities";
    /**
     * Récupérer liste de tous les authorisations
     * @return la liste des authorisations.
     */
    @Cacheable(value = ALL_AUTHORITIES)
    List<Authority> findAll();
}
