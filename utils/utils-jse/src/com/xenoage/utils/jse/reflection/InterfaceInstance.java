package com.xenoage.utils.jse.reflection;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicLong;

public class InterfaceInstance {
	private Class<?> base;
	private Loader loader = new Loader();
	private static final AtomicLong uniq = new AtomicLong();

	public InterfaceInstance(Class<?> base) throws IllegalArgumentException {
		if (!base.isInterface()) {
			throw new IllegalArgumentException("Class " + base.getName() + " is not an interface");
		}
		for (Method m : base.getMethods()) {
			if (!Modifier.isStatic(m.getModifiers()) && !m.isDefault()) {
				throw new IllegalArgumentException("Interface " + base.getName() + " contains non-static method " + m.getName() + " without default");
			}
		}
		this.base = base;
	}

	private class Loader extends ClassLoader {

		public Class findClass(String name) {
			final byte[] definition = loadClassData(name);
			return defineClass(name, definition, 0, definition.length);
		}

		private byte[] loadClassData(String name) {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			try {
				ClassWriter writer = new ClassWriter(new ClassInfo(
						name,
						"java.lang.Object",
						new String[] { InterfaceInstance.this.base.getName() }
				));
				writer.writeTo(bytes);
				final byte[] definition = bytes.toByteArray();

				// DEBUG
				// java.nio.file.Files.write(java.nio.file.Paths.get(name + ".class"), definition);

				return definition;
			} catch (final Exception e) {
				throw new RuntimeException("Cannot load " + name, e);
			}
		}
	}

	public Class<?> defineClass() throws ClassNotFoundException {
		return loader.loadClass(base.getName() + "$Proxy$" + uniq.getAndIncrement());
	}
}
