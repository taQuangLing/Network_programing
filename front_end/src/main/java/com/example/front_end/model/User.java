package com.example.front_end.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String avatar;
    private String bio;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDate birthday;
    private String interest;
}
