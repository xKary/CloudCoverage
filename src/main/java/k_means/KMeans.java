package k_means;
import java.util.LinkedList;
import java.util.ListIterator;

public class KMeans<T extends Meanable<T>> {
    LinkedList<T> elementos;

    public KMeans() {
        elementos = new LinkedList<T>();
    }

    public KMeans(Iterable<T> iterable){
        this();
        for(T elem: iterable)
            elementos.add(elem);
    }

    public KMeans(KMeans<T> separador) {
        this(separador.elementos);
    }

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

    public LinkedList<Cluster<T>> getClusters(LinkedList<T> initVals, Distance<T> measure) {
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
                new_clusters.push(new Cluster(cl.mean()));
            }
            prevs = clusters;
            clusters = new_clusters;

            means_r_same = compare_means(clusters, prevs);
            n += 1;
        } while(!means_r_same && n <= 500);

        return prevs;
    }


    // public LinkedList<Cluster<T>> getClusters(int k, Distance<T> measure) {
    // }

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
