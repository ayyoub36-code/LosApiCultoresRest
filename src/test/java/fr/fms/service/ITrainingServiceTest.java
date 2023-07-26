package fr.fms.service;

import fr.fms.dao.CategoryRepository;
import fr.fms.dao.TrainingRepository;
import fr.fms.entities.Category;
import fr.fms.entities.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ITrainingServiceTest {

    // ctrl + shift + t => cree la classe test équivalente

    @Mock
    TrainingRepository repo;

    @Mock
    CategoryRepository repoCategory;

    @InjectMocks
    ImplTrainingService service;


    @Test
    void getAllTrainings_Test() {
        // given
        List<Training> trainingList = new ArrayList<Training>();
        Category dev = new Category(null, "DevWeb", null);
        trainingList.add(new Training(1L, "Java", "Java SE 8 sur 5 jours", 3500, 1, "java-logo.png", dev));
        trainingList.add(new Training(2L, "Python", "Python sur 5 jours", 3000, 1, "python.png", dev));

        //when
        when(repo.findAll()).thenReturn(trainingList);
        List<Training> lst = service.getTrainings();

        //then
        assertEquals(2, lst.size());
        assertEquals(trainingList.get(0).getDescription(), lst.get(0).getDescription());


    }

    @Test
    void saveTraining() throws IOException {

        //given
        Category dev = new Category(null, "DevWeb", null);
        Training t = new Training(1L, "Java", "Java SE 8 sur 5 jours", 3500, 1, "java-logo.png", dev);

        //when
        when(repo.save(t)).thenReturn(t);

        //then
        String folder = System.getProperty("user.home") + "\\trainings\\images\\";
        byte[] inputArray = Files.readAllBytes(Paths.get(folder + t.getPhoto()));
        MockMultipartFile file = new MockMultipartFile("tempFileName", inputArray);
        Training trainInserted = service.saveTraining(t, file);

        assertEquals(trainInserted.getDescription(), "Java SE 8 sur 5 jours");
        assertEquals(trainInserted.getPrice(), 3500);
    }

    @Test
    void deleteTraining() {
        // les methodes void different façon de fonctionnement
        //given
        Category dev = new Category(null, "DevWeb", null);
        Training t = new Training(1L, "Java", "Java SE 8 sur 5 jours", 3500, 1, "java-logo.png", dev);

        // appel de la methode a tester
        service.deleteTraining(t.getId());

        // verifier que le service a appeler la methode delete du repo
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void readTraining_Test() {
        //given
        Category dev = new Category(null, "DevWeb", null);
        Training t = new Training(1L, "Java", "Java SE 8 sur 5 jours", 3500, 1, "java-logo.png", dev);

        //when
        when(repo.findById(1L)).thenReturn(Optional.of(t));
        Training trainById = service.readTraining(1L).get();

        //then
        assertEquals(trainById.getDescription(), "Java SE 8 sur 5 jours");
        assertEquals(trainById.getPrice(), 3500);


    }

    @Test
    void readCategory_Test() {
        //given
        Category dev = new Category(null, "DevWeb", null);

        //when
        when(repoCategory.findById(1L)).thenReturn(Optional.of(dev));
        Category category = service.readCategory(1L).get();

        //then
        assertEquals(category.getName(), "DevWeb");

    }

    @Test
    void getCategories_Test() {
        //given
        List<Category> listCategories = new ArrayList<Category>();
        listCategories.add(new Category(1L, "DevWeb", null));
        listCategories.add(new Category(2L, "CMS", null));

        //when
        when(repoCategory.findAll()).thenReturn(listCategories);
        List<Category> lst = service.getCategories();

        //then
        assertEquals(2, lst.size());
        assertEquals("DevWeb", lst.get(0).getName());


    }
}