package client.models.message;

public class OtherFields {
    private String deckName;
    private String cardName;
    private String myCardId;
    private String opponentCardId;
    private String[] myCardIds;

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setMyCardId(String myCardId) {
        this.myCardId = myCardId;
    }

    public void setOpponentCardId(String opponentCardId) {
        this.opponentCardId = opponentCardId;
    }

    public void setMyCardIds(String[] myCardIds) {
        this.myCardIds = myCardIds;
    }
}
