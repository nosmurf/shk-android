package com.nosmurf.data.mapper;

import com.nosmurf.data.model.AccessDto;
import com.nosmurf.domain.model.Access;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Daniel on 03/12/2016.
 */

public class AccessResponseDtoMapper implements Mapper<List<AccessDto>, List<Access>> {

    private final AccessDtoMapper accessDtoMapper;

    @Inject
    public AccessResponseDtoMapper(AccessDtoMapper accessDtoMapper) {
        this.accessDtoMapper = accessDtoMapper;
    }

    @Override
    public List<AccessDto> modelToData(List<Access> accesses) {
        // TODO: 04/12/2016
        return null;
    }

    @Override
    public List<Access> dataToModel(List<AccessDto> accessDtos) {
        List<Access> accesses = null;
        if (accessDtos != null) {
            accesses = new ArrayList<>();
            for (AccessDto accessDto : accessDtos) {
                accesses.add(accessDtoMapper.dataToModel(accessDto));
            }
        }
        return accesses;
    }
}