package com.example.musicplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo,Long> {  
} 
