package io.github.romvoid95;

/**
 * Simple version handling.
 *
 * @author ROMVoid95
 */
public class Version {

	public int major;
	public int minor;
	public int build;
	public String classifier;

	public static final Version version(String majorNum, String minorNum, String buildNum) {
		if (majorNum.startsWith("@")) {
			return new Version(Num.dev(), Num.dev(), Num.dev()).classifier("-DEV");
		} else {
			return new Version(Num.of(majorNum), Num.of(majorNum), Num.of(majorNum)).classifier("-SNAPSHOT");
		}
	}

	/**
	 * Creates a version
	 *
	 * @param majorNum - major version
	 * @param minorNum - minor version
	 * @param buildNum - build version
	 */
	public <T extends Number> Version(Num<T> majorNum, Num<T> minorNum, Num<T> buildNum) {
		major = majorNum.getInt();
		minor = minorNum.getInt();
		build = buildNum.getInt();
	}

	/**
	 * Creates a version
	 *
	 * @param majorNum - major version
	 * @param minorNum - minor version
	 * @param buildNum - build version
	 */
	public Version(int majorNum, int minorNum, int buildNum) {
		major = majorNum;
		minor = minorNum;
		build = buildNum;
	}

	public Version classifier(String classifier) {
		this.classifier = classifier;
		return this;
	}

	/**
	 * Gets a version object from a string.
	 *
	 * @param s - string object
	 *
	 * @return version if applicable, otherwise null
	 */
	public static Version get(String s) {
		String[] split = s.replace('.', ':').split(":");
		if (split.length != 3) {
			return null;
		}
		for (String i : split) {
			for (Character c : i.toCharArray()) {
				if (!Character.isDigit(c)) {
					return null;
				}
			}
		}
		int[] digits = new int[3];
		for (int i = 0; i < 3; i++) {
			digits[i] = Integer.parseInt(split[i]);
		}
		return new Version(digits[0], digits[1], digits[2]);
	}

	/**
	 * Resets the version number to "0.0.0."
	 */
	public void reset() {
		major = 0;
		minor = 0;
		build = 0;
	}

	/**
	 * @param version Version to check against
	 *
	 * @return 1: greater than, 0: equal to, -1: less than
	 */
	public byte comparedState(Version version) {
		if (version.major > major) {
			return -1;
		} else if (version.major == major) {
			if (version.minor > minor) {
				return -1;
			} else if (version.minor == minor) {
				return (byte) Integer.compare(build, version.build);
			}
			return 1;
		}
		return 1;
	}

	@Override
	public String toString() {
		if ((major == 0) && (minor == 0) && (build == 0)) {
			return "";
		}
		return major + "." + minor + "." + build + classifier;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = (31 * result) + build;
		result = (31 * result) + major;
		result = (31 * result) + minor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Version other = (Version) obj;
		return (build == other.build) && (major == other.major) && (minor == other.minor) && (classifier == other.classifier);
	}

	public static class Num<N extends Number> {
		private final N num;

		public static <N extends Number> Num<N> of(String number) {
			return new Num<>(number);
		}

		public static <N extends Number> Num<N> dev() {
			return new Num<>("0");
		}

		Num(String number) {
			this.num = (N) Integer.getInteger(number);
		}

		public int getInt() {
			return num.intValue();
		}
	}
}
