package me.dannly.selecaomaisprati.domain.model;

import java.time.LocalDate;

public class Student extends Person {

    private final double grade;

    public Student(String nome, LocalDate nascimento, long phone, double grade) {
        super(nome, nascimento, phone);
        this.grade = grade;
    }

    public double getGrade() {
        return grade;
    }
}
