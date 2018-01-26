package eu.mihosoft.vmftext.tests.onetomany;

import eu.mihosoft.vmftext.tests.onetomany.parser.OneToManyModelParser;
import eu.mihosoft.vmftext.tests.onetomany.unparser.BaseFormatter;
import eu.mihosoft.vmftext.tests.onetomany.unparser.OneToManyModelUnparser;


public class Test {
    @org.junit.Test(timeout = 1000/*ms*/)
    public void oneToManyTest() {
        OneToManyModelParser parser = new OneToManyModelParser();
        OneToManyModel model = parser.parse("(12.34value:3.2)[]");
        OneToManyModelUnparser unparser = new OneToManyModelUnparser();
        unparser.setFormatter(new BaseFormatter());
        String out = unparser.unparse(model);
        System.out.println("OUT: " + out);
    }
}
