package com.nosmurf.data.mapper;

public interface Mapper<Data, Model> {

    Data modelToData(Model model);

    Model dataToModel(Data data);

}
