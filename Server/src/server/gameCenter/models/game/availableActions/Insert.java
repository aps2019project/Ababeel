package server.gameCenter.models.game.availableActions;


import server.detaCenter.models.card.Card;

public class Insert {
    private Card card;

    Insert(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
