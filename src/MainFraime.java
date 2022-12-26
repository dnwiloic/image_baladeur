import viewTools.MyProgressBar;
import viewTools.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class MainFrame extends JFrame {
    private boolean isInitialise;
    private ArrayList<Player> playerList;
    JPanel grillePanel;
    JPanel mainPane;
    JPanel joueurZone;
    JPanel grille_progress;

    private MyProgressBar pDelay;
    private JLabel notifyLabel;
    private int nbrCase;
    private JButton[] tabBtn;
    private String imagePath;

    public boolean isInitialise() {
        return isInitialise;
    }

    public MainFrame() {
        //initialisation de la fenetre
        pDelay = new MyProgressBar();
        notifyLabel = new JLabel("rien a signaler");
        notifyLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        grillePanel = new JPanel();
        mainPane = new JPanel();
        joueurZone = new JPanel();
        playerList = new ArrayList<Player>();
        //JPanel hBox = new JPanel();// joueurZone + grille_progress
        grille_progress = new JPanel();// grille + progressBar
        imagePath ="twitter.png";

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,10,600,400);

        //boite de dialogue pour recupere le nom

    }

    public int getNbrCase() {
        return nbrCase;
    }

    public MyProgressBar getpDelay() {
        return pDelay;
    }

    public void initialise(int nbrCase,Client clt){
        initialise(nbrCase,-1,clt);
    }
    public void initialise(int nbrCase, int posImage,Client clt){
        this.nbrCase = nbrCase;
        if (isInitialise == false){
            // panel principal
            mainPane.setLayout(new BorderLayout());
            mainPane.add(notifyLabel,BorderLayout.SOUTH);
            mainPane.add(joueurZone,BorderLayout.WEST);
            mainPane.add(grille_progress,BorderLayout.CENTER);

            //grille_progress panel
            grille_progress.setLayout(new BoxLayout(grille_progress, BoxLayout.PAGE_AXIS));
            grille_progress.add(grillePanel);
            grille_progress.add(pDelay);

            //initialisation de la liste de joueur
            init_jouerZone(new ArrayList<String>());

            //initialisation de la grille
            int nbLigne = (int)Math.sqrt(nbrCase);
            GridLayout maGrille=new GridLayout(nbLigne,nbrCase/nbLigne);
            grillePanel.setLayout(maGrille);

            //initialisation des bouttons
            tabBtn = new JButton[ nbrCase ];
            for (int i=0; i<tabBtn.length ; i++){
                tabBtn[i]=new JButton();
                tabBtn[i].addActionListener(new GereClickButton(i,clt));
                grillePanel.add(tabBtn[i]);
            }
            if(posImage>-1)
                tabBtn[posImage].setIcon(new ImageIcon(imagePath));

            this.getContentPane().add(mainPane);
            this.setVisible(true);
        }
    }

    public void init_jouerZone(ArrayList<String> listName){
        joueurZone.removeAll();
        joueurZone.setLayout(new BoxLayout(joueurZone,BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel("liste des Joueurs");
        title.setFont( new Font(Font.SERIF,Font.BOLD, 22));
        joueurZone.add( title );

        //vidons a liste des joueuer
        if(!listName.isEmpty()) {
            playerList.clear();

            //remplissons la liste ds joueur
            for (String elt : listName) {
                playerList.add(new Player(elt));
            }
        }
        for(Player p:playerList){
            joueurZone.add(p);
        }
    }

    public void change_playeerSelected(int newSelected){
        for( Player p: playerList)
            p.deselect();
        playerList.get(newSelected).select();
    }
    public void setPosImage(int posImage) {
        if (posImage < nbrCase && posImage>=0) {
            if (tabBtn == null) return;
            for (JButton btn : tabBtn) {
                if (btn != null)
                    btn.setIcon(null);
                else return;
            }
            tabBtn[posImage].setIcon(new ImageIcon(imagePath));
        }
    }
}