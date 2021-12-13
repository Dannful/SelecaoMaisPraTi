package me.dannly.selecaomaisprati.domain.repository;

import me.dannly.selecaomaisprati.domain.model.Person;

import java.util.List;

public interface Repository {

    List<Person> getAll();

    void insert(Person person);

    void insert(int index, Person person);

    void delete(int index);

    Person get(int index);

    List<Person> get(String name);
}
