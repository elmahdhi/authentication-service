package com.ecoomerce.autorisation.rest;

import com.ecoomerce.autorisation.config.auth.AuthoritiesConstants;
import com.ecoomerce.autorisation.dto.UserDto;
import com.ecoomerce.autorisation.services.AccountService;
import com.ecoomerce.autorisation.utils.PaginationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing {@link com.ecoomerce.autorisation.models.User}.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateur", description = "Endpoint exposé pour gérer les utilisateurs")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
public class UserController {

    private final AccountService accountService;
    private final MaiaUserService maiaUserService;

    /**
     * {@code POST /users} : Ajouter des utilisateurs et les affecter un rôle
     *
     * @param userDtoList Objet pour mapper le body json de la requête post
     */
    @PostMapping("")
    @Operation(summary = "Ajouter des utilisateurs et les affecter un rôle",
        description = "Ajouter des utilisateurs et les affecter un rôle")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Requête mal formée", content = @Content),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Utilisateur non autorisé", content = @Content),
        @ApiResponse(responseCode = "200", description = "Opération réussie",
            content = @Content(schema = @Schema(implementation = UserDto[].class)))
    })
    public ResponseEntity<List<UserDto>> addUsers(@Valid @RequestBody List<UserDto> userDtoList) {
        log.debug("REST requête d'ajout des utilisateurs maia à l'application");
        var userDtos = accountService.addImportedUsers(userDtoList);
        return ResponseEntity.ok().body(userDtos);
    }

    /**
     * {@code PUT  /users} : Updates an existing user.
     *
     * @param userDTO l'utilisateur à modifier.
     * @return le {@link ResponseEntity} statut {@code 200 (OK)} et le body de l'utilisateur à jour.
     */
    @PutMapping("")
    @Operation(summary = "Mettre à jour un utilisateur", description = "Mettre à jour un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Requête mal formée", content = @Content),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Utilisateur non autorisé", content = @Content),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvée", content = @Content),
        @ApiResponse(responseCode = "200", description = "Opération réussie", content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDTO) {
        log.debug("REST requête de mise à jour d'un utilisateur : {}", userDTO);
        var result = accountService.save(userDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET /users} : Récuperer tous les utilisateurs.
     *
     * @param pageable Information de pagination.
     * @return le {@link ResponseEntity} statut {@code 200 (OK)} et la liste des utilisateurs dans le body.
     */
    @GetMapping("")
    @Operation(summary = "Récuperer toutes les utilisateurs", description = "Récuperer toutes les utilisateurs", method = "GET")
    @PageableAsQueryParam
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Requête mal formée", content = @Content),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Utilisateur non autorisé", content = @Content),
        @ApiResponse(responseCode = "200", description = "Opération réussie",
            content = @Content(schema = @Schema(implementation = UserDto[].class)))
    })
    public ResponseEntity<List<UserDto>> getAllUsers(@Parameter(hidden = true) final Pageable pageable) {
        log.debug("REST requête de récupération d'une page de User");
        var page = accountService.findAll(pageable);
        var headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /users/:id} : Récuperer un utilisateur à partir de son gaia
     *
     * @param gid le matricule GAIA
     * @return le {@link ResponseEntity} statut {@code 200 (OK)} et le utilisateur dans le body ou le statut {@code 404 (Not Found)}.
     */
    @GetMapping("/{gid}")
    @Operation(summary = "Récupérer un utilisateur", description = "Récupérer un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Requête mal formée", content = @Content),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Utilisateur non autorisé", content = @Content),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvée", content = @Content),
        @ApiResponse(responseCode = "200", description = "Opération réussie",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    public ResponseEntity<UserDto> getUser(@PathVariable String gid) {
        log.debug("REST requête de récupération d'un utilisateur {}", gid);
        var userDTO = accountService.findById(gid);
        return userDTO.map(userDto -> ResponseEntity.ok().body(userDto)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /users/:id} : supprimer l'utilisateur à partir de son "id".
     *
     * @param gid l'identifiant gaia de l'utilisateur à supprimer.
     * @return le {@link ResponseEntity} statut {@code 204 (No Content)}.
     */
    @DeleteMapping("/{gid}")
    @Operation(summary = "Supprimer un utilisateur", description = "Supprimer un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Action confirmée",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
    })
    public ResponseEntity<Void> deleteUser(@PathVariable String gid) {
        log.debug("REST requête de suppression d'un utilisateur : {}", gid);
        accountService.delete(gid);
        return ResponseEntity.ok().build();
    }
}
