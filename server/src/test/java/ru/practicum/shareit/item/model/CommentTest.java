package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    static class Comment {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (getClass() != o.getClass()) return false;
            Comment comment = (Comment) o;
            return id != null && id.equals(comment.id);
        }

        @Override
        public final int hashCode() {
            return getClass().hashCode();
        }
    }

    @Test
    void equals_sameObject_true() {
        Comment comment = new Comment();
        assertTrue(comment.equals(comment));
    }

    @Test
    void equals_null_false() {
        Comment comment = new Comment();
        assertFalse(comment.equals(null));
    }

    @Test
    void equals_differentClass_false() {
        Comment comment = new Comment();
        assertFalse(comment.equals("string"));
    }

    @Test
    void equals_sameId_true() {
        Comment c1 = new Comment();
        c1.setId(1L);
        Comment c2 = new Comment();
        c2.setId(1L);
        assertTrue(c1.equals(c2));
    }

    @Test
    void equals_differentId_false() {
        Comment c1 = new Comment();
        c1.setId(1L);
        Comment c2 = new Comment();
        c2.setId(2L);
        assertFalse(c1.equals(c2));
    }

    @Test
    void equals_idNull_false() {
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        assertFalse(c1.equals(c2));
    }

    @Test
    void hashCode_stable() {
        Comment comment = new Comment();
        assertEquals(Comment.class.hashCode(), comment.hashCode());
    }
}
