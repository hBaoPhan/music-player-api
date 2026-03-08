package com.example.musicplayer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musicplayer.entity.Todo;
import com.example.musicplayer.repository.TodoRepository;
import com.example.musicplayer.service.MessageService;

@RestController
@RequestMapping("/todos")
public class TodoController {
    
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MessageService mess;

    @GetMapping
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    // Thêm mới một công việc
    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }


}
