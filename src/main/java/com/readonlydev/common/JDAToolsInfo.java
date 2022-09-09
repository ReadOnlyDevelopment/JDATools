
package com.readonlydev.common;

import com.readonlydev.common.version.Version;

public final class JDAToolsInfo {

    public static final String  MAJOR   = "@MAJOR@";

    public static final String  MINOR   = "@MINOR@";

    public static final String  PATCH   = "@PATCH@";

    public static final Version VERSION = new Version(MAJOR, MINOR, PATCH);

    public static final String  GITHUB  = "https://github.com/ROMVoid95/JDATools";

    public static final String  AUTHOR  = "JDATools";

}
