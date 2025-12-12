package zoo.animals;

// Абстрактный базовый класс для всех животных
public abstract class Animal {
    protected String name;
    protected int age;
    protected double weight;
    protected String habitat;
    
    public Animal(String name, int age, double weight, String habitat) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.habitat = habitat;
    }
    
    public abstract void makeSound();
    public abstract void eat();
    public abstract void sleep();
    
    public void displayInfo() {
        System.out.println("Имя: " + name + ", Возраст: " + age + " лет, Вес: " + weight + " кг");
        System.out.println("Среда обитания: " + habitat);
    }
    
    // Геттеры и сеттеры
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public String getHabitat() { return habitat; }
    
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHabitat(String habitat) { this.habitat = habitat; }
}