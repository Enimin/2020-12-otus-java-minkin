package ru.rt.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{
    private final EntityClassMetaData entityMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityMetaData){
        this.entityMetaData = entityMetaData;
    }

    @Override
    public String getSelectAllSql() {
        var tableName = entityMetaData.getName();
        return String.format("SELECT * FROM %s", tableName);
    }

    @Override
    public String getSelectByIdSql() {
        var tableName = entityMetaData.getName();
        var idField = entityMetaData.getIdField().getName();
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, idField);
    }

    @Override
    public String getInsertSql() {
        var tableName = entityMetaData.getName();
        var tableFields = entityMetaData.getFieldsWithoutId()
                                               .stream()
                                               .map(Field::getName)
                                               .collect(Collectors.joining(", "));
        var fieldMarker = tableFields.replaceAll("\\w+","?");
        return String.format("INSERT INTO %s(%s) VALUES (%s)", tableName, tableFields, fieldMarker);
    }

    @Override
    public String getUpdateSql() {
        var tableName = entityMetaData.getName();
        var tableFields = entityMetaData.getFieldsWithoutId()
                                            .stream()
                                            .map(field -> field.getName() + " = ?")
                                            .collect(Collectors.joining(", "));
        var idField = entityMetaData.getIdField().getName();
        return String.format("UPDATE %s SET %s WHERE %s = ?", tableName, tableFields, idField);
    }
}
