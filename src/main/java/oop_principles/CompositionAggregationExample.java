package oop_principles;

import java.util.ArrayList;
import java.util.List;

// Примеры композиции и агрегации
public class CompositionAggregationExample {
    
    // Пример композиции (сильная связь, жизненный цикл совпадает)
    static class Engine {
        private String type;
        private int horsepower;
        
        public Engine(String type, int horsepower) {
            this.type = type;
            this.horsepower = horsepower;
            System.out.println("Создан двигатель: " + type + " (" + horsepower + " л.с.)");
        }
        
        public void start() {
            System.out.println("Двигатель запущен");
        }
        
        public void stop() {
            System.out.println("Двигатель остановлен");
        }
    }
    
    static class Car {
        // Композиция - двигатель создается и уничтожается вместе с автомобилем
        private Engine engine;
        private String model;
        
        public Car(String model, String engineType, int horsepower) {
            this.model = model;
            this.engine = new Engine(engineType, horsepower); // Композиция
            System.out.println("Создан автомобиль: " + model);
        }
        
        public void startCar() {
            System.out.print(model + ": ");
            engine.start();
        }
        
        public void stopCar() {
            System.out.print(model + ": ");
            engine.stop();
        }
        
        // Не можем заменить двигатель на другой - композиция
        // public void setEngine(Engine newEngine) {
        //     this.engine = newEngine;
        // }
    }
    
    // Пример агрегации (слабая связь, независимый жизненный цикл)
    static class Wheel {
        private String brand;
        private int diameter;
        
        public Wheel(String brand, int diameter) {
            this.brand = brand;
            this.diameter = diameter;
            System.out.println("Создано колесо: " + brand + " (" + diameter + " дюймов)");
        }
        
        public void rotate() {
            System.out.println("Колесо " + brand + " вращается");
        }
    }
    
    static class CarWithAggregation {
        private String model;
        private List<Wheel> wheels; // Агрегация
        
        public CarWithAggregation(String model) {
            this.model = model;
            this.wheels = new ArrayList<>();
            System.out.println("Создан автомобиль с агрегацией: " + model);
        }
        
        // Агрегация - можем добавлять и удалять колеса
        public void addWheel(Wheel wheel) {
            if (wheels.size() < 4) {
                wheels.add(wheel);
                System.out.println("Добавлено колесо к " + model);
            } else {
                System.out.println("У автомобиля уже 4 колеса");
            }
        }
        
        public void removeWheel(Wheel wheel) {
            if (wheels.remove(wheel)) {
                System.out.println("Удалено колесо из " + model);
            }
        }
        
        public void drive() {
            System.out.println(model + " едет на колесах:");
            for (Wheel wheel : wheels) {
                wheel.rotate();
            }
        }
    }
    
    // Дополнительный пример: университет (агрегация) и факультеты (композиция)
    static class Department {
        private String name;
        
        public Department(String name) {
            this.name = name;
            System.out.println("Создан факультет: " + name);
        }
    }
    
    static class Student {
        private String name;
        
        public Student(String name) {
            this.name = name;
            System.out.println("Создан студент: " + name);
        }
    }
    
    static class University {
        // Композиция - факультеты создаются вместе с университетом
        private List<Department> departments;
        
        // Агрегация - студенты существуют независимо от университета
        private List<Student> students;
        
        public University() {
            this.departments = new ArrayList<>();
            this.students = new ArrayList<>();
            
            // При создании университета создаем факультеты (композиция)
            departments.add(new Department("Факультет информатики"));
            departments.add(new Department("Факультет математики"));
            System.out.println("Создан университет с факультетами");
        }
        
        // Агрегация - студенты могут поступать и отчисляться
        public void enrollStudent(Student student) {
            students.add(student);
            System.out.println("Студент " + student + " зачислен");
        }
        
        public void expelStudent(Student student) {
            if (students.remove(student)) {
                System.out.println("Студент " + student + " отчислен");
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== КОМПОЗИЦИЯ (сильная связь) ===");
        System.out.println("Объект создается и уничтожается вместе с родителем");
        Car car1 = new Car("Toyota Camry", "V6", 249);
        car1.startCar();
        car1.stopCar();
        
        System.out.println("\n=== АГРЕГАЦИЯ (слабая связь) ===");
        System.out.println("Объекты существуют независимо");
        
        // Создаем колеса отдельно
        Wheel wheel1 = new Wheel("Michelin", 17);
        Wheel wheel2 = new Wheel("Michelin", 17);
        Wheel wheel3 = new Wheel("Michelin", 17);
        Wheel wheel4 = new Wheel("Michelin", 17);
        Wheel spareWheel = new Wheel("Bridgestone", 17); // Запасное колесо
        
        CarWithAggregation car2 = new CarWithAggregation("Honda Civic");
        
        // Добавляем колеса (агрегация)
        car2.addWheel(wheel1);
        car2.addWheel(wheel2);
        car2.addWheel(wheel3);
        car2.addWheel(wheel4);
        
        car2.drive();
        
        // Можем заменить колесо
        System.out.println("\nЗамена колеса:");
        car2.removeWheel(wheel4);
        car2.addWheel(spareWheel);
        car2.drive();
        
        System.out.println("\n=== Пример университета ===");
        University university = new University();
        
        // Студенты существуют независимо
        Student student1 = new Student("Иван Иванов");
        Student student2 = new Student("Петр Петров");
        
        university.enrollStudent(student1);
        university.enrollStudent(student2);
        
        // Студент может быть отчислен, но продолжит существование
        university.expelStudent(student1);
    }
}