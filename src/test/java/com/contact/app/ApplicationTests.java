package com.contact.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.contact.app.controller.ContactController;
import com.contact.app.model.Person;
import com.contact.app.repository.ContactRepository;
import com.contact.app.service.ContactService;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.Annotation;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class ApplicationTests {
    @Autowired
    ContactRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void checkController() {
        boolean hasAnnotation = false;

        Annotation annotation = AnnotationUtils.findAnnotation(ContactController.class, Controller.class);
        if (annotation != null) {
            hasAnnotation = true;
        }

        assertTrue(hasAnnotation);
    }


    @Test
    public void checkService() {
        boolean hasAnnotation = false;

        Annotation annotation = AnnotationUtils.findAnnotation(ContactService.class, Service.class);
        if (annotation != null) {
            hasAnnotation = true;
        }

        assertTrue(hasAnnotation);
    }

    @Test
    public void checkRepository() {
        boolean hasAnnotation = false;

        Annotation annotation = AnnotationUtils.findAnnotation(ContactRepository.class, Repository.class);
        if (annotation != null) {
            hasAnnotation = true;
        }

        assertTrue(hasAnnotation);
    }

    @Test
    public void checkEntity() {
        boolean hasAnnotation = false;

        Annotation annotation = AnnotationUtils.findAnnotation(Person.class, Entity.class);
        if (annotation != null) {
            hasAnnotation = true;
        }

        assertTrue(hasAnnotation);
    }

    @Test
    public void testPostPerson() throws Exception {
        Person person = new Person("Foo Bar", "84762834957");
        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response = mockMvc.perform(post("/contact/save")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(person)))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value("Foo Bar"))
                .andExpect(jsonPath("$.mobile").value("84762834957"))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");
        assertEquals(true, repository.findById(id).isPresent());

    }

    @Test
    public void testRetrievePerson() throws Exception {
        Person weather = new Person("Foo Bar", "84762834957");
        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response = mockMvc.perform(post("/contact/save")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(weather)))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value("Foo Bar"))
                .andExpect(jsonPath("$.mobile").value("84762834957"))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(get("/contact/retrieve/" + id))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Foo Bar"))
                .andExpect(jsonPath("$.mobile").value("84762834957"))
                .andExpect(status().isOk());

    }
}