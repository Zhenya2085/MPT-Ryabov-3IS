package utils;

import java.util.*;

public class CardGame {
    
    // Перечисление мастей
    public enum Suit {
        HEARTS("Черви"), DIAMONDS("Бубны"), CLUBS("Трефы"), SPADES("Пики");
        
        private final String russianName;
        
        Suit(String russianName) {
            this.russianName = russianName;
        }
        
        public String getRussianName() {
            return russianName;
        }
    }
    
    // Перечисление достоинств
    public enum Rank {
        TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5),
        SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
        TEN("10", 10), JACK("Валет", 11), QUEEN("Дама", 12),
        KING("Король", 13), ACE("Туз", 14);
        
        private final String name;
        private final int value;
        
        Rank(String name, int value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() {
            return name;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    // Класс карты
    public static class Card {
        private final Suit suit;
        private final Rank rank;
        private boolean inDeck;
        
        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
            this.inDeck = true;
        }
        
        public Suit getSuit() {
            return suit;
        }
        
        public Rank getRank() {
            return rank;
        }
        
        public boolean isInDeck() {
            return inDeck;
        }
        
        public void setInDeck(boolean inDeck) {
            this.inDeck = inDeck;
        }
        
        @Override
        public String toString() {
            return rank.getName() + " " + suit.getRussianName();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Card card = (Card) obj;
            return suit == card.suit && rank == card.rank;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(suit, rank);
        }
    }
    
    // Класс колоды
    public static class Deck {
        private List<Card> cards;
        private List<Card> dealtCards; // Карты вне колоды
        
        public Deck() {
            cards = new ArrayList<>();
            dealtCards = new ArrayList<>();
            initializeDeck();
        }
        
        // Инициализация колоды (52 карты)
        private void initializeDeck() {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
        
        // Перетасовка колоды
        public void shuffle() {
            // Возвращаем все карты в колоду перед перетасовкой
            returnAllCards();
            
            Random random = new Random();
            for (int i = cards.size() - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                Card temp = cards.get(i);
                cards.set(i, cards.get(j));
                cards.set(j, temp);
            }
            System.out.println("Колода перетасована.");
        }
        
        // Сдача карты
        public Card dealCard() {
            if (cards.isEmpty()) {
                System.out.println("Колода пуста!");
                return null;
            }
            
            // Ищем первую карту, которая еще в колоде
            for (Card card : cards) {
                if (card.isInDeck()) {
                    card.setInDeck(false);
                    dealtCards.add(card);
                    return card;
                }
            }
            
            System.out.println("Все карты уже сданы!");
            return null;
        }
        
        // Сдача нескольких карт
        public List<Card> dealCards(int count) {
            List<Card> dealt = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Card card = dealCard();
                if (card != null) {
                    dealt.add(card);
                } else {
                    break;
                }
            }
            return dealt;
        }
        
        // Возврат карты в колоду
        public boolean returnCard(Card card) {
            if (dealtCards.contains(card)) {
                card.setInDeck(true);
                dealtCards.remove(card);
                return true;
            } else if (!cards.contains(card)) {
                // Карта не из этой колоды
                System.out.println("Эта карта не из этой колоды!");
                return false;
            } else if (card.isInDeck()) {
                System.out.println("Эта карта уже в колоде!");
                return false;
            }
            return false;
        }
        
        // Возврат всех карт в колоду
        public void returnAllCards() {
            for (Card card : dealtCards) {
                card.setInDeck(true);
            }
            dealtCards.clear();
            System.out.println("Все карты возвращены в колоду.");
        }
        
        // Показать колоду
        public void displayDeck() {
            System.out.println("\nКарты в колоде (" + getCardsInDeckCount() + "):");
            int count = 0;
            for (Card card : cards) {
                if (card.isInDeck()) {
                    System.out.print(card + " | ");
                    count++;
                    if (count % 4 == 0) {
                        System.out.println();
                    }
                }
            }
            System.out.println();
            
            System.out.println("Карты вне колоды (" + dealtCards.size() + "):");
            for (Card card : dealtCards) {
                System.out.print(card + " | ");
            }
            System.out.println();
        }
        
        // Количество карт в колоде
        public int getCardsInDeckCount() {
            int count = 0;
            for (Card card : cards) {
                if (card.isInDeck()) {
                    count++;
                }
            }
            return count;
        }
        
        // Количество карт вне колоды
        public int getDealtCardsCount() {
            return dealtCards.size();
        }
    }
    
    // Пример игры
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Deck deck = new Deck();
        
        System.out.println("=== ИГРА В КАРТЫ ===");
        System.out.println("Создана новая колода из 52 карт.");
        
        List<Card> player1Hand = new ArrayList<>();
        List<Card> player2Hand = new ArrayList<>();
        
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Показать колоду");
            System.out.println("2. Перетасовать колоду");
            System.out.println("3. Сдать карту игроку 1");
            System.out.println("4. Сдать карту игроку 2");
            System.out.println("5. Сдать 5 карт каждому игроку");
            System.out.println("6. Показать руки игроков");
            System.out.println("7. Вернуть карту в колоду");
            System.out.println("8. Вернуть все карты в колоду");
            System.out.println("9. Выход");
            System.out.print("Выберите действие: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 9");
                continue;
            }
            
            switch (choice) {
                case 1:
                    deck.displayDeck();
                    break;
                    
                case 2:
                    deck.shuffle();
                    break;
                    
                case 3:
                    Card card1 = deck.dealCard();
                    if (card1 != null) {
                        player1Hand.add(card1);
                        System.out.println("Игроку 1 сдана карта: " + card1);
                    }
                    break;
                    
                case 4:
                    Card card2 = deck.dealCard();
                    if (card2 != null) {
                        player2Hand.add(card2);
                        System.out.println("Игроку 2 сдана карта: " + card2);
                    }
                    break;
                    
                case 5:
                    System.out.println("Сдаем по 5 карт каждому игроку:");
                    List<Card> cards1 = deck.dealCards(5);
                    List<Card> cards2 = deck.dealCards(5);
                    
                    player1Hand.addAll(cards1);
                    player2Hand.addAll(cards2);
                    
                    System.out.println("Игрок 1 получил: " + cards1);
                    System.out.println("Игрок 2 получил: " + cards2);
                    break;
                    
                case 6:
                    System.out.println("\nРука игрока 1 (" + player1Hand.size() + " карт):");
                    for (Card card : player1Hand) {
                        System.out.println("  " + card);
                    }
                    
                    System.out.println("\nРука игрока 2 (" + player2Hand.size() + " карт):");
                    for (Card card : player2Hand) {
                        System.out.println("  " + card);
                    }
                    break;
                    
                case 7:
                    System.out.print("Введите карту для возврата (например, 'Туз Черви'): ");
                    String cardInput = scanner.nextLine();
                    
                    // Простая логика поиска карты
                    boolean found = false;
                    for (Card card : player1Hand) {
                        if (card.toString().equals(cardInput)) {
                            if (deck.returnCard(card)) {
                                player1Hand.remove(card);
                                System.out.println("Карта возвращена в колоду");
                            }
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        for (Card card : player2Hand) {
                            if (card.toString().equals(cardInput)) {
                                if (deck.returnCard(card)) {
                                    player2Hand.remove(card);
                                    System.out.println("Карта возвращена в колоду");
                                }
                                found = true;
                                break;
                            }
                        }
                    }
                    
                    if (!found) {
                        System.out.println("Карта не найдена в руках игроков");
                    }
                    break;
                    
                case 8:
                    deck.returnAllCards();
                    player1Hand.clear();
                    player2Hand.clear();
                    System.out.println("Все карты возвращены в колоду, руки игроков очищены");
                    break;
                    
                case 9:
                    System.out.println("Игра завершена.");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Неверный выбор. Введите число от 1 до 9.");
            }
        }
    }
}