package fr.fms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fms.entities.Training;
import fr.fms.exceptions.RecordNotFoundException;
import fr.fms.service.ImplTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
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

    @DeleteMapping("/trainings/{id}")
    public void deleteTraining(@PathVariable("id") Long id) {
        implTrainingService.deleteTraining(id);
    }

    @GetMapping("/trainings/{id}")
    public Training getTrainingById(@PathVariable("id") Long id) {
        return implTrainingService.readTraining(id).orElseThrow(() -> new RecordNotFoundException("Id de Formation " + id + " n'existe pas"));
    }
}
