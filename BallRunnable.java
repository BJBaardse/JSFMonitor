/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class BallRunnable implements Runnable {

    private Ball ball;
    private RW rw;

    public BallRunnable(Ball ball,RW rw) {
        this.ball = ball;
        this.rw = rw;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (ball.getColor() == Color.BLUE && ball.isEnteringCs())
                {
                    //System.out.println("WRITER entering");
                    rw.enterWriter();
                }
                if (ball.getColor() == Color.BLUE && ball.isLeavingCs())
                {
                    //System.out.println("WRITER leaving");
                    rw.exitWriter();
                }
                if (ball.getColor() == Color.RED && ball.isEnteringCs())
                {
                   // System.out.println("READER entering");
                    rw.enterReader();
                }
                if (ball.getColor() == Color.RED && ball.isLeavingCs())
                {
                    //System.out.println("READER leaving");
                    rw.exitReader();
                }
                ball.move();
                   
                Thread.sleep(ball.getSpeed());
                
            } catch (InterruptedException ex) {
                if (ball.isIn() && ball.getColor() == Color.BLUE)
                {
                    rw.exitWriter();
                }
                if (ball.isIn() && ball.getColor() == Color.RED)
                {
                    rw.exitReader();
                }
                Thread.currentThread().interrupt();
            }
        }
    }
}
