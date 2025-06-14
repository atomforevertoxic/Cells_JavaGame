package Scripts.Game;

import java.util.ArrayList;
import java.util.List;

public class LevelProgressService {
    private final List<Boolean> completedLevels;
    private final boolean resetOnRestart;

    public LevelProgressService(int totalLevels, boolean resetOnRestart) {
        this.resetOnRestart = resetOnRestart;
        this.completedLevels = new ArrayList<>(totalLevels);
        // Инициализируем первый уровень как доступный
        for (int i = 0; i < totalLevels; i++) {
            completedLevels.add(i == 0);
        }
    }

    public boolean isLevelUnlocked(int levelId) {

        return levelId >= 0 && levelId < completedLevels.size() && completedLevels.get(levelId);
    }

    public void completeLevel(int levelId) {
        if (levelId + 1 < completedLevels.size()) {
            completedLevels.set(levelId + 1, true);
        }
    }

    public int getTotalLevels() {
        return completedLevels.size();
    }

    public void resetProgress() {
        for (int i = 1; i < completedLevels.size(); i++) {
            completedLevels.set(i, false);
        }
    }
}