package io.github.romvoid95;

public final class JDAToolsInfo {
	public static final String MAJOR = "@VERSION_MAJOR@";
	public static final String MINOR = "@VERSION_MINOR@";
	public static final String REVISION = "@VERSION_REVISION@";
	public static final Version VERSION = Version.version(MAJOR, MINOR, REVISION);
	public static final String GITHUB = "https://github.com/ROMVoid95/JDATools";
	public static final String AUTHOR = "JDATools";
}
