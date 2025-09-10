package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT it.id FROM Item it")
    Set<Long> findAllIdItems();

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
    @Query("SELECT it FROM Item it WHERE it.user.id = ?1")
    List<Item> findItemsFromUserId(Long userId);

    @Query("SELECT it FROM Item it " +
            "WHERE LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(it.description) LIKE LOWER(CONCAT('%', :text, '%'))")
    List<Item> findItemsBySearch(String text);
}