import java.io.*;
import java.util.ArrayList;

public class Save
{
    public static void save(String name, int width, int height, int x_grid, int y_grid, ArrayList<Atom> atoms, ArrayList<Bond> bonds)
    {
        File file = new File(name);
        try
        {
            file.createNewFile();
        } catch (IOException e) {}

        try
        {
            PrintWriter pout = new PrintWriter(file);
            pout.println(width + " " + height + " " + x_grid + " " + y_grid);
            for (Atom a: atoms)
                pout.println("a " + a.symbol + " " + a.selected + " " + a.shape.getX() + " " + a.shape.getY() + " " + a.shape.getWidth() + " " + a.shape.getHeight());
            for (Bond b: bonds)
                pout.println("b " + b.bond_width + " " + b.atom_index_1 + " " + b.atom_index_2);

        } catch (FileNotFoundException e) {}
    }
}
