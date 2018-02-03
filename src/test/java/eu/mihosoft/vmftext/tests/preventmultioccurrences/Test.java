package eu.mihosoft.vmftext.tests.preventmultioccurrences;

import eu.mihosoft.vmf.runtime.core.VIterator;
import eu.mihosoft.vmftext.tests.expressionlang.ExpressionLangModel;
import eu.mihosoft.vmftext.tests.expressionlang.NumberExpr;
import eu.mihosoft.vmftext.tests.expressionlang.PlusMinusOpExpr;
import eu.mihosoft.vmftext.tests.expressionlang.Prog;
import org.junit.Assert;

import java.util.stream.Collectors;

public class Test {
    @org.junit.Test
    public void preventMultipleOccurrencesOfInstanceTest() {

        ExpressionLangModel model = ExpressionLangModel.newBuilder().build();

        PlusMinusOpExpr operator = PlusMinusOpExpr.newBuilder().
                withLeft(NumberExpr.newBuilder().withValue(2.0).build()).
                withRight(NumberExpr.newBuilder().withValue(3.0).build()).build();

        model.setRoot(Prog.newBuilder().withExpression(operator).build());

        boolean multipleOccurrences1 = model.vmf().content().stream(VIterator.IterationStrategy.UNIQUE_PROPERTY)
                .collect(Collectors.groupingBy(System::identityHashCode, Collectors.counting())).
                        values().stream().filter(n->n>1).count()>0;

        operator.setLeft(operator);

        boolean multipleOccurrences2 = model.vmf().content().stream(VIterator.IterationStrategy.UNIQUE_PROPERTY)
                .collect(Collectors.groupingBy(System::identityHashCode, Collectors.counting())).
                        values().stream().filter(n->n>1).count()>0;

        Assert.assertTrue("The model does not contain multiple occurrences of the same instance", !multipleOccurrences1);

        Assert.assertTrue("The model does contain multiple occurrences of the same instance", multipleOccurrences2);

    }
}
