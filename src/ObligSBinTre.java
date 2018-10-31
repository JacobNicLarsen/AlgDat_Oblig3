import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ObligSBinTre<T> implements Beholder<T>{


    private static final class Node<T>  // en indre nodeklasse
    {
        private T verdi;            // nodens verdi
        private Node<T> venstre;    // referanse til venstre barn/subtre
        private Node<T> høyre;      // referanse til høyre barn/subtre
        private Node<T> forelder;   // referanse til foreldernoden

        private Node(T verdi, Node<T> v, Node<T> h, Node<T> f)    // konstruktør
        {
            this.verdi = verdi; venstre = v; høyre = h; this.forelder = f;
        }

        private Node(T verdi) { this.verdi = verdi; }  // konstruktør

    } // class Node<T>


    private Node<T> rot;
    private int endringer;
    private int antall;
    private final Comparator<? super T> comp;

    ObligSBinTre(Comparator<? super T> c){
        rot = null;
        antall = 0;
        comp = c;
    }


    @Override
    public final boolean leggInn(T verdi)    // skal ligge i class SBinTre
    {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null, r = null;             // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0){
            q.venstre = p;         // venstre barn til q
            p.forelder = q;
        }
        else{
            q.høyre = p;                        // høyre barn til q
            p.forelder = q;
        }

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null) return false;            // treet har ikke nullverdier

        Node<T> p = rot;                            // starter i roten
        while (p != null)                           // sjekker p
        {
            int cmp = comp.compare(verdi, p.verdi);   // sammenligner
            if (cmp < 0) p = p.venstre;               // går til venstre
            else if (cmp > 0) p = p.høyre;            // går til høyre
            else return true;                         // cmp == 0, funnet
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) return false; // Treet har ingen nullverdier

        Node<T> p = rot, q = null; // q er forelder til p og j er barn til p;

        while (p != null){ // finner første forekomst av verdi og forelder noden
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) {q = p; p = p.venstre;}
            else if (cmp > 0) {q = p; p = p.høyre;}
            else break;
        }
        if (p == null) return false;


        if (p.venstre == null && p.høyre == null){
            System.out.println("If ble triggerd fordi p er en bladnode med verdi: " + p.verdi + " og venstre og høyre " + p.venstre + " " + p.høyre + ", foreldernoden er " + q.verdi);
            if (p == rot){
                rot = null;
                System.out.print("Jeg en en snik som blir kjørt");
            }
            else if (q.høyre == p){
                q.høyre = q.venstre;
                p.forelder = null;
                System.out.println( "q nye verdi " + q.verdi);
            }
            else if (q.venstre == p){
                q.venstre = q.høyre;
                p.forelder = null;
            }
        }

        else if (p.venstre  == null || p.høyre == null){ // om noden har null eller et barn
            System.out.print("Else if triggerd, p verdi: " + p.verdi + ",");
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;
            if (p == rot) rot = b;
            else if (p == q.venstre){
                System.out.println(" Med venstre barn " + p.venstre.verdi);
                q.venstre = b;
                b.forelder = q;
            }
            else {
                System.out.print(" Med høyre barn " + p.høyre.verdi);
                q.høyre = b;
                b.forelder = q;
                System.out.println(" b sin forelder er nå " + b.forelder.verdi + " og q sin høyre er " + q.høyre.verdi);
            }
        }
        else{ // om noen har 2 barn
            System.out.println("Else trigged fordi p: " + p.verdi + "  har to barn " + p.venstre.verdi + " " + p.høyre.verdi);
            Node<T> r = nesteInorden(p), s = r.forelder, b = nesteInorden(r);

            p.verdi = r.verdi;

            if (s != p){
                //System.out.println("Forelder til inorden: " + s.verdi + " " + " Node verdi: " + p.verdi + " " + " Neste inorden: " + r.verdi);
                //System.out.println("r sin høyre og venste verdi " + " r.høyre: " + r.høyre + " r.venstre " + r.venstre);
                s.venstre = r.høyre;

            }
            else {
                System.out.println(" + else ble triggerd");
                s.høyre = r.høyre;
                b.forelder = s;
            }
        }
        antall--;
        return true;
    }

    public int fjernAlle(T verdi){
        int antallFjernet = 0;
        while (inneholder(verdi)){
            fjern(verdi);
            antallFjernet++;
        }
        return antallFjernet;
    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi){

        Node<T> p = rot;
        int antallVerdi = 0;

        while (p != null){
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else
            {
                if (cmp == 0) antallVerdi++;
                p = p.høyre;
            }
        }
        return antallVerdi;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private static <T> Node<T> nesteInorden(Node<T> p){

        if (p.høyre != null){
            Node<T> q = p.høyre;

            while (q.venstre != null) {
                q = q.venstre;
            }
            return q;

        }

        Node<T> f = p.forelder;
        while (f != null && p == f.høyre){
            p = f;
            f = f.forelder;
        }

        return f;
    }


    @Override
    public String toString(){
        if (tom()) return "[]";

        StringBuilder s = new StringBuilder();
        s.append("[");

        Node<T> p = rot;
        while (p.venstre != null) p = p.venstre;
        s.append(p.verdi);

        while (nesteInorden(p) != null){
            p = nesteInorden(p);
            s.append(",").append(" ").append(p.verdi);
        }

        s.append("]");

        return s.toString();
    }

    public String omvendtString(){
        StringJoiner s = new StringJoiner(", ", "[", "]");
        if (!tom()) inorden(x -> s.add(x != null ? x.toString():"Null"));
        return s.toString();
    }

    public void inorden(Consumer<? super T> oppgave)  // iterativ inorden
    {
        if (tom()) return;            // tomt tre

        Deque<Node<T>> stakk = new ArrayDeque<>();
        Node<T> p = rot;   // starter i roten og går til venstre
        for ( ; p.høyre != null; p = p.høyre) stakk.add(p);

        while (true)
        {
            oppgave.accept(p.verdi);      // oppgaven utføres

            if (p.venstre != null)          // til venstre i høyre subtre
            {
                for (p = p.venstre; p.høyre != null; p = p.høyre)
                {
                    stakk.add(p);
                }
            }
            else if (!stakk.isEmpty())
            {
                p = stakk.removeLast();   // p.høyre == null, henter fra stakken
            }
            else break;          // stakken er tom - vi er ferdig

        } // while
    }

    public String høyreGren(){
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    public String lengstGren(){
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    public String[] grener(){
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    public String bladnodeverdier(){
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    } public String postString(){
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext() {
            return p != null;  // Denne skal ikke endres!
        }
        @Override
        public T next(){
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
                        public void remove(){
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        } // BladnodeIterator
    }

    public void nivåorden(Consumer<? super T> oppgave)                // skal ligge i class BinTre
    {
        if (tom()) return;                   // tomt tre

        Queue<Node<T>> kø = new ArrayDeque<>();   // Se Avsnitt 4.2.2
        kø.add(rot);                     // legger inn roten

        while (!kø.isEmpty())                    // så lenge som køen ikke er tom
        {
            Node<T> p = kø.remove();             // tar ut fra køen
            oppgave.accept(p.verdi);

            if (p.venstre != null) kø.add(p.venstre);
            if (p.høyre != null) kø.add(p.høyre);

        }
    }

}
