package zoo.animals;

// Абстрактный класс млекопитающих
public abstract class Mammal extends Animal {
    protected int furLength; // длина меха в см
    protected boolean hasTail;
    
    public Mammal(String name, int age, double weight, String habitat, 
                  int furLength, boolean hasTail) {
        super(name, age, weight, habitat);
        this.furLength = furLength;
        this.hasTail = hasTail;
    }
    
    public abstract void feedMilk(); // Кормление молоком - особенность млекопитающих
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Длина меха: " + furLength + " см, Хвост: " + (hasTail ? "есть" : "нет"));
    }
    
    // Геттеры и сеттеры
    public int getFurLength() { return furLength; }
    public boolean hasTail() { return hasTail; }
    
    public void setFurLength(int furLength) { this.furLength = furLength; }
    public void setHasTail(boolean hasTail) { this.hasTail = hasTail; }
}