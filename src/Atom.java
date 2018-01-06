import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Atom
{
    public String symbol;
    public int atomic_number;
    public Ellipse2D shape;
    public int width = 30;
    public int height = 30;
    public boolean selected = false;

    public Atom(String _symbol, int pos_x, int pos_y)
    {
        symbol = _symbol;
        shape = new Ellipse2D.Double(pos_x - width / 2, pos_y - height / 2, width, height);
    }
}
