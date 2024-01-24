package com.ecoomerce.autorisation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Propriété spécifique à l'application
 * <p>
 * Ces propriétés sont configurés dans le fichier {@code application.yml}.
 */
@ConfigurationProperties(prefix = "abj", ignoreUnknownFields = false)
@Getter
@Setter
public class ApplicationProperties {

    private final App app = new App();
    private final WebSSO webSSO = new WebSSO();
    private final Authentication authentication = new Authentication();
    private final Security security = new Security();
    private final CorsConfiguration cors = new CorsConfiguration();
    private final Batch batch = new Batch();
    private final Cache cache = new Cache();
    private final Jasypt jasypt = new Jasypt();

    @Getter
    @Setter
    public static class App {

        private String name;
        private String version;
    }

    @Getter
    @Setter
    public static class Authentication {

        private Header headers;
    }

    @Getter
    @Setter
    public static class Header {

        private String userId;
        private String firstName;
        private String lastName;
        private String email;
        private String groups;
        private String authorization;
        private String authorizationPrefix;
        private String authorizationSecret;
        private Long tokenExpiration;
    }

    @Getter
    @Setter
    public static class WebSSO {

        private boolean mock;

        private String mockedUser;
    }

    @Getter
    @Setter
    public static class Batch {

        private Maia maia;
    }

    @Getter
    @Setter
    public static class Maia {

        private String ressourceDirectory;
        private String archiveDirectory;
        private RotationPolicy rotationPolicy;
        private int chunkSize;
        private SkipPolicy skipPolicy;
    }

    public enum RotationPolicy {
        NONE, DELETE, ARCHIVE
    }

    @Getter
    @Setter
    public static class SkipPolicy {

        private boolean enabled;
        private int skipCount;
    }

    @Getter
    @Setter
    public static class Security {

        private boolean mock;

        private String mockedUser;
    }

    @Getter
    @Setter
    public static class Cache {

        private final Ehcache ehcache = new Ehcache();

        @Getter
        @Setter
        public static class Ehcache {

            private int timeToLiveSeconds = 3600; // 1 hour

            private long maxEntries = 100;

        }

    }

    @Getter
    @Setter
    public static class Jasypt {
        private final Encryptor encryptor = new Encryptor();

        @Getter
        @Setter
        public static class Encryptor{

            private String algorithm;
            private String ivGeneratorClassname;
            private String password;
        }
    }

}
