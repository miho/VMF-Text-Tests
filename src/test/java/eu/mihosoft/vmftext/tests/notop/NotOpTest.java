package eu.mihosoft.vmftext.tests.notop;

import eu.mihosoft.vmftext.tests.notoptest.NotOpModel;
import eu.mihosoft.vmftext.tests.notoptest.parser.NotOpModelParser;
import eu.mihosoft.vmftext.tests.notoptest.unparser.NotOpModelUnparser;
import org.junit.Assert;
import org.junit.Test;

public class NotOpTest {
    @Test
    public void notOpTest() {

        NotOpModelParser parser = new NotOpModelParser();
        NotOpModelUnparser unparser = new NotOpModelUnparser();

        // rule 1 alt 0
        NotOpModel model1 = parser.parse("def altOne");
        String s1 = unparser.unparse(model1).trim();
        System.out.println("CASE1: " + s1);

        Assert.assertEquals("def altOne r1", s1);

        // rule 1 alt 1
        NotOpModel model2 = parser.parse("abc altTwo");
        String s2 = unparser.unparse(model2).trim();
        System.out.println("CASE2: " + s2);
        Assert.assertEquals("abc altTwo r1", s2);

        // rule 2 alt 0
        NotOpModel model3 = parser.parse("abc altOne");
        String s3 = unparser.unparse(model3).trim();
        System.out.println("CASE3: " + s3);
        Assert.assertEquals("abc altOne r2",s3);

        // rule 2 alt 1
        NotOpModel model4 = parser.parse("def altTwo");
        String s4 = unparser.unparse(model4).trim();
        System.out.println("CASE4: " + s4);
        Assert.assertEquals("def altTwo r2", s4);

        // rule 3 alt 0
        NotOpModel model5 = parser.parse("this altOneR3");
        String s5 = unparser.unparse(model5).trim();
        System.out.println("CASE5: " + s5);
        Assert.assertEquals("this altOneR3", s5);
    }
}
