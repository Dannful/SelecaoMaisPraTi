package me.dannly.selecaomaisprati.presentation;

import me.dannly.selecaomaisprati.data.repository.RepositoryImpl;
import me.dannly.selecaomaisprati.domain.model.Person;
import me.dannly.selecaomaisprati.domain.model.Student;
import me.dannly.selecaomaisprati.domain.repository.Repository;
import me.dannly.selecaomaisprati.presentation.util.Action;
import me.dannly.selecaomaisprati.presentation.util.Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    private static final Repository repository = new RepositoryImpl();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private static void createPerson() {
        final Student student = retrieveInputsAndCreateStudent(null);
        if (student.getGrade() == -1) {
            repository.insert(new Person(student.getName(), student.getBirthDate(), student.getPhone()));
        } else {
            repository.insert(student);
        }
        start();
    }

    private static int chooseBetweenPeople(List<Person> people) {
        if (people.size() == 1) return 0;
        people.forEach(Main::printPersonInformation);
        int id = Util.scanNext("- Há mais de uma pessoa com o nome provido. Insira o ID daquela que deseja. ", obj -> obj == null || repository.get(obj) == null, null, () -> Integer.parseInt(scanner.next().trim()));
        return repository.get(id) != null ? id : -1;
    }

    private static void updatePersonByName(String name) {
        int id = chooseBetweenPeople(repository.get(name));
        if (id == -1) {
            start();
            return;
        }
        updatePersonById(id);
    }

    private static void updatePersonById(int id) {
        final Person person = repository.get(id);
        final Student student = retrieveInputsAndCreateStudent(person instanceof Student ? (Student) person : new Student(person.getName(), person.getBirthDate(), person.getPhone(), -1d));
        final Person newPerson = student.getGrade() == -1 ? new Person(student.getName(), student.getBirthDate(), student.getPhone()) : new Student(student.getName(), student.getBirthDate(), student.getPhone(), student.getGrade());
        newPerson.setRegisterDate(person.getRegisterDate());
        repository.insert(id, newPerson);
        start();
    }

    private static boolean canKeep(Student existent, Object o) {
        return existent != null && o == null;
    }

    private static Long phoneToLong(String number) {
        return Util.parseLongOrNull(number.replaceAll("\\(", "")
                .replaceAll("\\)", "").replaceAll("-", "")
                .replaceAll(" ", "").replaceAll("\\+", ""));
    }

    private static String longToPhone(long phone) {
        final String phoneString = String.valueOf(phone);
        return "+" + phoneString.substring(0, 2) + " " + phoneString.substring(2, 4) + " " + phoneString.substring(4, 8) + "-" + phoneString.substring(8, 12);
    }

    private static Student retrieveInputsAndCreateStudent(Student existent) {
        final String keep = existent != null ? "(insira M para manter o valor atual) " : "";
        final String name = Util.scanNext("- Insira o novo nome: " + keep, (s) -> s.trim().isEmpty(), null, () -> scanner.next().trim());
        final LocalDate birthDate = Util.scanNext("- Insira a nova data de nascimento (dd/MM/aaaa): " + keep, Objects::isNull, existent != null ? () -> null : null, () -> Util.parseDateOrNull(dateFormatter, scanner.next().trim()));
        final Long phone = phoneToLong(Util.scanNext("- Insira o novo número de telefone. (+XX XX XXXX-XXXX) " + keep, obj -> obj == null || ((existent != null && !obj.equalsIgnoreCase("m")) && !Pattern.matches("\\+[0-9]{2} [0-9]{2} [0-9]{4}-[0-9]{4}", obj)), existent != null ? () -> null : null, () -> scanner.next().trim()));
        final Double grade = Util.scanNext("Deseja inserir uma nota final? (de 0 a 10, digite \"n/não\" para omitir) " + keep, Objects::isNull, existent != null ? () -> null : null, () -> {
            final String input = scanner.next().trim();
            final Double retrievedDouble = Util.parseDoubleOrNull(input);
            if (retrievedDouble == null) return input.equalsIgnoreCase("m") ? -2d : -1d;
            return retrievedDouble >= 0 && retrievedDouble <= 10 ? retrievedDouble : null;
        });
        return new Student(existent != null && name.equalsIgnoreCase("m") ? existent.getName() : name, canKeep(existent, birthDate) ? existent.getBirthDate() : birthDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000, canKeep(existent, phone) ? existent.getPhone() : phone, existent != null && grade == -2 ? existent.getGrade() : grade);
    }

    private static void printPersonInformation(Person person) {
        System.out.println(repository.getAll().indexOf(person) + ". " + (person instanceof Student ? "Aluno" : "Pessoa") + " - " + person.getName());
        System.out.println("  - Data de nascimento: " + person.formatDate(person.getBirthDate()).format(dateFormatter));
        System.out.println("  - Data de cadastro: " + person.formatDateTime(person.getRegisterDate()).format(dateTimeFormatter));
        System.out.println("  - Número de telefone: " + longToPhone(person.getPhone()));
        System.out.println("  - Última vez modificado: " + person.formatDateTime(person.getLastChangedDate()).format(dateTimeFormatter));
        if (person instanceof Student) System.out.println("  - Nota final: " + ((Student) person).getGrade());
    }

    private static void listPeople() {
        if (repository.getAll().isEmpty()) printNobodyRegistered();
        else repository.getAll().forEach(Main::printPersonInformation);
        start();
    }

    private static void printNobodyRegistered() {
        System.out.println("- Não há ninguém registrado.");
    }

    private static void deletePersonByName(String name) {
        if (repository.getAll().isEmpty()) {
            printNobodyRegistered();
            start();
            return;
        }
        deletePersonById(chooseBetweenPeople(repository.get(name)));
    }

    private static void deletePersonById(int id) {
        repository.delete(id);
        start();
    }

    private static boolean invalidId(int id) {
        if (repository.get(id) == null) {
            System.out.println("- Não há ninguém com esse ID.");
            start();
            return true;
        }
        return false;
    }

    private static boolean invalidName(String input) {
        if (repository.get(input).isEmpty()) {
            System.out.println("- Não há ninguém com esse nome.");
            start();
            return true;
        }
        return false;
    }

    private static void modifyByNameOrId(Consumer<Integer> modifyById, Consumer<String> modifyByName) {
        String input = Util.scanNext("- Insira o ID/nome da pessoa/do aluno. ", (s) -> s.trim().isEmpty(), null, () -> scanner.next().trim());
        Integer id = Util.parseIntOrNull(input);
        if (id != null && !invalidId(id)) {
            modifyById.accept(id);
        } else if (!invalidName(input)) {
            modifyByName.accept(input);
        }
    }

    private static void launchAction(Action action) {
        switch (action) {
            case CREATE: {
                createPerson();
                break;
            }
            case LIST: {
                listPeople();
                break;
            }
            case UPDATE: {
                modifyByNameOrId(Main::updatePersonById, Main::updatePersonByName);
                break;
            }
            case DELETE: {
                modifyByNameOrId(Main::deletePersonById, Main::deletePersonByName);
                break;
            }
            case TERMINATE: {
                System.exit(0);
                break;
            }
        }
    }

    private static void start() {
        launchAction(Util.scanNext("- Como deseja prosseguir? (" + Arrays.stream(Action.values()).map(action -> action.getActionName().toUpperCase(Locale.ROOT)).collect(Collectors.joining(", ")) + ") ", Objects::isNull, null, () -> Action.findByActionName(scanner.next().trim())));
    }

    public static void main(String[] args) {
        start();
    }
}