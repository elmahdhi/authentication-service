package com.ecoomerce.autorisation.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Jasypt par défaut.
 */
@Slf4j
@Configuration
public class JasyptConfig {

    /**
     * Password d'encryptage/decryptage JASYPT.
     */
    @Value("${jasypt.encryptor.password:}")
    private String password = "";

    /**
     * L'algorithme de cryptage
     */
    @Value("${jasypt.encryptor.algorithm:PBEWithMD5AndDES}")
    private String algorithm;

    /**
     * La classe de génération.
     */
    @Value("${jasypt.encryptor.iv-generator-classname:org.jasypt.iv.NoIvGenerator}")
    private String ivGeneratorClassname;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        log.info("Configuration jasypt : jasypt.encryptor.algorithm={}; jasypt.encryptor.iv-generator-classname={}",
            this.algorithm, this.ivGeneratorClassname);
        var encryptor = new PooledPBEStringEncryptor();
        var config = new SimpleStringPBEConfig();
        config.setPassword(this.password);
        config.setAlgorithm(this.algorithm);
        config.setPoolSize(4);
        config.setIvGeneratorClassName(this.ivGeneratorClassname);
        encryptor.setConfig(config);
        return encryptor;
    }

}
