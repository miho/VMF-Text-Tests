package eu.mihosoft.vmftext.tests.typemappings;

import eu.mihosoft.vmftext.tests.typemappings.parser.TypeMappingsModelParser;
import eu.mihosoft.vmftext.tests.typemappings.unparser.TypeMappingsModelUnparser;

public class Test {

    @org.junit.Test
    public void test() {
        TypeMappingsModelParser   parser   = new TypeMappingsModelParser();
        TypeMappingsModelUnparser unparser = new TypeMappingsModelUnparser();

//        NumberLiteral numberLiteral = IntLiteral.newBuilder().withValue(3).build();
//
//        Expression e = ValueExpression.newBuilder().withValue(numberLiteral).build();
//
//        e = numberLiteral;
    }

}
