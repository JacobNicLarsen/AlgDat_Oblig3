import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        ObligSBinTre<String> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        System.out.println(tre.antall());  // Utskrift: 0
    }
}
