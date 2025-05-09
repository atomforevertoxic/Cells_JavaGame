package Scripts.Cells;

import java.util.Objects;

public class Key
{



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Key key = (Key) o;

        //заглушка для точки расширения
        return true;
    }
}
