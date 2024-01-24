package com.ecoomerce.autorisation.rest;

import fr.grdf.softwarefactory.abj.configuration.auth.SecurityUtils;
import fr.grdf.softwarefactory.abj.dto.UserDto;
import fr.grdf.softwarefactory.abj.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whoami")
@Tag(name = "Authentification", description = "Endpoint exposé pour gérer les authentifications")
@Slf4j
public class AuthenticationController {

    /**
     * {@code GET  /whoami} : récupérer l'utilisateur courant.
     *
     * @return le {@link ResponseEntity} avec un statut {@code 200 (OK)} contenant l'utilisateur.
     */
    @GetMapping("")
    @Operation(summary = "Retourner l'utilisateur courant", description = "Retourner l'utilisateur courant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Requête mal formée", content = @Content),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Utilisateur non autorisé", content = @Content),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvée", content = @Content),
        @ApiResponse(responseCode = "200", description = "Opération réussie",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    public ResponseEntity<User> whoami() {
        log.debug("REST requête pour récupérer l'utilisateur courant");
        return SecurityUtils.getCurrentUser().map(user -> ResponseEntity.ok().body(user))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
