package fr.fms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fms.entities.Training;
import fr.fms.exceptions.RecordNotFoundException;
import fr.fms.service.ImplTrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@Slf4j
public class TrainingController {

    // folder images
    private static final String FOLDER = "C:\\Users\\MehdiouiM\\Desktop\\Images\\";
    @Autowired
    private ImplTrainingService implTrainingService;


    @GetMapping("/trainings")
    public List<Training> allTrainings() {
        return implTrainingService.getTrainings();
    }

    @GetMapping("/trainings/photo/{id}")
    public byte[] getTrainingPhoto(@PathVariable Long id) throws IOException {
        Training t = implTrainingService.getTraining(id).get();
        return Files.readAllBytes(Paths.get(t.getPhoto()));
    }


    @PostMapping("/trainings")
    public ResponseEntity<Training> saveTraining(@RequestParam("file") MultipartFile file, @RequestParam("training") String t) throws IOException {

        Training train = new ObjectMapper().readValue(t, Training.class);
        String filePath = FOLDER + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());
        train.setPhoto(filePath);
        Training training = implTrainingService.saveTraining(train, file);
        if (Objects.isNull(training)) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(training.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/trainings/{id}")
    public ResponseEntity<?> deleteTraining(@PathVariable("id") Long id) {

        try {
            implTrainingService.deleteTraining(id);
        }
        catch (Exception e) {
            log.error("Pb avec suppression de la formation d'id : {}",id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Formation avec id = " + id + " introuvable.");
        }
        log.info("suppression de la formation d'id : {}", id);
        return ResponseEntity.status(HttpStatus.OK).body("Formation avec id = " + id + " Supprimé avec succés !");
    }

    @GetMapping("/trainings/{id}")
    public Training getTrainingById(@PathVariable("id") Long id) {
        return implTrainingService.readTraining(id).orElseThrow(() -> new RecordNotFoundException("Id de Formation " + id + " n'existe pas"));
    }
}
