package org.mt4j.util.gdx;

import org.mt4j.AbstractMTApplication;
import org.mt4j.util.gdx.FileHandle;
import org.mt4j.util.gdx.Files;

/** @author mzechner
 * @author Nathan Sweet */
public final class DesktopFiles implements Files {
//	static public final String externalPath = System.getProperty("user.home") + "/";
	static public final String externalPath = System.getProperty("user.home") + AbstractMTApplication.separator;

	@Override
	public FileHandle getFileHandle (String fileName, FileType type) {
		return new DesktopFileHandle(fileName, type);
	}

	@Override
	public FileHandle classpath (String path) {
		return new DesktopFileHandle(path, FileType.Classpath);
	}

	@Override
	public FileHandle internal (String path) {
		return new DesktopFileHandle(path, FileType.Internal);
	}

	@Override
	public FileHandle external (String path) {
		return new DesktopFileHandle(path, FileType.External);
	}

	@Override
	public FileHandle absolute (String path) {
		return new DesktopFileHandle(path, FileType.Absolute);
	}

	@Override
	public FileHandle local (String path) {
		return new DesktopFileHandle(path, FileType.Local);
	}

	@Override
	public String getExternalStoragePath () {
		return externalPath;
	}

	@Override
	public boolean isExternalStorageAvailable () {
		return true;
	}

	@Override
	public String getLocalStoragePath () {
		return "";
	}

	@Override
	public boolean isLocalStorageAvailable () {
		return true;
	}
}
