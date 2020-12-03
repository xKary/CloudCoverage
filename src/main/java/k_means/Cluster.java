package k_means;
import java.util.LinkedList;

/**
 * Clase para modelar un cluster.
 * Guarda los elementos de un tipo T
 * Permite sacar la media de los elementos del Cluster
 */
public class Cluster<T extends Meanable<T>> {
    LinkedList<T> elems;
    T middle;

    /**
     * Constructor por omisión. Se crea un cluster con 0 elementos
     */
    public Cluster() {
        elems = new LinkedList<T>();
    }

    /**
     * Constructor que recibe el punto medio de este cluster
     * Se crea el cluster con 0 elementos
     * @param middle el elemento medio del cluster
     */
    public Cluster(T middle) {
        this();
        this.middle = middle;
    }

    /**
     * Constructor que recibe un iterable y llena el cluster con esos elementos
     * @param iterable una colección de elementos con la cual llenar al cluster
     */
    public Cluster(Iterable<T> iterable){
        this();
        for(T elem: iterable)
            elems.add(elem);
    }

    /**
     * Constructor de copia de Cluster
     * @clust el cluster a copiar
     */
    public Cluster(Cluster<T> clust) {
        this(clust.elems);
        this.middle = clust.middle;
    }

    /**
     * Agrega un elemento de tipo T al cluster
     * @param elem El elemento a agregar al cluster
     */
    public void push(T elem) {
        this.elems.add(elem);
    }

    /**
     * Obtiene los elementos del cluster
     * @return LinkedList<T> Los elementos de T en una lista ligada
     */
    public LinkedList<T> getElements() {
        return elems;
    }

    /**
     * Obtiene el objeto de tipo T que es el punto medio del cluster
     * @return T El punto medio del cluster
     */
    public T getMiddle() {
        return middle;
    }

    /**
     * Obtiene la media de los elementos del cluster
     * @return T El elemento con la media de los elementos del cluster
     */
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
