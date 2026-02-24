package com.hfbe_Polina.web_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения.
 * <p>
 * Является точкой входа в систему и отвечает за запуск встроенного сервера,
 * инициализацию контекста Spring, сканирование компонентов и конфигурацию приложения.
 * </p>
 *
 * <p><b>Основные функции:</b></p>
 * <ul>
 *     <li>Запуск приложения через метод {@link SpringApplication#run}</li>
 *     <li>Автоматическое сканирование пакетов и создание бинов</li>
 *     <li>Инициализация MVC‑контроллеров, сервисов, репозиториев и сущностей</li>
 * </ul>
 *
 * <p><b>Расположение класса:</b></p>
 * <p>
 * Класс находится в корневом пакете проекта, что позволяет Spring Boot
 * автоматически сканировать все подпакеты:
 * <code>controllers</code>, <code>services</code>, <code>repositories</code>, <code>entities</code>.
 * </p>
 */
@SpringBootApplication
public class WebProjectApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(WebProjectApplication.class, args);
    }
}
