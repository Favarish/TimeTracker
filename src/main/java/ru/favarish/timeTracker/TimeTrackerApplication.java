package ru.favarish.timeTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Класс TimeTrackerApplication запускает Spring
 * @author Fedor Zlobin
 * @version v0.1
 */
@SpringBootApplication
@EnableScheduling
public class TimeTrackerApplication {
	/**
	 * Точка входа в программу
	 * Запуск Spring Application
	 * @param args входные данные для запуска приложения
	 */
	public static void main(String[] args) {
		SpringApplication.run(TimeTrackerApplication.class, args);
	}

}
