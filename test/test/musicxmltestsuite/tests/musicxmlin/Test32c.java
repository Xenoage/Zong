package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.collections.CollectionUtils.set;
import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import musicxmltestsuite.tests.base.Base32c;

import org.junit.Test;

import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;


public class Test32c
	implements Base32c, MusicXmlInTest {
	
	@Test public void test() {
		Voice voice = getFirstVoice();
		for (int chordIndex : range(voice.getElements()))
			assertAnnotations(chordIndex, (Chord) voice.getElements().get(chordIndex));
	}
	
	private void assertAnnotations(int chordIndex, Chord chord) {
		Set<Annotation> annotations = set(chord.getAnnotations());
		assertEquals(""+chordIndex, 2, annotations.size());
		assertTrue(""+chordIndex, containsArticulation(annotations, ArticulationType.Accent));
		assertTrue(""+chordIndex, containsArticulation(annotations, ArticulationType.Staccato));
	}
	
	private boolean containsArticulation(Set<Annotation> annotations, ArticulationType articulation) {
		return annotations.stream().anyMatch(
			a -> ((Articulation) a).getType() == articulation);
	}
	
}
