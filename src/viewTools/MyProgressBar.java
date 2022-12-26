package viewTools;

import javax.swing.*;
import javax.swing.plaf.ProgressBarUI;

public class MyProgressBar extends JPanel implements Runnable {
    private JProgressBar progress;
    private JLabel progressLabel;
    private Thread myThread;
    private boolean interrump;
    private boolean initialized = false;
    private boolean finish;
    final static int DELAY = 20; //delai d'attente en en seconde

    public MyProgressBar() {
        super();
        interrump = false;
        finish = false;
        this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
        progress = new JProgressBar();
        progressLabel = new JLabel(" ~~/~~ ");
        this.add(progressLabel);
        this.add(progress);
        myThread = new Thread(this);
    }

    public boolean isFinish() {
        return finish;
    }

    @Override
    public void run() {
        initialized = true;
        interrump = false;

        while(true) {
            int startTime = (int) (System.currentTimeMillis()/1000);
            int currentTime = 0;
            finish=false;
            while ( !interrump && currentTime < DELAY) {
                currentTime = (int) (System.currentTimeMillis() / 1000) - startTime;
                progressLabel.setText(" " + (DELAY - currentTime) + " s ");
                progress.setValue((DELAY - currentTime) * 100 / DELAY);
            }
            progressLabel.setText(" ~~/~~ ");
            finish = true;
        }
    }

    public void launch(){
        if(initialized){
            interrump = false;
            return;
        }
        myThread.start();
    }

    public void stop(){
        interrump = true;
        progress.setValue(0);
        progressLabel.setText(" ~~/~~ ");
    }

    public static void main(String[] args){
        JFrame fenetre = new JFrame("progress");
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setBounds(200,5,400,70 );
        MyProgressBar p = new MyProgressBar();
        fenetre.getContentPane().add(p);
        p.launch();
        fenetre.setVisible(true);
    }
}
