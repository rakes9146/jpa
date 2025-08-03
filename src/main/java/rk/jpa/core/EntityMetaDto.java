package rk.jpa.core;

import java.lang.annotation.Annotation;
import java.util.Map;

public class EntityMetaDto {

    private Class<? extends Annotation> annotationName;
    private Map<String, String> annotatoonKeyVal;
    private Map<String,String> entityFieldDbCoumnMap;
    public EntityMetaDto() {
    }

    public EntityMetaDto(Map<String, String> annotatoonKeyVal, Class<? extends Annotation> annotationName) {
        this.annotatoonKeyVal = annotatoonKeyVal;
        this.annotationName = annotationName;
    }

    public Class<? extends Annotation> getAnnotationName() {
        return annotationName;
    }

    public void setAnnotationName(Class<? extends Annotation> annotationName) {
        this.annotationName = annotationName;
    }

    public Map<String, String> getAnnotatoonKeyVal() {
        return annotatoonKeyVal;
    }

    public void setAnnotatoonKeyVal(Map<String, String> annotatoonKeyVal) {
        this.annotatoonKeyVal = annotatoonKeyVal;
    }

    public Map<String, String> getEntityFieldDbCoumnMap() {
        return entityFieldDbCoumnMap;
    }

    public void setEntityFieldDbCoumnMap(Map<String, String> entityFieldDbCoumnMap) {
        this.entityFieldDbCoumnMap = entityFieldDbCoumnMap;
    }
}
