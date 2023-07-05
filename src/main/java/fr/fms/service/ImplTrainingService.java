package fr.fms.service;

import fr.fms.dao.CategoryRepository;
import fr.fms.dao.TrainingRepository;
import fr.fms.entities.Category;
import fr.fms.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class ImplTrainingService implements ITrainingService {



    @Autowired(required = true)
    TrainingRepository trainingRepository;

    @Autowired
    CategoryRepository categoryRepository;


    @Override
    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Training saveTraining(Training t, MultipartFile file) throws IOException {
        return trainingRepository.save(t);
    }

    @Override
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }

    @Override
    public Optional<Training> readTraining(Long id) {
        return trainingRepository.findById(id);
    }

    @Override
    public Optional<Training> getTraining(Long id) {
        Optional<Training> t = trainingRepository.findById(id);
        if (t.isPresent()) {
            return t;
        }
        return null;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> readCategory(Long id) {
        return categoryRepository.findById(id);
    }


}
