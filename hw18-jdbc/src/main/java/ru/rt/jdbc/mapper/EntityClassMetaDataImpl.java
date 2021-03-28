package ru.rt.jdbc.mapper;

import ru.rt.crm.model.Id;
import ru.rt.crm.model.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData{
    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final String tableName;
    private final List<Field> allFields;
    private final Field  idField;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz){
        this.clazz = clazz;
        this.constructor = constructor();
        this.tableName = tableName();
        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = idField();
        this.fieldsWithoutId = fieldsWithoutId();
    }

    @Override
    public <T> Constructor<T> getConstructor() {
        return (Constructor<T>) constructor;
    }

    @Override
    public String getName() {
        return tableName;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return new ArrayList<>(allFields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return new ArrayList<>(fieldsWithoutId);
    }

    private Constructor<T> constructor(){
        Constructor<T> constructor = null;
        try{
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return constructor;
    }

    private Field idField(){
        Field idField = null;
        for(Field field : allFields){
            if (field.isAnnotationPresent(Id.class)){
                field.setAccessible(true);
                idField = field;
            }
        }
        if (idField == null){
            throw new IllegalArgumentException();
        }
        return idField;
    }

    private List<Field> fieldsWithoutId(){
        List<Field> fieldsWithoutId = new ArrayList<>();
        for(Field field : allFields){
            if (!field.isAnnotationPresent(Id.class)){
                field.setAccessible(true);
                fieldsWithoutId.add(field);
            }
        }
        if (fieldsWithoutId.size() == 0){
            throw new IllegalArgumentException();
        }
        return fieldsWithoutId;
    }

    private String tableName(){
        var tableName = "";
        if (clazz.isAnnotationPresent(Table.class)){
            tableName = clazz.getAnnotation(Table.class).tableName();
        }
        if (tableName.equals("")){
            throw new IllegalArgumentException();
        }
        return tableName;
    }
}
