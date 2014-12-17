/**
 * The model for radar scan and accumulator
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
import java.util.*;
public class Radar
{
    
    // stores whether each cell triggered detection for the current scan of the radar
    private boolean[][] currentScan;
    private boolean[][] oldScan;
    
    // value of each cell is incremented for each scan in which that cell triggers detection 
    private int[][] accumulator;
    
    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;
    private int xvel;
    private int yvel;
    private int possxvel;
    private int possyvel;
    
    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;
    
    // number of scans of the radar since construction
    private int numScans;

    /**
     * Constructor for objects of class Radar
     * 
     * @param   rows    the number of rows in the radar grid
     * @param   cols    the number of columns in the radar grid
     */
    public Radar(int cols, int rows, int xvel, int yvel, int monsterLocationCol, int monsterLocationRow)
    {
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false
        oldScan = new boolean[rows][cols];
        accumulator = new int[11][11]; // elements will be set to 0
        
        // randomly set the location of the monster (can be explicity set through the
        //  setMonsterLocation method
        //monsterLocationRow = (int)(Math.random() * rows);
        //monsterLocationCol = (int)(Math.random() * cols);
        //xvel = (int)(Math.random()*10 ) - 5;
        //yvel = (int)(Math.random()*10 ) - 5;
       this.xvel = xvel;
       this.yvel = yvel;
       this.monsterLocationCol = monsterLocationCol;
       this.monsterLocationRow = monsterLocationRow;
        
        //monsterLocationRow = 1;
        //monsterLocationCol = 1;
        noiseFraction = 0.05;
        numScans= 0;
    }
    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void start()
    {
        currentScan[monsterLocationRow][monsterLocationCol] = true;
        injectNoise();
        copy();
        updateMonsterLocation();
        
    }
    
    public void copy()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
               oldScan[row][col] = currentScan[row][col];
            }
        }
    }
    
    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public boolean scan()
    {
        if(monsterLocationRow > 100 || monsterLocationRow < 0 || monsterLocationCol < 0 || monsterLocationCol > 100)
        {
            System.out.println("The monster has left the screen");
            return false;
        }
        else
        {
            int highest = 0;
            int sechighest = 0;
            // zero the current scan grid
            copy();
            for(int row = 0; row < currentScan.length; row++)
            {
                for(int col = 0; col < currentScan[0].length; col++)
                {
                    currentScan[row][col] = false;
                }
            }
            
            // detect the monster
            currentScan[monsterLocationRow][monsterLocationCol] = true;
            
            // inject noise into the grid
            injectNoise();
            
            // update the accumulator
            
            for(int row = 0; row < currentScan.length; row++)
            {
                for(int col = 0; col < currentScan[0].length; col++)
                {
                    if(currentScan[row][col])
                    {
                        for(int row2 = 0; row2 < oldScan.length; row2++)
                        {
                            for(int col2 = 0; col2 < oldScan[0].length; col2++)
                            {
                                if(oldScan[row2][col2])
                                {
                                    int rowDiff = row - row2;
                                    int colDiff = col - col2;
                                    //System.out.println("Checkpoint 1 " + row + " " + col + " " + row2 + " " + col2);
                                    if(Math.abs(rowDiff) <= 5 && Math.abs(colDiff) <= 5)
                                    {
                                        accumulator[rowDiff + 5][colDiff + 5]++;
                                    }                                    
                                }
                            }
                        }
                    }
                }
            }
            
            for(int row = 0; row < accumulator.length; row++)
            {
                for( int col = 0; col < accumulator[0].length; col ++)
                {
                    if(accumulator[row][col] > highest)
                    {
                        highest = accumulator[row][col];
                        possxvel = col - 5;
                        possyvel = row - 5;
                    }
                    if(accumulator[row][col] < highest && accumulator[row][col] >= sechighest)
                    {
                        sechighest = accumulator[row][col];
                    }
                }
            }
            
            if(highest > sechighest + 8)
            {
                System.out.println("The slope of the x is: " + possxvel);
                System.out.println("The slope of the y is: " + possyvel);
                return false;
            }
        
            updateMonsterLocation();
            // keep track of the total number of scans
            numScans++;
            return true;
        }
    }

    /**
     * Updates the location of the monster
     * 
     * @param   row     the row in which the monster is located
     * @param   col     the column in which the monster is located
     * @pre row and col must be within the bounds of the radar grid
     */
    public void updateMonsterLocation()
    {
        // remember the row and col of the monster's location
        monsterLocationRow+= yvel;
        monsterLocationCol+= xvel;
        //System.out.println("monsterlocation " + monsterLocationCol + " " + monsterLocationRow);
        
        // update the radar grid to show that something was detected at the specified location
        //currentScan[row][col] = true;
    }
    
     /**
     * Sets the probability that a given cell will generate a false detection
     * 
     * @param   fraction    the probability that a given cell will generate a flase detection expressed
     *                      as a fraction (must be >= 0 and < 1)
     */
    public void setNoiseFraction(double fraction)
    {
        noiseFraction = fraction;
    }
    
    /**
     * Returns true if the specified location in the radar grid triggered a detection.
     * 
     * @param   row     the row of the location to query for detection
     * @param   col     the column of the location to query for detection
     * @return true if the specified location in the radar grid triggered a detection
     */
    public boolean velocityDetected(int row, int col)
    {
        return currentScan[row][col];
    }
    
    /**
     * Returns the number of times that the specified location in the radar grid has triggered a
     *  detection since the constructor of the radar object.
     * 
     * @param   row     the row of the location to query for accumulated detections
     * @param   col     the column of the location to query for accumulated detections
     * @return the number of times that the specified location in the radar grid has
     *          triggered a detection since the constructor of the radar object
     */
    public int getAccumulatedDetection(int row, int col)
    {
        return accumulator[row][col];
    }
    
    /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getNumRows()
    {
        return currentScan.length;
    }
    
     /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getPossxVel()
    {
        return possxvel;
    }
    
     
    
     /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getPossyVel()
    {
        return possyvel;
    }
    
    /**
     * Returns the number of columns in the radar grid
     * 
     * @return the number of columns in the radar grid
     */
    public int getNumCols()
    {
        return currentScan[0].length;
    }
    
    /**
     * Returns the number of scans that have been performed since the radar object was constructed
     * 
     * @return the number of scans that have been performed since the radar object was constructed
     */
    public int getNumScans()
    {
        return numScans;
    }
    
    /**
     * Sets cells as falsely triggering detection based on the specified probability
     * 
     */
    private void injectNoise()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                // each cell has the specified probablily of being a false positive
                if(Math.random() < noiseFraction)
                {
                    currentScan[row][col] = true;
                }
            }
        }
    }
    
}