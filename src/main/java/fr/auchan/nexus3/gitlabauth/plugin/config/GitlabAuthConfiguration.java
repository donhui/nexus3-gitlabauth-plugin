package fr.auchan.nexus3.gitlabauth.plugin.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
@Named
public class GitlabAuthConfiguration {
    private static final String CONFIG_FILE = "gitlabauth.properties";

    private static final Duration DEFAULT_PRINCIPAL_CACHE_TTL = Duration.ofMinutes(1);

    private static final String DEFAULT_GITLAB_URL = "https://gitlab.com";

    private static final String GITLAB_API_URL_KEY = "gitlab.api.url";

    private static final String NEXUS_DEFAULT_ROLES = "nexus.defaultRoles";

    private static final String GITLAB_PRINCIPAL_CACHE_TTL_KEY = "gitlab.principal.cache.ttl";

    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabAuthConfiguration.class);

    private Properties configuration;

    @PostConstruct
    public void init() {
        configuration = new Properties();

        try {
            configuration.load(Files.newInputStream(Paths.get(".", "etc", CONFIG_FILE)));
        } catch (IOException e) {
            LOGGER.warn("Error reading gitlab oauth properties, falling back to default configuration", e);
        }
    }

    public String getGitlabApiUrl() {
        return configuration.getProperty(GITLAB_API_URL_KEY, DEFAULT_GITLAB_URL);
    }

    public Set<String> getDefaultRoles() {
        String defaultRoles = configuration.getProperty(NEXUS_DEFAULT_ROLES);
        String [] defaultRolesList = defaultRoles.split(",");
        Set<String> defaultRolesSet = new HashSet<>(Arrays.asList(defaultRolesList));
        return defaultRolesSet;
    }

    public Duration getPrincipalCacheTtl() {
        return Duration.parse(configuration.getProperty(GITLAB_PRINCIPAL_CACHE_TTL_KEY, DEFAULT_PRINCIPAL_CACHE_TTL.toString()));
    }
}