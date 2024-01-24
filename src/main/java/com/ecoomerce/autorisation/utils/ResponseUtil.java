package com.ecoomerce.autorisation.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Classe Utilisateur pour la gestion de ResponseEntity.
 */
public interface ResponseUtil {

    /**
     * Encapsuler {@link ResponseEntity} avec le statut {@link HttpStatus#OK} dans le headers sinon
     * retourne {@link ResponseEntity} avec  {@link HttpStatus#NOT_FOUND}.
     *
     * @param <X>           type de la réponse
     * @param maybeResponse réponse à retourner si présente
     * @return si présent retourne la réponse {@code maybeResponse} sinon {@link HttpStatus#NOT_FOUND}
     */
    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    /**
     * Encapsuler {@link ResponseEntity} avec le statut {@link HttpStatus#OK} dans le headers
     * leve une exception {@link ResponseStatusException} avec le statut {@link HttpStatus#NOT_FOUND}.
     *
     * @param <X>           type de la réponse
     * @param maybeResponse réponse à retourner si présente
     * @param header        headers à ajouter dans la réponse
     * @return si présent retourne la réponse {@code maybeResponse}
     */
    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
