package Scripts.Utils;

import com.google.gson.Gson;
import java.io.FileReader;
import java.util.List;

public class LevelLoader {
    private static class LevelData {
        List<LevelInfo> levels;
    }

    private static class LevelInfo {
        int id;
        String name;
        int rows;
        int cols;
        List<WallPosition> walls;
        List<KeyPosition> keys;
        StartPosition start;
        ExitPosition exit;
    }

    private static class WallPosition { int q; int r; }
    private static class KeyPosition { int q; int r; }
    private static class StartPosition { int q; int r; }
    private static class ExitPosition { int q; int r; }

    public static LevelInfo loadLevel(int levelId, String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            LevelData data = new Gson().fromJson(reader, LevelData.class);
            return data.levels.stream()
                    .filter(level -> level.id == levelId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Level not found"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load level", e);
        }
    }
}