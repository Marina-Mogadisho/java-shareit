package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(scripts = "/data.sql")
class CommentMapperDbTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void toCommentDto_shouldMapFieldsCorrectly_usingDataFromDb() {
        // Берём комментарий с id = 1 (зависит от data.sql)
        Comment comment = commentRepository.findById(1L).orElseThrow();

        CommentDto dto = CommentMapper.toCommentDto(comment);

        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getText(), dto.getText());
        assertEquals(comment.getAuthor().getId(), dto.getAuthorUserId());
        assertEquals(comment.getAuthor().getName(), dto.getAuthorName());
        assertEquals(comment.getCreated(), dto.getCreated());
    }
}
