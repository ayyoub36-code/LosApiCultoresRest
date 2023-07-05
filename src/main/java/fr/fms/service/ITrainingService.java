package fr.fms.service;

import fr.fms.entities.Category;
import fr.fms.entities.Training;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ITrainingService {

    List<Training> getTrainings();


    Training saveTraining(Training t, MultipartFile file) throws IOException;

    void deleteTraining(Long id);

    Optional<Training> readTraining(Long id);

    Optional<Training> getTraining(Long id);

    Category getCategoryById(Long id);

    List<Category> getCategories();

    Optional<Category> readCategory(Long id);

}
