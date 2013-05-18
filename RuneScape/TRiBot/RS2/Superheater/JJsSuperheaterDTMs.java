package scripts;

import java.awt.Color;
import java.awt.Point;

import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.DTM;
import org.tribot.api.types.colour.DTMPoint;
import org.tribot.api.types.colour.DTMSubPoint;
import org.tribot.api.types.colour.Tolerance;

public class JJsSuperheaterDTMs {
	boolean guiCompleted = false;
	
	public DTM tinDTM(){
		//Tin
		DTMPoint DTM_PT_0 = new DTMPoint(new Color(97, 93, 93), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_0 = { new DTMSubPoint(new ColourPoint(new Point(-11, 0), new Color( 80, 63, 44)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(8, 6), new Color( 101, 97, 97)), new Tolerance(10, 10, 10),1)};
		DTM tinDTM = new DTM(DTM_PT_0, DTM_PTS_0);
		return tinDTM;
	}
	
	public DTM copperDTM(){
		// Copper
		DTMPoint DTM_PT_1 = new DTMPoint(new Color(178, 101, 54), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_1 = { new DTMSubPoint(new ColourPoint(new Point(-11, 5), new Color( 97, 77, 54)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(8, 7), new Color( 186, 106, 56)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(12, -4), new Color( 103, 82, 57)), new Tolerance(10, 10, 10),1)};
		DTM copperDTM = new DTM(DTM_PT_1, DTM_PTS_1);
		return copperDTM;
	}
	
	public DTM ironDTM(){
		// Iron
		DTMPoint DTM_PT_2 = new DTMPoint(new Color(56, 33, 25), new Tolerance(5, 5, 5));
		DTMSubPoint [] DTM_PTS_2 = { new DTMSubPoint(new ColourPoint(new Point(-12, 3), new Color( 98, 78, 55)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(11, -7), new Color( 107, 85, 59)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, 5), new Color( 56, 33, 25)), new Tolerance(10, 10, 10),1)};
		DTM ironDTM = new DTM(DTM_PT_2, DTM_PTS_2);
		return ironDTM;
	}
	
	public DTM silverDTM(){
		// Silver
		DTMPoint DTM_PT_3 = new DTMPoint(new Color(129, 129, 136), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_3 = { new DTMSubPoint(new ColourPoint(new Point(-10, 2), new Color( 95, 76, 52)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(8, 7), new Color( 136, 136, 143)), new Tolerance(10, 10, 10),1)};
		DTM silverDTM = new DTM(DTM_PT_3, DTM_PTS_3);
		return silverDTM;
	}
	
	public DTM coalDTM(){
		// Coal
		DTMPoint DTM_PT_4 = new DTMPoint(new Color(32, 32, 24), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_4 = { new DTMSubPoint(new ColourPoint(new Point(-1, -4), new Color( 32, 32, 24)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(8, 5), new Color( 35, 35, 27)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(-11, 4), new Color( 93, 74, 51)), new Tolerance(10, 10, 10),1)};
		DTM coalDTM = new DTM(DTM_PT_4, DTM_PTS_4);
		return coalDTM;
	}
	
	public DTM goldDTM(){
		// Gold
		DTMPoint DTM_PT_5 = new DTMPoint(new Color(183, 146, 25), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_5 = { new DTMSubPoint(new ColourPoint(new Point(-11, 3), new Color( 99, 79, 55)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, 6), new Color( 180, 143, 25)), new Tolerance(10, 10, 10),1)};
		DTM goldDTM = new DTM(DTM_PT_5, DTM_PTS_5);
		return goldDTM;
	}
	
	public DTM mithrilDTM(){
		// Mithril
		DTMPoint DTM_PT_6 = new DTMPoint(new Color(54, 55, 82), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_6 = { new DTMSubPoint(new ColourPoint(new Point(-11, 2), new Color( 97, 77, 54)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, 6), new Color( 53, 53, 80)), new Tolerance(10, 10, 10),1)};
		DTM mithrilDTM = new DTM(DTM_PT_6, DTM_PTS_6);
		return mithrilDTM;
	}
	
	public DTM adamantDTM(){
		// Adamant
		DTMPoint DTM_PT_7 = new DTMPoint(new Color(60, 70, 61), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_7 = { new DTMSubPoint(new ColourPoint(new Point(-11, 3), new Color( 99, 79, 55)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, 6), new Color( 58, 68, 59)), new Tolerance(10, 10, 10),1)};
		DTM adamantDTM = new DTM(DTM_PT_7, DTM_PTS_7);
		return adamantDTM;
	}
	
	public DTM runeDTM(){
		// Rune
		DTMPoint DTM_PT_9 = new DTMPoint(new Color(66, 80, 86), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_9 = { new DTMSubPoint(new ColourPoint(new Point(-11, 3), new Color( 95, 76, 52)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, 6), new Color( 64, 78, 84)), new Tolerance(10, 10, 10),1)};
		DTM runeDTM = new DTM(DTM_PT_9, DTM_PTS_9);
		return runeDTM;
	}
	
	public DTM[] oreDTMs(){
		DTM[] dtms = {tinDTM(), copperDTM(), ironDTM(), silverDTM(), coalDTM(), goldDTM(), mithrilDTM(), adamantDTM(), runeDTM()};
		return dtms;
	}
	
	public DTM superheatDTM(){
		// Superheat DTM
		DTMPoint DTM_PT_10 = new DTMPoint(new Color(237, 125, 3), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_10 = { new DTMSubPoint(new ColourPoint(new Point(-5, 5), new Color( 253, 226, 49)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(6, 5), new Color( 194, 161, 16)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(0, -7), new Color( 239, 82, 4)), new Tolerance(10, 10, 10),1)};
		DTM superheatDTM = new DTM(DTM_PT_10, DTM_PTS_10);
		return superheatDTM;
	}
	
	public DTM natureDTM(){
		// Nature rune
		DTMPoint DTM_PT_11 = new DTMPoint(new Color(117, 112, 111), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_11 = { new DTMSubPoint(new ColourPoint(new Point(-14, 2), new Color( 0, 0, 2)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(2, 14), new Color( 0, 0, 2)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(14, 1), new Color( 0, 0, 2)), new Tolerance(10, 10, 10),1)};
		DTM natureDTM = new DTM(DTM_PT_11, DTM_PTS_11);
		return natureDTM;
	}
}