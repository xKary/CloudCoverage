package k_means;

/**
 * Interfaz para calcular la distanacia entre dos objetos de tipo T
 * Nos sirve para implementar la lambda que queramos
 */
public interface Distance<T> {
    public float dist(T a, T b);
}
