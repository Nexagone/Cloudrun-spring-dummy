package com.example.dummydata.service;

import com.example.dummydata.model.User;
import com.example.dummydata.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DummyDataService {

    @Autowired
    private UserRepository userRepository;
    private final Faker faker = new Faker();

    // Génère des données au démarrage de l'application
    @PostConstruct
    public void init() {
        generateDummyData();
    }

    // Génère des données toutes les 5 minutes
    @Scheduled(fixedRate = 300000) // 5 minutes = 300000 ms
    public void generateDummyData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName(faker.name().fullName());
            user.setEmail(faker.internet().emailAddress());
            user.setCompany(faker.company().name());
            user.setAge(faker.number().numberBetween(18, 80));
            users.add(user);
        }
        // Sauvegarde les nouvelles données sans supprimer les anciennes
        userRepository.saveAll(users);
        System.out.println("Nouvelles données fictives ajoutées à " + java.time.LocalDateTime.now());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
