package k_means;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Clase que usa el algoritmo de K-Means para separar una colección de objetos
 * en diferentes clusters o grupos
 */
public class KMeans<T extends Meanable<T>> {
    LinkedList<T> elementos;

    /**
     * Constructor por omisión.
     * Crea al cluster con 0 elementos.
     */
    public KMeans() {
        elementos = new LinkedList<T>();
    }

    /**
     * Constructor que crea al separador con una colleción de objetos de tipo T
     * @param iterable Colleción de datos con la cual crear al KMeans
     */
    public KMeans(Iterable<T> iterable){
        this();
        for(T elem: iterable)
            elementos.add(elem);
    }

    /**
     * Constructor de copia
     * @separador El objeto KMeans a copiar
     */
    public KMeans(KMeans<T> separador) {
        this(separador.elementos);
    }

    /**
     * Separa la colección del objeto en diferentes clusters, dependiendo
     * de la función measure que se le pase
     * @param clusters Se llenan estos clusters con los objetos de KMeans.
     * No es funcional
     * @param measure al función con la que se va a medir la distancia de
     * cada elemento al cluster
     */
    public void fillClusters(LinkedList<Cluster<T>> clusters, Distance<T> measure) {
        for (T elem: elementos) {
            Cluster min_cluster = clusters.peek();
            float min_distance = measure.dist(elem, (T)min_cluster.getMiddle());

            for (Cluster cl: clusters) {
                float distance = measure.dist(elem, (T)cl.getMiddle());
                if(distance < min_distance) {
                    min_distance = distance;
                    min_cluster = cl;
                }
            }

            min_cluster.push(elem);
        }
    }

    /**
     * Separa la colección de objetos que tiene el KMeans. Los separa dependiendo
     * de unos (initVals) valores iniciales con una función (measure).
     * @param initVals Una lista ligada con los valores iniciales de los clusters
     * @param measure la función con la cual medir la distancia entre los
     * elementos y la media del cluster
     * @return LinkedList<LinkedList<T>> Lista ligada con una lista ligada de
     * los elementos ya separados
     */
    public LinkedList<LinkedList<T>> getClusters(LinkedList<T> initVals, Distance<T> measure) {
        LinkedList<Cluster<T>> prevs;
        LinkedList<Cluster<T>> new_clusters;

        LinkedList<Cluster<T>> clusters = new LinkedList<Cluster<T>>();
        for(T mean: initVals) {
            clusters.add(new Cluster(mean));
        }

        int n = 0;
        boolean means_r_same = false;
        do {
            fillClusters(clusters, measure);

            new_clusters = new LinkedList<Cluster<T>>();
            for(Cluster cl: clusters) {
                new_clusters.add(new Cluster(cl.mean()));
            }
            prevs = clusters;
            clusters = new_clusters;

            means_r_same = compare_means(clusters, prevs);
            n += 1;
        } while(!means_r_same && n <= 25);

        LinkedList<LinkedList<T>> res = new LinkedList<LinkedList<T>>();
        for (Cluster<T> cluster: prevs) {
            res.add(cluster.getElements());
        }
        return res;
    }


    // public LinkedList<Cluster<T>> getClusters(int k, Distance<T> measure) {
    // }

    /**
     * Compara dos clusters y ve si son iguales
     * @param list1 Lista de clusters a comparar
     * @param list2 Segunda lista de clusters a comparar
     * @return boolean Verdadero si son iguales
     */
    public boolean compare_means(LinkedList<Cluster<T>> list1, LinkedList<Cluster<T>> list2) {
        ListIterator<Cluster<T>> iter1 = list1.listIterator();
        ListIterator<Cluster<T>> iter2 = list2.listIterator();

        while(iter1.hasNext()) {
            Cluster<T> cls1 = iter1.next();
            Cluster<T> cls2 = iter2.next();

            if(!cls1.equals(cls2)) {
                return false;
            }
        }
        return true;
    }
}
