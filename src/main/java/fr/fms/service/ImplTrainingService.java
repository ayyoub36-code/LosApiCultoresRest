package fr.fms.service;

import fr.fms.dao.TrainingRepository;
import fr.fms.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImplTrainingService implements ITrainingService{
    @Autowired
    TrainingRepository trainingRepository;
    @Override
    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Training saveTraining(Training t) {
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

}
