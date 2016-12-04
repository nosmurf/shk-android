package com.nosmurf.data.mapper;

import com.nosmurf.data.model.AccessDto;
import com.nosmurf.domain.model.Access;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Daniel on 04/12/2016.
 */

public class AccessDtoMapper implements Mapper<AccessDto, Access> {

    @Inject
    public AccessDtoMapper() {
        super();
    }

    @Override
    public AccessDto modelToData(Access access) {
        return null;
        // TODO: 03/12/2016
    }

    @Override
    public Access dataToModel(AccessDto accessDto) {
        Access access = null;
        if (accessDto != null) {
            access = new Access();
            access.setDisplayName(null);
            access.setDate(new Date(accessDto.getDatetime()));
            access.setNfc(accessDto.isNfc());
            access.setFace(accessDto.isFace());
        }
        return access;
    }
}

