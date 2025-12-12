package zoo.behaviors;

// Интерфейс для плавающих существ
public interface Swimmable {
    void swim();
    
    default void dive() {
        System.out.println("Ныряет...");
    }
    
    default void surface() {
        System.out.println("Всплывает...");
    }
}