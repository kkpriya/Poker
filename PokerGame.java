package PJ4;

import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.google.com/ig/directory?type=gadgets&url=www.labpixies.com/campaigns/videopoker/videopoker.xml
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks, 
 * 	Queens, Kings, or Aces. Lower pairs do not pay out. 
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */

/* This is the main poker game class.
 * It uses Deck and Card objects to implement poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */

public class PokerGame {

	// default constant values
	private static final int startingBalance = 100;
	private static final int numberOfCards = 5;
	private static final int ACE = 1;

	// default constant payout value and playerHand types
	private static final int[] multipliers = { 1, 2, 3, 5, 6, 9, 25, 50, 250 };
	private static final String[] goodHandTypes = { "Royal Pair", "Two Pairs", "Three of a Kind", "Straight", "Flush",
			"Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

	// must use only one deck
	private static final Deck oneDeck = new Deck(1);

	// holding current poker 5-card hand, balance, bet
	private List<Card> playerHand;
	private int playerBalance;
	private int playerBet;

	/** default constructor, set balance = startingBalance */
	public PokerGame() {
		this(startingBalance);
	}

	/** constructor, set given balance */
	public PokerGame(int balance) {
		this.playerBalance = balance;
	}

	/**
	 * This display the payout table based on multipliers and goodHandTypes
	 * arrays
	 */
	private void showPayoutTable() {
		System.out.println("\n\n");
		System.out.println("Payout Table   	      Multiplier   ");
		System.out.println("=======================================");
		int size = multipliers.length;
		for (int i = size - 1; i >= 0; i--) {
			System.out.println(goodHandTypes[i] + "\t|\t" + multipliers[i]);
		}
		System.out.println("\n\n");
	}

	/**
	 * Check current playerHand using multipliers and goodHandTypes arrays Must
	 * print yourHandType (default is "Sorry, you lost") at the end of function.
	 * This can be checked by testCheckHands() and main() method.
	 */
	private void checkHands(){ 
		Collections.sort(this.playerHand, new Comparator<Card>() {
	         public int compare(Card c1, Card c2){
	           int rank1 = c1.getRank();
	           int rank2 = c2.getRank();
	           if (rank1 < rank2) {
	             return -1;
	           } else{
	             return 1;
	           }
	         }
	         });
	       displayHand();
	       
	       int handtype = 0;
	       if (isFlush() && isStraight()) {
	         if (playerHand.get(0).getRank() == 10 || playerHand.get(0).getRank() == 1)
	           handtype = 8; //Royal Flush
	         else{
	           handtype = 7; //Straight Flush
	         }
	       }
	       else if(isFourofAKind()){
	           handtype = 6;
	       }
	       else if(isFullHouse()){
	           handtype = 5;
	       }
	       else if(isFlush()){
	           handtype = 4;
	       }
	       else if(isStraight()){
	           handtype = 3;
	       }
	       else if(isThreeofAKind()){
	           handtype = 2;
	       }
	       else if(isTwoPair()){
	           handtype = 1;
	       }
	       else if(isRoyalPair()){
	           handtype = 0;
	       }
	       else{
	           handtype = -1;
	       }
	 
	       switch (handtype){
	         case -1:
	           System.out.println("\nSorry, you lost!");
	           break;
	         default:
	           int bonus = multipliers[handtype];
	           System.out.print("\n");
	           System.out.printf("%s \n",goodHandTypes[handtype]);
	           this.playerBalance += (this.playerBet * bonus);
	           break;
	       }
	     }
	 
		

	/*************************************************
	 * add new private methods here ....
	 *
	 *************************************************/

	private boolean isFlush() {
		boolean result = true;
		for (int i = 0; i < numberOfCards-1; i++) {
			if (playerHand.get(i).getSuit() != playerHand.get(i + 1).getSuit()) {
				result = false;
			}
		}
		return result;
	}

	private boolean isStraight() {
		int consectCards = 0;
	       int firstRank = playerHand.get(0).getRank();
	       int rank = 0;
	       for (int i = 0; i < numberOfCards - 1; i++) {
	         // if the next card is one greater than the current card, then the current hand is straight
	         if (playerHand.get(i).getRank() + 1 == playerHand.get(i + 1).getRank()) {
	           consectCards++;
	           rank = playerHand.get(i + 1).getRank();
	         }
	         if (consectCards == 3 && rank == 13 && firstRank == 1) {
	           return true;
	         }
	       }
	 
	       return consectCards == 4;
	     }

	private boolean isFourofAKind() {
		boolean result = false;
		if (playerHand.get(0).getRank() == playerHand.get(1).getRank()
				|| playerHand.get(numberOfCards - 2).getRank() == playerHand.get(numberOfCards - 1).getRank()) {
			int start = playerHand.get(0).getRank() == playerHand.get(1).getRank() ? 0 : 1;
			int end = playerHand.get(0).getRank() == playerHand.get(1).getRank() ? (numberOfCards - 1) : numberOfCards;
			for (int i = start; i < end -1; i++) {
				if (playerHand.get(i).getRank() != playerHand.get(i + 1).getRank()) {
					result = false;
				}
			} result = true;
		}
		return result;

	}

	private boolean isFullHouse() {
		return isThreeofAKind() && isTwoPair() && isTwoofAKindFinder() != isThreeofAKindFinder();

	}

	private boolean isThreeofAKind() {
		return isThreeofAKindFinder() != 0;

	}

	private int isThreeofAKindFinder() {
		int cardsWithSameRank = 0;
		int rank = 0;
		for (int i = 0; i < numberOfCards - 1; i++) {
			if (playerHand.get(i).getRank() == playerHand.get(i + 1).getRank()) {
				cardsWithSameRank++;
			} 
		}
		if (cardsWithSameRank == 2) {
			return rank;
		} else {
			cardsWithSameRank = 0;
		}
		return 0;
	}

	private boolean isTwoOfaKind() {
		return isTwoofAKindFinder() != 0;

	}

	private int isTwoofAKindFinder() {
		int cardsWithSameRank = 0;
		int rank = 0;
		for (int i = 0; i < numberOfCards - 1; i++) {
			if (playerHand.get(i).getRank() == playerHand.get(i + 1).getRank()) {
				cardsWithSameRank += 1;
				rank = playerHand.get(i).getRank();
				if (cardsWithSameRank > 1) {
					cardsWithSameRank = 0;
				    rank = 0;
				}
			}
		}
		if (cardsWithSameRank == 1) {
			return rank;
		} return 0;
	}
	private boolean isTwoPair() {
	       int pairCnt = 0;
	       for(int i = 0; i < numberOfCards - 1; ) {
	         if (playerHand.get(i).getRank() == playerHand.get(i + 1).getRank()) {
	           pairCnt++;
	           i = i + 2;        
	         } else {
	           i++;
	         }
	       }
	 
	       return pairCnt == 2;
	     }
	 
	     private boolean isRoyalPair() {
	       for(int i = 0; i < numberOfCards - 1; i++) {
	         if (playerHand.get(i).getRank() == playerHand.get(i + 1).getRank() && (playerHand.get(i).getRank() == ACE || playerHand.get(i).getRank() > 10)) {
	           return true;
	         }
	       }
	 
	       return false;
	     }

	private void showBalance() {
		System.out.printf("Your balance is %d \n", playerBalance);
	}

	private void getBetAndVerify() {
		System.out.printf("Please enter your bet:\n", playerBalance);
		Scanner get_bet = new Scanner(System.in);

		do {
			this.playerBet = get_bet.nextInt();
		} while (!(this.playerBet > 0) || !(this.playerBet <= this.playerBalance));
	}

	private void updateBalance() {
		this.playerBalance -= this.playerBet;
	}

	private void displayHand() {
		int currentSize = playerHand.size();
		for (int k = 0; k < currentSize; k++) {
			System.out.print(playerHand.get(k).toString());
		}
	}

	private void updateCards() {
		String userInput; 
	       List<String> keep_list;
	       List<Card> replacementHand = new ArrayList<Card>(); 
	       int keep_size;
	       int test_value;
	       boolean enter_new_number = false; 
	       do{
	         System.out.printf("Enter positions of cards to keep (e.g. 1, 4, 5): ");
	         Scanner keepScan = new Scanner(System.in);
	         userInput = keepScan.nextLine();
	         keep_list = Arrays.asList(userInput.split(", *"));
	         keep_size = keep_list.size();
	         for (int i = 0; i < keep_size ; i++){
	           try{
	             test_value = Integer.parseInt(keep_list.get(i));
	             if ( test_value >= 1 && test_value <= numberOfCards)
	             {
	               enter_new_number = false;
	             }
	             else{
	               enter_new_number = true;
	               System.out.println("invalid number");
	               break; 
	             }
	 
	           } catch(NumberFormatException e){
	             enter_new_number = true;
	               System.out.println("not a number");
	           }
	         }
	       } while(enter_new_number);  
	       int number_to_replace = numberOfCards - keep_list.size(); 
	       try{
	         replacementHand = oneDeck.deal(number_to_replace); //get new cards
	       } catch (PlayingCardException e){
	           System.out.println("Exception dealing a new hand" + e.getMessage());
	       }
	 
	         //replace cards not on the keep list
	         //replace card if not on keep list
	         //following requires that the keep_list is sorted
	         for(int i = 0; i < keep_list.size(); i++){
	           replacementHand.add(playerHand.get( Integer.parseInt(keep_list.get(i)) - 1));
	         }
	         playerHand = replacementHand;
	 
	         System.out.println(" " + playerHand.toString());
	}

	private boolean retry() {
		boolean newGame = false;
		Scanner choiceScan = new Scanner(System.in);
		String choice = "n";

		if (playerBalance <= 0) {
			System.out.printf("Your balance is %d\n Bye!\n", playerBalance);
			newGame = false;
			return newGame;
		}

		System.out.printf("Your balance:$%d, one more game (y or n)?\n", playerBalance);
		Scanner playAgain = new Scanner(System.in);
		if (playAgain.hasNext() && (playAgain.nextLine().equalsIgnoreCase("y"))) {
			System.out.printf("Want to see payout table? (y or n) \n");
			Scanner showTable = new Scanner(System.in);
			if (showTable.hasNext() && (showTable.nextLine().equalsIgnoreCase("y"))) {
				showPayoutTable();
			}
			oneDeck.reset();
			newGame = true;
		} else {
			System.out.println("Bye");
			return false;
		}
		return newGame;
	}

	public void play() {
		/**
		 * The main algorithm for single player poker game
		 *
		 * Steps: showPayoutTable()
		 *
		 * ++ show balance, get bet verify bet value, update balance reset deck,
		 * shuffle deck, deal cards and display cards ask for positions of cards
		 * to keep get positions in one input line update cards check hands,
		 * display proper messages update balance if there is a payout if
		 * balance = O: end of program else ask if the player wants to play a
		 * new game if the answer is "no" : end of program else :
		 * showPayoutTable() if user wants to see it goto ++
		 */

		showPayoutTable();

		boolean keep_playing = true;

		while (keep_playing) {
			showBalance();
			getBetAndVerify();
			updateBalance();
			oneDeck.reset();
			oneDeck.shuffle();
			try {
				playerHand = oneDeck.deal(numberOfCards);
			} catch (PlayingCardException e) {
				System.out.println("Exception dealing a new hand" + e.getMessage());
			}
			displayHand();
			updateCards();
			checkHands();
			keep_playing = retry(); // false if user quits or loses
		}

	}

	/*************************************************
	 * Do not modify methods below
	 * /*************************************************
	 * 
	 * 
	 * /** testCheckHands() is used to test checkHands() method checkHands()
	 * should print your current hand type
	 */
	public void testCheckHands() {
		try {
			playerHand = new ArrayList<Card>();

			// set Royal Flush
			playerHand.add(new Card(1, 4));
			playerHand.add(new Card(10, 4));
			playerHand.add(new Card(12, 4));
			playerHand.add(new Card(11, 4));
			playerHand.add(new Card(13, 4));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Straight Flush
			playerHand.set(0, new Card(9, 4));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Straight
			playerHand.set(4, new Card(8, 2));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Flush
			playerHand.set(4, new Card(5, 4));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight",
			// "Flush ",
			// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"
			// };

			// set Four of a Kind
			playerHand.clear();
			playerHand.add(new Card(8, 4));
			playerHand.add(new Card(8, 1));
			playerHand.add(new Card(12, 4));
			playerHand.add(new Card(8, 2));
			playerHand.add(new Card(8, 3));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Three of a Kind
			playerHand.set(4, new Card(11, 4));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Full House
			playerHand.set(2, new Card(11, 2));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Two Pairs
			playerHand.set(1, new Card(9, 2));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Royal Pair
			playerHand.set(0, new Card(3, 2));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// non Royal Pair
			playerHand.set(2, new Card(3, 4));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/* Quick testCheckHands() */
	public static void main(String args[]) {
		PokerGame pokergame = new PokerGame();
		pokergame.testCheckHands();
	}
}
