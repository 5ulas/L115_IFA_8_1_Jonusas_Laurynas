package edu.ktu.ds.lab2.Jonušas;

import edu.ktu.ds.lab2.Jonušas.Person;
import edu.ktu.ds.lab2.gui.ValidationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PeopleGenerator {

    private int startIndex = 0, lastIndex = 0;
    private boolean isStart = true;

    private Person[] people;

    public Person[] generateShuffle(int setSize,
                                 double shuffleCoef) throws ValidationException {

        return generateShuffle(setSize, setSize, shuffleCoef);
    }

    /**
     * @param setSize
     * @param setTake
     * @param shuffleCoef
     * @return Gražinamas aibesImtis ilgio masyvas
     * @throws ValidationException
     */
    public Person[] generateShuffle(int setSize,
                                 int setTake,
                                 double shuffleCoef) throws ValidationException {

        Person[] people = IntStream.range(0, setSize)
                .mapToObj(i -> new Person.Builder().buildRandom())
                .toArray(Person[]::new);
        return shuffle(people, setTake, shuffleCoef);
    }

    public Person takeCar() throws ValidationException {
        if (lastIndex < startIndex) {
            throw new ValidationException(String.valueOf(lastIndex - startIndex), 4);
        }
        // Vieną kartą Automobilis imamas iš masyvo pradžios, kitą kartą - iš galo.
        isStart = !isStart;
        return people[isStart ? startIndex++ : lastIndex--];
    }

    private Person[] shuffle(Person[] people, int amountToReturn, double shuffleCoef) throws ValidationException {
        if (people == null) {
            throw new IllegalArgumentException("Žmonių nėra (null)");
        }
        if (amountToReturn <= 0) {
            throw new ValidationException(String.valueOf(amountToReturn), 1);
        }
        if (people.length < amountToReturn) {
            throw new ValidationException(people.length + " >= " + amountToReturn, 2);
        }
        if ((shuffleCoef < 0) || (shuffleCoef > 1)) {
            throw new ValidationException(String.valueOf(shuffleCoef), 3);
        }

        int amountToLeave = people.length - amountToReturn;
        int startIndex = (int) (amountToLeave * shuffleCoef / 2);

        Person[] takeToReturn = Arrays.copyOfRange(people, startIndex, startIndex + amountToReturn);
        Person[] takeToLeave = Stream
                .concat(Arrays.stream(Arrays.copyOfRange(people, 0, startIndex)),
                        Arrays.stream(Arrays.copyOfRange(people, startIndex + amountToReturn, people.length)))
                .toArray(Person[]::new);

        Collections.shuffle(Arrays.asList(takeToReturn)
                .subList(0, (int) (takeToReturn.length * shuffleCoef)));
        Collections.shuffle(Arrays.asList(takeToLeave)
                .subList(0, (int) (takeToLeave.length * shuffleCoef)));

        this.startIndex = 0;
        this.lastIndex = takeToLeave.length - 1;
        this.people = takeToLeave;
        return takeToReturn;
    }
}