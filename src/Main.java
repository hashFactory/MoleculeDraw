import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Main extends JApplet implements Runnable, MouseListener
{
    ArrayList<Atom> atoms = new ArrayList<>();

    public void init()
    {
        setSize(300, 300);
        setBackground(Color.white);
        addMouseListener(this);
    }

    public void start()
    {
        Thread th = new Thread(this);
        th.start();
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        for (Atom a : atoms)
        {
            g2.drawOval((int)a.shape.getX(), (int)a.shape.getY(), (int)a.shape.getWidth(), (int)a.shape.getHeight());
            g2.drawString(a.symbol, (int)a.shape.getX() + (int)a.shape.getWidth() / 4, (int)a.shape.getY() + (int)(a.shape.getHeight() / 1.5));
        }
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

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Atom atom = new Atom("C", e.getX(), e.getY());
        atoms.add(atom);
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
}
