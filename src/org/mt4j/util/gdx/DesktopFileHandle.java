package org.mt4j.util.gdx;

import java.io.File;

import org.mt4j.util.gdx.FileHandle;
import org.mt4j.util.gdx.Files.FileType;

/** @author mzechner
 * @author Nathan Sweet */
public final class DesktopFileHandle extends FileHandle {
	public DesktopFileHandle (String fileName, FileType type) {
		super(fileName, type);
	}

	public DesktopFileHandle (File file, FileType type) {
		super(file, type);
	}

	public FileHandle child (String name) {
		if (file.getPath().length() == 0) return new DesktopFileHandle(new File(name), type);
		return new DesktopFileHandle(new File(file, name), type);
	}

	public FileHandle sibling (String name) {
		if (file.getPath().length() == 0) throw new RuntimeException("Cannot get the sibling of the root.");
		return new DesktopFileHandle(new File(file.getParent(), name), type);
	}

	public FileHandle parent () {
		File parent = file.getParentFile();
		if (parent == null) {
			if (type == FileType.Absolute)
				parent = new File("/");
			else
				parent = new File("");
		}
		return new DesktopFileHandle(parent, type);
	}

	public File file () {
		if (type == FileType.External) return new File(DesktopFiles.externalPath, file.getPath());
		return file;
	}
}
