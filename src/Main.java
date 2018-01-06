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

    public void init()
    {
        setSize(300, 300);
        setBackground(Color.white);
        addKeyListener(this);
        addMouseListener(this);
        offscreen = createImage(300, 300);
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

        // Draw bonds
        for (Bond b : bonds)
        {
            bufferGraphics.setStroke(new BasicStroke(3));
            bufferGraphics.drawLine((int)atoms.get(b.atom_index_1).shape.getX() + (int)atoms.get(b.atom_index_1).shape.getWidth() / 2, (int)atoms.get(b.atom_index_1).shape.getY() + (int)atoms.get(b.atom_index_1).shape.getHeight() / 2, (int)atoms.get(b.atom_index_2).shape.getX() + (int)atoms.get(b.atom_index_2).shape.getWidth() / 2, (int)atoms.get(b.atom_index_2).shape.getY() + (int)atoms.get(b.atom_index_2).shape.getHeight() / 2);
        }

        // Draw atoms
        for (Atom a : atoms)
        {
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
            bufferGraphics.setStroke(new BasicStroke(1));

            // Draw text
            bufferGraphics.drawString(a.symbol, (int)a.shape.getX() + (int)a.shape.getWidth() / 4, (int)a.shape.getY() + (int)(a.shape.getHeight() / 1.5));
        }

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

        Atom atom = new Atom("C", e.getX(), e.getY());

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
        System.out.println("TYPED KEY");
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        System.out.println("PRESSED KEY");
        System.out.println("Pressed " + e.getKeyCode());

        if (e.getKeyChar() == ' ')
        {
            Bond bond = null;

            int x = 0;
            ArrayList<Integer> selected_atoms = new ArrayList<>(2);
            for (int i = 0; i < atoms.size(); i++)
            {
                if (atoms.get(i).selected)
                {
                    selected_atoms.add(i);
                    x++;
                }
            }

            System.out.println("Space bar + " + x);

            if (x == 2)
                bond = new Bond(1, selected_atoms.get(0), selected_atoms.get(1));

            if (bond != null)
                bonds.add(bond);
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        System.out.println("RELEASED KEY");
    }
}
