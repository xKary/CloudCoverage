package k_means;

/**
 * Interfaz para decir que a un objeto se le puede sacar la media a una
 * colección de ellos
 */
public interface Meanable<T> {
    public T mean(Iterable<T> a);
}
