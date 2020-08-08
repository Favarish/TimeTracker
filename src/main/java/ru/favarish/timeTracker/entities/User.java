package ru.favarish.timeTracker.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс представления пользователя
 *
 * @author Fedor Zlobin
 * @version v0.1
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    /**
     * id пользователя
     */
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    /**
     * имя пользователя
     */
    private String name;

    /**
     * дата рождения пользователя
     */
    private Date dateBirth;

    /**
     * описание пользователя
     */
    private String description;

    public User(String name, Date dateBirth, String description) {
        this.name = name;
        this.dateBirth = dateBirth;
        this.description = description;
    }
}
