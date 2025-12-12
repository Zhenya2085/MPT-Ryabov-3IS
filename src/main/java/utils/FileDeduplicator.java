package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileDeduplicator {
    
    // Метод для удаления дубликатов
    public static DeduplicationResult removeDuplicates(String inputFile, String outputFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputFile));
        Set<String> uniqueLines = new LinkedHashSet<>(); // Сохраняем порядок
        List<String> duplicateLines = new ArrayList<>();
        
        // Собираем уникальные строки и дубликаты
        for (String line : lines) {
            if (!uniqueLines.add(line)) {
                duplicateLines.add(line);
            }
        }
        
        // Записываем уникальные строки в выходной файл
        Files.write(Paths.get(outputFile), uniqueLines);
        
        // Записываем информацию о дубликатах в тот же файл
        List<String> outputContent = new ArrayList<>(uniqueLines);
        outputContent.add("\n=== СТАТИСТИКА ===");
        outputContent.add("Всего строк в исходном файле: " + lines.size());
        outputContent.add("Уникальных строк: " + uniqueLines.size());
        outputContent.add("Удалено дубликатов: " + duplicateLines.size());
        
        if (!duplicateLines.isEmpty()) {
            outputContent.add("\nУдаленные дубликаты:");
            // Группируем дубликаты для лучшего отображения
            Map<String, Long> duplicateCounts = duplicateLines.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            
            for (Map.Entry<String, Long> entry : duplicateCounts.entrySet()) {
                outputContent.add("  '" + entry.getKey() + "' - " + entry.getValue() + " раз(а)");
            }
        }
        
        Files.write(Paths.get(outputFile), outputContent);
        
        return new DeduplicationResult(lines.size(), uniqueLines.size(), duplicateLines);
    }
    
    // Метод для восстановления сжатой версии
    public static void expandFile(String compressedFile, String outputFile, Map<String, Integer> duplicateCounts) throws IOException {
        List<String> compressedLines = Files.readAllLines(Paths.get(compressedFile));
        List<String> expandedLines = new ArrayList<>();
        
        for (String line : compressedLines) {
            if (!line.startsWith("=== СТАТИСТИКА ===")) {
                int count = duplicateCounts.getOrDefault(line, 1);
                for (int i = 0; i < count; i++) {
                    expandedLines.add(line);
                }
            } else {
                // Достигли секции статистики
                break;
            }
        }
        
        Files.write(Paths.get(outputFile), expandedLines);
    }
    
    // Вспомогательный класс для результата
    public static class DeduplicationResult {
        private int originalLineCount;
        private int uniqueLineCount;
        private List<String> duplicates;
        
        public DeduplicationResult(int originalLineCount, int uniqueLineCount, List<String> duplicates) {
            this.originalLineCount = originalLineCount;
            this.uniqueLineCount = uniqueLineCount;
            this.duplicates = duplicates;
        }
        
        public int getOriginalLineCount() { return originalLineCount; }
        public int getUniqueLineCount() { return uniqueLineCount; }
        public List<String> getDuplicates() { return duplicates; }
        public int getDuplicateCount() { return duplicates.size(); }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== УДАЛЕНИЕ ДУБЛИКАТОВ ИЗ ФАЙЛА ===");
        
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Удалить дубликаты из файла");
            System.out.println("2. Восстановить файл из сжатой версии");
            System.out.println("3. Выход");
            System.out.print("Выберите действие: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 3");
                continue;
            }
            
            switch (choice) {
                case 1:
                    try {
                        System.out.print("Введите путь к исходному файлу: ");
                        String inputFile = scanner.nextLine();
                        
                        System.out.print("Введите путь для выходного файла: ");
                        String outputFile = scanner.nextLine();
                        
                        DeduplicationResult result = removeDuplicates(inputFile, outputFile);
                        
                        System.out.println("\nРезультат обработки:");
                        System.out.println("Исходное количество строк: " + result.getOriginalLineCount());
                        System.out.println("Уникальных строк: " + result.getUniqueLineCount());
                        System.out.println("Удалено дубликатов: " + result.getDuplicateCount());
                        System.out.println("Результат сохранен в: " + outputFile);
                        
                        // Сохраняем информацию для возможного восстановления
                        saveRestorationInfo(inputFile + ".restore", result.getDuplicates());
                        
                    } catch (IOException e) {
                        System.out.println("Ошибка при работе с файлом: " + e.getMessage());
                    }
                    break;
                    
                case 2:
                    try {
                        System.out.print("Введите путь к сжатому файлу: ");
                        String compressedFile = scanner.nextLine();
                        
                        System.out.print("Введите путь для восстановленного файла: ");
                        String restoredFile = scanner.nextLine();
                        
                        System.out.print("Введите путь к файлу с информацией для восстановления: ");
                        String restoreInfoFile = scanner.nextLine();
                        
                        // Загружаем информацию о дубликатах
                        Map<String, Integer> duplicateCounts = loadRestorationInfo(restoreInfoFile);
                        
                        expandFile(compressedFile, restoredFile, duplicateCounts);
                        
                        System.out.println("Файл восстановлен: " + restoredFile);
                        
                    } catch (IOException e) {
                        System.out.println("Ошибка при восстановлении файла: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.println("Программа завершена.");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Неверный выбор. Введите число от 1 до 3.");
            }
        }
    }
    
    private static void saveRestorationInfo(String filename, List<String> duplicates) throws IOException {
        // Группируем дубликаты и считаем количество
        Map<String, Integer> duplicateCounts = new HashMap<>();
        for (String duplicate : duplicates) {
            duplicateCounts.put(duplicate, duplicateCounts.getOrDefault(duplicate, 0) + 1);
        }
        
        // Добавляем 1 к счетчику (так как одна копия осталась в файле)
        for (Map.Entry<String, Integer> entry : duplicateCounts.entrySet()) {
            duplicateCounts.put(entry.getKey(), entry.getValue() + 1);
        }
        
        List<String> lines = new ArrayList<>();
        lines.add("=== ИНФОРМАЦИЯ ДЛЯ ВОССТАНОВЛЕНИЯ ===");
        for (Map.Entry<String, Integer> entry : duplicateCounts.entrySet()) {
            lines.add(entry.getKey() + ":" + entry.getValue());
        }
        
        Files.write(Paths.get(filename), lines);
    }
    
    private static Map<String, Integer> loadRestorationInfo(String filename) throws IOException {
        Map<String, Integer> duplicateCounts = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(filename));
        
        for (String line : lines) {
            if (!line.startsWith("===")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    duplicateCounts.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        }
        
        return duplicateCounts;
    }
}