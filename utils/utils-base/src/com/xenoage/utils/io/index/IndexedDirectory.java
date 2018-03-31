package com.xenoage.utils.io.index;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.utils.io.Directory;
import com.xenoage.utils.io.FilesystemItem;

/**
 * A {@link Directory} in the {@link FilesystemIndex}.
 * 
 * @author Andreas Wenger
 */
public class IndexedDirectory
	implements Directory {
	
	private String name;
	private List<FilesystemItem> children = alist();
	

	public IndexedDirectory(String name) {
		this.name = name;
	}

	public IndexedDirectory(String name, List<FilesystemItem> children) {
		this.name = name;
		this.children = children;
	}
	
	@Override public String getName() {
		return name;
	}

	@Override public List<FilesystemItem> getChildren() {
		return children;
	}
	
	public void addChild(FilesystemItem child) {
		assertNoDuplicate(child.getName());
		children.add(child);
	}
	
	private void assertNoDuplicate(String name) {
		for (FilesystemItem child : children)
			if (child.getName().equals(name))
				throw new IllegalStateException("Duplicate item: " + name);
	}

}
