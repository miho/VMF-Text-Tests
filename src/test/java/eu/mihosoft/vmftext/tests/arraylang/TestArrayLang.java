package eu.mihosoft.vmftext.tests.arraylang;

import eu.mihosoft.vcollections.VList;
import eu.mihosoft.vmftext.tests.arraylang.parser.ArrayLangModelParser;
import eu.mihosoft.vmftext.tests.arraylang.unparser.ArrayLangModelUnparser;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import org.junit.Test;


public class TestArrayLang {
    @Test
    public void parseModelTest() {
        ArrayLangModelParser parser = new ArrayLangModelParser();
        ArrayLangModel model1 = parser.parse("(1.0)");
        Array array1 = model1.getRoot();
        Assert.assertEquals(array1.getValues().size(),1);
        Assert.assertEquals(array1.getValues().get(0),1.0,1e-12);

        ArrayLangModel model2 = parser.parse("(1.0, 1.2, 2.3)");
        Array array2 = model2.getRoot();
        Assert.assertEquals(3, array2.getValues().size());
        Assert.assertEquals(VList.newInstance(Arrays.asList(1.0,1.2,2.3)), array2.getValues());
    }

    @Test
    public void unparseModelTest() {

        ArrayLangModel model1 = ArrayLangModel.newInstance();
        model1.setRoot(Array.newBuilder().withValues(VList.newInstance(Arrays.asList(1.0))).build());
        ArrayLangModelUnparser unparser1 = new ArrayLangModelUnparser();
        String s1 = unparser1.unparse(model1).trim(); // for the default formatter we are ok with leading & trailing spaces
        Assert.assertEquals("( 1.0 )", s1);

        ArrayLangModel model2 = ArrayLangModel.newInstance();
        model2.setRoot(Array.newBuilder().withValues(VList.newInstance(Arrays.asList(1.0, 2.1, 100.7))).build());
        ArrayLangModelUnparser unparser2 = new ArrayLangModelUnparser();
        String s2 = unparser2.unparse(model2).trim(); // for the default formatter we are ok with leading & trailing spaces
        Assert.assertEquals("( 1.0 , 2.1 , 100.7 )", s2);

    }

    @Test
    public void parseUnparseModelTest() {

        ArrayLangModelParser parser1 = new ArrayLangModelParser();
        ArrayLangModel model1 = parser1.parse("(1.0)");
        ArrayLangModelUnparser unparser1 = new ArrayLangModelUnparser();
        String s1 = unparser1.unparse(model1);
        ArrayLangModel model1up = parser1.parse(s1);

        Assert.assertEquals(model1, model1up);

        ArrayLangModelParser parser2 = new ArrayLangModelParser();
        ArrayLangModel model2 = parser2.parse("(1.1,2.3,100.7)");
        ArrayLangModelUnparser unparser2 = new ArrayLangModelUnparser();
        String s2 = unparser2.unparse(model2);
        ArrayLangModel model2up = parser2.parse(s2);

        Assert.assertEquals(model2, model2up);
    }
}
