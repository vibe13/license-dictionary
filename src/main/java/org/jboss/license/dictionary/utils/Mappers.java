package org.jboss.license.dictionary.utils;

import java.lang.reflect.Type;
import java.util.List;

import org.jboss.license.dictionary.model.License;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import api.LicenseRest;

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

    public static final ModelMapper limitedMapper;
    public static final ModelMapper fullMapper;

    static {
        limitedMapper = new ModelMapper();
        fullMapper = new ModelMapper();
    }
}
