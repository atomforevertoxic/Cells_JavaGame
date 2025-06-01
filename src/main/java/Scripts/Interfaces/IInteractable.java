package Scripts.Interfaces;

import Scripts.Game.LevelModel;
import Scripts.Player;

public interface IInteractable {
    boolean handlePlayerInteraction(Player player, LevelModel model);
}
