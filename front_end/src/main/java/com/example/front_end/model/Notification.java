package com.example.front_end.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Integer id;
    private String username;
    private String avatar;
    private String title;
    private String content;
    private boolean seen;
}
