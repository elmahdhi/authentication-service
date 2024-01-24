package com.ecoomerce.autorisation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class ProblemConfiguration implements InitializingBean {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() {
        objectMapper.registerModules(
            new ProblemModule(),
            new ConstraintViolationProblemModule()
        );
    }
}
