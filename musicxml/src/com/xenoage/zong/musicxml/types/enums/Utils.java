package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import static com.xenoage.utils.EnumUtils.getEnumValueNamed;

import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlDataException;


public class Utils {
	
	/**
	 * Reads the given enum value from the given value, based on the given enum values.
	 * If the value is null or can not be parsed, an {@link XmlDataException} is thrown,
	 * which contains the given name and value.
	 */
	public static <T> T read(String name, String value, T[] values) {
		T ret = getEnumValue(value, values);
		if (ret == null)
			throw new XmlDataException(name + " = " + value);
		return ret;
	}
	
	/**
	 * Reads the given enum value from the given value, based on the given enum values.
	 * If the value is null, the given replacement is returned.
	 * If the value is not null but can not be parsed, an {@link XmlDataException} is thrown,
	 * which contains the given name and value.
	 */
	public static <T> T readOr(String name, String value, T[] values, T replacement) {
		if (value == null)
			return replacement;
		T ret = getEnumValue(value, values);
		if (ret == null)
			throw new XmlDataException(name + " = " + value);
		return ret;
	}
	
	/**
	 * Reads the given enum value from the given value, based on the given {@link EnumWithXmlNames} enum values.
	 * If the value is null or can not be parsed, an {@link XmlDataException} is thrown,
	 * which contains the given name and value.
	 */
	public static <T extends EnumWithXmlNames> T read(
		String name, String value, T[] values) {
		T ret = getEnumValueNamed(value, values);
		if (ret == null)
			throw new XmlDataException(name + " = " + value);
		return ret;
	}
	
	/**
	 * Reads the given enum value from the given value, based on the given {@link EnumWithXmlNames} enum values.
	 * If the value is null, the given replacement is returned.
	 * If the value is not null but can not be parsed, an {@link XmlDataException} is thrown,
	 * which contains the given name and value.
	 */
	public static <T extends EnumWithXmlNames> T readOr(
		String name, String value, T[] values, T replacement) {
		if (value == null)
			return replacement;
		T ret = getEnumValueNamed(value, values);
		if (ret == null)
			throw new XmlDataException(name + " = " + value);
		return ret;
	}

}
