package me.dannly.selecaomaisprati.data.repository;

import me.dannly.selecaomaisprati.domain.model.Person;
import me.dannly.selecaomaisprati.domain.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class RepositoryImpl implements Repository {

    private final List<Person> people = new ArrayList<>();

    @Override
    public List<Person> getAll() {
        return people;
    }

    @Override
    public void insert(Person person) {
        people.add(person);
    }

    @Override
    public void insert(int index, Person person) {
        people.set(index, person);
    }

    @Override
    public void delete(int index) {
        if (index >= 0 && index < people.size())
            people.remove(index);
    }

    @Override
    public Person get(int index) {
        return index >= 0 && index < people.size() ? people.get(index) : null;
    }

    @Override
    public List<Person> get(String name) {
        if (name == null)
            return List.of();
        return people.stream().filter(person -> person.getName().equals(name)).toList();
    }
}