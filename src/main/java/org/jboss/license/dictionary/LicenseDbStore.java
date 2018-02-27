package org.jboss.license.dictionary;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.license.dictionary.model.License;
import org.jboss.license.dictionary.model.LicenseAlias;
import org.jboss.license.dictionary.model.LicenseApprovalStatus;
import org.jboss.logging.Logger;

/**
 * mstodo: Header
 *
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com <br>
 *         Date: 11/17/17
 */
@ApplicationScoped
public class LicenseDbStore {

    private static final Logger log = Logger.getLogger(LicenseDbStore.class);

    @PersistenceContext
    private EntityManager entityManager;

    public License save(License license) {
        log.debug("Saving license: " + license);
        entityManager.persist(license);
        return license;
    }

    public License update(License license) {
        log.debug("Updating license: " + license);
        license = entityManager.merge(license);
        return license;
    }

    public License getLicense(Integer id) {
        log.debugf("Get license by id:  %d", id);
        return entityManager.find(License.class, id);
    }

    public List<License> getAllLicense() {
        log.debug("Get all license ...");
        return entityManager.createQuery("SELECT e FROM License e", License.class).getResultList();
    }

    public boolean deleteLicense(Integer id) {
        log.debug("Deleting license: " + id);
        License entity = entityManager.find(License.class, id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }

    public LicenseApprovalStatus getLicenseApprovalStatus(Integer id) {
        log.debugf("Get licenseApprovalStatus by id:  %d", id);
        return entityManager.find(LicenseApprovalStatus.class, id);
    }

    public List<LicenseApprovalStatus> getAllLicenseApprovalStatus() {
        log.debugf("Get get all license approval status ...");
        return entityManager.createQuery("SELECT e FROM LicenseApprovalStatus e", LicenseApprovalStatus.class).getResultList();
    }

    public LicenseAlias saveLicenseAlias(LicenseAlias licenseAlias) {
        log.debug("Saving licenseAlias: " + licenseAlias);
        entityManager.persist(licenseAlias);
        return licenseAlias;
    }

    public void replaceAllLicensesWith(Collection<License> licenses) {
        getAllLicense().forEach(entityManager::remove);
        licenses.forEach(entity -> {
            try {
                entityManager.persist(entity);
            } catch (Exception exc) {
                log.error(exc);
            }
        });
    }

    public LicenseApprovalStatus saveLicenseApprovalStatus(LicenseApprovalStatus licenseApprovalStatus) {
        log.debug("Saving licenseApprovalStatus: " + licenseApprovalStatus);
        entityManager.persist(licenseApprovalStatus);
        return licenseApprovalStatus;
    }
}
