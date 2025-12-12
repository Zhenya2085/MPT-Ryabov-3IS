package zoo.behaviors;

// Интерфейс для бегающих существ
public interface Runnable {
    void run();
    
    default void startRunning() {
        System.out.println("Начинает бег...");
    }
    
    default void stopRunning() {
        System.out.println("Останавливается...");
    }
}