package viewTools;

import javax.swing.*;
import javax.swing.text.Style;
import java.awt.*;

public class Player extends JPanel {
    public JRadioButton radio;
    public JLabel name;
    final static int FONT_SIZE = 18;
    public Player(String name) {
        super();
        radio = new JRadioButton();
        radio.setEnabled(false);
        this.name = new JLabel(" " + name + " ");
        this.name.setFont(new Font(Font.SANS_SERIF,0,FONT_SIZE));
        this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
        this.add(radio);
        this.add(this.name);
    }

    public void select(){
        radio.setSelected(true);
        name.setFont(new Font("",Font.BOLD,FONT_SIZE));
    }

    public void deselect(){
        radio.setSelected(false);
        name.setFont(new Font("",0,FONT_SIZE));
    }

    public static void main(String[] args){
        JFrame fenetre = new JFrame("progress");
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane,BoxLayout.PAGE_AXIS));
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setBounds(20,10,800,70 );
        Player p1 =new Player("loic");
        Player p2 =new Player("Ema");
        Player p3 =new Player("Jean");
        p1.select();

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        fenetre.getContentPane().add(pane);
        fenetre.setVisible(true);
    }
}
