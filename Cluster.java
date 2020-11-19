import java.util.LinkedList;

public class Cluster<T extends Meanable<T>> {
    LinkedList<T> elems;
    T middle;

    public Cluster() {
        elems = new LinkedList<T>();
    }

    public Cluster(T middle) {
        this();
        this.middle = middle;
    }

    public Cluster(Iterable<T> iterable){
        this();
        for(T elem: iterable)
            elems.add(elem);
    }

    public Cluster(Cluster<T> clust) {
        this(clust.elems);
    }

    public void push(T elem) {
        this.elems.add(elem);
    }

    public LinkedList<T> getElements() {
        return elems;
    }

    public T getMiddle() {
        return middle;
    }

    public T mean() {
        T res = middle.mean(elems);
        if (res == null) {
            return middle;
        }
        return res;
    }


    @Override
    public boolean equals(Object obj) {
        Cluster cls = (Cluster) obj;

        return middle.equals(cls.middle);
    }
}
