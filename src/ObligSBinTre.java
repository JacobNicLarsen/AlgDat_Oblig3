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
        endringer = 0;
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
        return false;
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

    }

    @Override
    public Iterator<T> iterator() {
        return null;
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
        return "Hei";
    }

    public void inorden(Consumer<? super T> oppgave)  // iterativ inorden
    {
        if (tom()) return;            // tomt tre

        Deque<Node<T>> stakk = new ArrayDeque<>();
        Node<T> p = rot;   // starter i roten og går til venstre
        for ( ; p.venstre != null; p = p.venstre) stakk.add(p);

        while (true)
        {
            oppgave.accept(p.verdi);      // oppgaven utføres

            if (p.høyre != null)          // til venstre i høyre subtre
            {
                for (p = p.høyre; p.venstre != null; p = p.venstre)
                {
                    stakk.add(p);
                }
            }
            else if (!stakk.isEmpty())
            {
                p = stakk.pop();   // p.høyre == null, henter fra stakken
            }
            else break;          // stakken er tom - vi er ferdig

        } // while
    }

}
