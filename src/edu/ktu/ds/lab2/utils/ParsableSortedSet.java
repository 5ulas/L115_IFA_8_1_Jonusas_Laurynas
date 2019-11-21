package edu.ktu.ds.lab2.utils;

import edu.ktu.ds.lab2.Jonušas.Person;

public interface ParsableSortedSet<E> extends SortedSet<E> {

    void add(String dataString);

    void load(String fName);

    Object clone() throws CloneNotSupportedException;

}
