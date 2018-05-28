package eu.mihosoft.vmftext.tests.json;


import eu.mihosoft.vmftext.tests.json.parser.JSONModelParser;
import eu.mihosoft.vmftext.tests.json.unparser.JSONModelUnparser;
import org.junit.Assert;

public class Test {

    @org.junit.Test
    public void testJSON() {
        JSONModelParser parser = new JSONModelParser();

        JSONModel model = parser.parse(
                    "{ \"version\" : 1.0 ,"
                        +  " \"data\" : {"
                        +  " \"sampleArray\" : [ \"mystring\" , true , false , { \"name\" : \"my name\" } ]"
                        +  " }"
                        + "} ");

        // output unchanged json document
        JSONModelUnparser unparser = new JSONModelUnparser();
        System.out.println(unparser.unparse(model));

        // iterate over all pairs and print them and their type
        System.out.println("Pairs:");
        model.vmf().content().stream(Pair.class).
                forEach(p-> System.out.println(" -> key: " + p.getKey() +
                        ", type: " + p.getValue().getClass().getSimpleName()));

        // change version number
        // we check both, name and type
        model.vmf().content().stream(Pair.class).
                filter(p->"version".equals(p.getKey())).map(p->p.getValue()).
                filter(v->v instanceof NumberValue).map(v->(NumberValue)v).forEach(v->

                // Automatic conversion! We use double here. No need to convert to String.
                v.setValue(2.0)
        );

        // invert all boolean values
        model.vmf().content().stream(BooleanValue.class).
                forEach(v->v.setValue(!v.getValue()));

        // insert pair
        Pair myPair = Pair.newInstance();
        myPair.setKey("my number");

        // number value
        NumberValue value = NumberValue.newInstance();

        // no conversion necessary! we can use double directly.
        value.setValue(1.2345);

        // add value to pair
        myPair.setValue(value);

        ((ObjectValue)model.getRoot().getValue()).getValue().getPairs().add(myPair);

        // output changed json document
        String changedJson = unparser.unparse(model);

        String expectedJson = "{ \"version\" : 2.0 , \"data\" : { \"sampleArray\" : [ \"mystring\" , false , true , { \"name\" : \"my name\" } ] } , \"my number\" : 1.2345 }";


        Assert.assertEquals(expectedJson, changedJson.trim());

    }

}
