package me.dannly.selecaomaisprati.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Person {

    private final String name;
    private final long birthDate;
    private final long phone;
    private long registerDate;
    private long lastChangedDate;

    public Person(String name, long birthDate, long phone) {
        this.name = name;
        this.birthDate = birthDate;
        this.registerDate = System.currentTimeMillis();
        this.lastChangedDate = System.currentTimeMillis();
        this.phone = phone;
    }

    public long getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public LocalDate formatDate(long millis) {
        return Instant.ofEpochSecond(millis / 1000).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDateTime formatDateTime(long millis) {
        return Instant.ofEpochSecond(millis / 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public long getBirthDate() {
        return birthDate;
    }

    public long getRegisterDate() {
        return registerDate;
    }

    public long getLastChangedDate() {
        return lastChangedDate;
    }

    public void setRegisterDate(long registerDate) {
        this.registerDate = registerDate;
        lastChangedDate = System.currentTimeMillis();
    }
}
