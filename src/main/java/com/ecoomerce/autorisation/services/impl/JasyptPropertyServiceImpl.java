package com.ecoomerce.autorisation.services.impl;

import com.ecoomerce.autorisation.services.JasyptPropertyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JasyptPropertyServiceImpl implements JasyptPropertyService {

    @Value("${jasypt.encryptor.iv-generator-classname:org.jasypt.iv.NoIvGenerator}")
    private String ivGeneratorClassname;


    private static final String START = "{\"result\" : \"";
    private static final String END = "\"}";


    @Override
    public String encrypt(String text, String secret, String algo) {
        var result = "Problème de cryptage merci de vérifier vos données";
        try {

            var encryptor = creatEncryptor(text, algo, secret);

            result = encryptor.encrypt(text);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return START + result + END;
        }
    }

    @Override
    public String decrypt(String text, String secret, String algo) {
        var result = "Problème de décryptage merci vérifier vos données";
        try {
            //Remove password decoration if exist
            text = RegExUtils.replaceFirst(text, "^ENC\\(", StringUtils.EMPTY);
            text = StringUtils.removeEnd(text, ")");

            var encryptor = creatEncryptor(text, algo, secret);

            result = encryptor.decrypt(text);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return START + result + END;
        }
    }

    private StandardPBEStringEncryptor creatEncryptor(String password, String algo, String secret){
        var config = new SimpleStringPBEConfig();
        config.setPassword(secret);
        config.setAlgorithm(algo);
        config.setPoolSize(4);
        config.setIvGeneratorClassName(this.ivGeneratorClassname);
        var encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }
}
