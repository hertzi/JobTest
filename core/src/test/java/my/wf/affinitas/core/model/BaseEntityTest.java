package my.wf.affinitas.core.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
public class BaseEntityTest {

    private BaseEntity entity1;
    private BaseEntity entity2;
    private class TestBaseEntity extends BaseEntity{};

    @Before
    public void setUp() throws Exception {
        entity1 = new TestBaseEntity();
        entity2 = new TestBaseEntity();
    }

    @Test
    public void shouldBeEqualById() throws Exception {
        entity1.setId(1234141412431243L);
        entity2.setId(1234141412431243L);
        assertEquals(entity1, entity2);
    }

    @Test
    public void shouldNotBeEqualByDifferentId() throws Exception {
        entity1.setId(1234141412431243L);
        entity2.setId(2234141412431243L);
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void shouldNotBeEqualByNullId() throws Exception {
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void shouldBeEqualBySameObject() throws Exception {
        entity2 = entity1;
        assertEquals(entity1, entity2);
    }

    @Test
    public void shouldHaveDifferentHashesById() throws Exception {
        entity1.setId(1234141412431243L);
        entity2.setId(2234141412431243L);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    public void shouldHaveSameHashesById() throws Exception {
        entity1.setId(1234141412431243L);
        entity2.setId(1234141412431243L);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashesByNullId() throws Exception {
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

}