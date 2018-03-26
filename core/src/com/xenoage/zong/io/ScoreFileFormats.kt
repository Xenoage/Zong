package com.xenoage.zong.io

import com.xenoage.utils.document.io.FileFormat
import com.xenoage.zong.core.Score


/**
 * A list of common file formats.
 */
enum class ScoreFileFormats(
		val format: FileFormat<Score>
) {

	Midi(FileFormat<Score>("Midi", "Midi", ".mid")),
	MP3(FileFormat<Score>("MP3", "MPEG Audio Layer III", ".mp3")),
	MusicXML(FileFormat<Score>("MusicXML", "MusicXML", ".mxl", listOf(".xml"))),
	OGG(FileFormat<Score>("OGG", "Ogg Vorbis", ".ogg")),
	PDF(FileFormat<Score>("PDF", "PDF", ".pdf")),
	PNG(FileFormat<Score>("PNG", "PNG", ".png")),
	WAV(FileFormat<Score>("WAV", "Waveform Audio", ".wav"))

}
