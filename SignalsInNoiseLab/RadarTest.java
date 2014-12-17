

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RadarTest.
 *
 * @author  Bradley
 * @version 12/16/14
 */
public class RadarTest
{
    @Test
    public void testNumber1()
    
    {
        boolean flag = true;
        Radar radar = new Radar(100,100,1,1,1,1);
        radar.setNoiseFraction(0.01);
        while (flag == true)
        {
            flag = radar.scan();
        }    
        assertEquals(1,radar.getPossxVel());
        assertEquals(1,radar.getPossyVel());
    }
    
    @Test
    public void testNumber2()
    
    {
        boolean flag = true;
        Radar radar = new Radar(100,100,-2,1,99,1);
        radar.setNoiseFraction(0.01);
        while (flag == true)
        {
            flag = radar.scan();
        }    
        assertEquals(-2,radar.getPossxVel());
        assertEquals(1,radar.getPossyVel());
    }
    
    
    @Test
    public void testNumber3()
    {
        boolean flag = true;
        Radar radar = new Radar(100,100,1,-2,1,99);
        radar.setNoiseFraction(0.01);
        while (flag == true)
        {
            flag = radar.scan();
        }    
        assertEquals(1,radar.getPossxVel());
        assertEquals(-2,radar.getPossyVel());
    }
    
    @Test
    public void testNumber4()
    
    {
        boolean flag = true;
        Radar radar = new Radar(100,100,-3,-3,99,99);
        radar.setNoiseFraction(0.01);
        while (flag == true)
        {
            flag = radar.scan();
        }    
        assertEquals(-3,radar.getPossxVel());
        assertEquals(-3,radar.getPossyVel());
    }
}
