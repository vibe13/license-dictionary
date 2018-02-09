package org.jboss.license.dictionary.utils;

import api.LicenseRest;

import org.jboss.license.dictionary.model.License;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * mstodo: Header
 *
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com <br>
 *         Date: 11/17/17
 */
public class Mappers {

    public static final Type licenseRestListType = new TypeToken<List<LicenseRest>>() {
    }.getType();

    public static final Type licenseListType = new TypeToken<List<License>>() {
    }.getType();

    public static final ModelMapper fullMapper;
    public static final ModelMapper limitedMapper;

    static {
        limitedMapper = new ModelMapper();
        // limitedMapper.typeMap(License.class, LicenseRest.class).addMappings(mapping -> {
        // mapping.skip(License::setContent);
        // });

        fullMapper = new ModelMapper();
    }
}
