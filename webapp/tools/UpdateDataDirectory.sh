cd ../war
cp -r ../../shared/data .
java -jar ../tools/FilesystemIndexCreator.jar -dir data
