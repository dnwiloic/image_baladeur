import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Service extends Thread{
    private String name;
    private Socket socket;
    private ObjectInputStream entree;
    private ObjectOutputStream sortie;
    private int index;

    public Socket getSocket() {
        return socket;
    }

    public String getServName() {
        return name;
    }

    public ObjectInputStream getEntree() {
        return entree;
    }

    public ObjectOutputStream getSortie() {
        return sortie;
    }

    public int getIndex() {
        return index;
    }

    Service(Socket socket){
        this.socket = socket;
        try {
            entree = new ObjectInputStream(socket.getInputStream());
            sortie = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
            try {
                socket.close();
            }
            catch(IOException ne){
                e.getStackTrace();
            }
        }
    }
    public void run(){
        index = Serveur.listClient.indexOf(this);
        System.out.println("Index : "+index);
        try {
            System.out.println("alons-y");
            //demande du nom du nouveau client connecte
            sortie.writeObject(new Message<String>(Message.TYPE_GET_PLAYER_NAME,"Votre nom"));
            Message mReply=(Message) entree.readObject();
            name = (String) mReply.getMsgContent();
            System.out.println("Nouveau client connectee: "+name);
            Serveur.sendNomsClient();
            //Envoie de l'index de ce client

            System.out.println("Envoie de l'index de ce client : "+ this.index);
            sortie.writeObject(new Message<Integer>(Message.TYPE_CUSTOM_INDEX,index));

            //envoi du nombre de cases
            System.out.println("Envois du nombre de cases: "+Serveur.nbrCase);
            sortie.writeObject( new Message<Integer>(Message.TYPE_NBR_CASE, Serveur.nbrCase));
            //if( ((Message)((Message<?>) entree.readObject())).getTypeMsg()==Message.OK);

            //Envoys de l'index actuel de l'image
            System.out.println("Envoys de l'index: "+Serveur.currentCase);
            sortie.writeObject(new Message<Integer>(Message.TYPE_CURRENT_NUM,Serveur.currentCase));
            //if( ((Message)((Message<?>) entree.readObject())).getTypeMsg()==Message.OK);


            while (true){
//                System.out.println("Taille de la liste des clients : "+Serveur.listClient.size());
                if(Serveur.currentSelected == this.index ){
                    System.out.println("En attente du jeu de: "+this.index);
                    Message recep = (Message)entree.readObject();
                    System.out.println("recu!!");
                    if(recep.getTypeMsg() != Message.TYPE_OUT_DELAY && ((int)recep.getMsgContent())!=-1) {
                        System.out.println("dans les temps ");
                        Serveur.currentCase = (int) recep.getMsgContent();
                        System.out.println("......" + Serveur.currentCase);
                        Serveur.broadCost(new Message(Message.TYPE_CURRENT_NUM, Serveur.currentCase));

                    }else { System.out.println("Hors delay"); }
                    Serveur.changeCurrentSelected();
                }
            }

        } catch (RuntimeException e) {
            System.out.println("Erreur"+ e.getStackTrace().toString());
            e.printStackTrace();
//            if( Serveur.listClient.get(Serveur.currentSelected)==this)
//                Serveur.changeCurrentSelected();
//            Serveur.listClient.remove(this);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

class Serveur {
    public static int nbrCase;
    public static boolean changeHand = false;
    public static int currentSelected = 0;//client ayant la main
    public static int currentCase;//index de la case contenant l'image
    public static ArrayList<Service> listClient = new ArrayList<Service>();


    public static void changeCurrentSelected() throws IOException {
        if(currentSelected < listClient.size()-1)
            currentSelected ++;
        else currentSelected = 0;
        System.out.println("Envois de l'aval: "+ Serveur.currentSelected);
        broadCost(new Message(Message.TYPE_CURRENT_PLAYER_INDEX,Serveur.currentSelected));
    }


    public static void main(String[] args){
        Service tmp;
        int portEcoute = 10302;
        ServerSocket standardiste;
        Socket socket;
        JOptionPane getNbrCases = new JOptionPane();
        String msg = "Entrer le nombre de cases";
        /*do {
            try {
                nbrCase = Integer.parseInt(
                        JOptionPane.showInputDialog(null, msg, "Nombre de case",
                                JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                msg="Entrer le nombre de cases \n Vous devez entrer un nombre positif.";
                nbrCase = -1;
            }
        }while (nbrCase < 0);*/
        nbrCase = 8;
        try {
            standardiste = new ServerSocket(portEcoute);
            while (true){
                socket = standardiste.accept();
                System.out.println("Nouvelle connection");
                tmp = new Service(socket);
                listClient.add(tmp);
                tmp.start();
                //envoie de l'index du joueur courant au nouveau client
                Thread.sleep(500);
                tmp.getSortie().writeObject(new Message(Message.TYPE_CURRENT_PLAYER_INDEX,Serveur.currentSelected));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void broadCost( Message m) throws IOException {
        /*
        Cette fonction permet de transmettre
        la nouvelle valeur de la case qui doit contenir l'image
         */

        for( Service src : listClient){
            src.getSortie().writeObject(m);
        }
    }

    public static void sendNomsClient() throws IOException {
        ArrayList<String> nomsClient =new ArrayList<String>();
        for(Service serv: listClient ){
            nomsClient.add( serv.getServName());
        }
        System.out.println(nomsClient);
        broadCost(new Message<ArrayList<String>>(Message.TYPE_PLAYER_LIST,nomsClient));
    }
}
