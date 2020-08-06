package ru.favarish.timeTracker.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    private Integer userID;

    private String descriptionTask;

    private Date timeStart;

    private Date timeFinish;
}
