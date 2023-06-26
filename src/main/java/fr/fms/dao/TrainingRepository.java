package fr.fms.dao;

import fr.fms.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository <Training, Long>{
}
