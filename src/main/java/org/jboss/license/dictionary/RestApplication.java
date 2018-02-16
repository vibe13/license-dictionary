package org.jboss.license.dictionary;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jboss.license.dictionary.endpoint.ConfigEndpoint;
import org.jboss.license.dictionary.endpoint.ExportEndpoint;
import org.jboss.license.dictionary.endpoint.ImportEndpoint;
import org.jboss.license.dictionary.endpoint.LicenseEndpoint;
import org.jboss.license.dictionary.endpoint.LicenseStatusEndpoint;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com <br>
 *         Date: 11/10/17
 */
@ApplicationPath("/rest")
public class RestApplication extends Application {

    /*
     * KEEP IN SYNC WITH rest-config.service.ts
     */

    public static final String REST_VERS_1 = "/v1";

    public static final String CONFIG_ENDPOINT = "/config";
    public static final String EXPORT_ENDPOINT = "/export";
    public static final String IMPORT_ENDPOINT = "/import";
    public static final String LICENSE_STATUS_ENDPOINT = "/license-status";
    public static final String LICENSE_ENDPOINT = "/license";

    public static final String IMPORT_ENDPOINT_IMPORT_LICENSE_API = "/licenses";
    public static final String IMPORT_ENDPOINT_IMPORT_LICENSE_ALIAS_API = "/licenses-alias";

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        addEndpoints(resources);
        return resources;
    }

    private void addEndpoints(Set<Class<?>> resources) {
        resources.add(ConfigEndpoint.class);
        resources.add(ImportEndpoint.class);
        resources.add(ExportEndpoint.class);
        resources.add(LicenseStatusEndpoint.class);
        resources.add(LicenseEndpoint.class);
    }
}
