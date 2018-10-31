import javax.xml.bind.Element;
import java.util.Comparator;
import java.util.Random;

public class Main {

    public static void main(String[] args) {


        String s;
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        int[] i = {6, 3, 9, 1, 5, 7, 10, 6, 8, 11, 8};
        for (Integer e : i) tre.leggInn(e);



        tre.fjern(6);
        System.out.println(tre);
        System.out.println(tre.omvendtString());
        tre.fjern(8);
        System.out.println(tre);
        System.out.println(tre.omvendtString());
        tre.fjern(10);
        System.out.println(tre);
        System.out.println(tre.omvendtString());
        tre.fjern(11);
        System.out.println(tre);
        System.out.println(tre.omvendtString());
        tre.fjern(8);
        System.out.println(tre);
        System.out.println(tre.omvendtString());
        tre.fjern(7);
        System.out.println(tre);
        System.out.println(tre.omvendtString());

        tre.nivåorden(x-> System.out.print(x + ", "));
        System.out.println();
        System.out.println(tre.antall());



        int antallFeil = 0;

         //antallFeil += oppgave5();
        //antallFeil += oppgave6();
        //antallFeil += oppgave7();
        //antallFeil += oppgave8();
        //antallFeil += oppgave9();
        //antallFeil += oppgave10();
/*
        if (antallFeil == 0)
        {
            System.out.println("Gratulerer!! Du passerte testen!");
        }
        else
        {
            System.out.println
                    ("\nDette må forbedres. Du har minst " + antallFeil + " feil!");
        }*/
    }

    public static int oppgave5() {
        int antallFeil = 0;

        ObligSBinTre<Integer> tre =
                new ObligSBinTre<>(Comparator.naturalOrder());

        String s;

        tre.leggInn(6);
        tre.fjern(6);

        s = tre.toString();
        if (!s.equals("[]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5a: Feil i fjern(T)!");
        }

        int[] a = {6,3,9,1,5,7,10,2,4,8,11,6,8};
        for (int verdi : a) tre.leggInn(verdi);

        boolean fjernet = tre.fjern(12);
        s = tre.toString();

        if (!s.equals("[1, 2, 3, 4, 5, 6, 6, 7, 8, 8, 9, 10, 11]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5b: Feil i fjern(T)! Tallet 12 er ikke i treet!");
        }

        if (fjernet == true)
        {
            antallFeil++;
            System.out.println("Oppgave 5c: Feil i fjern(T)! Skal returnere false når");
            System.out.println("verdien ikke er i treet.");
        }

        if (tre.antall() != 13)
        {
            antallFeil++;
            System.out.println("Oppgave 5d: Feil i fjern(T)! Variabelen antall skal");
            System.out.println("ikke endres for en mislykket fjerning.");
        }

        fjernet = tre.fjern(2);
        s = tre.toString();

        if (!s.equals("[1, 3, 4, 5, 6, 6, 7, 8, 8, 9, 10, 11]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5e: Feil i fjern(T)!");
        }

        if (fjernet == false)
        {
            antallFeil++;
            System.out.println("Oppgave 5f: Feil i fjern(T)! Skal returnere true");
            System.out.println("for en vellykket fjerning.");
        }

        if (tre.antall() != 12)
        {
            antallFeil++;
            System.out.println("Oppgave 5g: Feil i fjern(T)! Variabelen antall skal");
            System.out.println("reduseres med 1 for en vellykket fjerning.");
        }

        tre.fjern(4);
        s = tre.toString();

        tre.nivåorden(x-> System.out.print(x + ", "));
        System.out.println();
        if (!s.equals("[1, 3, 5, 6, 6, 7, 8, 8, 9, 10, 11]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5h: Feil i fjern(T)!");
        }
///////// FEIL HER!

        tre.fjern(6);
        s = tre.toString();

        if (!s.equals("[1, 3, 5, 6, 7, 8, 8, 9, 10, 11]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5i: Feil i fjern(T)!");
        }

        tre.fjern(8);
        s = tre.toString();

        if (!s.equals("[1, 3, 5, 6, 7, 8, 9, 10, 11]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5j: Feil i fjern(T)!");
        }

        tre.fjern(10); tre.fjern(11); tre.fjern(8); tre.fjern(7);
        s = tre.toString();

        if (!s.equals("[1, 3, 5, 6, 9]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5k: Feil i fjern(T)!");
        }
/*
        tre.fjern(1); tre.fjern(3); tre.fjern(5); tre.fjern(9);

        s = tre.toString();

        if (!s.equals("[6]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5l: Feil i fjern(T)!");
        }

        tre.nullstill();

        if (tre.antall() != 0)
        {
            antallFeil++;
            System.out.println("Oppgave 5m: Feil i nullstill() - antall er feil!");
        }

        s = tre.toString();

        if (!s.equals("[]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5n: Feil i nullstill()!");
        }

        try
        {
            tre.nullstill();
        }
        catch (Exception e)
        {
            antallFeil++;
            System.out.println
                    ("Oppgave 5o: Skal ikke kaste unntak når et tomt tre nullstilles!");
        }

        try
        {
            if (tre.fjernAlle(0) != 0)
            {
                antallFeil++;
                System.out.println("Oppgave 5p: Feil i fjernAlle(T) for tomt tre!");
            }
        }
        catch (Exception e)
        {
            antallFeil++;
            System.out.println
                    ("Oppgave 5q: Kaster unntak i fjernAlle(T) for tomt tre!");
        }

        tre.leggInn(0);

        try
        {
            if (tre.fjernAlle(0) != 1)
            {
                antallFeil++;
                System.out.println
                        ("Oppgave 5r: Feil i fjernAlle(T) for tre med en verdi!");
            }
        }
        catch (Exception e)
        {
            antallFeil++;
            System.out.println
                    ("Oppgave 5s: Kaster unntak i fjernAlle(T) for tre med en verdi!");
        }

        int[] b = {1,4,1,3,1,2,1,1};
        for (int verdi : b) tre.leggInn(verdi);

        if (tre.fjernAlle(1) != 5)
        {
            antallFeil++;
            System.out.println("Oppgave 5t: Feil i fjernAlle(T)!");
        }

        s = tre.toString();
        if (!s.equals("[2, 3, 4]"))
        {
            antallFeil++;
            System.out.println("Oppgave 5u: Feil i fjernAlle(T)!");
        }

        tre = new ObligSBinTre<>(Comparator.naturalOrder());

        Random r = new Random();
        for (int i = 0; i < 500_000; i++) tre.leggInn(r.nextInt(1_000_000));

        long tid = System.currentTimeMillis();
        tre.nullstill();
        tid = System.currentTimeMillis() - tid;

        if (tid < 10)
        {
            antallFeil++;
            System.out.println("Oppgave 5v: Har du kodet nullstill() ved kun");
            System.out.println("nullstille hode og antall? Alle nodeverdier og");
            System.out.println("pekere i treet skal nulles!");
        }
*/
        return antallFeil;
    }  // slutt på Oppgave 5
}
