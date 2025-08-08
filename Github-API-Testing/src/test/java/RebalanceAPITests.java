// AppTest.java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

public class RebalanceAPITests {

    // Logic Tests
    @Test
    void InitializeTest() {
        RebalanceCalculator calculator = new RebalanceCalculator();
    }
    @Test
    void portfolioOneTest() {
        RebalanceCalculator calculator = new RebalanceCalculator();
        calculator.addTicker("SPMO", 1000.0, 0.70);
        calculator.addTicker("BRKB", 1000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();
    }
    @Test
    void portfolioTwoTest() {
        RebalanceCalculator calculator = new RebalanceCalculator();
        calculator.addTicker("SPMO", 700.0, 0.70);
        calculator.addTicker("BRKB", 200.0, 0.20);
        calculator.addTicker("IDMO", 100.0, 0.10);
        calculator.calculateDeltaWithoutSelling();
    }
    @Test
    void portfolioThreeTest() {
        RebalanceCalculator calculator = new RebalanceCalculator();
        calculator.addTicker("SPMO", 2000.0, 0.70);
        calculator.addTicker("BRKB", 3000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();
    }

    // Input Test

    // Output Test



}
