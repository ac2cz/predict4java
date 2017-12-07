/**
 predict4java: An SDP4 / SGP4 library for satellite orbit predictions

 Copyright (C)  2004-2010  David A. B. Johnson, G4DPZ.

 Author: David A. B. Johnson, G4DPZ <dave@g4dpz.me.uk>

 Comments, questions and bug reports should be submitted via
 http://sourceforge.net/projects/websat/
 More details can be found at the project home page:

 http://websat.sourceforge.net

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, visit http://www.fsf.org/
 */
package uk.me.g4dpz.satellite;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author David A. B. Johnson, g4dpz
 *
 */
public final class SatPosTest {

    private static final String FORMAT_4F = "%4.0f %4.0f";
    protected static final String[] AO85_TLE = {
            "AO-85",
            "1 40967U 15058D   17333.47991085  .00000229  00000-0  44518-4 0 01659",
            "2 40967 064.7779 077.4197 0184716 018.3944 342.3672 14.75581611024180"};
    
    static final GroundStationPosition GROUND_STATION =
            new GroundStationPosition(45.510948, -73.506672, 10);
    private static final String FORMAT_9_7F = "%9.4f";
    
	/**
     * Default Constructor.
     */
    public SatPosTest() {
    }

    @Test
    public void dopplerCalc() {
    	
    	DateTime timeNow = new DateTime("2017-12-07T18:16:00Z");
        final TLE tle = new TLE(AO85_TLE);
        final Satellite satellite = SatelliteFactory.createSatellite(tle);
        final SatPos satellitePosition = satellite.getPosition(GROUND_STATION, timeNow.toDate());
        Assert.assertEquals("1724.2296", String.format(FORMAT_9_7F, satellitePosition.getDopplerFrequency(145980000)));
        
    }
    
    @Test
    public void footprintCalculatedCorrectly() {
        final SatPos pos = new SatPos();
        pos.setLatitude(0);
        pos.setLongitude(0);
        pos.setAltitude(1000);
        double[][] rangeCircle = pos.getRangeCircle();
        Assert.assertEquals("  30    0", String.format(FORMAT_4F, rangeCircle[0][0], rangeCircle[0][1]));
        Assert.assertEquals("   1  330", String.format(FORMAT_4F, rangeCircle[89][0], rangeCircle[89][1]));
        Assert.assertEquals(" -30  359", String.format(FORMAT_4F, rangeCircle[179][0], rangeCircle[179][1]));
        Assert.assertEquals("  -1   30", String.format(FORMAT_4F, rangeCircle[269][0], rangeCircle[269][1]));


        pos.setLatitude(10.0 / 360.0 * 2.0 * Math.PI);
        pos.setLongitude(10.0 / 360.0 * 2.0 * Math.PI);
        pos.setAltitude(1000);
        rangeCircle = pos.getRangeCircle();
        Assert.assertEquals("  40   10", String.format(FORMAT_4F, rangeCircle[0][0], rangeCircle[0][1]));
        Assert.assertEquals("   9  339", String.format(FORMAT_4F, rangeCircle[89][0], rangeCircle[89][1]));
        Assert.assertEquals(" -20    9", String.format(FORMAT_4F, rangeCircle[179][0], rangeCircle[179][1]));
        Assert.assertEquals("   8   41", String.format(FORMAT_4F, rangeCircle[269][0], rangeCircle[269][1]));
    }
}
