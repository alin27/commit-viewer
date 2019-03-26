package codacy.com.commitviewer.controller;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CommitViewerControllerTest {

    private MockMvc mockMvc;

    @Test
    public void postRequestToGetCommitsWithValidPayloadShouldReturnOkResponse() throws Exception {
//        MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
//        String user = "{\"name\": \"bob\", \"email\" : \"bob@domain.com\"}";
        String payload = "{\"projectOwner\": \"testOwner\", \"projectName\" : \"testProject\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/commits")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void postRequestToGetCommitsWithInvalidPayloadShouldReturnBadRequest() throws Exception {
        String payload = "{\"projectOwner\": \"\", \"projectName\" : \"testProject\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectOwner", Is.is("Project owner cannot be missing or empty")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }
}
