package com.example.healthcheckapi;

import com.example.healthcheckapi.config.AmazonDynamoDbClient;
import com.example.healthcheckapi.config.AmazonSNSClient;
import com.example.healthcheckapi.controller.UserController;
import com.example.healthcheckapi.model.User;
import com.example.healthcheckapi.repository.ImageRepository;
import com.example.healthcheckapi.repository.UserRepository;
import com.example.healthcheckapi.service.ImageStorageService;
import com.timgroup.statsd.StatsDClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.mockito.BDDMockito.given;


@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserGetPutTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataSource dataSource;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ImageStorageService service;

    @MockBean
    private StatsDClient statsDClient;

    @MockBean
    private AmazonSNSClient amazonSNSClient;

    @MockBean
    private AmazonDynamoDbClient amazonDynamoDbClient;

    @Test
    public void testGetUser() throws Exception
    {
        User user = new User("First", "Last", "$2a$10$2HhYKmJsFT7pYZdi172TZezZAELLhNnEYoXHrmAX0JFCCZ8n00/eS", "email@mail.com");
        user.setVerified(true);
        given(userRepository.findByUsername("email@mail.com")).willReturn(Optional.of(user));

        mockMvc.perform(get("/v1/user/self")
                        .with(httpBasic("email@mail.com","password")).
                        with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testPutUser() throws Exception
    {
        User user = new User("First", "Last", "$2a$10$2HhYKmJsFT7pYZdi172TZezZAELLhNnEYoXHrmAX0JFCCZ8n00/eS", "email@mail.com");
        user.setVerified(true);
        given(userRepository.findByUsername("email@mail.com")).willReturn(Optional.of(user));

        String json = "{\"first_name\":\"NewFirst\",\"last_name\":\"NewLast\",\"password\":\"updated\"}";

        mockMvc.perform(put("/v1/user/self").content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("email@mail.com","password"))
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }
}

