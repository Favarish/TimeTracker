package ru.favarish.timeTracker.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    private String name;

    private Date dateBirth;

    private String description;

    public User(String name, Date dateBirth, String description) {
        this.name = name;
        this.dateBirth = dateBirth;
        this.description = description;
    }
}
