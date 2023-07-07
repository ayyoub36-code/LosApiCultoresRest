package fr.fms;

import fr.fms.dao.CategoryRepository;
import fr.fms.dao.TrainingRepository;
import fr.fms.entities.AppRole;
import fr.fms.entities.AppUser;
import fr.fms.entities.Category;
import fr.fms.entities.Training;
import fr.fms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class ApiTrainingsApplication implements CommandLineRunner {
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    AccountService accountService;

    @Autowired
    CategoryRepository categoryRepository;
    // folder images
    private static final String FOLDER_IMAGE = "C:\\Users\\MehdiouiM\\Desktop\\Images\\";

    public static void main(String[] args) {
        SpringApplication.run(ApiTrainingsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //generateData();

        // generateUsers();
    }

    private void generateUsers() {
        accountService.saveUser(new AppUser(null, "user", "1234", new ArrayList<>()));
        accountService.saveUser(new AppUser(null, "admin", "1234", new ArrayList<>()));
        accountService.saveRole(new AppRole(null, "ADMIN"));
        accountService.saveRole(new AppRole(null, "USER"));
        accountService.addRoleToUser("user", "USER");
        accountService.addRoleToUser("admin", "USER");
        accountService.addRoleToUser("admin", "ADMIN");

    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


 /*   private void generateData() {
        Category dev = categoryRepository.save(new Category(null, "DevWeb", null));
        Category cms = categoryRepository.save(new Category(null, "CMS", null));
        Category bureatique = categoryRepository.save(new Category(null, "Bureautique", null));
        Category ia = categoryRepository.save(new Category(null, "IA", null));

        trainingRepository.save(new Training(null, "Java", "Java SE 8 sur 5 jours", 3500, 1, FOLDER_IMAGE + "java-logo.png", dev));
        trainingRepository.save(new Training(null, "DotNet", "DotNet & entity framework sur 5 jours", 2750, 1, FOLDER_IMAGE + "dotNet.png", dev));
        trainingRepository.save(new Training(null, "PowerBi", "Business Intelligence sur 5 jours", 3000, 1, FOLDER_IMAGE + "default.png", ia));
        trainingRepository.save(new Training(null, "NodeJs", "Prise en main de NodeJs/Express sur 2 jours", 1400, 1, FOLDER_IMAGE + "node.png", dev));
        trainingRepository.save(new Training(null, "Php", "Initiation au Dev/Web avec Php sur 4 jours", 1300, 1, FOLDER_IMAGE + "php.png", dev));
        trainingRepository.save(new Training(null, "WordPress", "Découverte du cms wordpress sur 4 jours", 1100, 1, FOLDER_IMAGE + "wordPress.png", cms));
        trainingRepository.save(new Training(null, "Excel", "Initiation au tableur Excel sur 4 jours", 700, 1, FOLDER_IMAGE + "excel.png", bureatique));
        trainingRepository.save(new Training(null, "PowerPoint", "Initiation à la création de powerPoint  sur 4 jours", 800, 1, FOLDER_IMAGE + "default.png", bureatique));
   } */

}
