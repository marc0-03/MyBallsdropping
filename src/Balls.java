import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Balls extends Canvas implements Runnable {

    private Thread thread;
    int fps = 60;
    private boolean isRunning, StandstilY,StandstilX;

    private BufferStrategy bs;

    private int counter,BouncyNumberCheck, i;
    private double SpeedY,SpeedX,Height,Timedif,Gravity,Timeconstraint, BouncyNumber, Bouncecompare, Timething,PositonX;
    private ArrayList<Ball> Balls = new ArrayList<Ball>();


    public Balls() {
        JFrame frame = new JFrame("My balls");
        this.setSize(1200,800);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);

        int counter=0;
        int BouncyNumberCheck=0;
        JOptionPane.showMessageDialog(null, "Daug, u wanna know about balls dropping");
        int X = Integer.parseInt(JOptionPane.showInputDialog(null, "Wanna do the quick options?\nHit 1 for yes, anyhing else hit another number"));
        if (X!=1){
         SpeedY = Double.parseDouble(JOptionPane.showInputDialog(null, "Does the ball have any starting speed on the Y axis\nIn Meters per second"));
         SpeedX = Double.parseDouble(JOptionPane.showInputDialog(null, "Does the ball have any starting speed on the X axis\nIn Meters per second"));

         if (SpeedX!=0){
             StandstilX=false;
         }

         Height = Double.parseDouble(JOptionPane.showInputDialog(null, "How high up is this ball\nIn Meters"));
         Timedif = Double.parseDouble(JOptionPane.showInputDialog(null, "what time diffrence should be beetwen the measurement\nIn Seconds"));
         Gravity = Double.parseDouble(JOptionPane.showInputDialog(null, "choose a gravity constant\nIn Meters per second"));
         Timeconstraint = Double.parseDouble(JOptionPane.showInputDialog(null, "choose at what time the measurement should stop\nIn Seconds"));
        while (BouncyNumberCheck==0) {
            BouncyNumber = Double.parseDouble(JOptionPane.showInputDialog(null, "How much speed declines with each bounce\nMust be equal or below 1 and more than 0"));
            if (BouncyNumber <= 1 && BouncyNumber > 0) {
                BouncyNumberCheck = 1;
            }
        }
        } else {
            SpeedY=0;
            SpeedX=40;
            Height=500;
            Timedif=0.1;
            Gravity=9.82;
            Timeconstraint=1000;
            BouncyNumber=0.8;
            PositonX=595;
            StandstilX=false;
        }

        Balls.add(new Ball( PositonX,Height,SpeedX,SpeedY));

    }

    public void update() {
        if (counter*Timedif<Timeconstraint) {
            counter++;
            for (i=0; i<Balls.size();i++) {
                Speedupdate();
                Heightupdate();
                PosistionXupdate();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Time constraint reached\n   Simulation complete");
            isRunning=false;
        }


    }

    public void draw() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        update();

        g.setColor(Color.white);
        g.fillRect(0,0,1200,800);

        g.setColor(Color.red);
        for (i=0; i<Balls.size();i++) {
            g.fillOval((int) Balls.get(i).X, 780- ((int)Balls.get(i).Y-10), 10,10);
        }



        g.dispose();
        bs.show();
    }

    public void Speedupdate(){
        if (Balls.get(i).Standstill==false) {
                Balls.get(i).SpeedY -= Timedif*Gravity;
        }
    }

    public void Heightupdate(){
        if (Balls.get(i).Standstill==false) {
            if ((Balls.get(i).Y + (Balls.get(i).SpeedY * Timedif)) > 0) {
                Balls.get(i).Y += Balls.get(i).SpeedY * Timedif;
                System.out.println("| "+Balls.get(i).Y+" m| "+Balls.get(i).SpeedY+" m/s| " +(counter*Timedif)+" t|");
            } else {
                CheckBounce();
            }
        } else {
            System.out.println("| "+Balls.get(i).Y+" m| "+Balls.get(i).SpeedY+" m/s| " +(counter*Timedif)+" t| |StandstillY");
            Balls.get(i).SpeedX*=0.95;
            if (Balls.get(i).SpeedX>-0.01&&Balls.get(i).SpeedX<0.01){
                Balls.get(i).SpeedX=0;
                Balls.get(i).StandstillX=true;
            }
        }
    }

    public void CheckBounce(){

        Balls.get(i).SpeedY+=Timedif*Gravity;

        Timething=((-1*(Balls.get(i).SpeedY)-Math.sqrt((Balls.get(i).SpeedY*Balls.get(i).SpeedY)+(2*Gravity*Balls.get(i).Y)))/Gravity)*-1;

        Balls.get(i).SpeedY-=Timething*Gravity;
        Balls.get(i).Y+=SpeedY*Timething;

        System.out.println("| "+Balls.get(i).Y+" m| "+Balls.get(i).SpeedY+" m/s| " +((counter-1)*Timedif+Timething)+" t| |BOUNCE FLOOR|");


        Timething=Timedif-Timething;
        Balls.get(i).SpeedY*=-1;
        Balls.get(i).SpeedY*=BouncyNumber;
        Balls.get(i).SpeedY+=Timething*Gravity;
        Balls.get(i).Y+=Balls.get(i).SpeedY*Timething;
        if (Balls.get(i).SpeedY<3){
            Balls.get(i).SpeedY=0;
            Balls.get(i).Y=0;
            Balls.get(i).Standstill =true;
        }
        System.out.println("| "+Balls.get(i).Y+" m| "+Balls.get(i).SpeedY+" m/s| " +(counter*Timedif)+" t|");
        Balls.get(i).SpeedX*=0.9;



    }

    public void PosistionXupdate(){
        if (Balls.get(i).StandstillX==false) {
            if (Balls.get(i).X + Balls.get(i).SpeedX * Timedif < 0 || Balls.get(i).X + Balls.get(i).SpeedX * Timedif > 1190) {
                BounceX();
            } else {
                Balls.get(i).X += Balls.get(i).SpeedX * Timedif;
                System.out.println("| "+Balls.get(i).X+" m| "+Balls.get(i).SpeedX+" m/s| " +(counter*Timedif)+" t|");
            }
        } else {
            System.out.println("| "+Balls.get(i).X+" m| "+Balls.get(i).SpeedX+" m/s| " +(counter*Timedif)+" t| |StandstillX");
            if (Balls.get(i).StandstillX && Balls.get(i).Standstill){
                Balls.remove(i);
            }
        }
    }

    public void BounceX(){
        if (Balls.get(i).X>600){
            Timething=((1190-Balls.get(i).X)/Balls.get(i).SpeedX);
        } else {
            Timething=(Balls.get(i).X/Balls.get(i).SpeedX);
        }
        Balls.get(i).X+=Balls.get(i).SpeedX*Timething;

        System.out.println("| "+Balls.get(i).X+" m| "+Balls.get(i).SpeedX+" m/s| " +((counter-1)*Timedif+Timething)+" t| |BOUNCE WALL|");

        Timething=Timedif-Timething;
        Balls.get(i).SpeedX*=-1;
        Balls.get(i).SpeedX*=0.8;
        Balls.get(i).X+=Balls.get(i).SpeedX*Timething;
        System.out.println("| "+Balls.get(i).X+" m| "+Balls.get(i).SpeedX+" m/s| " +(counter*Timedif)+" t|");
    }

    public static void main(String[] args) {
        Balls painting = new Balls();
        painting.start();
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double deltaT = 1000.0 / fps;
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            long now = System.currentTimeMillis();
            if (now - lastTime > deltaT) {
                draw();
                lastTime = now;
            }
        }
        stop();
    }

    private class KL implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == 'e' || e.getKeyChar() == 'E') {

                Balls.add(new Ball( PositonX+Math.random()*60-30,Height+Math.random()*60-30,SpeedX,SpeedY));
                Balls.add(new Ball( PositonX+Math.random()*60-30,Height+Math.random()*60-30,SpeedX,SpeedY));
                Balls.add(new Ball( PositonX+Math.random()*60-30,Height+Math.random()*60-30,SpeedX,SpeedY));
            }

            if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P') {
            }
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }

    }

}
