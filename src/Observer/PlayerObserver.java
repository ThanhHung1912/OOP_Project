package Observer;

import java.awt.geom.Rectangle2D;

public interface PlayerObserver {
    void playerHasAttacked(Rectangle2D.Float attackBox);
}
