package eu.mihosoft.vmftext.tests.notop;

import eu.mihosoft.vmftext.tests.notoptest.NotOpModel;
import eu.mihosoft.vmftext.tests.notoptest.parser.NotOpModelParser;
import eu.mihosoft.vmftext.tests.notoptest.unparser.NotOpModelUnparser;
import org.junit.Test;

public class NotOpTest {
    @Test
    public void notOpTest() {

        NotOpModelParser parser = new NotOpModelParser();
        NotOpModelUnparser unparser = new NotOpModelUnparser();

        NotOpModel model1 = parser.parse("abc altTwo");
        String s1 = unparser.unparse(model1);
        System.out.println("CASE1: " + s1);

        NotOpModel model2 = parser.parse("def altOne");
        String s2 = unparser.unparse(model1);
        System.out.println("CASE2: " + s2);

        NotOpModel model3 = parser.parse("abc altOne");
        String s3 = unparser.unparse(model1);
        System.out.println("CASE3: " + s3);
    }
}
