package iams.cardgame.tute.tr;

import iams.cardgame.tute.CardModel;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.CardModel.Suit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TranslatorEn extends Translator
{
    @Override
    public String getWindowTitle()
    {
        return "Tute for two players";
    }

    @Override
    public String getPlayerPointsString(int playerPoints)
    {
        return "Points: " + playerPoints;
    }

    @Override
    public String getPlayerGamesString(int player1Games, int player2Games)
    {
        return "Games: " + player1Games + " - " + player2Games;
    }

    @Override
    public String getSuitName(Suit suit)
    {
        switch (suit)
        {
            case Batons: return "Batons";
            case Cups: return "Cups";
            case Swords: return "Swords";
            case Coins: return "Coins";
            default: throw new AssertionError();
        }
    }
    
    @Override
    public String getRankName(Rank rank)
    {
        switch (rank)
        {
            case Ace: return "Ace";
            case King: return "King";
            case Knight: return "Knight";
            case Knave: return "Knave";
            default: return rank.name().replaceAll("^V", "");
        }
    }
    
    @Override
    public String getCardNameString(CardModel currentCard)
    {
        return this.getRankName(currentCard.rank) + " of " + this.getSuitName(currentCard.suit);
    }

    @Override
    public String getPlus10DeMonteString()
    {
        return "+10 bonus for the winner of the last trick";
    }

    @Override
    public String getChangePintaString()
    {
        return "Exchange card for the trump";
    }

    @Override
    public String getTuteDeclarationString(Rank rank)
    {
        return "Tute (" + (rank == Rank.King ? "Kings" : "Knights") + ")";
    }

    @Override
    public String getPlusPointsString(int countValue)
    {
        return "+" + countValue;
    }

    @Override
    public String getPlusTwentyFortyPointsString(int countValue, Suit suit)
    {
        return "+" + countValue + " of " + this.getSuitName(suit);
    }

    @Override
    public String getTwentyFortyDeclarationString(Suit pintaSuit, Suit declarationSuit)
    {
        if (pintaSuit == declarationSuit)
            return "Forty!";
        else
            return "Twenty (" + this.getSuitName(declarationSuit) + ")"; 
    }

    @Override
    public String getDeclareRenuncioString()
    {
        return "Renuncio";
    }

    @Override
    public String getMenuItemNames(String key)
    {
        switch (key)
        {
            case "OPTIONS": return "Options";
            case "RESTART": return "Restart";
            case "QUIT": return "Quit game";
            case "GAME": return "Game";
            case "RULES": return "Rules";
            case "LANGUAGES": return "Languages";
            case "PORTUGUESE": return "Portuguese (BR)";
            case "ENGLISH": return "English";
            case "SPANISH": return "Spanish";
            case "COLORBACKGROUND": return "Background Color";
            case "GREEN": return "Green";
            case "RED": return "Red";
            case "BLUE": return "Blue";
            default: throw new AssertionError();
        }
    }

    @Override
    public String getRulesText() throws IOException {
    	try {    
    		return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\iams\\cardgame\\tute\\tr\\Rules.txt")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    	
    	return new String();
    }

	@Override
	public String getCardsRemainText(String key) {
		return "Remaining cards: " + key;
	}

	@Override
	public String getCardsOverdueText(String key) {
		return "Cards Overdue: " + key;
	}
}
