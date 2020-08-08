package ru.favarish.timeTracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс представления задачи
 *
 * @author Fedor Zlobin
 * @version v0.1
 */
@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    /**
     * id задачи
     */
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    /**
     * id пользователя, который решает данную задачу
     */
    private Integer userId;

    /**
     * описание задачи
     */
    private String descriptionTask;

    /**
     * время начала выполнения задачи
     */
    private Date timeStart;

    /**
     * время завершения выполнения задачи
     */
    private Date timeFinish;

    public Task(Integer userId, String descriptionTask, Date timeStart) {
        this.userId = userId;
        this.descriptionTask = descriptionTask;
        this.timeStart = timeStart;
    }
}
