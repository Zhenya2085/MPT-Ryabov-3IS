package utils;

import java.util.Scanner;

public class LeapYearChecker {
    
    public static boolean isLeapYear(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Год должен быть положительным числом");
        }
        
        // Правило високосного года:
        // 1. Год делится на 4
        // 2. Если год делится на 100, он должен делиться на 400
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== ПРОВЕРКА ВИСОКОСНОГО ГОДА ===");
        System.out.println("Правила:");
        System.out.println("1. Год високосный, если он делится на 4");
        System.out.println("2. Но если год делится на 100, он должен делиться на 400");
        System.out.println("Примеры: 2000 - високосный, 1900 - не високосный");
        System.out.println("Введите 'exit' для выхода\n");
        
        while (true) {
            System.out.print("Введите год: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            
            try {
                int year = Integer.parseInt(input);
                
                if (isLeapYear(year)) {
                    System.out.println(year + " - ВИСОКОСНЫЙ год");
                } else {
                    System.out.println(year + " - НЕ високосный год");
                }
                
                // Дополнительная информация
                System.out.println("Количество дней в феврале: " + (isLeapYear(year) ? "29" : "28"));
                
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка: " + e.getMessage());
            }
        }
        
        scanner.close();
        System.out.println("Программа завершена.");
    }
}