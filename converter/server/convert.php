<?php	

//the Zong! Converter Server must be started, before this script works

	if (isset($_FILES['musicFile']))
	{
		include_once("settings.php");
		
		$temppath = $GLOBALS['SETTINGS']['temppath'];
		$jobspath = $GLOBALS['SETTINGS']['jobspath'];
		$errorspath = $GLOBALS['SETTINGS']['errorspath'];
		
		//get uploaded file
		$tmpname = $_FILES['musicFile']['tmp_name'];
		$name = basename($tmpname);
		move_uploaded_file($tmpname, $temppath.$name);
		$newname = $name . $GLOBALS['SETTINGS']['extension'];
		
		//execute converter - OBSOLETE
		/*
		$command = str_replace('%input%', $temppath.$name, $GLOBALS['SETTINGS']['command']);
		$command = str_replace('%output%', $temppath.$newname, $command);
		exec($command);
		*/
		//create a .job file
		$jobname = time() . "_" . rand() . ".job";
		$joberrorfile = $errorspath.$jobname.".error";
		$fh = fopen($jobspath.$jobname, 'w') or die("Can't create job file");
		fwrite($fh, "--convert\n");
		fwrite($fh, $temppath.$name . "\n");
		fwrite($fh, $temppath.$newname . "\n");
		fwrite($fh, "PDF");
		fclose($fh);
		
		$finished = false;
		for ($i = 0; $i < 30; $i++)
		{
			//wait a moment
			sleep(1);
			//check for success of failure
			if (file_exists($temppath.$newname))
			{
				// Create file type
				header("Content-Type: application/pdf");
				// create new name for download
				$extension=".".array_pop(explode(".",$_FILES['musicFile']['name']));
				$save_as_name = basename($_FILES['musicFile']['name'],$extension).$GLOBALS['SETTINGS']['extension'];
				header("Content-Disposition: attachment; filename=\"".$save_as_name."\"");
				// send file
				readfile($temppath.$newname);
				$finished = true;
				break;
			}
			else if (file_exists($joberrorfile))
			{
				include("error.php");
				$finished = true;
				break;
			}
		}
		if (!$finished)
		{
			include("timeout.php");
		}
		
		// delete temporary files
		@unlink($temppath.$name);
		@unlink($temppath.$newname);
		@unlink($joberrorfile);
	}
?>
