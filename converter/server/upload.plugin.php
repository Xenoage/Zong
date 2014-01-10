<?php
include_once("upload.lang.php");
include_once("settings.php");
?>
<div id="upload">
	<form action="convert.php" method="post" enctype="multipart/form-data">
		<p>
			<?php echo $GLOBALS['LANG']['upload']['description']; ?><br />
			<input name="musicFile" type="file" size="50" maxlength="<?php echo $GLOBALS['SETTINGS']['max_upload_size']; ?>" />
			<input name="next" value="convert" type="hidden" />
			<input name="submit" type="submit" value="<?php echo $GLOBALS['LANG']['upload']['button_go']; ?>" />
		</p>
	</form>
</div>
