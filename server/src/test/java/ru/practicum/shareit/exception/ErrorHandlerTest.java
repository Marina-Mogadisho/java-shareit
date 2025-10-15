package ru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Создадим тестовый контроллер, чтобы искусственно выбрасывать исключения для теста ErrorHandler
@RestController
@RequestMapping("/test")
class TestExceptionController {

    @GetMapping("/validation")
    public void validation() {
        throw new ValidationException("Проверка ValidationException");
    }

    @GetMapping("/notfound")
    public void notFound() {
        throw new NotFoundException("Проверка NotFoundException");
    }

    @GetMapping("/internal")
    public void internal() {
        throw new EnternalException("Проверка EnternalException");
    }

    @GetMapping("/conflict")
    public void conflict() {
        throw new ConflictException("Проверка ConflictException");
    }
}

@WebMvcTest(controllers = TestExceptionController.class)
class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validationExceptionHandler() throws Exception {
        mockMvc.perform(get("/test/validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details.message").value("Проверка ValidationException"))
                .andExpect(jsonPath("$.details.exception").value(ValidationException.class.getName()))
                .andExpect(jsonPath("$.details.exceptionTrace").exists());
    }

    @Test
    void notFoundExceptionHandler() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.details.message").value("Проверка NotFoundException"))
                .andExpect(jsonPath("$.details.exception").value(NotFoundException.class.getName()));

    }

    @Test
    void enternalExceptionHandler() throws Exception {
        mockMvc.perform(get("/test/internal"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Unexpected error."));
    }

    @Test
    void conflictExceptionHandler() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict of changes."));
    }
}
