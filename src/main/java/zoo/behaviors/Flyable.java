package zoo.behaviors;

// Интерфейс для летающих существ
public interface Flyable {
    void fly();
    
    default void takeOff() {
        System.out.println("Взлетает...");
    }
    
    default void land() {
        System.out.println("Приземляется...");
    }
}