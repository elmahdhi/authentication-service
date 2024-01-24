package com.ecoomerce.autorisation.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service qui permet de crypter et décrypter les passwords pour JASYPT
 */
@Tag(name = "JASYPT-ENCRYPTION", description = "Endpoint exposé pour gérer les cryptage JASYPT")
@RestController
@RequestMapping("/api/jasypt/")
@Profile({"dev", "test"})
@Slf4j
//TODO Ce mécanisme est temporairment disponible poour sécuriser les données sensibles va être remplacé par VAULT
public class JasyptController {

    @Value("${jasypt.encryptor.iv-generator-classname:org.jasypt.iv.NoIvGenerator}")
    private String ivGeneratorClassname;
    private static final String START = "{\"result\" : \"";
    private static final String END = "\"}";

    /**
     * Cryptage d'un password
     */
    @Operation(
        summary = "Encryption with jasypt tool,",
        method = "GET",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cryptage réussi avec succès"),
            @ApiResponse(responseCode = "404", description = "Problème durant le cryptage des données"),
            @ApiResponse(responseCode = "400", description = "Problème durant le cryptage des données"),
            @ApiResponse(content = @Content(
                schema = @Schema(implementation = String.class),
                mediaType = "application/text"))
        })
    @GetMapping("/encrypt")
    public ResponseEntity<String> encryption(@Parameter(description = "Entrer le text à crypter", required = true) @RequestParam String text,
                                             @Parameter(description = "Clé de cryptage à utiliser", required = true) @RequestParam(required = false) String secret,
                                             @Parameter(description = "L'algorithme de cryptage à utiliser") @RequestParam(defaultValue = "PBEWITHHMACSHA512ANDAES_256") String type) {
        var result = "Problème de cryptage merci de vérifier vos données";
        try {
            var encryptor = new StandardPBEStringEncryptor();

            var config = new SimpleStringPBEConfig();
            config.setPassword(secret);
            config.setAlgorithm(type);
            config.setPoolSize(4);
            config.setIvGeneratorClassName(this.ivGeneratorClassname);
            encryptor.setConfig(config);

            result = encryptor.encrypt(text);
            if (StringUtils.isNotEmpty(result)) {
                return ResponseEntity.status(HttpStatus.OK).body(START + result + END);
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(START + result + END);
        }
    }

    /**
     * Décryptage d'un password
     */
    @Operation(
        summary = "Decryption with jasypt tool,",
        method = "GET",
        responses = {
            @ApiResponse(responseCode = "200", description = "Décryptage réussi avec succès"),
            @ApiResponse(responseCode = "404", description = "Problème durant le décryptage des données"),
            @ApiResponse(responseCode = "400", description = "Problème durant le décryptage des données"),
            @ApiResponse(content = @Content(
                schema = @Schema(implementation = String.class),
                mediaType = "application/text"))
        })
    @GetMapping("/decrypt")
    public ResponseEntity<String> decryption(@Parameter(description = "Entrer le text à décrypter", required = true) @RequestParam String text,
                                             @Parameter(description = "Secret Key To Be Used For Encryption", required = true) @RequestParam(required = false) String secret,
                                             @Parameter(description = "Enter Encryption Type | Algorithme") @RequestParam(defaultValue = "PBEWITHHMACSHA512ANDAES_256") String type) {
        var result = "Problème de décryptage merci vérifier vos données";
        try {
            var encryptor = new StandardPBEStringEncryptor();

            //Remove password decoration if exist
            text = RegExUtils.replaceFirst(text, "^ENC\\(", StringUtils.EMPTY);
            text = StringUtils.removeEnd(text, ")");

            var config = new SimpleStringPBEConfig();
            config.setPassword(secret);
            config.setAlgorithm(type);
            config.setPoolSize(4);
            config.setIvGeneratorClassName(this.ivGeneratorClassname);
            encryptor.setConfig(config);

            result = encryptor.decrypt(text);
            if (StringUtils.isNotEmpty(result)) {
                return ResponseEntity.status(HttpStatus.OK).body(START + result + END);
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(START + result + END);
        }
    }
}
