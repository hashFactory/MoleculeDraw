import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class DataPackage
{
    int _width, _height, _grid_x, _grid_y;

    ArrayList<Atom> _atoms = new ArrayList<>();
    ArrayList<Bond> _bonds = new ArrayList<>();
}

public class Load
{
    public static DataPackage load(String filename)
    {
        File file = new File(filename);
        Scanner file_scanner = null;
        try
        {
            file_scanner = new Scanner(file);
            System.out.println(file_scanner.hasNext());
        } catch (FileNotFoundException e) {}

        DataPackage dp = new DataPackage();
        dp._width = file_scanner.nextInt();
        dp._height = file_scanner.nextInt();
        dp._grid_x = file_scanner.nextInt();
        dp._grid_y = file_scanner.nextInt();

        while (file_scanner.hasNextLine())
        {
            if (file_scanner.hasNext() && file_scanner.next().equalsIgnoreCase("a"))
            {
                String symbol = file_scanner.next();
                //boolean selected = file_scanner.nextBoolean();
                file_scanner.nextBoolean();
                // TODO: Get width and height from atom in file
                int x = file_scanner.nextInt();
                int y = file_scanner.nextInt();
                file_scanner.nextLine();

                Atom a = new Atom(symbol, x, y);
                dp._atoms.add(a);
            }
            if (file_scanner.hasNext() && file_scanner.next().equalsIgnoreCase("b"))
            {
                int bond_width = file_scanner.nextInt();
                int atom_index_1 = file_scanner.nextInt();
                int atom_index_2 = file_scanner.nextInt();
                file_scanner.nextLine();

                Bond b = new Bond(bond_width, atom_index_1, atom_index_2);
                dp._bonds.add(b);
            }
        }

        return dp;
    }
}
