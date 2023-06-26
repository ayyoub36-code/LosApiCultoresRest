package fr.fms;

import fr.fms.dao.TrainingRepository;
import fr.fms.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiTrainingsApplication implements CommandLineRunner {
	@Autowired
	TrainingRepository trainingRepository;
	public static void main(String[] args) {
		SpringApplication.run(ApiTrainingsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		generateData();
	}

	private void generateData() {
		trainingRepository.save(new Training(null, "Java", "Java SE 8 sur 5 jours", 3500, 1));
		trainingRepository.save(new Training(null, "DotNet", "DotNet & entity framework sur 5 jours", 2750, 1));
		trainingRepository.save(new Training(null, "PowerBi", "Business Intelligence sur 5 jours", 3000, 1));
		trainingRepository.save(new Training(null, "NodeJs", "Prise en main de NodeJs/Express sur 2 jours", 1400, 1));
		trainingRepository.save(new Training(null, "Php", "Initiation au Dév/Web avec Php sur 4 jours", 1300, 1));
	}

}
