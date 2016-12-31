#!/bin/bash
# Convert all MusicXML files to lilypond, then to PNG,
# crop their bottom part (Lilypond footer) and trim the images
for file in *.{xml,mxl}
do

	# convert from MusicXML to Lilypond
	musicxml2ly -o "$file.ly" "$file"

	# convert from Lilypond to PNG
	lilypond -fpng -o "$file" "$file.ly"

	# when there are multiple pages, rename the first one to the expected name
	mv "$file-page1.png" "$file.png"

	# crop the resulting image and scale it down
	mogrify -trim -crop +0-80 +repage -trim -scale 70% "$file.png"

done
