import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GereClickButton implements ActionListener {
    int btnIndex;
    public static int currentIndex = -1;
    private Client clt;
    public GereClickButton(int index, Client clt) {
        System.out.println("Ajout de evt");
        this.btnIndex = index;
        this.clt = clt ;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( clt.isSelected())
            currentIndex=btnIndex;
    }
}
