package org.game.service;

import org.game.dto.ActionData;
import org.game.dto.GameStructure;

public interface LabirinthService {

    GameStructure generateGameStructure(String username);

    GameStructure getGameStructure(String username);

    void setAction(ActionData data);
}
