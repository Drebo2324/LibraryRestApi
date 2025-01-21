package com.training.restApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.restApi.TestDataUtil;
import com.training.restApi.domain.dto.AuthorDto;
import com.training.restApi.domain.entities.AuthorEntity;
import com.training.restApi.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorReturnsHttp201() throws Exception {
        AuthorEntity testAuthor1 = TestDataUtil.createTestAuthorA();
        testAuthor1.setId(null);
        //Jackson tool convert Object into JSON
        String authorJson = objectMapper.writeValueAsString(testAuthor1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
                //isCreated() Code 202
        );
    }

    @Test
    public void testThatCreateAuthorReturnsSavedAuthor() throws Exception {
        AuthorEntity author1 = TestDataUtil.createTestAuthorA();
        author1.setId(null);
        String authorJson = objectMapper.writeValueAsString(author1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                //jsonPath allows to extract data from JSON response
                //"$" represents root of JSON document
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Hugh Mungus")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("69")
        );
    }

    @Test
    public void testListAuthorsReturnsHttp200() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testListAuthorsReturnsListOfAuthors() throws Exception{
        AuthorEntity author1 = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(author1);

        //check if first object in list is returned
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(savedAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(savedAuthor.getAge())
        );
    }

    @Test
    public void testGetAuthorReturnsHttp200IfAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorB();
        AuthorEntity savedAuthorEntity = authorService.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAuthorReturnsAuthorIfAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(savedAuthor.getAge())
        );
    }

    @Test
    public void testUpdateAuthorReturnsHttp200IfAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorC();
        AuthorEntity savedAuthor = authorService.save(author);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateAuthorUpdatesExistingAuthor() throws Exception {
        //existing author
        AuthorEntity author1 = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(author1);
        //new author
        AuthorEntity author2 = TestDataUtil.createTestAuthorB();
        //new author gets id of old author
        author2.setId(savedAuthor.getId());
        String updateAuthorJson = objectMapper.writeValueAsString(author2);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author2.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author2.getAge())
        );
    }

    @Test
    public void testPartialUpdateExistingAuthorReturnsHttp200() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDto();
        String authorJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDto();
        authorDto.setName("McLuvin");
        String authorJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("McLuvin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }

    @Test
    public void testDeleteAuthorReturnsHttp204() throws Exception{
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" +savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testDeleteAllAuthorReturnsHttp204() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
