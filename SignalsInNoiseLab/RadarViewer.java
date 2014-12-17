import javax.swing.JFrame;
import java.util.*;

/**
 * Class that contains the main method for the program and creates the frame containing the component.
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class RadarViewer
{
    /**
     * main method for the program which creates and configures the frame for the program
     *
     */
    public static void main(String[] args) throws InterruptedException
    {
        // create the radar, set the monster location, and perform the initial scan
        final int ROWS = 100;
        final int COLS = 100;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the x velocity you want the monster to have");
        int xvel = in.nextInt();
        System.out.println("Enter the y velocity you want the monster to have");
        int yvel = in.nextInt();
        System.out.println("Enter the x position you want the monster to have");
        int monsterLocationCol = in.nextInt();
        System.out.println("Enter the y position you want the monster to have");
        int monsterLocationRow = in.nextInt();
        
        Radar radar = new Radar(COLS, ROWS, xvel, yvel, monsterLocationCol, monsterLocationRow);
        radar.setNoiseFraction(0.01);
        radar.scan();
        
        JFrame frame = new JFrame();
        
        frame.setTitle("Signals in Noise Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // a frame contains a single component; create the radar component and add it to the frame
        RadarComponent component = new RadarComponent(radar);
        frame.add(component);
        
        // set the size of the frame to encompass the contained component
        frame.pack();
        
        // make the frame visible which will result in the paintComponent method being invoked on the
        //  component.
        frame.setVisible(true);
        
        // perform 100 scans of the radar wiht a slight pause between each
        // after each scan, instruct the Java Run-Time to redraw the window
        while(radar.scan())
        {
            Thread.sleep(100); // sleep 100 milliseconds (1/10 second)
            
            radar.scan();
            
            frame.repaint();
        }
    }

}