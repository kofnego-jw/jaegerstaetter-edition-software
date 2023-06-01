package at.kulinz.jaegerstaetter.datamodel.existdb;

import at.kulinz.jaegerstaetter.datamodel.DataModelException;

public interface ExistDavRepository {

    void delete(String dirPath) throws DataModelException;

    void deleteAll() throws DataModelException;

    void init() throws DataModelException;

    void mkdirs(String dirPath) throws DataModelException;
}
