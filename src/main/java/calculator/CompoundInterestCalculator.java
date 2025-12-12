package calculator;

import java.util.Scanner;

public class CompoundInterestCalculator {
    
    // Расчет будущей стоимости
    public static double calculateFutureValue(double principal, double rate, int periods) {
        return principal * Math.pow(1 + rate / 100, periods);
    }
    
    // Расчет необходимой процентной ставки
    public static double calculateRequiredRate(double presentValue, double futureValue, int periods) {
        return (Math.pow(futureValue / presentValue, 1.0 / periods) - 1) * 100;
    }
    
    // Расчет необходимого количества периодов
    public static double calculateRequiredPeriods(double presentValue, double futureValue, double rate) {
        return Math.log(futureValue / presentValue) / Math.log(1 + rate / 100);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== КАЛЬКУЛЯТОР СЛОЖНОГО ПРОЦЕНТА ===");
        System.out.println("1. Расчет будущей стоимости");
        System.out.println("2. Расчет необходимой процентной ставки");
        System.out.println("3. Расчет необходимого количества периодов");
        System.out.println("4. Выход");
        
        while (true) {
            System.out.print("\nВыберите операцию (1-4): ");
            int choice;
            
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 4");
                continue;
            }
            
            if (choice == 4) {
                break;
            }
            
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Введите начальную сумму: ");
                        double principal = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Введите годовую процентную ставку (%): ");
                        double rate = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Введите количество периодов (лет): ");
                        int periods = Integer.parseInt(scanner.nextLine());
                        
                        double futureValue = calculateFutureValue(principal, rate, periods);
                        System.out.printf("Будущая стоимость: %.2f\n", futureValue);
                        System.out.printf("Общий процентный доход: %.2f\n", futureValue - principal);
                        break;
                        
                    case 2:
                        System.out.print("Введите начальную сумму: ");
                        double pv = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Введите желаемую будущую сумму: ");
                        double fv = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Введите количество периодов (лет): ");
                        int n = Integer.parseInt(scanner.nextLine());
                        
                        if (fv <= pv) {
                            System.out.println("Ошибка: будущая сумма должна быть больше начальной");
                            break;
                        }
                        
                        double requiredRate = calculateRequiredRate(pv, fv, n);
                        System.out.printf("Необходимая годовая процентная ставка: %.2f%%\n", requiredRate);
                        break;
                        
                    case 3:
                        System.out.print("Введите начальную сумму: ");
                        pv = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Введите желаемую будущую сумму: ");
                        fv = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Введите годовую процентную ставку (%): ");
                        rate = Double.parseDouble(scanner.nextLine());
                        
                        if (fv <= pv) {
                            System.out.println("Ошибка: будущая сумма должна быть больше начальной");
                            break;
                        }
                        
                        double requiredPeriods = calculateRequiredPeriods(pv, fv, rate);
                        System.out.printf("Необходимое количество периодов: %.1f лет\n", requiredPeriods);
                        break;
                        
                    default:
                        System.out.println("Неверный выбор. Введите число от 1 до 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        
        scanner.close();
        System.out.println("Калькулятор завершил работу.");
    }
}