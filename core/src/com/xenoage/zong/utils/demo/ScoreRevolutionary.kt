package com.xenoage.zong.utils.demo

import com.xenoage.utils.collections.CollectionUtils.addOrNew
import com.xenoage.utils.collections.CollectionUtils.alist
import com.xenoage.utils.math.Fraction._0
import com.xenoage.utils.math.Fraction.fr
import com.xenoage.zong.core.music.Pitch.A
import com.xenoage.zong.core.music.Pitch.B
import com.xenoage.zong.core.music.Pitch.C
import com.xenoage.zong.core.music.Pitch.D
import com.xenoage.zong.core.music.Pitch.E
import com.xenoage.zong.core.music.Pitch.F
import com.xenoage.zong.core.music.Pitch.G
import com.xenoage.zong.core.music.Pitch.pi
import com.xenoage.zong.core.music.format.SP.sp
import com.xenoage.zong.core.position.MP.mp
import com.xenoage.zong.core.text.UnformattedText.ut

import java.util.ArrayList

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.commands.core.music.ColumnElementWrite
import com.xenoage.zong.commands.core.music.PartAdd
import com.xenoage.zong.commands.core.music.direction.DirectionAdd
import com.xenoage.zong.commands.core.music.group.BarlineGroupAdd
import com.xenoage.zong.commands.core.music.group.BracketGroupAdd
import com.xenoage.zong.commands.core.music.slur.SlurAdd
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.format.StaffLayout
import com.xenoage.zong.core.instrument.Instrument
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.MeasureSide
import com.xenoage.zong.core.music.Part
import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.music.annotation.Annotation
import com.xenoage.zong.core.music.annotation.Articulation
import com.xenoage.zong.core.music.annotation.ArticulationType
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.barline.BarlineStyle
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.chord.Note
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.clef.ClefType
import com.xenoage.zong.core.music.direction.Dynamic
import com.xenoage.zong.core.music.direction.DynamicValue
import com.xenoage.zong.core.music.direction.Tempo
import com.xenoage.zong.core.music.direction.Wedge
import com.xenoage.zong.core.music.direction.WedgeType
import com.xenoage.zong.core.music.format.BezierPoint
import com.xenoage.zong.core.music.format.Position
import com.xenoage.zong.core.music.group.BarlineGroup
import com.xenoage.zong.core.music.group.BracketGroup
import com.xenoage.zong.core.music.group.StavesRange
import com.xenoage.zong.core.music.key.TraditionalKey
import com.xenoage.zong.core.music.key.TraditionalKey.Mode
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.music.slur.Slur
import com.xenoage.zong.core.music.slur.SlurType
import com.xenoage.zong.core.music.slur.SlurWaypoint
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.time.TimeType
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.io.selection.Cursor


/**
 * This class creates a demo score
 * (Revolutionary Etude op. 10/12 Chopin).
 */
object ScoreRevolutionary {

	fun createScore(): Score {
		val score = Score()

		val instr = Instrument.defaultInstrument
		val `is` = score.format.interlineSpace
		val staffLayout = StaffLayout(`is` * 9)
		score.format.staffLayoutOther = staffLayout

		val accent = arrayOf(ArticulationType.Accent)
		val staccato = arrayOf(ArticulationType.Staccato)

		val f2 = Companion.fr(1, 2)
		val f4 = Companion.fr(1, 4)
		val f8 = Companion.fr(1, 8)
		val f16 = Companion.fr(1, 16)

		val attachC: Chord
		val firstSlurC: Chord
		val lastSlurC: Chord
		var firstSlurB: BezierPoint
		var lastSlurB: BezierPoint

		val pianoPart = Part("Piano", null, 2, alist(instr))
		PartAdd(score, pianoPart, 0, null).execute()

		//set barlines and brackets
		BarlineGroupAdd(score.stavesList, BarlineGroup(StavesRange(0, 1), BarlineGroup.Style.Common)).execute()
		BracketGroupAdd(score.stavesList, BracketGroup(StavesRange(0, 1), BracketGroup.Style.Bracket)).execute()

		//use cursor for more convenient input
		var cursor = Cursor(score, MP.mp0, true)

		//C minor, C (4/4) time
		cursor.write(TraditionalKey(-3, Mode.Minor) as ColumnElement)
		cursor.write(TimeSignature(TimeType.timeCommon))

		//first staff: g-clef and some notes
		cursor.write(Clef(ClefType.Companion.getClefTreble()))

		//measure 1
		val tempo = Tempo(f4, 160) //, , FontInfo.defaultValue, );
		tempo.text = Companion.ut("Allegro con fuoco.")
		tempo.positioning = Position(null, 22f, -5f, -5f)
		cursor.write(tempo as ColumnElement)
		cursor.write(attachC = chord(f2, accent, Companion.pi(Companion.getB(), 4), Companion.pi(Companion.getD(), 5), Companion.pi(Companion.getF(), 5), Companion.pi(Companion.getG(), 5), Companion.pi(Companion.getB(), 5)))
		attachC.directions = addOrNew(attachC.directions, Dynamic(DynamicValue.f))
		cursor.write(Rest(f2))

		//measure 2
		cursor.write(Rest(f2))
		cursor.write(Rest(f4))
		var cresc = Wedge(WedgeType.Crescendo)
		cresc.positioning = Position(null, null, -1f, -2f)
		cursor.write(cresc as MeasureElement)
		cursor.openBeam()
		cursor.write(firstSlurC = chord(Companion.fr(3, 16), accent, Companion.pi(Companion.getA(), -1, 4), Companion.pi(Companion.getE(), -1, 5), Companion.pi(Companion.getF(), 5), Companion.pi(Companion.getA(), -1, 5)))
		cursor.write(lastSlurC = chord(f16, Companion.pi(Companion.getG(), 4), Companion.pi(Companion.getG(), 5)))
		cursor.closeBeam()
		cursor.write(cresc.wedgeEnd as MeasureElement)
		firstSlurB = BezierPoint(Companion.sp(`is` * 0.8f, `is` * 7.6f), Companion.sp(`is`, `is` * 0.8f))
		lastSlurB = BezierPoint(Companion.sp(0, `is` * 6f), Companion.sp(-`is`, `is`))
		SlurAdd(Slur(SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

		//measure 3
		cursor.write(attachC = chord(f2, Companion.pi(Companion.getD(), 5), Companion.pi(Companion.getF(), 5), Companion.pi(Companion.getG(), 5), Companion.pi(Companion.getD(), 6)))
		DirectionAdd(Dynamic(DynamicValue.f), attachC).execute()
		cursor.write(Rest(f2))

		//measure 4
		cursor.write(Rest(f2))
		cursor.write(Rest(f4))
		cresc = Wedge(WedgeType.Crescendo)
		cresc.positioning = Position(null, null, -1f, -2f)
		cursor.write(cresc as MeasureElement)
		cursor.openBeam()
		cursor.write(firstSlurC = chord(Companion.fr(3, 16), accent, Companion.pi(Companion.getA(), -1, 4), Companion.pi(Companion.getE(), -1, 5), Companion.pi(Companion.getF(), 0, 5), Companion.pi(Companion.getA(), -1, 5)))
		cursor.write(lastSlurC = chord(f16, Companion.pi(Companion.getG(), 0, 4), Companion.pi(Companion.getG(), 0, 5)))
		cursor.closeBeam()
		cursor.write(cresc.wedgeEnd as MeasureElement)
		firstSlurB = BezierPoint(Companion.sp(`is` * 0.8f, `is` * 7.6f), Companion.sp(`is`, `is` * 0.8f))
		lastSlurB = BezierPoint(Companion.sp(0, `is` * 6f), Companion.sp(-`is`, `is`))
		SlurAdd(Slur(SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

		//measure 5
		cursor.write(attachC = chord(f4, staccato, Companion.pi(Companion.getF(), 5), Companion.pi(Companion.getG(), 5), Companion.pi(Companion.getD(), 6), Companion.pi(Companion.getF(), 6)))
		DirectionAdd(Dynamic(DynamicValue.f), attachC).execute()
		cursor.write(Rest(f4))
		cursor.write(Rest(f2))

		//second staff: f-clef some notes
		cursor = Cursor(cursor.score, mp(1, 0, 0, Companion.get_0(), 0), true)
		cursor.write(Clef(ClefType.Companion.getClefBass()))

		//measure 1
		cursor.openBeam()
		cursor.write(Rest(f8))
		cursor.write(firstSlurC = chord(f16, Companion.pi(Companion.getA(), -1, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 4)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getF(), 0, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getE(), -1, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 4)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getB(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getA(), -1, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 3)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getF(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getE(), -1, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 3)))
		cursor.closeBeam()

		//measure 2
		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getB(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getA(), -1, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 2)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getF(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getE(), -1, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 2)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.write(chord(f16, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.write(chord(f16, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(lastSlurC = chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.closeBeam()
		firstSlurB = BezierPoint(Companion.sp(0, `is` * 1.5f), Companion.sp(15, 5))
		lastSlurB = BezierPoint(Companion.sp(0, `is` * 7.5f), Companion.sp(-`is` * 5, `is` * 2))
		SlurAdd(Slur(SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

		//measure 3
		cursor.write(chord(f8, staccato, Companion.pi(Companion.getB(), 0, 1)))
		cursor.openBeam()
		cursor.write(firstSlurC = chord(f16, Companion.pi(Companion.getA(), -1, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 4)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getF(), 0, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getE(), -1, 4)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 4)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getB(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getA(), -1, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 3)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getF(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 3)))
		cursor.write(chord(f16, Companion.pi(Companion.getE(), -1, 3)))
		cursor.write(lastSlurC = chord(f16, Companion.pi(Companion.getD(), 0, 3)))
		cursor.closeBeam()
		firstSlurB = BezierPoint(Companion.sp(0, `is` * 1.5f), Companion.sp(15, 3))
		lastSlurB = BezierPoint(Companion.sp(0, `is` * 5f), Companion.sp(-`is` * 5.5f, `is` * 2))
		SlurAdd(Slur(SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

		//measure 4
		cursor.openBeam()
		cursor.write(firstSlurC = chord(f16, accent, Companion.pi(Companion.getB(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getA(), -1, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 2)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getF(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getE(), -1, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getD(), 0, 2)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.write(chord(f16, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.closeBeam()

		cursor.openBeam()
		cursor.write(chord(f16, accent, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.write(chord(f16, Companion.pi(Companion.getC(), 0, 2)))
		cursor.write(lastSlurC = chord(f16, Companion.pi(Companion.getG(), 0, 1)))
		cursor.closeBeam()
		firstSlurB = BezierPoint(Companion.sp(-`is`, `is` * 8.5f), Companion.sp(15, 4))
		lastSlurB = BezierPoint(Companion.sp(0, `is` * 7.5f), Companion.sp(-`is` * 5, `is` * 2))
		SlurAdd(Slur(SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

		//measure 5
		cursor.write(chord(f4, staccato, Companion.pi(Companion.getB(), 0, 1)))
		cursor.write(Rest(f4))
		cursor.write(Rest(f2))

		//end line
		val barlineEnd = Barline.Companion.barline(BarlineStyle.LightHeavy)
		ColumnElementWrite(barlineEnd, score.getColumnHeader(4), null, MeasureSide.Right).execute()

		return score
	}


	private fun chord(fraction: Fraction, vararg pitches: Pitch): Chord {
		return chord(fraction, null, *pitches)
	}


	private fun chord(fraction: Fraction, articulations: Array<ArticulationType>?, vararg pitches: Pitch): Chord {
		val chord = Chord(Note.Companion.notes(pitches), fraction)
		if (articulations != null) {
			val a = alist(articulations.size)
			for (at in articulations)
				a.add(Articulation(at))
			chord.annotations = a
		}
		return chord
	}


	private fun clwp(c: Chord, bezierPoint: BezierPoint): SlurWaypoint {
		return SlurWaypoint(c, null, bezierPoint)
	}


}
