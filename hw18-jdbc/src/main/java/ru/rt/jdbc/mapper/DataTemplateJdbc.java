package ru.rt.jdbc.mapper;

import ru.rt.core.repository.DataTemplate;
import ru.rt.core.repository.DataTemplateException;
import ru.rt.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection,
                                        entitySQLMetaData.getSelectByIdSql(),
                                        List.of(id),
                                        this::getObject);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection,
                                        entitySQLMetaData.getSelectAllSql(),
                                        Collections.emptyList(),
                                        this::getObjects)
                         .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T obj) {
        try {
            return dbExecutor.executeStatement(connection,
                                               entitySQLMetaData.getInsertSql(),
                                               getFieldValues(obj));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T obj) {
        try {
            dbExecutor.executeStatement(connection,
                                        entitySQLMetaData.getUpdateSql(),
                                        getFieldValues(obj));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T getObject(ResultSet resultSet){
        try {
            if (resultSet.next()) {
                return createInstance(resultSet);
            }
            return null;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | SQLException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<T> getObjects(ResultSet resultSet){
        var objList = new ArrayList<T>();
        try {
            while (resultSet.next()) {
                objList.add(createInstance(resultSet));
            }
            return objList;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | SQLException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldValues(T obj){
        List<Object> values = new ArrayList<>();
        try {
            for (Field field : entityClassMetaData.getFieldsWithoutId()){
                    values.add(field.get(obj));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return values;
    }

    private T createInstance(ResultSet resultSet) throws IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        var metaData = resultSet.getMetaData();
        var obj = entityClassMetaData.getConstructor().newInstance();
        var fields = entityClassMetaData.getAllFields();
        for (Field field : fields){
            var value = resultSet.getObject(field.getName());
            field.set(obj, value);
            field.setAccessible(false);
        }
        return (T) obj;
    }



}
