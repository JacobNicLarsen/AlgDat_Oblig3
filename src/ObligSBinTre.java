import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

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
            r = q;
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
}
