package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDtoCreate> jacksonTester1;
    private final UserDtoCreate userDtoCreate = new UserDtoCreate(
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Autowired
    private JacksonTester<UserDtoUpdate> jacksonTester2;
    private final UserDtoUpdate userDtoUpdate = new UserDtoUpdate(
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Autowired
    private JacksonTester<UserDtoResponse> jacksonTester3;
    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Test
    void userDtoCreate() throws Exception {
        JsonContent<UserDtoCreate> jsonContent = jacksonTester1.write(userDtoCreate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
    }

    @Test
    void userDtoRequestUpdate() throws Exception {
        JsonContent<UserDtoUpdate> jsonContent = jacksonTester2.write(userDtoUpdate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
    }

    @Test
    void userDtoResponse() throws Exception {
        JsonContent<UserDtoResponse> jsonContent = jacksonTester3.write(userDtoResponse);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
    }
}
