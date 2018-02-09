package org.jboss.license.dictionary;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.license.dictionary.model.License;
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

    public List<License> getAll() {
        log.debug("Get all licenses ...");
        return entityManager.createQuery("SELECT e FROM License e", License.class).getResultList();
    }

    public boolean delete(Integer licenseId) {
        log.debug("Deleting license: " + licenseId);
        License entity = entityManager.find(License.class, licenseId);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }

    public void replaceAllLicensesWith(Collection<License> entities) {
        getAll().forEach(entityManager::remove);
        entities.forEach(entityManager::persist);
    }
}
