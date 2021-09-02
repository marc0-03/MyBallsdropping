import java.awt.*;

    public class Ball {
        public double X,Y,SpeedX,SpeedY;
        private Rectangle hitBox;
        public boolean Standstill,StandstillX;

        public Ball(double x, double y, double SX, double SY) {
            X=x;
            Y=y;
            SpeedX=SX;
            SpeedY=SY;

            Standstill=false;
            StandstillX=false;

        }

        public double getX() {
            return X;
        }

        public double getY() {
            return Y;
        }

    }
