package com.xenoage.zong;

import com.xenoage.utils.lang.VocID;

/**
 * An index of all vocabulary of the standard Zong! components
 * together with a default english translation.
 * 
 * Additional Zong! modules, which may not be able to extend this
 * class, can simply implement the {@link VocID} interface to
 * create additional words.
 * 
 * @author Andreas Wenger
 */
public enum Voc
	implements VocID {

	About("About"),
	AllPictures("All pictures"),
	AttachFile("Attach the score document ({1})"),
	Audio("Audio"),
	AudioSettings("Audio settings"),
	AttachLog("Attach log file"),
	Beta("Beta"),
	Bits("Bits"),
	Blog("Blog"),
	Bottom("Bottom"),
	Cancel("Cancel"),
	CancelAtFirstError("Cancel at first error"),
	Channels("Channels"),
	CleanUpMemory("Clean up memory"),
	Close("Close"),
	CloseDocument("Close document"),
	CommandFailed("Could not perform the action."),
	CouldNotCreateBitmaps("Could not create the bitmaps."),
	CouldNotFindLAME(
		"Zong! supports saving scores in MP3 format, but requires the installation of the LAME encoder, which is not included in Zong! for legal reasons.\nPlease take 5 minutes to read &lt;a href=&quot;{1}&quot;&gt;how to install LAME&lt;/a&gt;."),
	CouldNotLoadImages("Could not load the images."),
	CouldNotLoadSymbolPool("Could not load the music symbols."),
	CouldNotSaveDocument("An error occurred while saving the document."),
	Convert("Convert"),
	Copyright("Copyright"),
	Creators("Creators"),
	Cubic("Cubic"),
	CurrentMemoryUsage("Current memory usage"),
	Default("Default"),
	DemoScores("Demo scores"),
	Description("Description"),
	Details("Details"),
	DeviceName("Device name"),
	DirectoryConversionResult("Conversion complete.\nConverted files: {1}\nErrors: {2}"),
	DirToMidi("Directory to Midi"),
	Distance("Distance"),
	Document("Document"),
	DownloadError("An error occurred while downloading the file."),
	EditFrameSettings("Change frame settings"),
	Error("Error"),
	ErrorMessage(
		"Please click on &quot;{voc:Buttons_ReportError}&quot; to inform the developer team about the error. This allows the developers to remove the error in future versions."),
	ErrorReportOrFeatureRequest("Error report or feature request"),
	ErrorSavingFile("Error while saving the file."),
	ErrorWhilePrinting("Error while printing."),
	ExampleMailAdress("your@mail.com"),
	Exit("Exit"),
	FatalError("Fatal Error"),
	FatalErrorMessage(
		"The program must be closed down. Please click on &quot;{voc:Buttons_ReportError}&quot; to inform the developer team about the error. This allows the developers to remove the error in future versions. Sorry for the inconvenience!"),
	FeedbackDescription("Did you find an error or do you miss a specific feature? Please tell us."),
	FeedbackTitle("Feedback"),
	FileToMidi("File to Midi"),
	ForMoreInfoSeeWebsite("For more information, questions or suggestions visit our homepage."),
	FrameSettings("Frame settings"),
	Height("Height"),
	Help("Help"),
	IncludeSubdirectories("Include subdirectories"),
	Info("Info"),
	InterpolationMode("Interpolation mode"),
	Language("Language"),
	LatencyMs("Latency (msec)"),
	Left("Left"),
	License("License"),
	Linear("Linear"),
	MailForFurtherQuestions("Your e-mail for further questions"),
	MaxMemory("Maximum memory"),
	MaxPolyphony("Max polyphony"),
	MemoryUsage("Memory usage"),
	MidiNotAvailable("The MIDI device is not available. Playback will not work."),
	MinStaffHeightMessage("The staves must be at least {1} mm high."),
	MovementNumber("Movement number"),
	MovementTitle("Movement title"),
	NoDocumentsAvailable("There are no documents available."),
	NoFileLoaded("No file loaded."),
	OK("OK"),
	Open("Open"),
	OpenDocument("Open document"),
	OpenFileInvalidFormat(
		"Could not open the document, since its format is not supported by {app.name}."),
	OpenFileIOError("Could not open the document, since the file could not be read."),
	OpenFileNotFound("Could not open the document, since the file does not exist."),
	PageFormat("Page format"),
	PageMargins("Page margins"),
	PageSettings("Page settings"),
	Parts("Parts"),
	PlayerDescription(
		"This program plays MusicXML files and converts them into different audio formats."),
	Point("Point"),
	Position("Position"),
	Print("Print"),
	ProblemDescription("Description of the problem/idea"),
	ProgramInfo("Program Info"),
	Readme("Readme"),
	ReportError("Report error via internet"),
	Right("Right"),
	Rights("Rights"),
	Rotation("Rotation"),
	SampleRateHz("Sample rate (Hz)"),
	SaveAs("Save as"),
	SaveFileUnknownOrigin("Could not download the document, since its origin is unknown."),
	ScoreInfo("Score Info"),
	SecurityError("The current security settings don't allow performing this action."),
	Select("Select"),
	SelectColor("Select color"),
	SelectDocument("Please select a document"),
	Send("Submit"),
	SendFailed(
		"The feedback form could not be submitted.\nEither the server is offline or you have no internet connection."),
	SendOK("The feedback form was sent.\nThank you for your support."),
	Settings("Settings"),
	ShowInfo("Show information"),
	ShowLog("Show log"),
	Sinc("Sinc"),
	Size("Size"),
	Soundbank("Soundbank"),
	Staves("Staves"),
	Top("Top"),
	UnknownError("Unknown error."),
	UnnamedScore("Unnamed score"),
	UseBugtracker("Experienced users should use our bugtracker instead."),
	Used("Used"),
	UseDefault("Use default"),
	Version("Version"),
	Warning("Warning"),
	Website("Website"),
	Width("Width"),
	WorkNumber("Work number"),
	WorkTitle("Work title");

	private final String defaultValue;


	private Voc(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override public String getDefaultValue() {
		return defaultValue;
	}

	@Override public String getID() {
		return this.toString();
	}

}
