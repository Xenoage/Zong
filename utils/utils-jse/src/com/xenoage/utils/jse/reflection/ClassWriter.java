package com.xenoage.utils.jse.reflection;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassWriter {
	ClassInfo clazz;

	private static final int CLASSFILE_MAJOR_VERSION = 49;
	private static final int CLASSFILE_MINOR_VERSION = 0;

	private static class ConstantPool {
		private static final int CONSTANT_UTF8              = 1;
		private static final int CONSTANT_UNICODE           = 2;
		private static final int CONSTANT_INTEGER           = 3;
		private static final int CONSTANT_FLOAT             = 4;
		private static final int CONSTANT_LONG              = 5;
		private static final int CONSTANT_DOUBLE            = 6;
		private static final int CONSTANT_CLASS             = 7;
		private static final int CONSTANT_STRING            = 8;
		private static final int CONSTANT_FIELD             = 9;
		private static final int CONSTANT_METHOD            = 10;
		private static final int CONSTANT_INTERFACEMETHOD   = 11;
		private static final int CONSTANT_NAMEANDTYPE       = 12;

		private List<Entry> pool = new ArrayList<>(32);
		private Map<Object, Short> map = new HashMap<>(16);

		private static abstract class Entry {
			public abstract void writeTo(DataOutputStream out) throws IOException;
		}

		private static class ValueEntry extends Entry {
			private Object value;

			public ValueEntry(Object value) {
				this.value = value;
			}

			public void writeTo(DataOutputStream out) throws IOException {
				if (value instanceof String) {
					out.writeByte(CONSTANT_UTF8);
					out.writeUTF((String) value);
				} else if (value instanceof Integer) {
					out.writeByte(CONSTANT_INTEGER);
					out.writeInt((Integer) value);
				} else if (value instanceof Float) {
					out.writeByte(CONSTANT_FLOAT);
					out.writeFloat((Float) value);
				} else if (value instanceof Long) {
					out.writeByte(CONSTANT_LONG);
					out.writeLong((Long) value);
				} else if (value instanceof Double) {
					out.writeDouble(CONSTANT_DOUBLE);
					out.writeDouble((Double) value);
				} else {
					throw new InternalError("bogus value entry: " + value);
				}
			}
		}

		private static class IndirectEntry extends Entry {
			private int tag;
			private short index0;
			private short index1;

			public IndirectEntry(int tag, short index) {
				this.tag = tag;
				this.index0 = index;
				this.index1 = 0;
			}

			public IndirectEntry(int tag, short index0, short index1) {
				this.tag = tag;
				this.index0 = index0;
				this.index1 = index1;
			}

			public void writeTo(DataOutputStream out) throws IOException {
				out.writeByte(tag);
				out.writeShort(index0);

				if (
						tag == CONSTANT_FIELD ||
								tag == CONSTANT_METHOD ||
								tag == CONSTANT_INTERFACEMETHOD ||
								tag == CONSTANT_NAMEANDTYPE
						) {
					out.writeShort(index1);
				}
			}

			public int hashCode() {
				return tag + index0 + index1;
			}

			public boolean equals(Object obj) {
				if (obj instanceof IndirectEntry) {
					IndirectEntry other = (IndirectEntry) obj;
					if (tag == other.tag && index0 == other.index0 && index1 == other.index1) {
						return true;
					}
				}
				return false;
			}
		}

		private short addEntry(Entry entry) {
			pool.add(entry);
			if (pool.size() >= 0xFFFF) {
				throw new IllegalArgumentException("Constant pool size limit exceeded");
			}
			return (short)pool.size();
		}

		private short get(Object key, java.util.function.Supplier<Entry> entry) {
			Short index = map.get(key);
			if (index != null) {
				return index;
			} else {
				short i = addEntry(entry.get());
				map.put(key, i);
				return i;
			}
		}

		private short getValue(final Object key) {
			return get(key, () -> new ValueEntry(key));
		}

		private short getIndirect(final IndirectEntry e) {
			return get(e, () -> e);
		}

		public short getUtf8(String s) {
			if (s == null) {
				throw new NullPointerException();
			}
			return getValue(s);
		}

		public short getNameAndType(String name, String descriptor) {
			return getIndirect(new IndirectEntry(
					CONSTANT_NAMEANDTYPE,
					getUtf8(name),
					getUtf8(descriptor)
			));
		}

		public short getClass(String name) {
			return getIndirect(new IndirectEntry(
					CONSTANT_CLASS,
					getUtf8(name.replace('.', '/'))
			));
		}

		public short getMethodRef(String className, String name, String descriptor) {
			return getIndirect(new IndirectEntry(CONSTANT_METHOD,
					getClass(className),
					getNameAndType(name, descriptor)
			));
		}

		public void writeTo(DataOutputStream out) throws IOException {
			out.writeShort(pool.size() + 1);
			for (Entry e : pool) {
				e.writeTo(out);
			}
		}
	}

	public ClassWriter(ClassInfo clazz) {
		this.clazz = clazz;
	}

	public void writeTo(OutputStream target) throws IOException {
		ConstantPool cp = new ConstantPool();
		DataOutputStream out = new DataOutputStream(target);

		// Reserve indexes in constant pool
		short thisClass = cp.getClass(clazz.name);
		short superClass = cp.getClass(clazz.parent);
		short[] interfaces = new short[clazz.interfaces.length];
		for (String intf : clazz.interfaces) {
			interfaces[interfaces.length - 1] = cp.getClass(intf);
		}

		// Default constructor
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream code = new DataOutputStream(bytes);
		cp.getUtf8("<init>");
		cp.getUtf8("()V");
		cp.getUtf8("Code");
		code.writeByte(42);                    // ALOAD_0
		code.writeByte(183);                   // INVOKESPECIAL
		code.writeShort(cp.getMethodRef(clazz.parent, "<init>", "()V"));
		code.writeByte(177);                   // RETURN

		// Header and constant pool
		out.writeInt(0xCAFEBABE);
		out.writeShort(CLASSFILE_MINOR_VERSION);
		out.writeShort(CLASSFILE_MAJOR_VERSION);
		cp.writeTo(out);

		// Declaration
		out.writeShort(0x00000001);
		out.writeShort(thisClass);
		out.writeShort(superClass);
		out.writeShort((short)interfaces.length);
		for (short base : interfaces) {
			out.writeShort(base);
		}

		// Member counts: Fields, methods
		out.writeShort(0);
		out.writeShort(1);

		// Default constructor
		out.writeShort(0x00000001);
		out.writeShort(cp.getUtf8("<init>"));
		out.writeShort(cp.getUtf8("()V"));
		out.writeShort(1);                        // One attribute: "Code"
		out.writeShort(cp.getUtf8("Code"));
		out.writeInt(12 + bytes.size() + 8 * 0);  // Exception table length: 0
		out.writeShort(1);                        // Stack
		out.writeShort(1);                        // Locals
		out.writeInt(bytes.size());
		bytes.writeTo(out);
		out.writeShort(0);                        // Exception table
		out.writeShort(0);

		// ClassFile attributes
		out.writeShort(0);
	}
}