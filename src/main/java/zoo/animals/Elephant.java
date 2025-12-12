package zoo.animals;

// Конкретный класс Слона
public class Elephant extends Mammal {
    private double tuskLength; // длина бивней в метрах
    private double trunkLength; // длина хобота в метрах
    
    public Elephant(String name, int age, double weight, String habitat, 
                    int furLength, boolean hasTail, double tuskLength, double trunkLength) {
        super(name, age, weight, habitat, furLength, hasTail);
        this.tuskLength = tuskLength;
        this.trunkLength = trunkLength;
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " трубит: Уууу-у-у!");
    }
    
    @Override
    public void eat() {
        System.out.println(name + " ест листья и фрукты хоботом");
    }
    
    @Override
    public void sleep() {
        System.out.println(name + " спит стоя");
    }
    
    @Override
    public void feedMilk() {
        System.out.println(name + " кормит слоненка молоком");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Длина бивней: " + tuskLength + " м, Длина хобота: " + trunkLength + " м");
    }
    
    public void sprayWater() {
        System.out.println(name + " обливается водой из хобота");
    }
    
    // Геттеры и сеттеры
    public double getTuskLength() { return tuskLength; }
    public double getTrunkLength() { return trunkLength; }
    
    public void setTuskLength(double tuskLength) { this.tuskLength = tuskLength; }
    public void setTrunkLength(double trunkLength) { this.trunkLength = trunkLength; }
}