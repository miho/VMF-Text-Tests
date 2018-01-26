package eu.mihosoft.vmftext.tests.zerotomany;

import eu.mihosoft.vmftext.tests.zerotomany.parser.ZeroToManyModelParser;
import eu.mihosoft.vmftext.tests.zerotomany.unparser.BaseFormatter;
import eu.mihosoft.vmftext.tests.zerotomany.unparser.Formatter;
import eu.mihosoft.vmftext.tests.zerotomany.unparser.ZeroToManyModelUnparser;

public class Test {
    @org.junit.Test(timeout = 1000/*ms*/)
    public void zeroToManyTest() {
        ZeroToManyModelParser parser = new ZeroToManyModelParser();
        ZeroToManyModel model = parser.parse("(12.34value:3.2)");
        ZeroToManyModelUnparser unparser = new ZeroToManyModelUnparser();
        unparser.setFormatter(new BaseFormatter());
        String out = unparser.unparse(model);
        System.out.println("OUT: " + out);
    }
}
