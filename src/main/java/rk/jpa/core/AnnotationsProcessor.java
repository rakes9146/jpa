package rk.jpa.core;

import rk.jpa.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationsProcessor {

    private Class entityObject;

    public Class getEntityObject() {
        return entityObject;
    }

    public void setEntityObject(Class entityObject) {
        this.entityObject = entityObject;
    }

    public AnnotationsProcessor(Class entityObject) {
        this.entityObject = entityObject;
    }

    public Map<String, List<EntityMetaDto>> getFieldsMetaData() {

        Map<String, List<EntityMetaDto>> fieldMetaDto = new LinkedHashMap<>();

        for (Field field : getFieldNameAndFieldObjMapping().values()) {
            getFieldAnnotationsKeyVal(field, entityObject, fieldMetaDto);
        }
        return fieldMetaDto;
    }

    public Map<String, Field> getFieldNameAndFieldObjMapping() {

        Map<String, Field> fieldNameFieldVal = new LinkedHashMap<>();
        if (!Objects.isNull(entityObject)) {
            Class<?> clazz = this.entityObject;
            for (Field field : clazz.getDeclaredFields()) {
                fieldNameFieldVal.put(field.getName(), field);
            }
        }
        return fieldNameFieldVal;
    }

    public void getFieldAnnotationsKeyVal(Field field, Object entityObject, Map<String, List<EntityMetaDto>> resultMap) {
        List<EntityMetaDto> list = new LinkedList<>();

        // List of all supported annotation classes
        List<Class<? extends Annotation>> supportedAnnotations = Arrays.asList(
                Column.class, OneToOne.class, OneToMany.class, ManyToOne.class,
                ManyToMany.class, Id.class, GenerationType.class, JoinColumn.class
        );

        for (Class<? extends Annotation> annotationType : supportedAnnotations) {
            processAnnotation(field, annotationType, list);
        }

        resultMap.put(field.getName(), list);
    }

    private <A extends Annotation> void processAnnotation(Field field, Class<A> annotationClass, List<EntityMetaDto> list) {
        if (field.isAnnotationPresent(annotationClass)) {
            A annotation = field.getAnnotation(annotationClass);
            EntityMetaDto dto = new EntityMetaDto();
            dto.setAnnotationName(annotationClass);

            Map<String, String> annotationData = Arrays.stream(annotation.annotationType().getDeclaredMethods())
                    .collect(Collectors.toMap(
                            method -> method.getName(),
                            method -> {
                                try {
                                    Object value = method.invoke(annotation);
                                    return value != null ? value.toString() : "";
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    ));


            dto.setAnnotatoonKeyVal(annotationData);

            list.add(dto);
        }
    }

    public Map<String, Object> getFieldNameValueMap() throws IllegalAccessException {

        Map<String, Object> map = new HashMap<>();
        if (!Objects.isNull(entityObject)) {
            Class<?> clazz = this.entityObject.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(this.entityObject));
            }
        }
        return map;
    }

    public Map<String, Class> getFieldNameAndTypeMapping() {

        Map<String, Class> fieldTypeMap = new HashMap<>();
        Class<?> clazz = this.entityObject;
        Field[] fields = clazz.getFields();
        Arrays.stream(clazz.getDeclaredFields()).forEach(sdf -> {
            sdf.setAccessible(true);
            ;
            System.out.println("Field Name " + sdf.getName() + " Get Value " + sdf.getType());
        });
        fieldTypeMap = Arrays.stream(clazz.getDeclaredFields()).collect(
                Collectors.toMap(field -> field.getName(), field -> field.getType())
        );

        return fieldTypeMap;
    }

    public String getTableName() {
        Class clazz = entityObject;
        Entity entity = (Entity) clazz.getAnnotation(Entity.class);
        return entity.name();
    }


    public Map<String, String> getEntityFieldAndDBColumnKeyMap() {

        Map<String, String> fieldColumnMapping = this.getFieldsMetaData().entrySet().stream()
                .collect(Collectors.toMap(stringListEntry -> stringListEntry.getKey(), stringListEntry -> {
                            return stringListEntry.getValue().stream()
                                    .filter(dto -> dto.getAnnotationName().getSimpleName().equals(Column.class.getSimpleName()))
                                    .map(dto -> dto.getAnnotatoonKeyVal())
                                    .map(stringListEntry1 -> stringListEntry1.get("name"))
                                    .findFirst().orElse("id");
                        }
                ));
        return fieldColumnMapping;
    }

}



