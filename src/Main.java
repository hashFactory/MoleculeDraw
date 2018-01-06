import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Main extends JApplet implements Runnable, MouseListener, KeyListener
{
    ArrayList<Atom> atoms = new ArrayList<>();
    ArrayList<Bond> bonds = new ArrayList<>();
    int index_selected;
    Graphics2D bufferGraphics;
    Image offscreen;
    boolean[] texts = new boolean[20];
    String buffer_symbol = "";

    public void init()
    {
        setSize(600, 600);
        setBackground(Color.white);
        addKeyListener(this);
        addMouseListener(this);
        offscreen = createImage(600, 600);
        bufferGraphics = (Graphics2D)offscreen.getGraphics();
        setFocusable(true);
    }

    public void start()
    {
        Thread th = new Thread(this);
        th.start();
    }

    public void paint(Graphics g)
    {
        bufferGraphics.clearRect(0, 0, getWidth(), getHeight());

        bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 20));

        // Draw grid
        bufferGraphics.setColor(Color.lightGray);

        int w, h;
        int width = (int)(getWidth() / 20.0), height = (int)(getHeight() / 20.0);

        for (w = 0; w <= getWidth(); w+=width)
            for (h = 0; h <= getHeight(); h+=height)
                bufferGraphics.drawLine((int)w, (int)h, (int)w+1, (int)h+1);

        // Draw bonds
        bufferGraphics.setColor(Color.black);
        for (Bond b : bonds)
        {
            bufferGraphics.setStroke(new BasicStroke(3));
            for (int i = 0; i < b.bond_width; i++)
                bufferGraphics.drawLine((int)atoms.get(b.atom_index_1).shape.getX() + (int)atoms.get(b.atom_index_1).shape.getWidth() / 2 + (i * 5), (int)atoms.get(b.atom_index_1).shape.getY() + (int)atoms.get(b.atom_index_1).shape.getHeight() / 2 + (i * 5), (int)atoms.get(b.atom_index_2).shape.getX() + (int)atoms.get(b.atom_index_2).shape.getWidth() / 2 + (i * 5), (int)atoms.get(b.atom_index_2).shape.getY() + (int)atoms.get(b.atom_index_2).shape.getHeight() / 2 + (i * 5));

        }

        // Draw atoms
        for (Atom a : atoms)
        {
            bufferGraphics.setStroke(new BasicStroke(1));

            // Draw oval
            if (!a.selected)
                bufferGraphics.setColor(new Color(230, 230, 230));
            else
                bufferGraphics.setColor(new Color(100, 100, 100));

            bufferGraphics.fillOval((int)a.shape.getX(), (int)a.shape.getY(), (int)a.shape.getWidth(), (int)a.shape.getHeight());

            bufferGraphics.setColor(Color.BLACK);

            if (a.selected)
                bufferGraphics.setStroke(new BasicStroke(5));

            bufferGraphics.drawOval((int)a.shape.getX(), (int)a.shape.getY(), (int)a.shape.getWidth(), (int)a.shape.getHeight());

            // Draw text
            bufferGraphics.drawString(a.symbol, (int)a.shape.getX() + (int)a.shape.getWidth() / 4, (int)a.shape.getY() + (int)(a.shape.getHeight() / 1.5));
        }

        bufferGraphics.setStroke(new BasicStroke(1));

        bufferGraphics.drawString("Buffered symbol: " + buffer_symbol, 0, 20);

        g.drawImage(offscreen, 0, 0, this);
    }

    @Override
    public void run()
    {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        while(true)
        {
            repaint();

            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException ex)
            {}
        }
    }

    public void bond()
    {
        Bond bond = null;

        ArrayList<Integer> selected_atoms = new ArrayList<>(2);
        for (int i = 0; i < atoms.size(); i++)
        {
            if (atoms.get(i).selected)
            {
                selected_atoms.add(i);
            }
        }

        if (selected_atoms.size() == 2)
        {
            System.out.println("new bond");
            bond = new Bond(1, selected_atoms.get(0), selected_atoms.get(1));
        }

        if (bonds.indexOf(bond) == -1)
        {
            System.out.println("added");
            bonds.add(bond);
            /*
            for (int i = 0; i < atoms.size(); i++)
                atoms.get(i).selected = false;*/
        }
    }

    public int get_selected()
    {
        int x = 0;

        for (int i = 0; i < atoms.size(); i++)
        {
            if (atoms.get(i).selected)
            {
                x++;
            }
        }

        return x;
    }

    public boolean check_if_in_atom(int x, int y)
    {
        for (int i = 0; i < atoms.size(); i++)
        {
            if (atoms.get(i).shape.contains(x, y))
            {
                index_selected = i;
                atoms.get(i).selected = !atoms.get(i).selected;
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        boolean create = true;
        for (Atom a : atoms)
            if (a.shape.contains(e.getX(), e.getY()))
                create = false;

        int x = (-(e.getX() % (getWidth() / 20)) + e.getX());
        int y = (-(e.getY() % (getHeight() / 20)) + e.getY());


        Atom atom = new Atom("C", x, y);

        if (create)
            atoms.add(atom);
        else
            check_if_in_atom(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        if (get_selected() == 2)
        {
            if (e.getKeyChar() <= '9' && e.getKeyChar() >= '1')
            {
                Bond bond = null;

                ArrayList<Integer> selected_atoms = new ArrayList<>(2);
                for (int i = 0; i < atoms.size(); i++)
                {
                    if (atoms.get(i).selected)
                    {
                        selected_atoms.add(i);
                    }
                }

                if (selected_atoms.size() == 2)
                    bond = new Bond(Integer.parseInt(e.getKeyChar() + ""), selected_atoms.get(0), selected_atoms.get(1));

                // TODO: Replace preexisting bond if it exists
                Bond temp_bond = null;
                boolean exists = false;
                int remove = 0;
                    for (Bond b : bonds)
                    {
                        if (b.atom_index_1 == bond.atom_index_1 && b.atom_index_2 == bond.atom_index_2)
                        {
                            exists = true;

                        }
                    }

                if (exists)
                {
                    System.out.println("exists");
                    bonds.remove(remove);
                    Bond new_bond = new Bond(Integer.parseInt(e.getKeyChar() + ""), selected_atoms.get(0), selected_atoms.get(1));
                    bonds.add(new_bond);
                }
                else
                {
                    if (bond != null)
                        bonds.add(bond);
                }
            }
        }

        if (get_selected() == 1)
        {
            if (e.getKeyChar() == '\n')
            {
                int x = 0;

                for (int i = 0; i < atoms.size(); i++)
                {
                    if (atoms.get(i).selected)
                    {
                        x = i;
                        break;
                    }
                }

                atoms.get(x).symbol = buffer_symbol;
                atoms.get(x).selected = false;
                buffer_symbol = "";
            }
            else
            {
                buffer_symbol += e.getKeyChar();
            }

        }

        if (e.getKeyChar() == ' ')
        {
            bond();
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
