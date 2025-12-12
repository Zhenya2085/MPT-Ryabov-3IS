package calculator;

import java.util.Scanner;

public class StringCalculator {
    
    public static double calculate(String expression) {
        // Удаляем пробелы
        expression = expression.replaceAll("\\s+", "");
        
        // Разбиваем выражение на части
        String[] parts = expression.split("[+\\-*/]");
        
        if (parts.length != 2) {
            throw new IllegalArgumentException("Неверный формат выражения. Ожидается: число оператор число");
        }
        
        double num1 = Double.parseDouble(parts[0]);
        double num2 = Double.parseDouble(parts[1]);
        
        // Определяем оператор
        char operator = expression.charAt(parts[0].length());
        
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                if (num2 == 0) {
                    throw new ArithmeticException("Деление на ноль!");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== СТРОЧНЫЙ КАЛЬКУЛЯТОР ===");
        System.out.println("Введите выражение в формате: число оператор число");
        System.out.println("Пример: 2 * 6.5 или 10 + 5 или 15 / 3");
        System.out.println("Доступные операторы: + - * /");
        System.out.println("Введите 'exit' для выхода");
        
        while (true) {
            System.out.print("\nВведите выражение: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            
            try {
                double result = calculate(input);
                System.out.println("Результат: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
        
        scanner.close();
        System.out.println("Калькулятор завершил работу.");
    }
}