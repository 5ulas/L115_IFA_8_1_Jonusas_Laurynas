package edu.ktu.ds.lab2.Jonušas;

import edu.ktu.ds.lab2.utils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/*
 * Aibės testavimas be Gui
 *
 */
public class ManoTestas {

    static Person[] people;
    static ParsableSortedSet<Person> cSeries = new ParsableBstSet<>(Person::new, Person.byHeight);

    public static void main(String[] args) throws CloneNotSupportedException {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus
        executeTest();
    }

    public static void executeTest() throws CloneNotSupportedException {
        Person c1 = new Person("zaidejas1", "pavarde1", 180, 190);
        Person c2 = new Person.Builder()
                .name("zaidejas2")
                .surname("pavarde2")
                .height(171)
                .weight(200)
                .build();
        
        Person c3 = new Person.Builder().buildRandom();
        Person c4 = new Person("Jonas Joanitis 150 51");
        Person c5 = new Person("Albertas Alberas 175 75");
        Person c6 = new Person("Modestas Modelinis 180 90");
        Person c7 = new Person("Tomas Fiesta 199 99");
        Person c8 = new Person("Laurynas Jonušas 190 90");
        Person c9 = new Person("Petras Petraitis 207 307");

        
        Person[] peopleArray = {c9, c7, c8, c5, c1, c6};

        Ks.oun("Žmonių Aibė:");
        ParsableSortedSet<Person> peopleSet = new ParsableBstSet<>(Person::new);

        for (Person c : peopleArray) {
            peopleSet.add(c);
            Ks.oun("Aibė papildoma: " + c + ". Jos dydis: " + peopleSet.size());
        }
        Ks.oun("");
        Ks.oun(peopleSet.toVisualizedString(""));

        ParsableSortedSet<Person> peopleSetCopy = (ParsableSortedSet<Person>) peopleSet.clone();

        peopleSetCopy.add(c2);
        peopleSetCopy.add(c3);
        peopleSetCopy.add(c4);
        Ks.oun("Papildyta autoaibės kopija:");
        Ks.oun(peopleSetCopy.toVisualizedString(""));

        c9.setHeight(100);

        Ks.oun("Originalas:");
        Ks.ounn(peopleSet.toVisualizedString(""));

        Ks.oun("Ar elementai egzistuoja aibėje?");
        for (Person c : peopleArray) {
            Ks.oun(c + ": " + peopleSet.contains(c));
        }
        Ks.oun(c2 + ": " + peopleSet.contains(c2));
        Ks.oun(c3 + ": " + peopleSet.contains(c3));
        Ks.oun(c4 + ": " + peopleSet.contains(c4));
        Ks.oun("");

        Ks.oun("Ar elementai egzistuoja aibės kopijoje?");
        for (Person c : peopleArray) {
            Ks.oun(c + ": " + peopleSetCopy.contains(c));
        }
        Ks.oun(c2 + ": " + peopleSetCopy.contains(c2));
        Ks.oun(c3 + ": " + peopleSetCopy.contains(c3));
        Ks.oun(c4 + ": " + peopleSetCopy.contains(c4));
        Ks.oun("");

        Ks.oun("Elementų šalinimas iš kopijos. Aibės dydis prieš šalinimą:  " + peopleSetCopy.size());
        for (Person c : new Person[]{c2, c1, c9, c8, c5, c3, c4, c2, c7, c6, c7, c9}) {
            peopleSetCopy.remove(c);
            Ks.oun("Iš Žmonių aibės kopijos pašalinama: " + c + ". Jos dydis: " + peopleSetCopy.size());
        }
        Ks.oun("");

        Ks.oun("Žmonių aibė su iteratoriumi:");
        Ks.oun("");
        for (Person c : peopleSet) {
            Ks.oun(c);
        }
        Ks.oun("");
        Ks.oun("Žmonių aibė AVL-medyje:");
        ParsableSortedSet<Person> peopleSetAVL = new ParsableAvlSet<>(Person::new);
        for (Person c : peopleArray) {
            peopleSetAVL.add(c);
        }
        Ks.ounn(peopleSetAVL.toVisualizedString(""));

        Ks.oun("Žmonių aibė su iteratoriumi:");
        Ks.oun("");
        for (Person c : peopleSetAVL) {
            Ks.oun(c);
        }

        Ks.oun("");
        Ks.oun("Žmonių aibė su atvirkštiniu iteratoriumi:");
        Ks.oun("");
        Iterator iter = peopleSetAVL.descendingIterator();
        while (iter.hasNext()) {
            Ks.oun(iter.next());
        }

        Ks.oun("");
        Ks.oun("Žmonių aibės toString() metodas:");
        Ks.ounn(peopleSetAVL);

        // Išvalome ir suformuojame aibes skaitydami iš failo
        peopleSet.clear();
        peopleSetAVL.clear();

        Ks.oun("");
        Ks.oun("Žmonių aibė DP-medyje:");
        peopleSet.load("data\\ban.txt");
        Ks.ounn(peopleSet.toVisualizedString(""));
        Ks.oun("Išsiaiškinkite, kodėl medis augo tik į vieną pusę.");

        Ks.oun("");
        Ks.oun("Žmonių aibė AVL-medyje:");
        peopleSetAVL.load("data\\ban.txt");
        Ks.ounn(peopleSetAVL.toVisualizedString(""));

        Set<String> peopleSet4 = Zmones.duplicatePersonNames(peopleArray);
        Ks.oun("Pasikartojantys žmonės:\n" + peopleSet4.toString());

        Set<String> peopleSet5 = Zmones.uniquePersonSurnames(peopleArray);
        Ks.oun("Unikalūs žmonės :\n" + peopleSet5.toString());
    }

    static ParsableSortedSet generateSet(int kiekis, int generN) {
        people = new Person[generN];
        for (int i = 0; i < generN; i++) {
            people[i] = new Person.Builder().buildRandom();
        }
        Collections.shuffle(Arrays.asList(people));

        cSeries.clear();
        Arrays.stream(people).limit(kiekis).forEach(cSeries::add);
        return cSeries;
    }
}