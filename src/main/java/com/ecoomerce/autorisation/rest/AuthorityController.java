package com.ecoomerce.autorisation.rest;

import fr.grdf.softwarefactory.abj.configuration.auth.AuthoritiesConstants;
import fr.grdf.softwarefactory.abj.models.Authority;
import fr.grdf.softwarefactory.abj.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
@RequiredArgsConstructor
@Tag(name = "Authorisation", description = "APIs exposées pour gérer les authorisations")
@Slf4j
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
public class AuthorityController {

    private final AccountService accountService;

    /**
     * {@code GET  /authorities} : Retourner la liste des authorities.
     *
     * @return {@link ResponseEntity} avec un statut {@code 200 (OK)} contenant la liste des authorities.
     */
    @GetMapping("")
    @Operation(summary = "Retourner la liste des authorities", description = "Retourner la liste des authorities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Requête mal formée", content = @Content),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Utilisateur non autorisé", content = @Content),
        @ApiResponse(responseCode = "200", description = "Opération réussie",
            content = @Content(schema = @Schema(implementation = Authority.class)))
    })
    public ResponseEntity<List<Authority>> findAllAuthorities() {
        log.debug("REST requête : récupération des authorities");
        var authorities = accountService.findAll();
        return ResponseEntity.ok().body(authorities);
    }

}
