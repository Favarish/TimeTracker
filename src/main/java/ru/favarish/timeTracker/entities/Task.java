package ru.favarish.timeTracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    private Integer userId;

    private String descriptionTask;

    private Date timeStart;

    private Date timeFinish;

    public Task(Integer userId, String descriptionTask, Date timeStart) {
        this.userId = userId;
        this.descriptionTask = descriptionTask;
        this.timeStart = timeStart;
    }
}
