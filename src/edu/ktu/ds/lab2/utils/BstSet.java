package edu.ktu.ds.lab2.utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.*;
import java.util.Stack;

/**
 * Rikiuojamos objektų kolekcijos - aibės realizacija dvejetainiu paieškos
 * medžiu.
 *
 * @param <E> Aibės elemento tipas. Turi tenkinti interfeisą Comparable<E>, arba
 *            per klasės konstruktorių turi būti paduodamas Comparator<E> interfeisą
 *            tenkinantis objektas.
 * 
 * @author darius.matulis@ktu.lt
 * @užduotis Peržiūrėkite ir išsiaiškinkite pateiktus metodus.
 */
public class BstSet<E extends Comparable<E>> implements SortedSet<E>, Cloneable {

    // Medžio šaknies mazgas
    protected BstNode<E> root = null;
    // Medžio dydis
    protected int size = 0;
    // Rodyklė į komparatorių
    protected Comparator<? super E> c = null;

    /**
     * Sukuriamas aibės objektas DP-medžio raktams naudojant Comparable<E>
     */
    public BstSet() {
        this.c = Comparator.naturalOrder();
    }

    /**
     * Sukuriamas aibės objektas DP-medžio raktams naudojant Comparator<E>
     *
     * @param c Komparatorius
     */
    public BstSet(Comparator<? super E> c) {
        this.c = c;
    }

    /**
     * Patikrinama ar aibė tuščia.
     *
     * @return Grąžinama true, jei aibė tuščia.
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return Grąžinamas aibėje esančių elementų kiekis.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Išvaloma aibė.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Patikrinama ar aibėje egzistuoja elementas.
     *
     * @param element - Aibės elementas.
     * @return Grąžinama true, jei aibėje egzistuoja elementas.
     */
    @Override
    public boolean contains(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in contains(E element)");
        }

        return get(element) != null;
    }

    /**
     * Aibė papildoma nauju elementu.
     *
     * @param element - Aibės elementas.
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in add(E element)");
        }

        root = addRecursive(element, root);
    }

    private BstNode<E> addRecursive(E element, BstNode<E> node) {
        if (node == null) {
            size++;
            return new BstNode<>(element);
        }

        int cmp = c.compare(element, node.element);

        if (cmp < 0) {
            node.left = addRecursive(element, node.left);
        } else if (cmp > 0) {
            node.right = addRecursive(element, node.right);
        }

        return node;
    }

    /**
     * Pašalinamas elementas iš aibės.
     *
     * @param element - Aibės elementas.
     */
    @Override
    public void remove(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in remove(E element)");
        }

        root = removeRecursive(element, root);
    }

    private BstNode<E> removeRecursive(E element, BstNode<E> node) {
        if (node == null) {
            return node;
        }
        // Medyje ieškomas šalinamas elemento mazgas;
        int cmp = c.compare(element, node.element);

        if (cmp < 0) {
            node.left = removeRecursive(element, node.left);
        } else if (cmp > 0) {
            node.right = removeRecursive(element, node.right);
        } else if (node.left != null && node.right != null) {
            /* Atvejis kai šalinamas elemento mazgas turi abu vaikus.
             Ieškomas didžiausio rakto elemento mazgas kairiajame pomedyje.
             Galima kita realizacija kai ieškomas mažiausio rakto
             elemento mazgas dešiniajame pomedyje. Tam yra sukurtas
             metodas getMin(E element);
             */
            BstNode<E> nodeMax = getMax(node.left);
            /* Didžiausio rakto elementas (TIK DUOMENYS!) perkeliamas į šalinamo
             elemento mazgą. Pats mazgas nėra pašalinamas - tik atnaujinamas;
             */
            node.element = nodeMax.element;
            // Surandamas ir pašalinamas maksimalaus rakto elemento mazgas;
            node.left = removeMax(node.left);
            size--;
            // Kiti atvejai
        } else {
            node = (node.left != null) ? node.left : node.right;
            size--;
        }

        return node;
    }

    private E get(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in get(E element)");
        }

        BstNode<E> node = root;
        while (node != null) {
            int cmp = c.compare(element, node.element);

            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node.element;
            }
        }

        return null;
    }

    /**
     * Pašalina maksimalaus rakto elementą paiešką pradedant mazgu node
     *
     * @param node
     * @return
     */
    BstNode<E> removeMax(BstNode<E> node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            node.right = removeMax(node.right);
            return node;
        } else {
            return node.left;
        }
    }

    /**
     * Grąžina maksimalaus rakto elementą paiešką pradedant mazgu node
     *
     * @param node
     * @return
     */
    BstNode<E> getMax(BstNode<E> node) {
        return get(node, true);
    }

    /**
     * Grąžina minimalaus rakto elementą paiešką pradedant mazgu node
     *
     * @param node
     * @return
     */
    BstNode<E> getMin(BstNode<E> node) {
        return get(node, false);
    }

    private BstNode<E> get(BstNode<E> node, boolean findMax) {
        BstNode<E> parent = null;
        while (node != null) {
            parent = node;
            node = (findMax) ? node.right : node.left;
        }
        return parent;
    }

    /**
     * Grąžinamas aibės elementų masyvas.
     *
     * @return Grąžinamas aibės elementų masyvas.
     */
    @Override
    public Object[] toArray() {
        int i = 0;
        Object[] array = new Object[size];
        for (Object o : this) {
            array[i++] = o;
        }
        return array;
    }

    /**
     * Aibės elementų išvedimas į String eilutę Inorder (Vidine) tvarka. Aibės
     * elementai išvedami surikiuoti didėjimo tvarka pagal raktą.
     *
     * @return elementų eilutė
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (E element : this) {
            sb.append(element.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Medžio vaizdavimas simboliais, žiūr.: unicode.org/charts/PDF/U2500.pdf
     * Tai 4 galimi terminaliniai simboliai medžio šakos gale
     */
    private static final String[] term = {"\u2500", "\u2534", "\u252C", "\u253C"};
    private static final String rightEdge = "\u250C";
    private static final String leftEdge = "\u2514";
    private static final String endEdge = "\u25CF";
    private static final String vertical = "\u2502  ";
    private String horizontal;

    /* Papildomas metodas, išvedantis aibės elementus į vieną String eilutę.
     * String eilutė formuojama atliekant elementų postūmį nuo krašto,
     * priklausomai nuo elemento lygio medyje. Galima panaudoti spausdinimui į
     * ekraną ar failą tyrinėjant medžio algoritmų veikimą.
     *
     * @author E. Karčiauskas
     */
    @Override
    public String toVisualizedString(String dataCodeDelimiter) {
        horizontal = term[0] + term[0];
        return root == null ? ">" + horizontal
                : toTreeDraw(root, ">", "", dataCodeDelimiter);
    }

    private String toTreeDraw(BstNode<E> node, String edge, String indent, String dataCodeDelimiter) {
        if (node == null) {
            return "";
        }
        String step = (edge.equals(leftEdge)) ? vertical : "   ";
        StringBuilder sb = new StringBuilder();
        sb.append(toTreeDraw(node.right, rightEdge, indent + step, dataCodeDelimiter));
        int t = (node.right != null) ? 1 : 0;
        t = (node.left != null) ? t + 2 : t;
        sb.append(indent).append(edge).append(horizontal).append(term[t]).append(endEdge).append(
                split(node.element.toString(), dataCodeDelimiter)).append(System.lineSeparator());
        step = (edge.equals(rightEdge)) ? vertical : "   ";
        sb.append(toTreeDraw(node.left, leftEdge, indent + step, dataCodeDelimiter));
        return sb.toString();
    }

    private String split(String s, String dataCodeDelimiter) {
        int k = s.indexOf(dataCodeDelimiter);
        if (k <= 0) {
            return s;
        }
        return s.substring(0, k);
    }

    /**
     * Sukuria ir grąžina aibės kopiją.
     *
     * @return Aibės kopija.
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        BstSet<E> cl = (BstSet<E>) super.clone();
        if (root == null) {
            return cl;
        }
        cl.root = cloneRecursive(root);
        cl.size = this.size;
        return cl;
    }

    private BstNode<E> cloneRecursive(BstNode<E> node) {
        if (node == null) {
            return null;
        }

        BstNode<E> clone = new BstNode<>(node.element);
        clone.left = cloneRecursive(node.left);
        clone.right = cloneRecursive(node.right);
        return clone;
    }
    
    /**
     * Randa medžio aukštį
     *
     * @return medžio aukštį
     */
    public int treeHeight(){
        if(root == null){
            return 0;
        }
        return Math.max(recursiveHeight(root.left), recursiveHeight(root.right));
    }

    private int recursiveHeight(BstNode<E> node){
        if(node == null){
            return 0;
        }
        return Math.max(recursiveHeight(node.left), recursiveHeight(node.right)) + 1;
    }
    /*
    Adds all of the elements in the specified collection to this set.
    */
    public boolean addAll(BstSet<? extends E> c){
        if (c == null) {
            return false;
        }
        for (E e : c) {
            add(e);
        }
        return true;
    }
    /*
    Retrieves and removes the last (highest) element, or returns null if this set is empty.
    */
    public E pollLast(){
        if (root == null) {
            return null;
        }
        BstNode<E> node = getMax(root);
        E tempNode = node.element;
        removeMax(root);
        return tempNode;
    }
    /*
    Returns the least element in this set strictly greater
    than the given element, or null if there is no such element.
    */
    public E higher(E e){   
        if (e == null || root == null) {
            return null;
        }
        if(root.element.compareTo(e) > 0){ // radom didesni
            // einam i kairiausia elementa nuo root kol sutinkam
            // maziausia elementa kuris yra didesnis uz e
            while(root.left != null){
                if((root.left.element.compareTo(e)) == -1){
                    return root.element;
                }
                root = root.left;
            }
            return root.element;
        }
        BstNode<E> node = root;
        while (node != null) {
            int cmp = node.element.compareTo(e); // radom didesni               
            if (cmp > 0) {
                break;
            }
            node = node.right;
        }
        if(node != null){
            while(node.left != null){
            if(node.element.compareTo(e) < 0){
                return node.element;
            }
            node = root.left;
            }
            return node.element;
        }
        return null;
    }
    /*
    Returns the greatest element in this
    set strictly less than the given element, or null if there is no such element.
    */
    public E lower(E e){
        if (e == null || root == null) {
            return null;
        }
        int cmp2;
            if((cmp2 = root.element.compareTo(e)) < 0){ // radom mazesni
            // einam i desiniausia elementa nuo root kol sutinkam
            // maziausia elementa kuris yra didesnis uz e
            BstNode<E> node = root;
            while(node.right != null){
                if(root.element.compareTo(e) < 0 && root.element.compareTo(node.element) >= 1){
                    node = root;
                }else if (root.element.compareTo(e) >= 0) {
                    return node.element;
                }
                root = root.right;
            }
            return node.element;
        }
        return null;
    }
    /*
    Returns the least element in this set greater than or
    equal to the given element, or null if there is no such element.
    */
    public E ceiling(E e){
         if (e == null || root == null) {
            return null;
        }
        if(root.element.compareTo(e) >= 0){ // radom didesni arba lygu
            // einam i kairiausia elementa nuo root kol sutinkam
            // maziausia elementa kuris yra didesnis uz e
            while(root.left != null){
                if((root.left.element.compareTo(e)) == -1){
                    return root.element;
                }
                root = root.left;
            }
            return root.element;
        }
        BstNode<E> node = root;
        while (node != null) {
            int cmp = node.element.compareTo(e); // radom didesni arba lygu            
            if (cmp >= 0) {
                break;
            }
            node = node.right;
        }
        if(node != null){
            while(node.left != null){
            if(node.element.compareTo(e) < 0){
                return node.element;
            }
            node = root.left;
            }
            return node.element;
        }
        return null;
    }
    
    /*
    Returns the greatest element in this set less than or
    equal to the given element, or null if there is no such element.
    didziauia elemetan sete kuris == arba < uz duotaji
    */
    
    public E floor(E e){
       if (e == null || root == null) {
            return null;
        }
        int cmp2;
            if((cmp2 = root.element.compareTo(e)) <= 0){ // radom mazesni arba lygu
            // einam i desiniausia elementa nuo root kol sutinkam
            // maziausia elementa kuris yra didesnis uz e
            BstNode<E> node = root;
            while(node.right != null){
                if(root.element.compareTo(e) <= 0 && root.element.compareTo(node.element) >= 1){
                    node = root;
                }else if (root.element.compareTo(e) >= 0) {
                    return node.element;
                }
                root = root.right;
            }
            return node.element;
        }
        return null;
    }
    
    /**
     * Grąžinamas aibės poaibis iki elemento.
     *
     * @param element - Aibės elementas.
     * @return Grąžinamas aibės poaibis iki elemento.
     */
    @Override
    public SortedSet<E> headSet(E element) {
        if(element == null)
        throw new UnsupportedOperationException("Klaida");
        SortedSet<E> naujas = new  BstSet();
        IteratorBst iter = new IteratorBst(true);
        while(iter.hasNext())
        {
            E el = iter.next();   //spausdina visus elementus kurie yra uz apsirasyto elemento
            if(el.equals(element))
            {
                return naujas;
            }
            naujas.add(el);
        }
        return null;
    }

    /**
     * Grąžinamas aibės poaibis nuo elemento element1 iki element2.
     *
     * @param element1 - pradinis aibės poaibio elementas.
     * @param element2 - galinis aibės poaibio elementas.
     * @return Grąžinamas aibės poaibis nuo elemento element1 iki element2.
     */
    @Override
    public SortedSet<E> subSet(E element1, E element2) {
        
        SortedSet<E> naujas = new BstSet<>();
        for (E elem : this) {
            int p = elem.compareTo(element1);
            int a = elem.compareTo(element2);
            if (p >= 0 && a == -1) {
                naujas.add(elem);
            }
        }
        return naujas;
    }

    /**
     * Grąžinamas aibės poaibis iki elemento.
     *
     * @param element - Aibės elementas.
     * @return Grąžinamas aibės poaibis nuo elemento.
     */
    @Override
    public SortedSet<E> tailSet(E element) {
        SortedSet<E> dalis = new BstSet<>();    
        int cmp;
        for (E item : this) {
            if ((cmp = item.compareTo(element)) >= 0) {
                dalis.add(item);
            }
        }
        return dalis;
    }
     /***
     * Apskaičiuoja medžio lapų skaičių
     * @return 
     */
    public int treeLeaves()
    {
        int leaves = 0;
        leaves = treeLeaves(leaves, root); //pradžia 
        return leaves;
    }
    public int treeLeaves(int leaves, BstNode<E> node)
    {
        if (node.left == null && node.right == null) //jei abu nuliai - elementas yra lapas
        {
           leaves++;   
        }
        if (node.right == null && node.left != null) //jei is kaires yra elementas, eis is naujo metoda su elementu is kaires
        {
            leaves = treeLeaves(leaves, node.left);
        }
        if (node.right != null && node.left == null) //su desine
        {
            leaves = treeLeaves(leaves, node.right);
        }
        if (node.right != null && node.left != null) //eina i abudu metodus
        {
            leaves = treeLeaves(leaves, node.right);
            leaves = treeLeaves(leaves, node.left);       
        }
        return leaves;
    }

    /**
     * Grąžinamas tiesioginis iteratorius.
     *
     * @return Grąžinamas tiesioginis iteratorius.
     */
    @Override
    public Iterator<E> iterator() {
        return new IteratorBst(true);
    }

    /**
     * Grąžinamas atvirkštinis iteratorius.
     *
     * @return Grąžinamas atvirkštinis iteratorius.
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new IteratorBst(false);
    }

    /**
     * Vidinė objektų kolekcijos iteratoriaus klasė. Iteratoriai: didėjantis ir
     * mažėjantis. Kolekcija iteruojama kiekvieną elementą aplankant vieną kartą
     * vidine (angl. inorder) tvarka. Visi aplankyti elementai saugomi steke.
     * Stekas panaudotas iš java.util paketo, bet galima susikurti nuosavą.
     */
    private class IteratorBst implements Iterator<E> {

        private Stack<BstNode<E>> stack = new Stack<>();
        // Nurodo iteravimo kolekcija kryptį, true - didėjimo tvarka, false - mažėjimo
        private boolean ascending;
        // Nurodo einamojo medžio elemento tėvą. Reikalingas šalinimui.
        private BstNode<E> parent = root;
        private BstNode<E> current = root;

        IteratorBst(boolean ascendingOrder) {
            this.ascending = ascendingOrder;
            this.toStack(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public E next() {
            if (!stack.empty()) {
//                // Grąžinamas paskutinis į steką patalpintas elementas
//                BstNode<E> n = stack.pop();
//                // Atsimenama tėvo viršunė. Reikia remove() metodui
//                parent = (!stack.empty()) ? stack.peek() : root;
//                BstNode<E> node = (ascending) ? n.right : n.left;
//                // Dešiniajame n pomedyje ieškoma minimalaus elemento,
//                // o visi paieškos kelyje esantys elementai talpinami į steką
//                toStack(node);
//                return n.element;
                current = stack.pop();
                parent = (!stack.empty()) ? stack.peek() : root;
                BstNode<E> node = (ascending) ? current.right : current.left;
                toStack(node);
                return current.element;   
            } else { // Jei stekas tuščias
                return null;
            }
        }

        @Override
        public void remove() {
            removeRecursive(current.element, current);
        }

        private void toStack(BstNode<E> n) {
            while (n != null) {
                stack.push(n);
                n = (ascending) ? n.left : n.right;
                
            }
        }
    }

    /**
     * Vidinė kolekcijos mazgo klasė
     *
     * @param <N> mazgo elemento duomenų tipas
     */
    protected class BstNode<N> {

        // Elementas
        protected N element;
        // Rodyklė į kairįjį pomedį
        protected BstNode<N> left;
        // Rodyklė į dešinįjį pomedį
        protected BstNode<N> right;

        protected BstNode() {
        }

        protected BstNode(N element) {
            this.element = element;
            this.left = null;
            this.right = null;
        }
    }
}
