package com.xenoage.zong

import com.xenoage.zong.Zong.projectFamilyName
import com.xenoage.zong.Zong.projectVersionLong

/**
 * Product and version information.
 */
object Zong {

	//the version of this program as an integer number
	const val projectIteration = 73

	//general information on this program
	const val projectFamilyName = "Zong!"
	const val projectVersion = "0.1-a"
	const val projectVersionLong = "$projectVersion.$projectIteration"
	const val projectIterationName = "Stonehenge"

	//package path
	const val projectPackage = "com.xenoage.zong"

	//name in filesystem
	const val filename = "zong"

	//copyright information
	const val copyright = "Copyright © 2006-2018 by Xenoage Software"

	//other information
	const val website = "www.zong-music.com"
	const val emailErrorReports = "support@zong-music.com"
	const val bugtracker = "xenoage.atlassian.net"
	const val blog = "http://blog.zong-music.com"

	//last version, that was "complete" regarding its functions, and not a
	//current snapshot or "work in progress"
	const val projectIterationLastWorking = "54"


	/**
	 * Gets the name of the program as a String,
	 * using the given "first" name of the project (like "Viewer" or "Editor").
	 */
	fun getName(firstName: String): String =
			"$projectFamilyName $firstName"

	/**
	 * Gets the name and version of the program as a String,
	 * using the given "first" name of the project (like "Viewer" or "Editor").
	 */
	fun getNameAndVersion(firstName: String): String =
		"$projectFamilyName $firstName $projectVersionLong"

}
