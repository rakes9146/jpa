package rk.jpa.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AnnotationProcessorTestWithPrint {

    @Test
    public void testAllFieldAnnotationsAndPrintValues() {
        User user = new User();
        AnnotationsProcessor processor = new AnnotationsProcessor(User1.class);

        Map<String, List<EntityMetaDto>> result = processor.getFieldsMetaData();
        assertNotNull(result, "Annotation map should not be null");

        for (Map.Entry<String, List<EntityMetaDto>> entry : result.entrySet()) {
            String fieldName = entry.getKey();
            List<EntityMetaDto> annotations = entry.getValue();

            System.out.println("📍 Field: " + fieldName);
            for (EntityMetaDto meta : annotations) {
                System.out.println("  🔹 Annotation: " + meta.getAnnotationName());
                if (meta.getAnnotatoonKeyVal() != null && !meta.getAnnotatoonKeyVal().isEmpty()) {
                    System.out.println("  🔸 Values:");
                    for (Map.Entry<String, String> kv : meta.getAnnotatoonKeyVal().entrySet()) {
                        System.out.println("     ➤ " + kv.getKey() + " = " + kv.getValue());
                    }
                }
            }

            for(Map.Entry<String,String> mapEntry : processor.getEntityFieldAndDBColumnKeyMap().entrySet()){
                System.out.println("Key : "+mapEntry.getKey()+" Value "+mapEntry.getValue());
            }
        }
    }
}
