package zoo;

import zoo.animals.*;
import zoo.behaviors.Flyable;
import zoo.behaviors.Swimmable;
import java.util.ArrayList;
import java.util.List;

public class ZooDemo {
    public static void main(String[] args) {
        System.out.println("=== ДЕМОНСТРАЦИЯ ЗООПАРКА ===\n");
        
        // Создаем животных
        Lion simba = new Lion("Симба", 5, 190, "Саванна", 10, true, "золотистый", true);
        Elephant dumbo = new Elephant("Дамбо", 10, 5000, "Джунгли", 2, true, 1.5, 2.0);
        Parrot rio = new Parrot("Рио", 3, 0.5, "Тропический лес", 0.8, "крючковатый", "разноцветный", true);
        Penguin happy = new Penguin("Хэппи", 4, 15, "Антарктида", 0.6, "острый", 50);
        Snake kaa = new Snake("Каа", 7, 25, "Джунгли", true, false, 25, 3.5, "полосатый");
        Turtle donatello = new Turtle("Донателло", 50, 30, "Пруд", true, false, 20, 40, true);
        
        // Добавляем животных в зоопарк
        List<Animal> zoo = new ArrayList<>();
        zoo.add(simba);
        zoo.add(dumbo);
        zoo.add(rio);
        zoo.add(happy);
        zoo.add(kaa);
        zoo.add(donatello);
        
        // Демонстрация полиморфизма
        System.out.println("=== Все животные в зоопарке ===");
        for (Animal animal : zoo) {
            System.out.println("\n--- " + animal.getName() + " ---");
            animal.displayInfo();
            animal.makeSound();
            animal.eat();
            animal.sleep();
            
            // Проверка интерфейсов
            if (animal instanceof Flyable) {
                ((Flyable) animal).fly();
            }
            if (animal instanceof Swimmable) {
                ((Swimmable) animal).swim();
            }
            
            // Специфичные методы для каждого типа
            if (animal instanceof Lion) {
                ((Lion) animal).hunt();
            } else if (animal instanceof Elephant) {
                ((Elephant) animal).sprayWater();
            } else if (animal instanceof Parrot) {
                ((Parrot) animal).mimicSound("Пока-пока!");
            } else if (animal instanceof Penguin) {
                ((Penguin) animal).slide();
            } else if (animal instanceof Snake) {
                ((Snake) animal).coil();
            } else if (animal instanceof Turtle) {
                ((Turtle) animal).hideInShell();
            }
        }
        
        // Демонстрация особенностей классов
        System.out.println("\n=== Особенности животных ===");
        simba.feedMilk();
        rio.layEggs();
        kaa.shedSkin();
        
        // Работа с интерфейсами
        System.out.println("\n=== Демонстрация интерфейсов ===");
        
        List<Flyable> flyingAnimals = new ArrayList<>();
        flyingAnimals.add(rio);
        flyingAnimals.add(happy); // Пингвин тоже реализует Flyable, хотя не летает
        
        System.out.println("\nЛетающие животные:");
        for (Flyable flyer : flyingAnimals) {
            flyer.takeOff();
            flyer.fly();
            flyer.land();
            System.out.println();
        }
        
        List<Swimmable> swimmingAnimals = new ArrayList<>();
        swimmingAnimals.add(happy);
        swimmingAnimals.add(kaa);
        swimmingAnimals.add(donatello);
        
        System.out.println("Плавающие животные:");
        for (Swimmable swimmer : swimmingAnimals) {
            swimmer.dive();
            swimmer.swim();
            swimmer.surface();
            System.out.println();
        }
    }
}