package com.example.dummydata.controller;

import com.example.dummydata.model.User;
import com.example.dummydata.service.DummyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DummyDataController {

    @Autowired
    private DummyDataService dummyDataService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return dummyDataService.getAllUsers();
    }
}
