package com.xenoage.utils.lang

//TODO
fun translate(vocId: String): String =
		"TODO! $vocId"

//TODO
fun translate(vocId: String, defaultValue: String): String =
		"TODO! $defaultValue"

//TODO
fun translate(vocId: VocID, defaultValue: String): String =
		translate("$vocId", defaultValue)