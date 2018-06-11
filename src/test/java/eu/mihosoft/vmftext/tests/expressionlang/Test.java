package eu.mihosoft.vmftext.tests.expressionlang;

import eu.mihosoft.vmftext.tests.expressionlang.parser.ExpressionLangModelParser;
import eu.mihosoft.vmftext.tests.expressionlang.unparser.ExpressionLangModelUnparser;
import org.junit.Assert;

import java.io.IOException;

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

        Assert.assertEquals(model, modelup);

        GrammarInterpreter interpreter = new GrammarInterpreter();
        Double valueExpected = interpreter.execute(model);

        Double valueUP = interpreter.execute(model);

        Assert.assertEquals(valueExpected, valueUP, 1e-12);
        Assert.assertEquals(model, modelup);
    }

    @org.junit.Test
    public void testParsePerRule() {
        ExpressionLangModelParser parser = new ExpressionLangModelParser();

        NumberExpr numberExpr = parser.parseNumberExpr("2.5");

        Assert.assertEquals(2.5, numberExpr.getValue(), 1e-12);


        PlusMinusOpExpr op = parser.parsePlusMinusOpExpr("2 + 3");

        Assert.assertEquals("+", op.getOperator());

        Assert.assertTrue("Left expr. should be a number expr.", op.getLeft() instanceof NumberExpr);
        Assert.assertEquals(2, ((NumberExpr)op.getLeft()).getValue(), 1e-12);

        Assert.assertTrue("Right expr. should be a number expr.", op.getRight() instanceof NumberExpr);
        Assert.assertEquals(3, ((NumberExpr)op.getRight()).getValue(), 1e-12);

        Expr expr = parser.parseExpr("(1.3+4)");

        Assert.assertTrue("Expected paranexpr., got " + expr.getClass(), expr instanceof ParanExpr);

        expr = parser.parseExpr("4.3");

        Assert.assertTrue("Expected numberexpr., got " + expr.getClass(), expr instanceof NumberExpr);
    }
}
