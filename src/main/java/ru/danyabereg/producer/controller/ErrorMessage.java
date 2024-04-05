package ru.danyabereg.producer.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class ErrorMessage {
    private Date timestamp;
    private String message;
}
