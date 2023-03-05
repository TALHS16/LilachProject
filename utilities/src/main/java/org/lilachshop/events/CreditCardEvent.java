package org.lilachshop.events;


import org.lilachshop.entities.CreditCard;

//Event to pass from SignUp4 to final stage to create new costumer
public class CreditCardEvent {
  CreditCard card;

    public CreditCard getCard() {
        return card;
    }

    public void setCard(CreditCard card) {
        this.card = card;
    }

    public CreditCardEvent(CreditCard card) {
      this.card =card;

    }
 }

