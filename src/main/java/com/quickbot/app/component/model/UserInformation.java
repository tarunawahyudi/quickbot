package com.quickbot.app.component.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation {
    private int day;
    private int month;
    private int year;
    private String gender;
}
