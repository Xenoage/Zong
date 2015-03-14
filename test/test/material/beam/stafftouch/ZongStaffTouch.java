package material.beam.stafftouch;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static material.beam.stafftouch.TouchExample.example32nd;
import static material.beam.stafftouch.TouchExample.example8th;

import java.util.List;

import lombok.Getter;
import material.ZongSuite;

/**
 * Examples of beams touching the staff.
 * 
 * @author Andreas Wenger
 */
public class ZongStaffTouch
	implements ZongSuite<TouchExample> {

	@Getter List<TouchExample> examples = alist(
		//8th beams
			//Upstem beams
			  //Above staff
					//touching top right
					example8th("8th upstem top right", 10, 9, Up).touch(),
					//touching top left
					example8th("8th upstem top left", 9, 10, Up).touch(),
					//touching both sides
					example8th("8th upstem top both", 9, 9, Up).touch(),
					//no touch
					example8th("8th upstem top none", 10, 10, Up),
				//Below staff
					example8th("8th upstem bottom right", -1, 0, Up).touch(),
					//touching top left
					example8th("8th upstem bottom left", 0, -1, Up).touch(),
					//touching both sides
					example8th("8th upstem bottom both", 0, 0, Up).touch(),
					//no touch
					example8th("8th upstem bottom none", -1, -1, Up),
				//Within staff
					example8th("8th upstem within 1", 2, 6, Up).touch(),
					example8th("8th upstem within 2", 6, 2, Up).touch(),
				//Crossing staff
					example8th("8th upstem crossing", -4, 12, Up).touch(),
			//Downstem beams
			  //Above staff
					//touching top right
					example8th("8th downstem top right", 9, 8, Down).touch(),
					//touching top left
					example8th("8th downstem top left", 8, 9, Down).touch(),
					//touching both sides
					example8th("8th downstem top both", 8, 8, Down).touch(),
					//no touch
					example8th("8th downstem top none", 9, 9, Down),
				//Below staff
					example8th("8th downstem bottom right", -2, -1, Down).touch(),
					//touching top left
					example8th("8th downstem bottom left", -1, -2, Down).touch(),
					//touching both sides
					example8th("8th downstem bottom both", -1, -1, Down).touch(),
					//no touch
					example8th("8th downstem bottom none", -2, -2, Down),
				//Within staff
					example8th("8th downstem within 1", 2, 6, Down).touch(),
					example8th("8th downstem within 2", 6, 2, Down).touch(),
				//Crossing staff
					example8th("8th downstem crossing", -4, 12, Down).touch(),
		//32nd beams
			//Upstem beams
			  //Above staff
					//touching top right
					example32nd("32nd upstem top right", 13, 12, Up).touch(),
					//touching top left
					example32nd("32nd upstem top left", 12, 13, Up).touch(),
					//touching both sides
					example32nd("32nd upstem top both", 12, 12, Up).touch(),
					//no touch
					example32nd("32nd upstem top none", 13, 13, Up),
				//Below staff
					example32nd("32nd upstem bottom right", -1, 0, Up).touch(),
					//touching top left
					example32nd("32nd upstem bottom left", 0, -1, Up).touch(),
					//touching both sides
					example32nd("32nd upstem bottom both", 0, 0, Up).touch(),
					//no touch
					example32nd("32nd upstem bottom none", -1, -1, Up),
				//Within staff
					example32nd("32nd upstem within 1", 2, 6, Up).touch(),
					example32nd("32nd upstem within 2", 6, 2, Up).touch(),
				//Crossing staff
					example32nd("32nd upstem crossing", -6, 14, Up).touch(),
			//Downstem beams
			  //Above staff
					//touching top right
					example32nd("32nd downstem top right", 9, 8, Down).touch(),
					//touching top left
					example32nd("32nd downstem top left", 8, 9, Down).touch(),
					//touching both sides
					example32nd("32nd downstem top both", 8, 8, Down).touch(),
					//no touch
					example32nd("32nd downstem top none", 9, 9, Down),
				//Below staff
					example32nd("32nd downstem bottom right", -5, -4, Down).touch(),
					//touching top left
					example32nd("32nd downstem bottom left", -4, -5, Down).touch(),
					//touching both sides
					example32nd("32nd downstem bottom both", -4, -4, Down).touch(),
					//no touch
					example32nd("32nd downstem bottom none", -5, -5, Down),
				//Within staff
					example32nd("32nd downstem within 1", 2, 6, Down).touch(),
					example32nd("32nd downstem within 2", 6, 2, Down).touch(),
				//Crossing staff
					example32nd("32nd downstem crossing", -6, 14, Down).touch()
	);
		
}
