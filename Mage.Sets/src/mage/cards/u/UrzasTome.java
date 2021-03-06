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
package mage.cards.u;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.Cost;
import mage.abilities.costs.common.ExileFromGraveCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.common.FilterHistoricCard;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetCardInYourGraveyard;

/**
 *
 * @author TheElk801
 */
public class UrzasTome extends CardImpl {

    public UrzasTome(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT}, "{2}");

        // {3}, {T}: Draw a card. Then discard a card unless you exile a historic card from your graveyard.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new UrzasTomeEffect(), new GenericManaCost(3));
        ability.addCost(new TapSourceCost());
        this.addAbility(ability);
    }

    public UrzasTome(final UrzasTome card) {
        super(card);
    }

    @Override
    public UrzasTome copy() {
        return new UrzasTome(this);
    }
}

class UrzasTomeEffect extends OneShotEffect {

    public UrzasTomeEffect() {
        super(Outcome.Discard);
        staticText = "Draw a card. Then discard a card unless you exile a historic card from your graveyard";
    }

    public UrzasTomeEffect(final UrzasTomeEffect effect) {
        super(effect);
    }

    @Override
    public UrzasTomeEffect copy() {
        return new UrzasTomeEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        new DrawCardSourceControllerEffect(1).apply(game, source);
        if (controller != null
                && controller.chooseUse(Outcome.Discard, "Exile a historic card from your graveyard?", source, game)) {
            Cost cost = new ExileFromGraveCost(new TargetCardInYourGraveyard(new FilterHistoricCard()));
            if (cost.canPay(source, source.getSourceId(), controller.getId(), game)) {
                if (cost.pay(source, game, source.getSourceId(), controller.getId(), false, null)) {
                    return true;
                }
            }
        }
        if (controller != null) {
            controller.discard(1, false, source, game);
            return true;
        }
        return false;
    }
}
