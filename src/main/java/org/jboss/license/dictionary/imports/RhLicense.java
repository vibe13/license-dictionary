package org.jboss.license.dictionary.imports;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jboss.license.dictionary.model.License;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import api.LicenseApprovalStatusRest;
import api.LicenseRest;
import lombok.Data;

/**
 * mstodo: Header
 *
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com <br>
 *         Date: 11/13/17
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RhLicense {

    // private Set<LicenseAliasRest> aliases;

    private String id;

    private String url;
    private String spdx_license_url;
    private String license_text_url;

    private String fedora_abbrev;
    private String fedora_name;

    private String spdx_name;
    private String spdx_abbrev;

    private String approved;

    public LicenseRest toLicenseRest() {

        String code = firstNotBlank(fedora_abbrev, spdx_abbrev);
        code = code == null ? "-" + id : code;

        code = UUID.randomUUID().toString();

        LicenseRest entity = LicenseRest.Builder.newBuilder().code(code).fedoraName(fedora_name)
                .fedoraAbbreviation(fedora_abbrev).spdxName(spdx_name).spdxAbbreviation(spdx_abbrev).url(url)
                .textUrl(license_text_url).licenseApprovalStatus(LicenseApprovalStatusRest.restEntityFromJsonString(approved))
                .build();

        // TO BE REMOVED AFTER JSON FIXES
        entity = fixLicenseRestExceedingFields(entity);

        // entity.setSpdxUrl(spdx_license_url);

        return entity;
    }

    public String getName(String alias) {
        String name = firstNotBlank(fedora_name, spdx_name);
        return name != null && !name.trim().isEmpty() ? name : alias;
    }

    private static String firstNotBlank(String... values) {
        return Arrays.stream(values).filter(StringUtils::isNotBlank).findFirst().orElse(null);
    }

    public boolean isValid() {
        boolean valid = true;

        if (StringUtils.isBlank(fedora_name) && StringUtils.isBlank(spdx_name)) {
            System.out.println("***" + id + ": Fedora and SPDX names are empty");
            valid = false;
        }
        if (StringUtils.isNotBlank(fedora_name) && StringUtils.isNotBlank(fedora_abbrev)
                && StringUtils.length(fedora_abbrev) > License.ABBREV_MAX_LENGHT) {
            System.out.println("***" + id + ": Fedora abbrev exceeds " + License.ABBREV_MAX_LENGHT + " chars");
            valid = false;
        }
        if (StringUtils.isNotBlank(spdx_name) && StringUtils.isNotBlank(spdx_abbrev)
                && StringUtils.length(spdx_abbrev) > License.ABBREV_MAX_LENGHT) {
            System.out.println("***" + id + ": SPDX abbrev exceeds " + License.ABBREV_MAX_LENGHT + " chars");
            valid = false;
        }
        return valid;
    }

    private LicenseRest fixLicenseRestExceedingFields(LicenseRest licenseRest) {
        licenseRest.setCode(
                licenseRest.getCode().substring(0, Math.min(License.ABBREV_MAX_LENGHT, licenseRest.getCode().length())));
        licenseRest.setFedoraAbbreviation(licenseRest.getFedoraAbbreviation().substring(0,
                Math.min(License.ABBREV_MAX_LENGHT, licenseRest.getFedoraAbbreviation().length())));
        licenseRest.setSpdxAbbreviation(licenseRest.getSpdxAbbreviation().substring(0,
                Math.min(License.ABBREV_MAX_LENGHT, licenseRest.getSpdxAbbreviation().length())));
        return licenseRest;
    }

    public static RhLicense fromLicenseRest(LicenseRest licenseRest) {

        return RhLicense.Builder.newBuilder().id(licenseRest.getId()).fedora_abbrev(licenseRest.getFedoraAbbreviation())
                .fedora_name(licenseRest.getFedoraName()).spdx_abbrev(licenseRest.getSpdxAbbreviation())
                .spdx_name(licenseRest.getSpdxName()).url(licenseRest.getUrl()).license_text_url(licenseRest.getTextUrl())
                .approved(LicenseApprovalStatusRest.jsonFromRestEntity(licenseRest.getLicenseApprovalStatus())).build();
    }

    public static class Builder {

        private String id;
        private String url;
        private String spdx_license_url;
        private String license_text_url;
        private String fedora_abbrev;
        private String fedora_name;
        private String spdx_name;
        private String spdx_abbrev;
        private String approved;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(Integer id) {
            this.id = id.toString();
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder spdx_license_url(String spdx_license_url) {
            this.spdx_license_url = spdx_license_url;
            return this;
        }

        public Builder license_text_url(String license_text_url) {
            this.license_text_url = license_text_url;
            return this;
        }

        public Builder fedora_abbrev(String fedora_abbrev) {
            this.fedora_abbrev = fedora_abbrev;
            return this;
        }

        public Builder fedora_name(String fedora_name) {
            this.fedora_name = fedora_name;
            return this;
        }

        public Builder spdx_name(String spdx_name) {
            this.spdx_name = spdx_name;
            return this;
        }

        public Builder spdx_abbrev(String spdx_abbrev) {
            this.spdx_abbrev = spdx_abbrev;
            return this;
        }

        public Builder approved(String approved) {
            this.approved = approved;
            return this;
        }

        public RhLicense build() {
            RhLicense rhLicense = new RhLicense();
            rhLicense.setId(id);
            rhLicense.setFedora_name(fedora_name);
            rhLicense.setFedora_abbrev(fedora_abbrev);
            rhLicense.setSpdx_name(spdx_name);
            rhLicense.setSpdx_abbrev(spdx_abbrev);
            rhLicense.setUrl(url);
            rhLicense.setLicense_text_url(license_text_url);
            rhLicense.setSpdx_license_url(spdx_license_url);
            rhLicense.setApproved(approved);

            return rhLicense;
        }
    }

    /*
     * "3dfx Glide License": { "approved": "yes", "fedora_abbrev": "Glide", "fedora_name": "3dfx Glide License", "id": "1",
     * "license_text_url": "http://rcm-guest.app.eng.bos.redhat.com/rcm-guest/staging/avibelli/licenses/Glide.txt",
     * "spdx_abbrev": "Glide", "spdx_license_url": "https://spdx.org/licenses/Glide.html#licenseText", "spdx_name":
     * "3dfx Glide License", "url": "http://www.users.on.net/~triforce/glidexp/COPYING.txt" }
     */
}
