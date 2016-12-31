package com.xenoage.zong;

import java.io.File;
import java.util.ArrayList;

import static com.xenoage.utils.collections.ArrayUtils.containsRef;
import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * This class contains a method
 * that returns all subclasses of
 * a given class.
 *
 * @deprecated use org.reflections:reflections library instead
 * 
 * @author Andreas Wenger
 */
@Deprecated
public class SubclassCollector {
	
	/**
	 * Gets all classes which implement the given interface.
	 * TIDY: share code with getSubclasses
	 */
	public static ArrayList<Class<?>> getClassesImplementing(Class<?> interfaceClass) {
		//get class files
		ArrayList<String> classFiles = new ArrayList<String>();
		getClassFiles(new File(".."), classFiles, "");
		/* TEST
		for (String classFile : classFiles)
		{
		  System.out.println(classFile);
		} //*/
		//get classes with given superclass
		ArrayList<Class<?>> ret = alist();
		for (String classFile : classFiles) {
			String classID = classFile;
			classID = classID.substring(classID.indexOf("/", 1) + 1); //project level
			classID = classID.substring(classID.indexOf("/", 1) + 1); //bin dir level
			classID = classID.substring(0, classID.length() - ".class".length());
			classID = classID.replace('/', '.');
			try {
				Class<?> cls = Class.forName(classID);
				//TEST
				//System.out.println("Found:     " + cls);
				if (containsRef(cls.getInterfaces(), interfaceClass)) {
					ret.add(cls);
				}
			} catch (ClassNotFoundException ex) {
				//TEST
				//System.out.println("Not found: " + classID);
			} catch (ExceptionInInitializerError err) {
				//TEST
				//err.printStackTrace();
			} catch (NoClassDefFoundError err) {
			}
		}
		return ret;
	}

	/**
	 * Gets all subclasses of the given class.
	 */
	public static ArrayList<Class<?>> getSubclasses(Class<?> superClass) {
		//get class files
		ArrayList<String> classFiles = new ArrayList<String>();
		getClassFiles(new File(".."), classFiles, "");
		/* TEST
		for (String classFile : classFiles)
		{
		  System.out.println(classFile);
		} //*/
		//get classes with given superclass
		ArrayList<Class<?>> ret = alist();
		for (String classFile : classFiles) {
			String classID = classFile;
			classID = classID.substring(classID.indexOf("/", 1) + 1); //project level
			classID = classID.substring(classID.indexOf("/", 1) + 1); //bin dir level
			classID = classID.substring(0, classID.length() - ".class".length());
			classID = classID.replace('/', '.');
			try {
				Class<?> cls = Class.forName(classID);
				//TEST
				//System.out.println("Found:     " + cls);
				if (superClass.equals(cls.getSuperclass())) {
					ret.add(cls);
				}
			} catch (ClassNotFoundException ex) {
				//TEST
				//System.out.println("Not found: " + classID);
			} catch (ExceptionInInitializerError err) {
				//TEST
				//err.printStackTrace();
			} catch (NoClassDefFoundError err) {
			}
		}
		return ret;
	}

	/**
	 * Gets all subinterfaces of the given interface.
	 */
	public static ArrayList<Class<?>> getSubinterfaces(File dir, Class<?> superInterface) {
		//get class files
		ArrayList<String> classFiles = new ArrayList<String>();
		getClassFiles(dir, classFiles, "");
		//get interfaces with given superinterfaces
		ArrayList<Class<?>> ret = alist();
		for (String classFile : classFiles) {
			String classID = classFile.substring(0, classFile.length() - ".class".length());
			if (classID.startsWith("/"))
				classID = classID.substring(1);
			classID = classID.replace('/', '.');
			try {
				Class<?> cls = Class.forName(classID);
				if (cls.isInterface() &&
					containsRef(cls.getInterfaces(), superInterface)) {
					ret.add(cls);
				}
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (ExceptionInInitializerError err) {
			}
		}
		return ret;
	}

	/**
	 * Gets all classfiles in the given directory,
	 * adding them to the given list.
	 */
	private static void getClassFiles(File dir, ArrayList<String> classFiles, String currentDir) {
		//list files and directories
		File[] files = dir.listFiles();
		//add all class files
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".class")) {
				classFiles.add(currentDir + "/" + file.getName());
			}
		}
		//walk through all subdirectories
		//(ignore hidden ones, beginning with ".")
		for (File file : files) {
			if (file.isDirectory() && !file.getName().startsWith(".")) {
				getClassFiles(file, classFiles, currentDir + "/" + file.getName());
			}
		}
	}

}
