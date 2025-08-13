// AppTest.java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.*;

public class RebalanceAPITests {
    double delta = 0.01; // Acceptable difference
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
        //Assert deltas against hand calculated values
        assertEquals(calculator.getDeltaMap().get("SPMO"), 6000.0, delta);
        assertEquals(calculator.getDeltaMap().get("BRKB"), 1000.0, delta);
        assertEquals(calculator.getDeltaMap().get("IDMO"), 0.0, delta);
    }
    @Test
    void portfolioTwoTest()
    {
        RebalanceCalculator calculator = new RebalanceCalculator();
        calculator.addTicker("SPMO", 1000.0, 0.55);
        calculator.addTicker("PHYS", 2000.0, 0.15);
        calculator.addTicker("IDMO", 3000.0, 0.15);
        calculator.addTicker("ETH", 4000.0, 0.15);
        calculator.calculateDeltaWithoutSelling();
        //Assert deltas against hand calculated values
        assertEquals(calculator.getDeltaMap().get("SPMO"), 13666.66667, delta);
        assertEquals(calculator.getDeltaMap().get("PHYS"), 2000.0, delta);
        assertEquals(calculator.getDeltaMap().get("IDMO"), 1000.0, delta);
        assertEquals(calculator.getDeltaMap().get("ETH"), 0.0, delta);
    }

    @Test
    void portfolioThreeTest()
    {
        RebalanceCalculator calculator = new RebalanceCalculator();
        calculator.addTicker("VOO", 87434.0, 0.70);
        calculator.addTicker("VXUS", 45629.0, 0.20);
        calculator.addTicker("BND", 10000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();
        //Assert deltas against hand calculated values
        assertEquals(calculator.getDeltaMap().get("VOO"), 72267.5, delta);
        assertEquals(calculator.getDeltaMap().get("VXUS"), 0.0, delta);
        assertEquals(calculator.getDeltaMap().get("BND"), 12814.5, delta);
    }

    // Query Test
    void validKeyTest()
    {
        RebalanceCalculator calculator = new RebalanceCalculator();
        calculator.addTicker("SPMO", 2000.0, 0.70);
        calculator.addTicker("BRKB", 3000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();
        assertTrue(calculator.getDelta("SPMO") > -1);
    }
    void invalidKeyTest()
    {
        RebalanceCalculator calculator = new RebalanceCalculator();
        calculator.addTicker("SPMO", 2000.0, 0.70);
        calculator.addTicker("BRKB", 3000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();
        assertTrue(calculator.getDelta("ASML") == -1);
    }


    // Output Test



}
