package edu.ktu.ds.lab2.Jonušas;

import edu.ktu.ds.lab2.utils.BstSet;
import edu.ktu.ds.lab2.utils.Set;

public class Zmones {

    public static Set<String> duplicatePersonNames(Person[] people) {
        Set<Person> uni = new BstSet<>(Person.byName);
        Set<String> duplicates = new BstSet<>();
        for (Person person : people) {
            int sizeBefore = uni.size();
            uni.add(person);

            if (sizeBefore == uni.size()) {
                duplicates.add(person.getName());
            }
        }
        return duplicates;
    }

    public static Set<String> uniquePersonSurnames(Person[] people) {
        Set<String> uniqueModels = new BstSet<>();
        for (Person person : people) {
            uniqueModels.add(person.getSurname());
        }
        return uniqueModels;
    }
}
