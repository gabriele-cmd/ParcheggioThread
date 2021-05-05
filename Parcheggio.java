
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author informatica
 */
public class Parcheggio {
    
    private static Semaphore semVuoto = new Semaphore(10);
    private static Semaphore semPieno = new Semaphore (0);
    
    public static void main(String[] args) throws InterruptedException {
        Parck p = new Parck();
        AutoEntrata entrata = new AutoEntrata();
        AutoUscita uscita = new AutoUscita();
        
        entrata.start();
        uscita.start();
        
        entrata.join();
        uscita.join();
        
    }
    
    static class Parck{
    
        private static int PostiDisponibili = 10;
        

        public static int getPostiDisponibili() {
            return PostiDisponibili;
        }
    
        public void entra(){
            PostiDisponibili--;
        }
    
        public void esce(){
            PostiDisponibili++;
        }
    }
    
    static class AutoEntrata extends Thread {
        Parck p = new Parck();
    
        public void run(){
            while(true){
                try{
                    sleep((int) (Math.random() * 2000)); //tempo di guida prima di arrivare al parcheggio
                } catch(InterruptedException e) {   }


                try {
                    semVuoto.acquire();
                    p.entra();
                    
                    System.out.println(getName() + " è entrato nel parcheggio. Posti disponibili: " + p.getPostiDisponibili());
                } catch (InterruptedException ex) {   }
                
                semPieno.release();
            }
        }
    }
    
    static class AutoUscita extends Thread {
        Parck p = new Parck();
        
        public void run(){
            while(true){
                try {
                    sleep((int) (Math.random() * 3000)); //tempo di permanenza della macchina del parcheggio
                } catch (InterruptedException e) {   }
                
                
                try {
                    semPieno.acquire();
                    p.esce();
                    System.out.println(getName() + " è uscito dal parcheggio. Posti disponibili: " + p.getPostiDisponibili());
                } catch (InterruptedException ex) {
                    
                }
                
                
                semVuoto.release();
            }
        }
        
    }
}
