package eu.mihosoft.vmftext.tests.minijava;

public class TypeConverter {
    public static Boolean stringToType(String s) {
        return "[]".equals(s);
    }

    public static String typeToString(Boolean b) {
        if(b!=null && b) {
            return "[]";
        } else {
            return "";
        }
    }
}
