package eu.mihosoft.vmftext.tests.matchcorrectalt;

import eu.mihosoft.vmftext.tests.matchcorrectalt.parser.MatchCorrectAltTestModelParser;
import eu.mihosoft.vmftext.tests.matchcorrectalt.unparser.MatchCorrectAltTestModelUnparser;

import eu.mihosoft.vmftext.tests.matchcorrectaltterminal.MatchCorrectAltTestTerminalOnlyModel;
import eu.mihosoft.vmftext.tests.matchcorrectaltterminal.parser.MatchCorrectAltTestTerminalOnlyModelParser;
import eu.mihosoft.vmftext.tests.matchcorrectaltterminal.unparser.MatchCorrectAltTestTerminalOnlyModelUnparser;
import org.junit.Assert;

public class Test {

    @org.junit.Test
    public void matchCorrectAltTest() {
        MatchCorrectAltTestModelParser parser = new MatchCorrectAltTestModelParser();
        MatchCorrectAltTestModel model1 = parser.parse("name: hello");
        MatchCorrectAltTestModel model2 = parser.parse("number: 12");
        MatchCorrectAltTestModel model3 = parser.parse("terminal: 123");

        MatchCorrectAltTestModelUnparser unparser = new MatchCorrectAltTestModelUnparser();
        String s1 = unparser.unparse(model1);
        System.out.println(s1);

        String s2 = unparser.unparse(model2);
        System.out.println(s2);

        String s3 = unparser.unparse(model3);
        System.out.println(s3);

        Assert.assertTrue("s1 needs to contain the string 'name'",s1.startsWith("name"));
        Assert.assertTrue("s2 needs to contain the string 'number'",s2.startsWith("number"));
        Assert.assertTrue("s3 needs to contain the string 'terminal'",s3.startsWith("terminal"));
    }


    @org.junit.Test
    public void matchCorrectAltTestTerminalOnly() {
        MatchCorrectAltTestTerminalOnlyModelParser parser = new MatchCorrectAltTestTerminalOnlyModelParser();
        MatchCorrectAltTestTerminalOnlyModel model1 = parser.parse("terminal2: 123");
        MatchCorrectAltTestTerminalOnlyModel model2 = parser.parse("terminal1: 456");

        MatchCorrectAltTestTerminalOnlyModelUnparser unparser = new MatchCorrectAltTestTerminalOnlyModelUnparser();
        String s1 = unparser.unparse(model1);
        System.out.println(s1);

        String s2 = unparser.unparse(model2);
        System.out.println(s2);

        Assert.assertTrue("s1 needs to contain the string 'terminal2'",s1.startsWith("terminal2"));
        Assert.assertTrue("s2 needs to contain the string 'terminal1'",s2.startsWith("terminal1"));
    }

}
