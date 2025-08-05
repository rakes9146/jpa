package rk.jpa.core;

import rk.jpa.annotations.Id;
import rk.jpa.jdbc.JpaJDBCExecutor;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class JpaQueryAndEntityConvertorService {

    AnnotationsProcessor annotationsProcessor;
    Map<String, Integer> fieldIndexMap = new HashMap<>();
    private Field fieldObj;

    public JpaQueryAndEntityConvertorService(Class clazz) {
        this.annotationsProcessor = new AnnotationsProcessor(clazz);
    }

    public String createInsertQuery() {

        //annotationsProcessor.getFieldsMetaData(). for
        return convertEntityToInsertQuery();
    }

    public String createTableQuery() {
        StringBuilder createBuider = new StringBuilder();
        createBuider.append("CREATE TABLE IF NOT EXISTS ")
                .append(annotationsProcessor.getTableName())
                .append("(");
        annotationsProcessor.getFieldsMetaData().forEach((key, val) -> {
            appendColumnNameAndTypeForCreateTable(createBuider, key, val);
        });
        createBuider.replace(createBuider.length() - 1, createBuider.length(), ");");

        return createBuider.toString();
    }


    private void appendColumnNameAndTypeForCreateTable(StringBuilder createBuilder, String fieldName, List<EntityMetaDto> entityMetaDtos) {

        entityMetaDtos.forEach(dto -> {
            String annotationType = dto.getAnnotationName().getSimpleName();
            switch (annotationType) {

                case "Id" -> {
                    createColumnAndTypeByValueForInsert(fieldName, dto, createBuilder, true);
                    createBuilder.append(" ");
                }
                case "GenerationType" -> System.out.println("No action required");
                case "Column" -> {
                    createColumnAndTypeByValueForInsert(fieldName, dto, createBuilder, false);
                    createBuilder.append(" ");
                }
                default -> throw new IllegalStateException("Unexpected value: " + dto.getAnnotationName());
            }
        });
        createBuilder.append(",");

    }

    private void createColumnAndTypeByValueForInsert(String fieldName, EntityMetaDto entityMetaDto, StringBuilder createBuilder, boolean isId) {
        Map<String, String> annotationKeyVal = entityMetaDto.getAnnotatoonKeyVal();
        if (annotationsProcessor.getFieldNameAndFieldObjMapping().get(fieldName).isAnnotationPresent(Id.class)) {
            createBuilder.append(fieldName)
                    .append(" ")
                    .append(getDataTypeMap(fieldName));

            createBuilder.append(" primary key AUTO_INCREMENT ");
        }
        annotationKeyVal.forEach((k, v) -> {
            if (k.equalsIgnoreCase("name")) {
                createBuilder.append(v)
                        .append(" ")
                        .append(getDataTypeMap(fieldName));
            }

        });
    }

//public String getColumnNameAndDataType

    public String convertEntityToInsertQuery() {

        StringBuilder insertQuery = new StringBuilder("INSERT INTO ")
                .append(annotationsProcessor.getTableName())
                .append(" (");
        StringBuilder insertQueryValues = new StringBuilder("values (");
        final int[] index = {1};
        annotationsProcessor.getEntityFieldAndDBColumnKeyMap().forEach((key, val) -> {
            if (!annotationsProcessor.getFieldNameAndFieldObjMapping().get(key).isAnnotationPresent(Id.class)) {
                insertQuery.append(val).append(",");
                insertQueryValues.append("?,");
                fieldIndexMap.put(key, index[0]);
                index[0]++;
            }
        });
        insertQuery.replace(insertQuery.length() - 1, insertQuery.length(), ") ");
        insertQueryValues.replace(insertQueryValues.length() - 1, insertQueryValues.length(), ");");
        insertQuery.append(insertQueryValues);

        return insertQuery.toString();
    }

    public String createUpdateQuery() {

        return convertEntityToUpdatQuery();
    }

    public String createDeleteQuery() {

        StringBuilder deleteQuery = new StringBuilder();
        deleteQuery.append("DELETE FROM ")
                .append(annotationsProcessor.getTableName())
                .append(" ")
                .append(" WHERE ");
        final int[] index = {0};
        annotationsProcessor.getEntityFieldAndDBColumnKeyMap().forEach((k, v) -> {
            if (annotationsProcessor.getFieldNameAndFieldObjMapping().get(k).isAnnotationPresent(Id.class)) {
                deleteQuery.append(v)
                        .append(" = ?;");
                index[0]++;

                fieldIndexMap.put(k, index[0]);
            }
        });

        return deleteQuery.toString();
    }

    public String getSelectQuery(Object id) {

        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT * FROM ")
                .append(this.annotationsProcessor.getTableName());

        if (!Objects.isNull(id)) {
            selectQuery.append(" WHERE ");
            final int[] index = {1};

            annotationsProcessor.getEntityFieldAndDBColumnKeyMap().forEach((key, val) -> {
                if (annotationsProcessor.getFieldNameAndFieldObjMapping().get(key).isAnnotationPresent(Id.class)) {
                    selectQuery.append(key).append("=").append("?");
                    fieldIndexMap.put(key, index[0]);
                    index[0]++;
                }
            });
        }
        selectQuery.append(";");


        return selectQuery.toString();
    }

    public String convertEntityToUpdatQuery() {

        StringBuilder updateQuery = new StringBuilder("UPDATE ")
                .append(annotationsProcessor.getTableName())
                .append(" SET ");
        StringBuilder whereCondition = new StringBuilder(" where ");
        final int[] index = {1};
        StringBuilder idKey = new StringBuilder();
        annotationsProcessor.getEntityFieldAndDBColumnKeyMap().forEach((key, val) -> {
            if (!annotationsProcessor.getFieldNameAndFieldObjMapping().get(key).isAnnotationPresent(Id.class)) {
                updateQuery.append(val).append("=").append("?,");
                fieldIndexMap.put(key, index[0]);
                index[0]++;

            } else {
                whereCondition.append(key).append(" = ?;");
                idKey.append(key);
            }
        });
        updateQuery.replace(updateQuery.length() - 1, updateQuery.length(), "");
        updateQuery.append(whereCondition);
        fieldIndexMap.put(idKey.toString(), index[0]++);

        return updateQuery.toString();

    }

    public Map<Integer, Object> entityFieldValueParaMapForSelect(Object object) {

        Map<String, String> ks = annotationsProcessor.getEntityFieldAndDBColumnKeyMap();

        Map<Integer, Object> classValMap = new HashMap<>();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (!field.isAnnotationPresent(Id.class))
                    classValMap.put(fieldIndexMap.get(ks.get(field.getName())), field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        return classValMap;
    }

    public Map<Integer, Object> entityFieldValueParaMapForInsert(Object object) {

        Map<String, String> ks = annotationsProcessor.getEntityFieldAndDBColumnKeyMap();

        Map<Integer, Object> classValMap = new HashMap<>();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (!field.isAnnotationPresent(Id.class))
                    classValMap.put(fieldIndexMap.get(field.getName()), field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        return classValMap;
    }

    public Map<Integer, Object> entityFieldValueParaMapForUpdate(Object object) {

        Map<String, String> ks = annotationsProcessor.getEntityFieldAndDBColumnKeyMap();

        Map<Integer, Object> classValMap = new HashMap<>();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                classValMap.put(fieldIndexMap.get(field.getName()), field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        return classValMap;
    }

    public Map<Integer, Object> entityFieldValueParaMapForSelect(Class clazz, Object primaryKey) {

        Map<String, String> ks = annotationsProcessor.getEntityFieldAndDBColumnKeyMap();

        Map<Integer, Object> classValMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            //set it false just addded for slelect query
            if (field.isAnnotationPresent(Id.class))
                classValMap.put(fieldIndexMap.get(field.getName()), primaryKey);

        }

        return classValMap;
    }

    public Map<Integer, Object> entityFieldValueParaMapForDelete(Object object) {

        Map<String, String> ks = annotationsProcessor.getEntityFieldAndDBColumnKeyMap();

        Map<Integer, Object> classValMap = new HashMap<>();
        Class clazz = annotationsProcessor.getEntityObject();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                if (field.isAnnotationPresent(Id.class)) {
                    if (object instanceof Integer idVal) {
                        classValMap.put(fieldIndexMap.get(field.getName()), idVal);
                    } else {
                        field.setAccessible(true);
                        classValMap.put(fieldIndexMap.get(field.getName()), field.get(object));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        return classValMap;
    }

    public String getDataTypeMap(String name) {

        Map<String, Class> fieldTypeMap = annotationsProcessor.getFieldNameAndTypeMapping();
        Class getType = fieldTypeMap.get(name);
        Map<Class, String> classDataMap = Map.of(Integer.class, "int", String.class, "varchar(200)", Double.class, "decimal",
                LocalDate.class, "date", LocalDateTime.class, "timestamp", Long.class, "int", Float.class, "decimal");

        return classDataMap.get(getType);
    }

    public <T> List<T> convertResultSetToEntityList(Class clazz, ResultSet resultSet) {

        List<T> entityList = getEntityFromDb(clazz, resultSet);
        return entityList;
    }

    public <T> T convertEntityToSingleResult(Class clazz, ResultSet resultSet) {

        T singleEntity = null;

        List<T> entityList = getEntityFromDb(clazz, resultSet);
        if (!entityList.isEmpty()) {
            singleEntity = entityList.get(0);
        }
        return singleEntity;
    }

    public <T> List<T> getEntityFromDb(Class clazz, ResultSet resultSet) {
        List<T> entityList = new LinkedList<T>();
        Object obj = null;
        try {
            Map<String, List<EntityMetaDto>> fieldColumnMapping = annotationsProcessor.getFieldsMetaData();
            if (!resultSet.isBeforeFirst()) {
            }

            if (resultSet.next()) {
                do {
                    obj = clazz.newInstance();
                    for (Field fieldObj : obj.getClass().getDeclaredFields()) {
                        fieldObj.setAccessible(true);
                        String fieldName = fieldObj.getName();
                        try {
                            T value = (T) resultSet.getObject(annotationsProcessor.getEntityFieldAndDBColumnKeyMap().get(fieldName));
                            value = JpaJDBCExecutor.convertDataType(value, fieldObj);
                            fieldObj.set(obj, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {

                            throw new RuntimeException(e);
                        }
                    }
                    entityList.add((T) obj);
                } while (resultSet.next());
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entityList;
    }

}
