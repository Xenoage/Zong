package material.beam.slant;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static material.beam.slant.Example.example;

import java.util.List;

import lombok.Getter;
import material.RossSuite;


/**
 * Examples from Ross, Chapter 3, Section 5
 * (p. 98 ff.)
 * 
 * @author Andreas Wenger
 */
public class RossBeamSlant
	implements RossSuite<Example> {
	
	//examples commented out with "rule?": example seems to be wrong or no
	//rule is presented which explains it
	@Getter List<Example> examples = alist(
		//hang/straddle explained
		example("p99 1").left(5, 3).width(6).right(6, 3.25),
		example("p99 2").left(7, 3).width(6.5).right(8, 3.25),
		//normal length
		example("p99 3").left(5, 3.5).width(6.5).right(4, 3.25),
		//larger slant
		example("p99 4").left(6, 3.25).width(6.5).right(8, 3.25),
		example("p99 5").left(6, 3.25).width(6.5).right(10, 4),
		//straddles
		example("p99 6").left(6, 3.25).width(6.5).right(6, 3.25),
		example("p99 7").left(8, 3.25).width(6.5).right(9, 3.5),
		//slant dependent on horizontal spacing: normal spacing beginning at example 6,
		//the previous examples are incorrect
		example("p100 6").left(2, 3.25).width(4.0).right(-1, 3.5),
		example("p100 7").left(2, 3.25).width(4.5).right(-1, 3.5),
		example("p100 8").left(2, 3.25).width(5.0).right(-1, 3.5),
		//close spacing. right stem is longer
		example("p101 close 1").left(2, 3.25).width(1.5).right(-1, 4.5),
		example("p101 close 2").left(2, 3.25).width(2.0).right(-1, 4.5),
		example("p101 close 3").left(2, 3.25).width(2.5).right(-1, 4.5),
		example("p101 close 4").left(2, 3.25).width(3.0).right(-1, 4.5),
		example("p101 close 5").left(2, 3.25).width(3.4).right(-1, 4.5), //from 3.5, we use normal spacing
		//by interval
		example("p101 a1").left(-6, 5).width(6).right(-3, 4),
		example("p101 a2").left(13, 4.5).width(6).right(11, 4),
		example("p101 b").left(6, 3.25).width(6).right(7, 3.5),
		example("p101 c1").left(1, 3).width(6).right(-1, 3.5),
		example("p101 c2").left(2, 3.25).width(6).right(0, 3.25),
		example("p101 d1").left(-1, 3).width(6).right(-4, 4),
		example("p101 d2").left(2, 3.25).width(6).right(-1, 3.5),
		example("p101 e").left(9, 4.75).width(6).right(2, 3.25),
		//diatonic movement of inner notes does not change the beam
		example("p103 1").left(0, 3.25).width(6).right(3, 3),
		example("p103 2").left(0, 3.25).middleNotes(1, 2).right(3, 3),
		example("p103 3").left(5, 3).width(6).right(9, 3.75),
		example("p103 4").left(5, 3).middleNotes(6, 7, 8).right(9, 3.75),
		//middle notes further away do not change the beam
		example("p103 5").left(0, 3.25).middleNotes(-1, -2).right(3, 3),
		example("p103 6").left(5, 3).middleNotes(8, 10, 9).right(9, 3.75),
		//examples for C (treble clef) and E (bass clef) intervals
		example("p104 r1 c1", -2, 3.25, -2, 3.25),
		example("p104 r1 c2", 5, 3, 5, 3),
		example("p104 r1 c3", 5, 3, 5, 3).stemDir(Up),
		example("p104 r2 c1", -2, 3.25, -1, 3),
		example("p104 r2 c2", -2, 3.25, 0, 3.25),
		example("p104 r2 c3", -2, 3.25, 1, 3),
		example("p104 r2 c4", -2, 4, 2, 3.25),
		example("p104 r2 c5", -2, 4.25, 3, 3),
		//rule? example("p104 r2 c6", -2, 5.25, 4, 3.25),
		example("p104 r2 c7", -2, 5.25, 5, 3),
		example("p104 r3 c1", -2, 3.25, -3, 3.5),
		example("p104 r3 c2", -2, 3.5, -4, 4),
		example("p104 r3 c3", -2, 3.5, -5, 4.5),
		//rule? example("p104 r3 c4", -2, 3.5, -6, 5),
		//rule? example("p104 r3 c5", -2, 3.5, -7, 5.5),
		//rule? example("p104 r4 c1", 5, 3, 4, 3.25).stemDir(Up),
		example("p104 r4 c2", 5, 3, 3, 3.5).stemDir(Up),
		example("p104 r4 c3", 5, 3, 2, 3.5),
		example("p104 r4 c4", 5, 3, 1, 3.75),
		example("p104 r4 c5", 5, 3, 0, 4.25),
		example("p104 r4 c6", 5, 3, -1, 4.75),
		example("p104 r4 c7", 5, 3, -2, 5.25),
		example("p104 r5 c1", 12, 4, 11, 3.75),
		example("p104 r5 c2", 12, 4, 10, 3.5),
		example("p104 r5 c3", 12, 4, 9, 3),
		example("p104 r5 c4", 12, 4, 8, 3.25),
		example("p104 r5 c5", 12, 4.25, 7, 3),
		//rule? example("p104 r5 c6", 12, 5.25, 6, 3.25),
		example("p104 r5 c7", 12, 5.25, 5, 3),
		example("p104 r6 c1", 5, 3.5, 4, 3.25),
		example("p104 r6 c2", 5, 3.5, 3, 3),
		example("p104 r6 c3", 5, 3.75, 2, 3.25).stemDir(Down),
		example("p104 r6 c4", 5, 3.5, 0, 2.5).stemDir(Down),
		example("p104 r7 c1", 5, 3, 6, 3.25),
		example("p104 r7 c2", 5, 3, 7, 3.5),
		example("p104 r7 c3", 5, 3, 8, 3.25),
		example("p104 r7 c4", 5, 3, 9, 3.75),
		example("p104 r7 c5", 5, 3, 10, 4.25),
		example("p104 r7 c6", 5, 3, 11, 4.75),
		example("p104 r7 c7", 5, 3, 12, 5.25),
		//TODO: examples for D (treble clef) and F (bass clef) intervals
		//TODO: examples for E (treble clef) and G (bass clef) intervals
		//TODO: examples for F (treble clef) and A (bass clef) intervals
		//examples for G (treble clef) and B (bass clef) intervals
		example("p108 r1 c1", 2, 3.25, 2, 3.25),
		example("p108 r1 c2", 9, 3.5, 9, 3.5),
		example("p108 r1 c3", 2, 3, 2, 3).stemDir(Down),
		example("p108 r2 c1", 2, 3.25, 3, 3),
		example("p108 r2 c2", 2, 3.25, 4, 3.25),
		example("p108 r2 c3", 2, 3.5, 5, 3),
		example("p108 r2 c4", 2, 4, 6, 3.25).stemDir(Up),
		//rule? example("p108 r2 c5", 2, 3.5, 7, 2.75).stemDir(Up), //Ross error: caption != notes
		example("p108 r2 c6", 2, 4.25, 8, 3).stemDir(Up),
		example("p108 r2 c7", 2, 4.25, 9, 2.75).stemDir(Up),
		example("p108 r3 c1", -5, 4.5, -2, 3.5),
		//rule? example("p108 r3 c2", -5, 4.5, -1, 3),
		//rule? example("p108 r3 c3", -5, 4.75, 0, 3.25),
		example("p108 r3 c4", -5, 4.75, 1, 3),
		example("p108 r3 c5", -5, 5.5, 2, 3.25),
		example("p108 r3 c6", 9, 2.75, 2, 4.25).stemDir(Up),
		example("p108 r4 c1", 2, 3.25, 1, 3.5),
		example("p108 r4 c2", 2, 3.25, 0, 3.25),
		example("p108 r4 c3", 2, 3.25, -1, 3.5),
		example("p108 r4 c4", 2, 3.25, -2, 4),
		//rule? example("p108 r4 c5", 2, 3.25, -3, 4.75),
		//rule? example("p108 r4 c6", 2, 3.25, -4, 5.25),
		example("p108 r4 c7", 2, 3.25, -5, 5.5),
		example("p108 r5 c1", 9, 3, 10, 3.25),
		example("p108 r5 c2", 9, 3, 11, 3.5),
		example("p108 r5 c3", 9, 3, 12, 4),
		//rule? example("p108 r5 c4", 9, 3, 13, 4.5),
		//rule? example("p108 r5 c5", 9, 3, 14, 5),
		//rule? example("p108 r5 c6", 9, 3, 15, 5.5),
		example("p108 r6 c1", 9, 3.5, 8, 3.25),
		example("p108 r6 c2", 9, 3.5, 7, 3),
		example("p108 r6 c3", 9, 3.5, 6, 3.25),
		example("p108 r6 c4", 9, 3.75, 5, 3),
		example("p108 r6 c5", 9, 4.5, 4, 3.5),
		example("p108 r6 c6", 9, 4.75, 3, 3),
		example("p108 r6 c7", 9, 4.75, 2, 3.25),
		example("p108 r7 c1", 2, 3.25, 4, 3.25).stemDir(Down),
		example("p108 r7 c2", 2, 3.25, 5, 3.75).stemDir(Down),
		example("p108 r7 c3", 2, 3.25, 6, 4),
		example("p108 r7 c4", 2, 3, 7, 4),
		example("p108 r7 c5", 2, 3.25, 8, 4.5),
		example("p108 r7 c6", 2, 3.25, 9, 4.75),
		//TODO: examples for A (treble clef) and C (bass clef) intervals
		//TODO: examples for B (treble clef) and D (bass clef) intervals
		//interval: very high and very low notes
		example("p111 r1 c1").left(-9, 6.5).width(6).right(-3, 4),
		example("p111 r1 c2").left(-4, 4).width(6).right(-3, 3.75),
		example("p111 r2 c1").left(16, 6).width(6).right(12, 4.5),
		example("p111 r2 c2").left(12, 4).width(6).right(11, 3.75),
		//interval 2nd
		example("p111 interval 2nd 1").left(-1, 3.5).width(6).right(0, 3.25),
		example("p111 interval 2nd 2").left(7, 3.5).width(6).right(6, 3.25),
		//interval 3rd
		example("p111 interval 3rd 1").left(0, 3.25).width(6).right(2, 3.25),
		example("p111 interval 3rd 2").left(1, 3.5).width(6).right(3, 3),
		//interval 4th
		example("p111 interval 4th 1").left(3, 3).width(6).right(6, 3.5),
		example("p111 interval 4th 2").left(6, 3.25).width(6).right(9, 3.5),
		//interval 5th
		example("p111 interval 5th 1").left(6, 3.25).width(6).right(10, 4),
		example("p111 interval 5th 2").left(-2, 4).width(6).right(2, 3.25),
		//interval 6th
		example("p111 interval 6th 1").left(-2, 4.25).width(6).right(3, 3),
		example("p111 interval 6th 2").left(-1, 4.5).width(6).right(4, 3.5),
		//interval 7th
		example("p111 interval 7th 1").left(-1, 4.75).width(6).right(5, 3),
		example("p111 interval 7th 2").left(0, 4.5).width(6).right(6, 3.25),
		//interval 8th
		example("p111 interval 8th 1").left(3, 3).width(6).right(10, 5.25),
		example("p111 interval 8th 2").left(2, 3.25).width(6).right(9, 4.75),
		//close spacing rules
		example("p113 close a1").left(0, 3.25).width(3).right(-5, 5.5),
		example("p113 close a2").left(8, 3.25).width(3).right(11, 4.5),
		example("p113 close b1").left(1, 3).width(3).right(-5, 5.5),
		example("p113 close b2").left(9, 4.5).width(3).right(5, 3),
		//more close spacing examples
		example("p114 r1 1").left(-2, 3.25).width(2.5).right(-1, 3),
		example("p114 r1 2").left(-2, 4).width(2.5).right(0, 3.25),
		example("p114 r1 3").left(-2, 4).width(2.5).right(1, 3),
		example("p114 r1 4").left(-2, 5).width(2.5).right(2, 3.25),
		example("p114 r1 5").left(-2, 5).width(2.5).right(3, 3),
		example("p114 r1 6").left(-1, 3.5).width(2.5).right(0, 3.25),
		example("p114 r1 7").left(-1, 3.5).width(2.5).right(1, 3),
		example("p114 r1 8").left(-1, 4.5).width(2.5).right(2, 3.25),
		example("p114 r1 9").left(-1, 4.5).width(2.5).right(3, 3),
		//TODO: p. 114, r2, r3, r4, r5, r6
		example("p114 r7 1").left(13, 6.5).width(2.5).right(6, 3.25),
		example("p114 r7 2").left(13, 6.5).width(2.5).right(5, 3),
		example("p114 r7 3").left(12, 4).width(2.5).right(10, 3.25),
		example("p114 r7 4").left(12, 4).width(2.5).right(9, 3),
		example("p114 r7 5").left(12, 5).width(2.5).right(8, 3.25),
		example("p114 r7 6").left(12, 5).width(2.5).right(7, 3),
		example("p114 r7 7").left(12, 6).width(2.5).right(6, 3.25),
		example("p114 r7 8").left(12, 6).width(2.5).right(5, 3),
		example("p114 r7 9").left(11, 3.5).width(2.5).right(10, 3.25),
		example("p114 r7 10").left(11, 3.5).width(2.5).right(9, 3),
		example("p114 r7 11").left(11, 4.5).width(2.5).right(8, 3.25),
		example("p114 r7 12").left(11, 4.5).width(2.5).right(7, 3),
		example("p114 r7 13").left(11, 5.5).width(2.5).right(6, 3.25),
		example("p114 r7 14").left(11, 5.5).width(2.5).right(5, 3),
		//horizontal beams
		example("p115 r1 1").left(5, 3.5).middleNotes(7, 4).right(5, 3.5),
		example("p115 r1 2").left(4, 3).middleNotes(5, 6).right(4, 3),
		example("p115 r1 3").left(5, 3.5).middleNotes(8, 4).right(5, 3.5),
		example("p115 r2 1").left(7, 4.75).middleNotes(3).right(5, 3.75),
		example("p115 r2 2").left(6, 4.25).middleNotes(6).right(9, 4.75),
		example("p115 r3 1").left(1, 4.5).middleNotes(4).right(2, 4),
		example("p115 r3 2").left(3, 3.5).middleNotes(4).right(1, 4.5),
		example("p115 r4 1").left(3, 3).middleNotes(1, 3).right(1, 4),
		example("p115 r4 2").left(3, 2.5).middleNotes(10, 3).right(10, 6),
		example("p115 r5 1").left(10, 5.25).middleNotes(6, 7).right(9, 4.75),
		example("p115 r5 2").left(10, 4.25).middleNotes(8, 8).right(9, 3.75),
		example("p115 r6").left(5, 3.5).middleNotes(4, 8).right(7, 4.5),
		example("p116 r1").left(4, 3.5).middleNotes(7, 2).right(7, 5),
		example("p116 r2").left(9, 4.75).middleNotes(6, 10).right(11, 5.75),
		example("p116 r3").left(12, 6.5).middleNotes(5, 5).right(5, 3),
		example("p116 r4").left(5, 3).middleNotes(5, 5).right(12, 6.5),
		example("p116 r5").left(2, 3.25).middleNotes(2, 2).right(-3, 5.75),
		example("p116 r6").left(-4, 6).middleNotes(1, 1).right(1, 3.5),
		example("p116 r7 1").left(1, 4.5).middleNotes(4, 3).right(2, 4),
		example("p116 r7 2").left(1, 3.75).middleNotes(2, 2).right(0, 4.25),
		example("p117 r1 1").left(0, 4.5).middleNotes(3, -1).right(2, 3.5),
		example("p117 r1 2").left(3, 3.5).middleNotes(-1, 4).right(2, 4),
		example("p117 r2 1").left(6, 4).middleNotes(5, 4, 3, 4).right(7, 4.5),
		example("p117 r2 2").left(5, 3.5).middleNotes(4, 3, 5, 4).right(3, 2.5),
		example("p117 r2 3").left(-1, 5).middleNotes(2, 3, 2, 1).right(0, 4.5),
		example("p117 r2 4").left(3, 3).middleNotes(2, 1, 3, 2).right(1, 4),
		example("p117 r3 1").left(6, 4.5).middleNotes(2, 3, 4, 5).right(6, 4.5),
		example("p117 r3 2").left(8, 5).middleNotes(7, 6, 5, 4).right(8, 5),
		//r4, r5: do not apply here (no 8th notes)
		example("p117 r7").left(2, 4).middleNotes(4, 4).right(2, 4)
		//TODO: p. 118
	);

}
