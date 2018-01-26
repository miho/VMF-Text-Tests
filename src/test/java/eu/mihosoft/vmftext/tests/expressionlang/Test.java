package eu.mihosoft.vmftext.tests.expressionlang;

import eu.mihosoft.vmftext.tests.expressionlang.parser.ExpressionLangModelParser;
import eu.mihosoft.vmftext.tests.expressionlang.unparser.ExpressionLangModelUnparser;
import org.junit.Assert;

public class Test {
    @org.junit.Test
    public void testExpressionLangInterpreter() {
        ExpressionLangModelParser parser = new ExpressionLangModelParser();
        ExpressionLangModel model = parser.parse("2.5+3*(14-27.218)/2.3");

        GrammarInterpreter interpreter = new GrammarInterpreter();
        Double value = interpreter.execute(model);

        Assert.assertEquals(-14.740869565217391, value, 1e-12);
    }

    @org.junit.Test
    public void testExpressionLangParseUnparseTest() {
        ExpressionLangModelParser parser = new ExpressionLangModelParser();
        ExpressionLangModel model = parser.parse("2.5+3*(14-27.218)/2.3");

        ExpressionLangModelUnparser unparser = new ExpressionLangModelUnparser();
        ExpressionLangModel modelup = parser.parse(unparser.unparse(model));

        Assert.assertEquals(model,modelup);

        GrammarInterpreter interpreter = new GrammarInterpreter();
        Double valueExpected = interpreter.execute(model);

        Double valueUP = interpreter.execute(model);

        Assert.assertEquals(valueExpected, valueUP, 1e-12);
        Assert.assertEquals(model,modelup);
    }
}
