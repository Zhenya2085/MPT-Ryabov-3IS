package zoo.animals;

// Конкретный класс Змеи
public class Snake extends Reptile {
    private double length; // длина в метрах
    private String pattern; // узор на коже
    
    public Snake(String name, int age, double weight, String habitat, 
                 boolean hasScales, boolean isVenomous, double bodyTemperature, 
                 double length, String pattern) {
        super(name, age, weight, habitat, hasScales, isVenomous, bodyTemperature);
        this.length = length;
        this.pattern = pattern;
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " шипит: Шшш-ш-ш!");
    }
    
    @Override
    public void eat() {
        System.out.println(name + " заглатывает добычу целиком");
    }
    
    @Override
    public void sleep() {
        System.out.println(name + " спит свернувшись кольцом");
    }
    
    @Override
    public void shedSkin() {
        System.out.println(name + " сбрасывает кожу");
    }
    
    @Override
    public void swim() {
        System.out.println(name + " плавает, извиваясь в воде");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Длина: " + length + " м, Узор: " + pattern);
    }
    
    public void coil() {
        System.out.println(name + " сворачивается в клубок");
    }
    
    // Геттеры и сеттеры
    public double getLength() { return length; }
    public String getPattern() { return pattern; }
    
    public void setLength(double length) { this.length = length; }
    public void setPattern(String pattern) { this.pattern = pattern; }
}