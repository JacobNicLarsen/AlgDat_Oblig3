import java.util.Comparator;
import java.util.Iterator;

public class ObligSBinTre<T> implements Beholder<T>{

    private int endringer;
    private int antall;

    ObligSBinTre(Comparator<? super T> e){
        endringer = 0;
        antall = 0;

    }


    @Override
    public boolean leggInn(T verdi) {
        return false;
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
