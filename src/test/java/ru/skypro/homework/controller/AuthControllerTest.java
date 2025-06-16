package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.TestSecurityConfig;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // Успешный логин
        when(authService.login("user", "pass")).thenReturn(true);
        // Неуспешный логин
        when(authService.login("user", "wrong")).thenReturn(false);
        // Успешная регистрация
        when(authService.register(any(Register.class))).thenReturn(true);
    }

    @Test
    public void testLogin_Success() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("pass");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin_Unauthorized() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("wrong");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegister_Success() throws Exception {
        Register register = new Register();
        register.setUsername("newUser");
        register.setPassword("1234");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("1234567890");
        register.setRole(Role.USER);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegister_BadRequest() throws Exception {
        Register invalidRegister = new Register();
        invalidRegister.setUsername("");

        when(authService.register(any(Register.class))).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegister)))
                .andExpect(status().isBadRequest());
    }
}
