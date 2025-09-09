package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemDtoResponse> getAllItems() {
        List<Item> listItem = itemRepository.getAllItems();
        return listItem   //Преобразование объектов типа User в объекты типа UserDtoResponse.
                .stream()
                .map(ItemMapper::toItemDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(Long userId, ItemDtoCreate itemDto) {
        log.info("Получен запрос пользователя на создание вещи: {}", itemDto);

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        Item createItem = itemRepository.createItem(userId, item);
        log.info("Item успешно создана: {}", createItem);
        return ItemMapper.toItemDto(createItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDtoUpdate itemDto) {

        Item item = new Item();
        item.setId(itemId);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.updateItem(userId, item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        itemRepository.deleteItem(userId, itemId);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = itemRepository.getItemById(userId, itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Set<Long> getAllIdItems() {
        return itemRepository.getAllIdItems();
    }

    @Override
    public List<ItemDTONameDescription> getListItemsFromUserId(Long userId) {
        List<Item> listItemsById = itemRepository.getListItemsFromUserId(userId);
        return listItemsById.stream()
                .map(ItemMapper::itemDTONameDescription)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoResponse> getItemsBySearch(String text) {
        List<Item> listItem = itemRepository.getItemsBySearch(text);
        return listItem   //Преобразование объектов типа User в объекты типа UserDtoResponse.
                .stream()
                .map(ItemMapper::toItemDtoResponse)
                .collect(Collectors.toList());
    }
}
