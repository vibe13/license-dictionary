package org.jboss.license.dictionary.endpoint;

import org.jboss.license.dictionary.LicenseStore;
import org.jboss.license.dictionary.RestApplication;
import org.jboss.license.dictionary.imports.RhLicense;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * mstodo: Header
 *
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com <br>
 *         Date: 11/21/17
 */
@Path(RestApplication.REST_VERS_1 + RestApplication.EXPORT_ENDPOINT)
public class ExportEndpoint {

    @Inject
    private LicenseStore store;

    @GET
    @Path("/licenses")
    public Map<String, RhLicense> exportLicenses() {
        Map<String, RhLicense> resultMap = new HashMap<>();
        /*
         * store.getAll().forEach(license -> license.getNameAliases().forEach( alias -> resultMap.put(alias,
         * license.toRhLicense()) ) );
         */
        return resultMap;
    }
}
