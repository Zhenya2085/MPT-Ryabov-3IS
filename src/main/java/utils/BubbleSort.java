package utils;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class BubbleSort {
    
    // Классическая сортировка пузырьком
    public static void bubbleSort(int[] array) {
        int n = array.length;
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            
            for (int j = 0; j < n - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    // Меняем элементы местами
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            
            // Если за проход не было обменов, массив отсортирован
            if (!swapped) {
                break;
            }
        }
    }
    
    // Оптимизированная версия с флагом
    public static void bubbleSortOptimized(int[] array) {
        int n = array.length;
        boolean swapped;
        int lastSwapIndex = n - 1;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            int currentSwapIndex = 0;
            
            for (int j = 0; j < lastSwapIndex; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                    currentSwapIndex = j;
                }
            }
            
            lastSwapIndex = currentSwapIndex;
            
            if (!swapped) {
                break;
            }
        }
    }
    
    // Генерация случайного массива
    public static int[] generateRandomArray(int size, int min, int max) {
        Random random = new Random();
        int[] array = new int[size];
        
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }
        
        return array;
    }
    
    // Вывод массива
    public static void printArray(int[] array, String message) {
        System.out.print(message + ": ");
        System.out.println(Arrays.toString(array));
    }
    
    // Проверка отсортированности массива
    public static boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== СОРТИРОВКА ПУЗЫРЬКОМ ===");
        
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Сгенерировать случайный массив и отсортировать");
            System.out.println("2. Ввести массив вручную");
            System.out.println("3. Сравнить классическую и оптимизированную версии");
            System.out.println("4. Выход");
            System.out.print("Выберите опцию: ");
            
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
            
            switch (choice) {
                case 1:
                    System.out.print("Введите размер массива: ");
                    int size = Integer.parseInt(scanner.nextLine());
                    System.out.print("Введите минимальное значение: ");
                    int min = Integer.parseInt(scanner.nextLine());
                    System.out.print("Введите максимальное значение: ");
                    int max = Integer.parseInt(scanner.nextLine());
                    
                    int[] randomArray = generateRandomArray(size, min, max);
                    int[] arrayCopy = Arrays.copyOf(randomArray, randomArray.length);
                    
                    printArray(randomArray, "Исходный массив");
                    
                    long startTime = System.nanoTime();
                    bubbleSort(randomArray);
                    long endTime = System.nanoTime();
                    
                    printArray(randomArray, "Отсортированный массив");
                    System.out.println("Время сортировки: " + (endTime - startTime) + " наносекунд");
                    System.out.println("Массив отсортирован: " + isSorted(randomArray));
                    break;
                    
                case 2:
                    System.out.print("Введите элементы массива через пробел: ");
                    String input = scanner.nextLine();
                    String[] parts = input.split("\\s+");
                    
                    int[] manualArray = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        manualArray[i] = Integer.parseInt(parts[i]);
                    }
                    
                    int[] manualArrayCopy = Arrays.copyOf(manualArray, manualArray.length);
                    
                    printArray(manualArray, "Исходный массив");
                    
                    startTime = System.nanoTime();
                    bubbleSort(manualArray);
                    endTime = System.nanoTime();
                    
                    printArray(manualArray, "Отсортированный массив");
                    System.out.println("Время сортировки: " + (endTime - startTime) + " наносекунд");
                    break;
                    
                case 3:
                    System.out.print("Введите размер массива для сравнения: ");
                    size = Integer.parseInt(scanner.nextLine());
                    
                    int[] testArray1 = generateRandomArray(size, 1, 1000);
                    int[] testArray2 = Arrays.copyOf(testArray1, testArray1.length);
                    
                    System.out.println("\nСравнение алгоритмов:");
                    
                    // Классическая сортировка
                    startTime = System.nanoTime();
                    bubbleSort(testArray1);
                    endTime = System.nanoTime();
                    long classicTime = endTime - startTime;
                    System.out.println("Классическая сортировка: " + classicTime + " наносекунд");
                    
                    // Оптимизированная сортировка
                    startTime = System.nanoTime();
                    bubbleSortOptimized(testArray2);
                    endTime = System.nanoTime();
                    long optimizedTime = endTime - startTime;
                    System.out.println("Оптимизированная сортировка: " + optimizedTime + " наносекунд");
                    
                    System.out.println("Разница: " + (classicTime - optimizedTime) + " наносекунд (" +
                                     String.format("%.2f", ((double)(classicTime - optimizedTime) / classicTime * 100)) + "%)");
                    break;
                    
                default:
                    System.out.println("Неверный выбор. Введите число от 1 до 4.");
            }
        }
        
        scanner.close();
        System.out.println("Программа завершена.");
    }
}