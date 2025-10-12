package ru.practicum.shareit.item.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item;
    private User author;

    @BeforeEach
    void setUp() {
        author = userRepository.save(User.builder()
                .name("Author")
                .email("author@mail.ru")
                .build());

        item = itemRepository.save(Item.builder()
                .name("Test Item")
                .description("Description")
                .available(true)
                .user(author)
                .build());

        Comment comment1 = new Comment();
        comment1.setItem(item);
        comment1.setAuthor(author);
        comment1.setText("Comment 1");
        comment1.setCreated(LocalDateTime.now().minusDays(1));
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setItem(item);
        comment2.setAuthor(author);
        comment2.setText("Comment 2");
        comment2.setCreated(LocalDateTime.now());
        commentRepository.save(comment2);
    }

    @Test
    void findCommentsListByItem_returnsAllCommentsForItem() {
        List<Comment> comments = commentRepository.findCommentsListByItem(item);

        assertNotNull(comments);
        assertEquals(2, comments.size());
        assertTrue(comments.stream().allMatch(c -> c.getItem().equals(item)));
        // Проверяем, что тексты комментариев есть в списке
        List<String> texts = comments.stream().map(Comment::getText).collect(Collectors.toList());
        assertTrue(texts.contains("Comment 1"));
        assertTrue(texts.contains("Comment 2"));
    }
}
