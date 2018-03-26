package com.xenoage.zong.io

import com.xenoage.utils.document.io.FileFormat
import com.xenoage.zong.core.Score


/**
 * A list of common file formats.
 */
enum class ScoreFileFormats private constructor(val format: FileFormat<Score>) {

	Midi(FileFormat<Score>("Midi", "Midi", ".mid")),
	MP3(FileFormat<T>("MP3", "MPEG Audio Layer III", ".mp3")),
	MusicXML(FileFormat<T>("MusicXML", "MusicXML", ".mxl", ".xml")),
	OGG(FileFormat<T>("OGG", "Ogg Vorbis", ".ogg")),
	PDF(FileFormat<T>("PDF", "PDF", ".pdf")),
	PNG(FileFormat<T>("PNG", "PNG", ".png")),
	WAV(FileFormat<T>("WAV", "Waveform Audio", ".wav"))

}
