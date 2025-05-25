package Scripts.Utils;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LevelLoader {
    private static class LevelData {
        List<LevelConfig> levels;
    }

    public static class LevelConfig {
        public int id;
        public String name;
        public int rows;
        public int cols;
        public List<WallPosition> walls;
        public List<KeyPosition> keys;
        public StartPosition start;
        public ExitPosition exit;
    }

    // потом getter и setter поставь
    public static class WallPosition { public int q; public int r; }
    public static class KeyPosition { public int q; public int r; }
    public static class StartPosition { public int q; public int r; }
    public static class ExitPosition { public int q; public int r; }

    public static List<LevelConfig> loadLevels() {
        try {
            InputStream inputStream = LevelLoader.class
                    .getClassLoader()
                    .getResourceAsStream("levels.json");

            if (inputStream == null) {
                throw new RuntimeException("Файл levels.json не найден в classpath!");
            }
            InputStreamReader reader = new InputStreamReader(inputStream);
            LevelData data = new Gson().fromJson(reader, LevelData.class);
            return data.levels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}