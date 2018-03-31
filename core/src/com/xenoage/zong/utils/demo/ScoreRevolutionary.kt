package com.xenoage.zong.utils.demo

import com.xenoage.utils.collections.addOrNew
import com.xenoage.utils.math.*
import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.commands.core.music.ColumnElementWrite
import com.xenoage.zong.commands.core.music.PartAdd
import com.xenoage.zong.commands.core.music.direction.DirectionAdd
import com.xenoage.zong.commands.core.music.group.BarlineGroupAdd
import com.xenoage.zong.commands.core.music.group.BracketGroupAdd
import com.xenoage.zong.commands.core.music.slur.SlurAdd
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.format.StaffLayout
import com.xenoage.zong.core.instrument.Instrument
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.MeasureSide
import com.xenoage.zong.core.music.Part
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.*
import com.xenoage.zong.core.music.annotation.ArticulationType
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.barline.BarlineStyle
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.clef.clefBass
import com.xenoage.zong.core.music.clef.clefTreble
import com.xenoage.zong.core.music.direction.*
import com.xenoage.zong.core.music.format.BezierPoint
import com.xenoage.zong.core.music.format.Position
import com.xenoage.zong.core.music.format.SP
import com.xenoage.zong.core.music.group.BarlineGroup
import com.xenoage.zong.core.music.group.BracketGroup
import com.xenoage.zong.core.music.group.StavesRange
import com.xenoage.zong.core.music.key.TraditionalKey
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.music.slur.Slur
import com.xenoage.zong.core.music.slur.SlurWaypoint
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.time.TimeType
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.text.UnformattedText
import com.xenoage.zong.io.selection.Cursor

/**
 * Creates the demo score "Revolutionary Etude op. 10/12 Chopin)".
 */
fun ScoreRevolutionary(): Score {

	val score = Score()

	val instr = Instrument.defaultInstrument
	val space = score.format.interlineSpace
	val staffLayout = StaffLayout(space * 9)
	score.format.staffLayoutOther = staffLayout

	val accent = ArticulationType.Accent
	val staccato = ArticulationType.Staccato

	var attachC: Chord
	var firstSlurC: Chord
	var lastSlurC: Chord
	var firstSlurB: BezierPoint
	var lastSlurB: BezierPoint

	val pianoPart = Part("Piano", null, 2, listOf(instr))
	PartAdd(score, pianoPart, 0, null).execute()

	//set barlines and brackets
	BarlineGroupAdd(score.stavesList, BarlineGroup(StavesRange(0, 1), BarlineGroup.Style.Common)).execute()
	BracketGroupAdd(score.stavesList, BracketGroup(StavesRange(0, 1), BracketGroup.Style.Bracket)).execute()

	//use cursor for more convenient input
	var cursor = Cursor(score)

	//C minor, C (4/4) time
	cursor.write(TraditionalKey(-3, TraditionalKey.Mode.Minor))
	cursor.write(TimeSignature(TimeType.timeCommon))

	//first staff: g-clef and some notes
	cursor.write(Clef(clefTreble))

	//measure 1
	val tempo = Tempo(_1_4, 160)
	tempo.text = UnformattedText("Allegro con fuoco.")
	tempo.positioning = Position(null, 22f, -5f, -5f)
	cursor.write(tempo)
	attachC = Chord(_1_2, pi(B, 4), pi(D, 5), pi(F, 5), pi(G, 5), pi(B, 5)).addArticulations(accent)
	cursor.write(attachC)
	attachC.directions = attachC.directions.addOrNew(Dynamic(DynamicValue.f))
	cursor.write(Rest(_1_2))

	//measure 2
	cursor.write(Rest(_1_2))
	cursor.write(Rest(_1_4))
	var cresc = Wedge(WedgeType.Crescendo)
	cresc.positioning = Position(null, null, -1f, -2f)
	cursor.write(cresc as MeasureElement)
	cursor.openBeam()
	firstSlurC = Chord(fr(3, 16), pi(A, -1, 4), pi(E, -1, 5), pi(F, 5), pi(A, -1, 5)).addArticulations(accent)
	cursor.write(firstSlurC)
	lastSlurC = Chord(_1_16, pi(G, 4), pi(G, 5))
	cursor.write(lastSlurC)
	cursor.closeBeam()
	cursor.write(cresc.wedgeEnd as MeasureElement)
	firstSlurB = BezierPoint(SP(space * 0.8f, space * 7.6f), SP(space, space * 0.8f))
	lastSlurB = BezierPoint(SP(0f, space * 6f), SP(-space, space))
	SlurAdd(Slur(Slur.SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

	//measure 3
	attachC = Chord(_1_2, pi(D, 5), pi(F, 5), pi(G, 5), pi(D, 6))
	cursor.write(attachC)
	DirectionAdd(Dynamic(DynamicValue.f), attachC).execute()
	cursor.write(Rest(_1_2))

	//measure 4
	cursor.write(Rest(_1_2))
	cursor.write(Rest(_1_4))
	cresc = Wedge(WedgeType.Crescendo)
	cresc.positioning = Position(null, null, -1f, -2f)
	cursor.write(cresc as MeasureElement)
	cursor.openBeam()
	firstSlurC = Chord(_3_16, pi(A, -1, 4), pi(E, -1, 5), pi(F, 0, 5), pi(A, -1, 5)).addArticulations(accent)
	cursor.write(firstSlurC)
	lastSlurC = Chord(_1_16, pi(G, 0, 4), pi(G, 0, 5))
	cursor.write(lastSlurC)
	cursor.closeBeam()
	cursor.write(cresc.wedgeEnd as MeasureElement)
	firstSlurB = BezierPoint(SP(space * 0.8f, space * 7.6f), SP(space, space * 0.8f))
	lastSlurB = BezierPoint(SP(0f, space * 6f), SP(-space, space))
	SlurAdd(Slur(Slur.SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

	//measure 5
	attachC = Chord(_1_4, pi(F, 5), pi(G, 5), pi(D, 6), pi(F, 6)).addArticulations(staccato)
	cursor.write(attachC)
	DirectionAdd(Dynamic(DynamicValue.f), attachC).execute()
	cursor.write(Rest(_1_4))
	cursor.write(Rest(_1_2))

	//second staff: f-clef some notes
	cursor.mp = MP(1, 0, 0, _0, 0)
	cursor.write(Clef(clefBass))

	//measure 1
	cursor.openBeam()
	cursor.write(Rest(_1_8))
	firstSlurC = Chord(_1_16, pi(A, -1, 4))
	cursor.write(firstSlurC)
	cursor.write(Chord(_1_16, pi(G, 0, 4)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(F, 0, 4)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(D, 0, 4)))
	cursor.write(Chord(_1_16, pi(E, -1, 4)))
	cursor.write(Chord(_1_16, pi(D, 0, 4)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(B, 0, 3)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(G, 0, 3)))
	cursor.write(Chord(_1_16, pi(A, -1, 3)))
	cursor.write(Chord(_1_16, pi(G, 0, 3)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(F, 0, 3)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(D, 0, 3)))
	cursor.write(Chord(_1_16, pi(E, -1, 3)))
	cursor.write(Chord(_1_16, pi(D, 0, 3)))
	cursor.closeBeam()

	//measure 2
	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(B, 0, 2)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(G, 0, 2)))
	cursor.write(Chord(_1_16, pi(A, -1, 2)))
	cursor.write(Chord(_1_16, pi(G, 0, 2)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(F, 0, 2)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(D, 0, 2)))
	cursor.write(Chord(_1_16, pi(E, -1, 2)))
	cursor.write(Chord(_1_16, pi(D, 0, 2)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(C, 0, 2)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(G, 0, 1)))
	cursor.write(Chord(_1_16, pi(C, 0, 2)))
	cursor.write(Chord(_1_16, pi(G, 0, 1)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(C, 0, 2)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(G, 0, 1)))
	cursor.write(Chord(_1_16, pi(C, 0, 2)))
	lastSlurC = Chord(_1_16, pi(G, 0, 1))
	cursor.write(lastSlurC)
	cursor.closeBeam()
	firstSlurB = BezierPoint(SP(0f, space * 1.5f), SP(15f, 5f))
	lastSlurB = BezierPoint(SP(0f, space * 7.5f), SP(-space * 5, space * 2))
	SlurAdd(Slur(Slur.SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

	//measure 3
	cursor.write(Chord(_1_8, pi(B, 0, 1)).addArticulations(staccato))
	cursor.openBeam()
	firstSlurC = Chord(_1_16, pi(A, -1, 4))
	cursor.write(firstSlurC)
	cursor.write(Chord(_1_16, pi(G, 0, 4)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(F, 0, 4)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(D, 0, 4)))
	cursor.write(Chord(_1_16, pi(E, -1, 4)))
	cursor.write(Chord(_1_16, pi(D, 0, 4)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(B, 0, 3)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(G, 0, 3)))
	cursor.write(Chord(_1_16, pi(A, -1, 3)))
	cursor.write(Chord(_1_16, pi(G, 0, 3)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(F, 0, 3)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(D, 0, 3)))
	cursor.write(Chord(_1_16, pi(E, -1, 3)))
	lastSlurC = Chord(_1_16, pi(D, 0, 3))
	cursor.write(lastSlurC)
	cursor.closeBeam()
	firstSlurB = BezierPoint(SP(0f, space * 1.5f), SP(15f, 3f))
	lastSlurB = BezierPoint(SP(0f, space * 5f), SP(-space * 5.5f, space * 2))
	SlurAdd(Slur(Slur.SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

	//measure 4
	cursor.openBeam()
	firstSlurC = Chord(_1_16, pi(B, 0, 2)).addArticulations(accent)
	cursor.write(firstSlurC)
	cursor.write(Chord(_1_16, pi(G, 0, 2)))
	cursor.write(Chord(_1_16, pi(A, -1, 2)))
	cursor.write(Chord(_1_16, pi(G, 0, 2)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(F, 0, 2)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(D, 0, 2)))
	cursor.write(Chord(_1_16, pi(E, -1, 2)))
	cursor.write(Chord(_1_16, pi(D, 0, 2)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(C, 0, 2)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(G, 0, 1)))
	cursor.write(Chord(_1_16, pi(C, 0, 2)))
	cursor.write(Chord(_1_16, pi(G, 0, 1)))
	cursor.closeBeam()

	cursor.openBeam()
	cursor.write(Chord(_1_16, pi(C, 0, 2)).addArticulations(accent))
	cursor.write(Chord(_1_16, pi(G, 0, 1)))
	cursor.write(Chord(_1_16, pi(C, 0, 2)))
	lastSlurC = Chord(_1_16, pi(G, 0, 1))
	cursor.write(lastSlurC)
	cursor.closeBeam()
	firstSlurB = BezierPoint(SP(-space, space * 8.5f), SP(15f, 4f))
	lastSlurB = BezierPoint(SP(0f, space * 7.5f), SP(-space * 5, space * 2))
	SlurAdd(Slur(Slur.SlurType.Slur, clwp(firstSlurC, firstSlurB), clwp(lastSlurC, lastSlurB), null)).execute()

	//measure 5
	cursor.write(Chord(_1_4, pi(B, 0, 1)).addArticulations(staccato))
	cursor.write(Rest(_1_4))
	cursor.write(Rest(_1_2))

	//end line
	val barlineEnd = Barline(BarlineStyle.LightHeavy)
	ColumnElementWrite(barlineEnd, score.getColumnHeader(4), null, MeasureSide.Right).execute()

	return score
}

private fun clwp(c: Chord, bezierPoint: BezierPoint): SlurWaypoint =
	SlurWaypoint(c, null, bezierPoint)