package com.ecoomerce.autorisation.rest.error;

import fr.grdf.softwarefactory.abj.utils.Constantes;
import fr.grdf.softwarefactory.abj.utils.MessageUtil;
import fr.grdf.softwarefactory.abj.web.rest.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.*;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static fr.grdf.softwarefactory.abj.web.rest.error.ErrorConstants.*;

/**
 * controlleur qui gère les exceptions déclenchées par les appels aux endpoints
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ExceptionHandler implements SecurityAdviceTrait, ProblemHandling {
    private final MessageUtil messages;

    /**
     * Cette Méthode permet de générer une réponse en erreur pour tout type d'exception
     * @param throwable l'exception détecté
     * @param status le statut http
     * @param type un URI absolu qui identifie le type de ce problème, si cet information n'est pas présente sa valeur sera  "about:blank"
     * @return un objet ProblemBuilder
     */
    @Override
    public ProblemBuilder prepare(
        final Throwable throwable, final StatusType status, final URI type) {

        //valeurs par défaut du code,title et détail
        String code = ERR_T_DEFAULT;

        //Cas spécifique pour gestion d'erreur fine
        if (throwable instanceof HttpMessageConversionException) {
            code = ERR_T_HTTP_CONVERSION;
        }
        if (throwable instanceof DataAccessException) {
            code = ERR_T_DATA_ACCESS;
        }
        if (containsPackageName(throwable.getMessage())) {
            code = ERR_T_RUNTIME;
        }
        if (status.getStatusCode()== HttpStatus.UNAUTHORIZED.value()) {
            code = ERR_T_ACCESS_UNAUTHORIZED;
        }
        if (status.getStatusCode()== HttpStatus.FORBIDDEN.value()) {
            code = ERR_T_ACCESS_DENIED;
        }
        String detail = messages.getMessage(code);
        String title = status.getReasonPhrase();

        //Cas d'un message d'erreur fonctionnelle
        if (throwable instanceof AppException) {
            code = ((AppException) throwable).getCode();
            detail = ((AppException) throwable).getDetail();
            title = messages.getMessage(((AppException) throwable).getTitle());
        }
        return Problem.builder()
            .withType(type)
            .withTitle(title)
            .withStatus(throwable instanceof AppException ? ((AppException) throwable).getStatus() : status )
            .withDetail(detail)
            .with(CODE_KEY, code);
    }

    /**
     * Cette méthode permet d'enrichir l'entité problem générée par la méthode prepare ci-dessus
     * @param entity entité problem
     * @param request la requête qui a provoqué une exception
     * @return une réponse en erreur qui respect la RFC 7807 + l'ajout d'un champ message
     * le body de la réponse contiendra les champs suivants :
     * type : URI identifiant le type du prblème rencontré
     * title: code de l'erreur (ça sera un code qui identifie l'emplacement de l'erreur au niveau du code)
     * detail : un message détaillé décrivant l'erreur ( à noter que pour certains exception il y aura un surcharge du message pour ne pas divulguer
                        des informations critiques comme la base de données)
     * message : un message d'erreur court (hors RFC)
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return null;
        }
        Problem problem = entity.getBody();
        if (problem == null) {
            return null;
        }

        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String requestUri = nativeRequest != null ? nativeRequest.getRequestURI() : Constantes.EMPTY_STRING;
        ProblemBuilder builder = Problem
            .builder()
            .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
            .withTitle(problem.getTitle())
            .withStatus(problem.getStatus())
            .withDetail(problem.getDetail())
            .with(CODE_KEY, problem.getParameters().get(CODE_KEY))
            .with(PATH_KEY, requestUri);
        // traiter le cas de violation de contraintes
        if (problem instanceof ConstraintViolationProblem || problem.getParameters().get(VIOLATIONS_KEY) != null) {
            builder.with(VIOLATIONS_KEY, problem.getParameters().get(VIOLATIONS_KEY));
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    /**
     * Cette Méthode permet de générer une réponse en erreur pour l'exception MethodArgumentNotValidException
     * @param ex exception MethodArgumentNotValidException
     * @param request requête http
     * @return une réponse en erreur spécifique à MethodArgumentNotValidException
     */
    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        var result = ex.getBindingResult();
        List<FieldError> fieldErrors = result
            .getFieldErrors()
            .stream()
            .map(
                f ->
                    FieldError.builder()
                        .objet(f.getObjectName().replaceFirst("Dto$", Constantes.EMPTY_STRING))
                        .champ(f.getField())
                        .message(!ObjectUtils.isEmpty(f.getDefaultMessage()) ? f.getDefaultMessage() : f.getCode())
                        .build()
            )
            .collect(Collectors.toList());
        Problem problem = Problem
            .builder()
            .withType(CONSTRAINT_VIOLATION_TYPE)
            .withTitle(TITLE_ERR_TECHNIQUE)
            .with(VIOLATIONS_KEY, fieldErrors)
            .withDetail(messages.getMessage(ERR_T_VALIDATION))
            .with(CODE_KEY, ERR_T_VALIDATION)
            .build();
        return create(ex, problem, request);
    }

    private boolean containsPackageName(String message) {
        // Ceci n'est pas la liste exhaustive
        return StringUtils.containsAny(message, "org.", "java.", "net.", "javax.", "com.", "io.", "de.", "fr.grdf.");
    }
}
