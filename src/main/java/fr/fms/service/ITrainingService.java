package fr.fms.service;

import fr.fms.entities.Training;

import java.util.List;
import java.util.Optional;

public interface ITrainingService {
    List<Training> getTrainings();

    Training saveTraining(Training t);

    void deleteTraining(Long id);

    Optional<Training> readTraining(Long id);
}
