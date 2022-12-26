import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {
    public static int portEcouteServeur = 10302;
    private String name;
    private ArrayList<String> listeJoueur;
    private String adresseServeur;
    ObjectInputStream entree;
    ObjectOutputStream sortie;
    Socket socket;
    private MainFrame fenetre;
    private boolean selected = false;
    int index;// index de ce client dans la liste des socket du serveur

    public MainFrame getFenetre() {
        if(!fenetre.isInitialise());
        return fenetre;
    }

    public Client(String name) {
      /*  String name = JOptionPane.showInputDialog(null,"Entrez votre nom","Nom du joueur",
                JOptionPane.QUESTION_MESSAGE);
        String addrServ = JOptionPane.showInputDialog(null,"Entrez l'adresse du serveur","Nom du joueur",
                JOptionPane.QUESTION_MESSAGE); */
        selected = false;
        String addrServ = "localhost";
        this.name = name;
        this.adresseServeur = addrServ;
        fenetre = new MainFrame();
        try {
            socket = new Socket(adresseServeur,portEcouteServeur);
            sortie = new ObjectOutputStream(socket.getOutputStream());
            entree = new ObjectInputStream(socket.getInputStream());
            this.start();
        }
        catch(IOException exc) {
            try {
                socket.close();
            }
            catch(IOException e){}
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setListeJoueur(ArrayList<String> listeJoueur) {
        this.listeJoueur = listeJoueur;
    }

    public void run() {
        Message message;
        int nbr;
        try {
            while (true){
                message = (Message) entree.readObject();
                System.out.println("CODE: "+(int)message.getTypeMsg() );
                switch ((int)message.getTypeMsg()){
                    case Message.TYPE_NBR_CASE:
                        nbr = (int)message.getMsgContent();
                        fenetre.initialise(nbr,this);
                        //sortie.writeObject(new Message<>(Message.OK));
                        System.out.println("Reception du nombre de case: "+ nbr);
                        break;
                    case Message.TYPE_CURRENT_NUM:
                        nbr = (int)message.getMsgContent();
                        fenetre.setPosImage(nbr);
                        //sortie.writeObject(new Message<>(Message.OK));
                        System.out.println("Reception du nombre du jeu: "+nbr);
                        break;
                    case Message.TYPE_CURRENT_PLAYER_INDEX:
                        GereClickButton.currentIndex = -1;
                        nbr = (int)message.getMsgContent();
                        int currentPlayer =nbr;
                        System.out.println("Reception de l'index courant: "+ nbr);
                        fenetre.change_playeerSelected(nbr);
                        if(this.index==currentPlayer){
                            selected = true;
                            System.out.println("A vous..."+name);
                            fenetre.getpDelay().launch();
                            while (!fenetre.getpDelay().isFinish() && GereClickButton.currentIndex <= -1){
                                System.out.println("----");
//                                if(GereClickButton.currentIndex > -1) {
//                                    //System.out.println(GereClickButton.currentIndex);
//                                    break;
//                                }
//                                if(fenetre.getpDelay().isFinish()){
//                                    sortie.writeObject(new Message<Integer>(Message.TYPE_OUT_DELAY));
//                                    break;
//                                }
                            }
                            System.out.println("click!!");
                            fenetre.getpDelay().stop();
                            sortie.writeObject(new Message<Integer>(Message.REPLY,GereClickButton.currentIndex));
                            System.out.println("Le jeu "+ GereClickButton.currentIndex+" a ete envoye.");
                        }
                        selected = false;
                        break;
                    case Message.TYPE_CUSTOM_INDEX:
                        nbr = (int)message.getMsgContent();
                        this.index = nbr;
                        //sortie.writeObject(new Message<>(Message.OK));
                        System.out.println("Reception de l'index de ce joueur: "+index);
                        break;
                    case Message.TYPE_PLAYER_LIST:
                        System.out.println("Reception de la liste des joueur: ");
                        fenetre.init_jouerZone( (ArrayList<String>) message.getMsgContent() );
                        //sortie.writeObject(new Message<>(Message.OK));
                        break;
                    case Message.TYPE_GET_PLAYER_NAME:
                        System.out.println("envoi du nom du joueur" +
                                " ");
                        sortie.writeObject(new Message<String>(Message.REPLY,this.name));
                        break;
                    default:
                        break;
                }
                //while (entree.available()<=0);
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private void sendMessag(int typeCurrentPlayerName, String name) {
//        sortie.writeObject();
//    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        new Client("j1");
        sc.nextLine();
        new Client("j2");
        sc.nextLine();
        new Client("j3");
    }
}
