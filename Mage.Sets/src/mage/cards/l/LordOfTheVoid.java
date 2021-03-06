/*
 /*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.l;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.DealsCombatDamageToAPlayerTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.Cards;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.common.FilterCreatureCard;
import mage.game.Game;
import mage.players.Player;
import mage.target.TargetCard;

/**
 *
 * @author Plopman
 */
public class LordOfTheVoid extends CardImpl {

    public LordOfTheVoid(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{4}{B}{B}{B}");
        this.subtype.add(SubType.DEMON);

        this.power = new MageInt(7);
        this.toughness = new MageInt(7);

        //Flying
        this.addAbility(FlyingAbility.getInstance());

        //Whenever Lord of the Void deals combat damage to a player, exile the top seven cards of that player's library, then put a creature card from among them onto the battlefield under your control.
        this.addAbility(new DealsCombatDamageToAPlayerTriggeredAbility(new LordOfTheVoidEffect(), false, true));
    }

    public LordOfTheVoid(final LordOfTheVoid card) {
        super(card);
    }

    @Override
    public LordOfTheVoid copy() {
        return new LordOfTheVoid(this);
    }
}

class LordOfTheVoidEffect extends OneShotEffect {

    public LordOfTheVoidEffect() {
        super(Outcome.PutCreatureInPlay);
        this.staticText = "exile the top seven cards of that player's library, then put a creature card from among them onto the battlefield under your control";
    }

    public LordOfTheVoidEffect(final LordOfTheVoidEffect effect) {
        super(effect);
    }

    @Override
    public LordOfTheVoidEffect copy() {
        return new LordOfTheVoidEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(this.getTargetPointer().getFirst(game, source));
        Player controller = game.getPlayer(source.getControllerId());
        if (player == null || controller == null) {
            return false;
        }

        Cards cards = new CardsImpl();
        cards.addAll(player.getLibrary().getTopCards(game, 7));
        controller.moveCards(cards, Zone.EXILED, source, game);
        if (!cards.getCards(new FilterCreatureCard(), game).isEmpty()) {
            TargetCard target = new TargetCard(Zone.EXILED, new FilterCreatureCard());
            if (controller.chooseTarget(outcome, cards, target, source, game)) {
                Card card = cards.get(target.getFirstTarget(), game);
                if (card != null) {
                    controller.moveCards(card, Zone.BATTLEFIELD, source, game, false, false, false, null);
                }
            }
        }
        return true;
    }
}
