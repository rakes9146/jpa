package rk.jpa.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationsProcessorTest {

    @Test
    public void testGetFieldAnnotations_withCompleteEntity() {
        User user = new User(); // Populate if needed
        AnnotationsProcessor processor = new AnnotationsProcessor(User1.class);

        Map<String, List<EntityMetaDto>> result = processor.getFieldsMetaData();

        assertNotNull(result, "The returned map should not be null");
        assertTrue(result.containsKey("username"), "Should contain metadata for 'username'");
        assertNotNull(result);
        assertTrue(result.containsKey("username"));

        List<EntityMetaDto> metaDtos = result.get("username");
        assertFalse(metaDtos.isEmpty());

        EntityMetaDto meta = metaDtos.get(0);
        assertEquals("rk.jpa.annotations.Column", meta.getAnnotationName());

        Map<String, String> keyValMap = meta.getAnnotatoonKeyVal();
        assertNotNull(keyValMap);
        assertEquals("username", keyValMap.get("name"));

        assertTrue(result.containsKey("profile"), "Should contain metadata for 'profile'");
        assertTrue(result.containsKey("orders"), "Should contain metadata for 'orders'");
        assertTrue(result.containsKey("department"), "Should contain metadata for 'department'");
        assertTrue(result.containsKey("roles"), "Should contain metadata for 'roles'");

        List<EntityMetaDto> usernameMeta = result.get("username");
        assertNotNull(usernameMeta);
        assertFalse(usernameMeta.isEmpty());
        assertEquals("rk.jpa.annotations.Column", usernameMeta.get(0).getAnnotationName(), "Expected Column annotation");

        // Add similar assertions for other annotations if desired
    }

    @Test
    void testFieldValuesExtractionForUser() throws IllegalAccessException {
        Department dept = new Department(100L, "Engineering");
        Role role = new Role(200L, "Admin", List.of("READ", "WRITE"));
        Profile profile = new Profile(300L, "Techie", dept);
        User user = new User(1L, "john_doe", profile , dept, Set.of(role));

        AnnotationsProcessor processor = new AnnotationsProcessor(User1.class);
        Map<String, Object> values = processor.getFieldNameValueMap();

        assertEquals("john_doe", values.get("username"));
        assertEquals(profile, values.get("profile"));
        assertEquals(dept, values.get("department"));
        assertEquals(Set.of(role), values.get("roles"));

        assertEquals(6, values.size());

        System.out.println("🔍 Extracted Field Values:");
        values.forEach((field, value) -> {
            System.out.println("   ▪ " + field + " => " + value);
        });
    }
}
