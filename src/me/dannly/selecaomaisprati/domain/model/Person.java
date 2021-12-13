package me.dannly.selecaomaisprati.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Person {

    private final String name;
    private final LocalDate birthDate;
    private final long phone;
    private LocalDateTime registerDate;
    private LocalDateTime lastChangedDate;

    public Person(String name, LocalDate birthDate, long phone) {
        final LocalDateTime localDateTimeNow = LocalDateTime.now();
        this.name = name;
        this.birthDate = birthDate;
        this.registerDate = localDateTimeNow;
        this.lastChangedDate = localDateTimeNow;
        this.phone = phone;
    }

    public long getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLastChangedDate() {
        return lastChangedDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }


    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
        lastChangedDate = LocalDateTime.now();
    }
}
