import org.testng.annotations.Factory;

public class ParametrizedSwoopTestsFactory {
    @Factory
    public Object[] createInstances() {
        return new Object[] {
                new ParametrizedSwoopTests2("https://swoop.ge/category/636/sabavshvo/"),
                new ParametrizedSwoopTests2("https://swoop.ge/category/4/gartoba/"),
                new ParametrizedSwoopTests2("https://swoop.ge/category/15/kveba/")
        };
    }
}
