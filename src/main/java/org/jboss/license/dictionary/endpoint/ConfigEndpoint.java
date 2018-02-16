package org.jboss.license.dictionary.endpoint;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.license.dictionary.RestApplication;
import org.jboss.license.dictionary.config.KeycloakConfig;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com <br>
 *         Date: 11/21/17
 */
@Path(RestApplication.REST_VERS_1 + RestApplication.CONFIG_ENDPOINT)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigEndpoint {

    @Inject
    @ConfigurationValue("keycloak.url")
    private String keycloakUrl;

    @Inject
    @ConfigurationValue("keycloak.realm")
    private String keycloakRealm;

    @Inject
    @ConfigurationValue("keycloak.uiClientId")
    private String keycloakClientId;

    @GET
    @Path("keycloak-config")
    public KeycloakConfig getKeycloakConfig() {
        KeycloakConfig config = new KeycloakConfig();
        config.setClientId(keycloakClientId);
        config.setRealm(keycloakRealm);
        config.setUrl(keycloakUrl);
        return config;
    }
}
