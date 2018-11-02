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
        endringer ++;
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
            if (p == rot){
                rot = null;
            }
            else if (q.høyre == p){
                q.høyre = null;
                p.forelder = null;
            }
            else if (q.venstre == p){
                q.venstre = null;
                p.forelder = null;
            }
        }

        else if (p.venstre  == null || p.høyre == null){ // om noden har null eller et barn

            Node<T> b = p.venstre != null ? p.venstre : p.høyre;
            if (p == rot) rot = b;
            else if (p == q.venstre){

                q.venstre = b;
                b.forelder = q;
            }
            else {
                q.høyre = b;
                b.forelder = q;
            }
        }
        else{ // om noen har 2 barn

            Node<T> r = nesteInorden(p), s = r.forelder, b = r.høyre;

            p.verdi = r.verdi;

            if (s != p){
                s.venstre = r.høyre;
            }
            else {
                s.høyre = r.høyre;
                b.forelder = s;
            }
        }
        antall--;
        endringer ++;
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
        if (!tom()) nullstill(rot);  // nullstiller
        rot = null; antall = 0; endringer ++;      // treet er nå tomt
    }

    private void nullstill(Node<T> p)
    {
        if (p.venstre != null)
        {
            nullstill(p.venstre);      // venstre subtre
            p.venstre = null;          // nuller peker
        }
        if (p.høyre != null)
        {
            nullstill(p.høyre);        // høyre subtre
            p.høyre = null;            // nuller peker
        }
        p.verdi = null;              // nuller verdien
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
        if (tom()) return "[]";
        StringJoiner s = new StringJoiner(", ", "[", "]");
        Node<T> p = rot;

        while (true){
            if (p.høyre == null && p.venstre == null) {
                s.add(p.verdi.toString());
                break;
            }
            else if (p.høyre != null){
                s.add(p.verdi.toString());
                p = p.høyre;
            }
            else {
                s.add(p.verdi.toString());
                p = p.venstre;
            }
        }

        return s.toString();
    }


    public String lengstGren(){

        if (tom()) return "[]";
        java.util.Deque<Node<T>> nodes = new java.util.ArrayDeque<Node<T>>();

        nodes.addFirst(rot);
        int høyde = høyde();
        int nivåteller = 0;

        while (høyde != nivåteller) {
            //Hent ut størrelsen på køen => dette er antall noder på dette nivået
            int antall = nodes.size();
            nivåteller++;

            //Skriv ut alle noder på dette nivået, og legg noder for neste nivå til køen
            for (int i=0; i<antall; ++i) {
                Node<T> current = nodes.removeLast();
                if (current.høyre != null) {
                    nodes.addFirst(current.høyre);
                }
                if (current.venstre != null) {
                    nodes.addFirst(current.venstre);
                }
            }

        }

        java.util.Deque<T> ut = new java.util.ArrayDeque<T>();
        Node<T> p = nodes.getFirst();
        while (p != null){
            ut.addFirst(p.verdi);
            p = p.forelder;
        }

        StringJoiner s = new StringJoiner(", ","[","]");

        while (!ut.isEmpty()){

            s.add(ut.pop().toString());
        }

        return s.toString();
    }



    private static void høyde(Node<?> p, int nivå, int[] maksnivå)
    {
        if (nivå > maksnivå[0]) maksnivå[0] = nivå;
        if (p.venstre != null) høyde(p.venstre, nivå + 1, maksnivå);
        if (p.høyre != null) høyde(p.høyre, nivå + 1, maksnivå);
    }

    public int høyde()
    {
        int[] maksnivå = {-1};                // tabellen har lengde 1
        if (!tom()) høyde(rot, 0, maksnivå);  // roten har nivå 0
        return maksnivå[0];                   // inneholder høyden
    }






    public String[] grener(){
        ArrayDeque<String> grener = new ArrayDeque<>();
        if(!tom()) {
            finnGrener(rot, grener);
        }
        return grener.toArray(new String[0]);
    }

    public void finnGrener(Node<T> p, ArrayDeque<String> grener){
        Stack<Node<T>> noder = new Stack<>();
        ArrayDeque<String> gren = new ArrayDeque<>();
        noder.push(p);

        while(!noder.isEmpty()) {
            Node<T> q = noder.peek();
            noder.pop();

            if(q.høyre != null) {
                noder.push(q.høyre);
            }
            if(q.venstre != null) {
                noder.push(q.venstre);
            }

            if(q.venstre == null && q.høyre == null) {
                while (q != null) {
                    gren.addFirst(q.verdi.toString());
                    q = q.forelder;
                }
                grener.add(gren.toString());
                gren.clear();
            }
        }
    }



    public void printPaths(Node<T> r,ArrayList<T> arr, ArrayList<T> ut)
    {
        if(r==null)
        {
            return;
        }
        arr.add(r.verdi);
        if(r.venstre==null && r.høyre==null)
        {
            ut.addAll(arr);
        }
        else
        {
            printPaths(r.venstre,arr, ut);
            printPaths(r.høyre,arr, ut);
        }

        arr.remove(arr.size()-1);
    }




    public String bladnodeverdier(){
        if (rot == null) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");
        bladnodeverdier(rot,s);
        return s.toString();
    }

    private void bladnodeverdier(Node<T> p, StringJoiner s) {
        if(p.venstre != null) {
            bladnodeverdier(p.venstre, s);
        }
        if(p.høyre != null) {
            bladnodeverdier(p.høyre, s);
        }
        if(p.høyre == null && p.venstre == null) {
            s.add(p.verdi.toString());
        }
    }

    public String postString(){
        Stack<Node<T>> s1, s2;
        StringJoiner s = new StringJoiner(", ","[", "]");

        s1 = new Stack<>();
        s2 = new Stack<>();

        if (rot == null)
            return s.toString();

        // Legger roten på stacken
        s1.push(rot);

        // Kjører når første stack ikke er tom
        while (!s1.isEmpty()) {
            // Tar en node fra stack en og legger på stack 2
            Node<T> temp = s1.pop();
            s2.push(temp);

            // Legger til venste og høyre barn til noden som ble fjernet
            if (temp.venstre != null)
                s1.push(temp.venstre);
            if (temp.høyre != null)
                s1.push(temp.høyre);
        }

        // Legger alle elementene på nye stacken
        while (!s2.isEmpty()) {
            Node<T> temp = s2.pop();
            s.add(temp.verdi.toString());
        }
        return s.toString();
    }


    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() {
            if (tom()) return;
            p = rot;

            while (true) {
                if (p.venstre != null) p = p.venstre;
                else if (p.høyre != null) p = p.høyre;
                else { q = p; break;}
            }
        }

        @Override
        public boolean hasNext() {
            return p != null;  // Denne skal ikke endres!
        }
        @Override
        public T next(){
            if (!hasNext()) throw new NoSuchElementException("Finnes ikke neste!");
            if (iteratorendringer != endringer) throw new ConcurrentModificationException("Det er gjort endringer på iteratoren");

            T verdi = p.verdi;

            q = p;
            p = nesteInorden(p);

            if(p != null) {
                while (p!= null && (p.høyre != null || p.venstre != null)) {
                    p = nesteInorden(p);
                }
            }

            removeOK = true;
            return verdi;
        }

        @Override
        public void remove(){
            if(!removeOK) throw new IllegalStateException("Kan ikke fjerne");

            Node<T> r = q.forelder;
            if(r == null) rot = null;
            else if(r.venstre == q) r.venstre = null;
            else r.høyre = null;

            iteratorendringer++;
            endringer++;
            antall--;
            removeOK = false;
        } // BladnodeIterator
    }

    public void nivåorden(Consumer<? super T> oppgave)
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

    /**
     * Funksjon som skiver ut bredde først, men med linjeskift mellom nivåer
     */
    void printBTBreadthFirstNewline() {
        java.util.Deque<Node<T>> nodes = new java.util.ArrayDeque<Node<T>>();

        nodes.addFirst(rot);

        while (!nodes.isEmpty()) {
            //Hent ut størrelsen på køen => dette er antall noder på dette nivået
            int antall = nodes.size();
            System.out.println("Antall noder dette nivå: " + antall);

            //Skriv ut alle noder på dette nivået, og legg noder for neste nivå til køen
            for (int i=0; i<antall; ++i) {
                Node<T> current = nodes.removeLast();
                System.out.print(current.verdi + ", ");
                if (current.venstre != null) {
                    nodes.addFirst(current.venstre);
                }
                if (current.høyre != null) {
                    nodes.addFirst(current.høyre);
                }
            }
            System.out.println();
        }
    }

}
