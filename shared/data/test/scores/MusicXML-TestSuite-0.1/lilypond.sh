# Convert all MusicXML files to lilypond, then to PNG,
# crop their bottom part (Lilypond footer) and trim the images
for file in *.xml
do
	musicxml2ly -o "$file.ly" "$file"
	lilypond -fpng -o "$file" "$file.ly"
	mogrify -trim -crop +0-50 +repage -trim "$file.png"
done
