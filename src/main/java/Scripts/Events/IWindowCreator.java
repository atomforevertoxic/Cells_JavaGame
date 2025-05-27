package Scripts.Events;

import javax.swing.*;

@FunctionalInterface
public interface IWindowCreator {
    JFrame create();
}
