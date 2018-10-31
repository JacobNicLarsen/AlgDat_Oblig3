import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.fjernAlle(4));
        tre.fjernAlle(7);
        System.out.println(tre.antall());
        //System.out.println(tre + ""+ tre.omvendtString());
        // [1, 2, 6, 9, 10] [10, 9, 6, 2, 1]// OBS: Hvis du ikke har gjort oppgave 4 kan du her bruke toString()


        //System.out.println(tre.toString());
        //tre.nivåorden(x -> System.out.print(x + ", "));
    }
}
