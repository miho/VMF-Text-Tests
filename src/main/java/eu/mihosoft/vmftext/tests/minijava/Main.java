package eu.mihosoft.vmftext.tests.minijava;

import eu.mihosoft.vmftext.tests.minijava.parser.MiniJavaModelParser;
import eu.mihosoft.vmftext.tests.minijava.unparser.MiniJavaModelUnparser;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        // generate the model instance by parsing a code file
        MiniJavaModelParser parser = new MiniJavaModelParser();
        MiniJavaModel model1 = parser.parse(new File("test-code/MiniJavaLongCodeFileTest1.java"));
        MiniJavaModel model2 = parser.parse(new File("test-code/MiniJavaLongCodeFileTest2.java"));

        MiniJavaModelUnparser unparser = new MiniJavaModelUnparser();
        //unparser.setFormatter(new MyFormatter());

        StringWriter w = new StringWriter();

        for (int i = 0; i < 10000; i++) {
            // unparse the current model
            //String s1 = unparser.unparse(model1);

            unparser.unparse(model1,w);

            // parse the model from the previously unparsed model
            //MiniJavaModel modelup1 = parser.parse(s1);

            //Assert.assertEquals(model1, modelup1);

            // unparse the current model
            //String s2 = unparser.unparse(model2);

            unparser.unparse(model2,w);

            // parse the model from the previously unparsed model
            //MiniJavaModel modelup2 = parser.parse(s2);

            //Assert.assertEquals(model2, modelup2);
        }

        w.close();
    }
}
