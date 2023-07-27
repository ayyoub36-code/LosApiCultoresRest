package fr.fms.web;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fms.entities.Category;
import fr.fms.entities.Training;
import fr.fms.service.ImplTrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc    // simuler les requettes HTTP
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ImplTrainingService service;

    @Autowired
    TrainingController controller;

    @Autowired
    ObjectMapper objectMapper;

    // list of trainings
    List<Training> trainings = new ArrayList<>();

    @BeforeEach
    public void setup() {
        Category dev = new Category(null, "DevWeb", null);
        trainings.add(new Training(1L, "Java", "Java SE 8 sur 5 jours", 3500, 1, "C:\\Users\\MehdiouiM\\Desktop\\Images\\java-logo.png", dev));
        trainings.add(new Training(2L, "Python", "Python sur 5 jours", 3000, 1, "python.png", dev));
    }

    @Test
    void contextInit() {
        assertNotNull(controller);
    }


    @Test
    void allTrainings_Test() throws Exception {
        when(service.getTrainings()).thenReturn(trainings);

        mockMvc.perform(get("/api/trainings").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(trainings.size()))) // le symbol $ pointe vers le contenu du body
                .andExpect(jsonPath("$[0].description", is(trainings.get(0).getDescription())));
    }


    @Test
    void getTrainingPhoto() throws Exception {
        //given
        Long trainingId = 1L;
        when(service.getTraining(trainingId)).thenReturn(Optional.ofNullable(trainings.get(0)));

        mockMvc.perform(get("/api/trainings/photo/{id}", trainingId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(result -> Files.readAllBytes(Paths.get(trainings.get(0).getPhoto())));
    }

    @Test
    void saveTraining() throws Exception {
        // insert
        Category dev = new Category(null, "DevWeb", null);
        Training t = new Training(1L, "Java", "Java SE 8 sur 5 jours", 3500, 1, "java-logo.png", dev);

        // param pour ignore les attributs null !!!
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        // api consomme du Json ! => convertir => objectMapper
        String insertTraining = objectMapper.writeValueAsString(t);

        String folder = "C:\\Users\\MehdiouiM\\Desktop\\Images\\";
        byte[] inputArray = Files.readAllBytes(Paths.get(folder + t.getPhoto()));
        MockMultipartFile file = new MockMultipartFile("tempFileName", inputArray);


        Map<String, Object> array = new HashMap<>();
        array.put("file", file);
        array.put("training", insertTraining);


        when(service.saveTraining(t, file)).thenReturn(t);

        mockMvc.perform(post("/api/trainings").contentType(MediaType.MULTIPART_FORM_DATA).content(String.valueOf(array))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    }

    @Test
    void deleteTraining() throws Exception {
        //given
        long trainingId = 2;
        when(service.readTraining(trainingId)).thenReturn(Optional.ofNullable(trainings.get(1)));
        //when
        doNothing().when(service).deleteTraining(trainingId); // empeche la suppression dans la BD
        //then
        String result = mockMvc.perform(delete("/api/trainings/{id}", trainingId)).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
        assertEquals("Formation avec id = " + trainingId + " Supprimé avec succés !", result);
    }

    @Test
    void getTrainingById_Test() throws Exception {
        Long trainingId = 1L;
        when(service.readTraining(trainingId)).thenReturn(Optional.ofNullable(trainings.get(0)));

        mockMvc.perform(get("/api/trainings/{id}", trainingId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(trainingId.intValue())))
                .andExpect(jsonPath("$.description", is(trainings.get(0).getDescription())));

    }
}