package org.jboss.license.dictionary.license;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.license.dictionary.LicenseStatusStore;
import org.jboss.license.dictionary.LicenseStore;
import org.jboss.license.dictionary.config.KeycloakConfig;
import org.jboss.license.dictionary.endpoint.LicenseEndpoint;
import org.jboss.license.dictionary.endpoint.LicenseStatusEndpoint;
import org.jboss.license.dictionary.imports.RhLicense;
import org.jboss.license.dictionary.model.License;
import org.jboss.license.dictionary.model.LicenseAlias;
import org.jboss.license.dictionary.model.LicenseApprovalStatus;
import org.jboss.license.dictionary.model.LicenseDeterminationType;
import org.jboss.license.dictionary.model.LicenseHintType;
import org.jboss.license.dictionary.model.Project;
import org.jboss.license.dictionary.model.ProjectVersion;
import org.jboss.license.dictionary.model.ProjectVersionLicense;
import org.jboss.license.dictionary.model.ProjectVersionLicenseCheck;
import org.jboss.license.dictionary.model.ProjectVersionLicenseHint;
import org.jboss.license.dictionary.utils.BadRequestException;
import org.jboss.license.dictionary.utils.ErrorDto;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import api.LicenseAliasRest;
import api.LicenseApprovalStatusRest;
import api.LicenseDeterminationTypeRest;
import api.LicenseHintTypeRest;
import api.LicenseRest;
import api.ProjectRest;
import api.ProjectVersionLicenseCheckRest;
import api.ProjectVersionLicenseHintRest;
import api.ProjectVersionLicenseRest;
import api.ProjectVersionRest;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com <br>
 *         Date: 11/3/17
 */
@RunWith(Arquillian.class)
public class LicenseResourceTest {

    public static final String MY_LICENSE_URL = "http://example.com/license/text.txt";
    public static final String MY_LICENSE_NAME = "mylicense";

    @Inject
    private LicenseEndpoint licenseResource;

    @Inject
    private LicenseStatusEndpoint licenseStatusResource;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class).addPackage(License.class.getPackage())
                .addPackage(LicenseAlias.class.getPackage()).addPackage(LicenseApprovalStatus.class.getPackage())
                .addPackage(LicenseDeterminationType.class.getPackage()).addPackage(LicenseHintType.class.getPackage())
                .addPackage(Project.class.getPackage()).addPackage(ProjectVersion.class.getPackage())
                .addPackage(ProjectVersionLicense.class.getPackage()).addPackage(ProjectVersionLicenseCheck.class.getPackage())
                .addPackage(ProjectVersionLicenseHint.class.getPackage())

                .addPackage(LicenseRest.class.getPackage()).addPackage(LicenseAliasRest.class.getPackage())
                .addPackage(LicenseApprovalStatusRest.class.getPackage())
                .addPackage(LicenseDeterminationTypeRest.class.getPackage()).addPackage(LicenseHintTypeRest.class.getPackage())
                .addPackage(ProjectRest.class.getPackage()).addPackage(ProjectVersionRest.class.getPackage())
                .addPackage(ProjectVersionLicenseRest.class.getPackage())
                .addPackage(ProjectVersionLicenseCheckRest.class.getPackage())
                .addPackage(ProjectVersionLicenseHintRest.class.getPackage())

                .addPackage(LicenseEndpoint.class.getPackage()).addPackage(LicenseStore.class.getPackage())
                .addPackage(LicenseStatusEndpoint.class.getPackage()).addPackage(LicenseStatusStore.class.getPackage())
                .addPackage(KeycloakConfig.class.getPackage())

                .addPackage(ErrorDto.class.getPackage()).addPackage(RhLicense.class.getPackage())
                .addPackage(BadRequestException.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml").addAsResource("project-test.yml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        PomEquippedResolveStage stage = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies();
        File[] libs = stage.resolve().withTransitivity().asFile();

        return webArchive.addAsLibraries(libs);
    }

    @CreateSwarm
    public static Swarm newContainer() throws Exception {
        return new Swarm().withProfile("test");
    }

    @Before
    public void setUp() {

        LicenseApprovalStatusRest APPROVED = LicenseApprovalStatusRest.Builder.newBuilder()
                .name(LicenseApprovalStatusRest.APPROVED.getName()).build();
        LicenseApprovalStatusRest NOT_APPROVED = LicenseApprovalStatusRest.Builder.newBuilder()
                .name(LicenseApprovalStatusRest.NOT_APPROVED.getName()).build();
        LicenseApprovalStatusRest UNKNOWN = LicenseApprovalStatusRest.Builder.newBuilder()
                .name(LicenseApprovalStatusRest.UNKNOWN.getName()).build();

        APPROVED = licenseStatusResource.addLicenseApprovalStatusRest(APPROVED);
        NOT_APPROVED = licenseStatusResource.addLicenseApprovalStatusRest(NOT_APPROVED);
        UNKNOWN = licenseStatusResource.addLicenseApprovalStatusRest(UNKNOWN);

        if (getLicenses(null, null, null, null, MY_LICENSE_NAME).isEmpty()) {
            LicenseRest license = LicenseRest.Builder.newBuilder().code(MY_LICENSE_NAME).fedoraName(MY_LICENSE_NAME)
                    .url(MY_LICENSE_URL).licenseApprovalStatus(APPROVED).build();

            license = licenseResource.addLicense(license);

            license.addAlias(null, "mylicense 1.0", license.getId());
            license.addAlias(null, "mylicense 1", license.getId());

            license = licenseResource.updateLicense(license.getId(), license);
        }
    }

    @Test
    public void shouldGetLicenseByName() {
        LicenseRest mylicense = getLicenses(MY_LICENSE_NAME, null, null, null, null).iterator().next();
        assertThat(mylicense).isNotNull();
        assertThat(mylicense.getUrl()).isEqualTo(MY_LICENSE_URL);
    }

    private List<LicenseRest> getLicenses(String fedoraName, String spdxName, String code, String nameAlias,
            String searchTerm) {
        return (List<LicenseRest>) licenseResource.getLicenses(fedoraName, spdxName, code, nameAlias, searchTerm, null, null)
                .getEntity();
    }

    @Test
    public void shouldGetLicenseByExactSearchTerm() {
        Collection<LicenseRest> licenses = getLicenses(null, null, null, null, "mylicense");
        assertThat(licenses).hasSize(1);
        LicenseRest mylicense = licenses.iterator().next();
        assertThat(mylicense).isNotNull();
        assertThat(mylicense.getUrl()).isEqualTo(MY_LICENSE_URL);
    }

    @Test
    public void shouldGetLicenseBySubstringSearchTerm() {
        Collection<LicenseRest> licenses = getLicenses(null, null, null, null, "ylicense");
        assertThat(licenses).hasSize(1);
        LicenseRest mylicense = licenses.iterator().next();
        assertThat(mylicense).isNotNull();
        assertThat(mylicense.getUrl()).isEqualTo(MY_LICENSE_URL);
    }

    @Test
    public void shouldGetLicenseById() {
        LicenseApprovalStatusRest APPROVED = licenseStatusResource
                .getLicenseApprovalStatusRest(LicenseApprovalStatusRest.APPROVED.getId());
        LicenseRest license = LicenseRest.Builder.newBuilder().fedoraName("licenseReadById").url("by-id.example.com")
                .code("licenseReadById").licenseApprovalStatus(APPROVED).build();
        
        license = licenseResource.addLicense(license);
        
        license.addAlias(null, "alias1", license.getId());
        license.addAlias(null, "alias2", license.getId());
        
        license = licenseResource.updateLicense(license.getId(), license);
        

        LicenseRest resultLicense = licenseResource.getLicense(license.getId());
        assertThat(resultLicense.getFedoraName()).isEqualTo(license.getFedoraName());
        assertThat(resultLicense.getUrl()).isEqualTo(license.getUrl());
        assertThat(resultLicense.getAliasNames()).hasSize(2);
        assertThat(resultLicense.getAliasNames()).containsExactlyInAnyOrder("alias1", "alias2");
    }
}