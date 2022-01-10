package me.dannly.selecaomaisprati.domain.model;

public class Student extends Person {

    private final double grade;

    public Student(String name, long birthDate, long phone, double grade) {
        super(name, birthDate, phone);
        this.grade = grade;
    }

    public double getGrade() {
        return grade;
    }
}
