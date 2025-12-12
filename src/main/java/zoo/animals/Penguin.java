package zoo.animals;

import zoo.behaviors.Swimmable;

// Конкретный класс Пингвина
public class Penguin extends Bird implements Swimmable {
    private double swimmingDepth; // максимальная глубина погружения
    
    public Penguin(String name, int age, double weight, String habitat, 
                   double wingspan, String beakType, double swimmingDepth) {
        super(name, age, weight, habitat, wingspan, beakType);
        this.swimmingDepth = swimmingDepth;
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " кричит: Ква-ква!");
    }
    
    @Override
    public void eat() {
        System.out.println(name + " ест рыбу");
    }
    
    @Override
    public void sleep() {
        System.out.println(name + " спит стоя в колонии");
    }
    
    @Override
    public void layEggs() {
        System.out.println(name + " откладывает яйца на льду");
    }
    
    @Override
    public void fly() {
        System.out.println(name + " не умеет летать, но отлично плавает!");
    }
    
    @Override
    public void swim() {
        System.out.println(name + " плавает под водой на глубине " + swimmingDepth + " метров");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Макс. глубина погружения: " + swimmingDepth + " м");
    }
    
    public void slide() {
        System.out.println(name + " скользит на животе по льду");
    }
    
    // Геттер и сеттер
    public double getSwimmingDepth() { return swimmingDepth; }
    public void setSwimmingDepth(double swimmingDepth) { this.swimmingDepth = swimmingDepth; }
}