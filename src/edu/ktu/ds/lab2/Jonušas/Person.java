/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ktu.ds.lab2.Jonušas;

import edu.ktu.ds.lab2.utils.BstSet;
import edu.ktu.ds.lab2.utils.Ks;
import java.util.*;
import edu.ktu.ds.lab2.utils.Ks;
import edu.ktu.ds.lab2.utils.Parsable;
/**
 *
 * @author llaur
 */
public class Person implements Parsable<Person>{

    // bendri duomenys visiems žmonėms (visai klasei)
    private static final int minWeight = 10;
    private static final double minHeight = 10;
    private static final double maxPrice = 333000.0;
    private static int personNr = 1000000;               //  ***** nauja
    private final static Random RANDOM = new Random(10000);  // Atsitiktinių generatorius
    private static final String idCode = "TA";   //  ***** nauja
    private static int serNr = 100;               //  ***** nauja
    
    private final String personIDNr;

    
    private String name = "";
    private String surname = "";
    private int height = -1;
    private int weight = -1;
    
    
    @Override
    public String toString() {
        return "Person{" + "personIDNr=" + personIDNr + ", name=" + name + ", surname=" + surname + ", height=" + height + ", weight=" + weight + '}';
    }
    
    public String getPersonIDNr() {
        return personIDNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Person() {
        personIDNr = idCode + (serNr++);    // suteikiamas originalus carRegNr
    }

    public Person(String name, String surname, int height, int weight) {
        personIDNr = idCode + (serNr++);    // suteikiamas originalus carRegNr
        this.name = name;
        this.surname = surname;
        this.height = height;
        this.weight = weight;
        validate();
    }

    public Person(String dataString) {
        personIDNr = idCode + (serNr++);    // suteikiamas originalus carRegNr
        this.parse(dataString);
        validate();
    }

    public Person(Person.Builder builder) {
        personIDNr = idCode + (serNr++);    // suteikiamas originalus carRegNr
        this.name = builder.name;
        this.surname = builder.surname;
        this.height = builder.height;
        this.weight = builder.weight;
        validate();
    }
    public Person create(String dataString) {
        return new Person(dataString);
    }

    private void validate() {
        String errorType = "";
        if (weight < minWeight) {
            errorType = "Per mažas svoris ["
                    + minWeight + "]";
        }
        if (height < minHeight) {
            errorType += " Per mažas ūgis [" + minHeight +  "]";
        }
        if (!errorType.isEmpty()) {
            Ks.ern("Žmogus yra blogai sugeneruotas: " + errorType);
        }
    }
    @Override
    public void parse(String dataString) {
        try {   // duomenys, atskirti tarpais
            Scanner scanner = new Scanner(dataString);
            name = scanner.next();
            surname = scanner.next();
            height = scanner.nextInt();
            weight = (scanner.nextInt());
        } catch (InputMismatchException e) {
            Ks.ern("Blogas duomenų formatas -> " + dataString);
        } catch (NoSuchElementException e) {
            Ks.ern("Trūksta duomenų -> " + dataString);
        }
    }
    @Override
    public int compareTo(Person person) {
        return getPersonIDNr().compareTo(person.getPersonIDNr());
    }

    public static Comparator<Person> byName = (Person c1, Person c2) -> c1.name.compareTo(c2.name); // pradžioje pagal markes, o po to pagal modelius

    public static Comparator<Person> byHeight = (Person c1, Person c2) -> {
        // didėjanti tvarka, pradedant nuo mažiausios
        if (c1.height < c2.height) {
            return -1;
        }
        if (c1.height > c2.height) {
            return +1;
        }
        return 0;
    };
    
    public static Comparator<Person> byHeightWeight = (Person c1, Person c2) -> {
        // ūgis mažėjančia tvarka, esant vienodiems lyginamas svoris 
        if (c1.height > c2.height) {
            return +1;
        }
        if (c1.height < c2.height) {
            return -1;
        }
        if (c1.weight > c2.weight) {
            return +1;
        }
        if (c1.weight < c2.weight) {
            return -1;
        }
        return 0;
    };
    // Person klases objektų gamintojas (builder'is)
    public static class Builder {

        private final static Random RANDOM = new Random(1949);  // Atsitiktinių generatorius
        private final static String[][] PEOPLE = { 
            {"Laurynas", "Jonušas", "190", "90"},
            {"Tomas", "Fiesta", "199", "99"},
            {"Modestas","Modelinis", "180", "90"},
            {"Albertas", "Alberas", "175", "75"},
            {"Jonas", "Joanitis", "150", "51"},
            {"Petras", "Petraitis", "207", "307"}
        };
        
        private String name = "";
        private String surname = "";
        private int height = -1;
        private int weight = -1;
        public Person build() {
            return new Person(this);
        }

        public Person buildRandom() {
            int ma = RANDOM.nextInt(PEOPLE.length);        // žmogaus indeksas  0..
            int mo = RANDOM.nextInt(PEOPLE[ma].length - 1) + 1;// žmogaus indeksas 1..
            return new Person(PEOPLE[ma][0],
                    PEOPLE[ma][mo],
                    150 + RANDOM.nextInt(60),// ūgis tarp 150 ir 210
                    50 + RANDOM.nextInt(150));// kaina tarp 800 ir 88800
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }
    }   
        public static void main(String[] args) {
            BstSet<Person> set = new BstSet<Person>();
            BstSet<Person> setA = new BstSet<Person>();
            Person p1 = new Person("E", "Jonusas", 190,100);
            Person p2 = new Person("A", "A1", 200,100);
            Person p3 = new Person("B", "A2", 201,45);
            Person p4 = new Person("A1", "A3", 210,55);
            Person p5 = new Person("D", "B1", 99,100);
            Person p6 = new Person("C", "B2", 98,100);
            set.add(p1);
            set.add(p2);
            set.add(p3);
            set.add(p4);
            set.add(p5);
            set.add(p6);
            // AVL uzpildymas
            setA.add(p1);
            setA.add(p2);
            setA.add(p3);
            setA.add(p4);
            setA.add(p5);
            setA.add(p6);
            Person p11 = new Person("XDDDDDDDDDDDDDDD", "Jonusas", 190,100);
            Person p22 = new Person("A", "A1", 200,100);
            Person p33 = new Person("B", "A2", 201,45);
            Person p44 = new Person("A1", "A3", 210,55);
            Person p55 = new Person("D", "B1", 99,100);
            Person p66 = new Person("EEE", "B2", 98,100);
            BstSet<Person> set2 = new BstSet<Person>();
            set2.add(p11);
            set2.add(p22);
            set2.add(p33);
            set2.add(p44);
            set2.add(p55);
            set2.add(p66);
            System.out.println(set.treeHeight());
            
            
//            System.out.println("Prieš remove() metodą");
//            System.out.println(setA.toString());
//            setA.remove(p1);
//            System.out.println("Po remove() metodo pašalinus pirmą elementą");
//            System.out.println(setA.toString());
            
//            System.out.println("Elementas perduodamas metodui:");
//            System.out.println(p3.toString());
//            System.out.println("higher() grazinamas elementas:");
//            Person p = set.higher(p3);
//            System.out.println(p.toString());       
//            
            
            
            
            
            
            
            
            
            
            
            
            
            
//            System.out.println("Žmonių setas:");
//            System.out.println(set.toString());
//            Person p = set.pollLast();
//            System.out.println("Grazinamas elmentas");
//            System.out.println(p.toString());
//            System.out.println("");
//            System.out.println("Žmonių setas po pollLast()");
//            System.out.println(set.toString());
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
//          System.out.println("Elementas perduodamas metodui:");
//          System.out.println(p5.toString());
//          System.out.println("floor() grazinamas elementas:");
//          Person p = set.floor(p5);
//          System.out.println(p.toString());     
            
//          System.out.println("Elementas perduodamas metodui:");
//          System.out.println(p5.toString());
//          System.out.println("ceiling() grazinamas elementas:");
//          Person p = set.ceiling(p5);
//          System.out.println(p.toString());

            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
//            // //<editor-fold defaultstate="collapsed" desc="addAll metodas">
//            System.out.println("Pirmas setas:");
//            System.out.println(set.toString());
//            System.out.println("Antras setas:");
//            System.out.println(set2.toString());
//            set.addAll(set2);
//            System.out.println("Sujungti setai");
//            System.out.println(set.toString());
////</editor-fold>
//            //<editor-fold defaultstate="collapsed" desc="lower() metodas">
//System.out.println("");
//System.out.println("Elementas perduodamas metodui:");
//System.out.println(p3.toString());
//Person p7 = set.lower(p3);
//System.out.println("lower() grazinamas elementas:");
//System.out.println(p7.toString());
////</editor-fold>
           

            int n = set.treeHeight();

        }
}
