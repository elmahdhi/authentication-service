package com.ecoomerce.autorisation.config.auth;

import fr.grdf.softwarefactory.abj.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Classe utilitaire pour Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Récupère l'utilisateur en cours
     *
     * @return <Optional>user</Optional>
     */
    public static Optional<User> getCurrentUser() {
        return Optional.ofNullable(extractUser(SecurityContextHolder.getContext().getAuthentication()));
    }

    /**
     * Extraction d'utilisateur connecté
     * @param authentication objet authentication
     * @return user
     */
    private static User extractUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Récupère le login de l'utilisateur en cours
     *
     * @return login de l'utilisateur
     */
    public static Optional<String> getCurrentUserLogin() {
        return Optional.ofNullable(extractPrincipal(SecurityContextHolder.getContext().getAuthentication()));
    }

    /**
     * Extraction du matricule d'utilisateur selon l'objet d'authentification
     * @param authentication objet authentication
     * @return Principal
     */
    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof User) {
            var user = (User) authentication.getPrincipal();
            return user.getGid();
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            var springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }

        return null;
    }

    /**
     * Vérifier si l'utilisateur est authentifié
     *
     * @return true si l'utilisateur est authentifié sinon false
     */
    public static boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * Vérifie si l'utilisateur en cours possède une "authorithy" spécifique
     *
     * @param authority "authority" à vérifier
     * @return true si l'utilisateur possède une "authority", sinon false
     */
    public static boolean hasCurrentUserThisAuthority(String authority) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).anyMatch(authority::equals);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        var authorities = authentication.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority);
    }

}
