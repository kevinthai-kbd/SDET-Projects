// RebalanceCalculatorTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RebalanceUnitTests {
    private RebalanceCalculator calculator;
    private final double delta = 0.01; // Acceptable difference for floating point

    @BeforeEach
    void setUp() {
        calculator = new RebalanceCalculator();
    }

    // ---------- Logic Tests ----------

    @Test
    void shouldCalculateCorrectDelta_whenPortfolioIsBalanced_caseOne() {
        // Arrange
        calculator.addTicker("SPMO", 1000.0, 0.70);
        calculator.addTicker("BRKB", 1000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);

        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertEquals(6000.0, calculator.getDeltaMap().get("SPMO"), delta);
        assertEquals(1000.0, calculator.getDeltaMap().get("BRKB"), delta);
        assertEquals(0.0, calculator.getDeltaMap().get("IDMO"), delta);
    }

    @Test
    void shouldCalculateCorrectDelta_whenPortfolioIsBalanced_caseTwo() {
        // Arrange
        calculator.addTicker("SPMO", 1000.0, 0.55);
        calculator.addTicker("PHYS", 2000.0, 0.15);
        calculator.addTicker("IDMO", 3000.0, 0.15);
        calculator.addTicker("ETH", 4000.0, 0.15);

        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertEquals(13666.66667, calculator.getDeltaMap().get("SPMO"), delta);
        assertEquals(2000.0, calculator.getDeltaMap().get("PHYS"), delta);
        assertEquals(1000.0, calculator.getDeltaMap().get("IDMO"), delta);
        assertEquals(0.0, calculator.getDeltaMap().get("ETH"), delta);
    }

    @Test
    void shouldCalculateCorrectDelta_whenPortfolioIsBalanced_caseThree() {
        // Arrange
        calculator.addTicker("VOO", 87434.0, 0.70);
        calculator.addTicker("VXUS", 45629.0, 0.20);
        calculator.addTicker("BND", 10000.0, 0.10);

        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertEquals(72267.5, calculator.getDeltaMap().get("VOO"), delta);
        assertEquals(0.0, calculator.getDeltaMap().get("VXUS"), delta);
        assertEquals(12814.5, calculator.getDeltaMap().get("BND"), delta);
    }

    // ---------- Query Tests ----------

    @Test
    void shouldReturnValidDelta_whenKeyExists() {
        // Arrange
        calculator.addTicker("SPMO", 2000.0, 0.70);
        calculator.addTicker("BRKB", 3000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();

        // Act
        double deltaValue = calculator.getDelta("SPMO");

        // Assert
        assertTrue(deltaValue > -1);
    }

    @Test
    void shouldReturnNegativeOne_whenKeyDoesNotExist() {
        // Arrange
        calculator.addTicker("SPMO", 2000.0, 0.70);
        calculator.addTicker("BRKB", 3000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();

        // Act
        double deltaValue = calculator.getDelta("ASML");

        // Assert
        assertEquals(-1, deltaValue, delta);
    }

    // ---------- Output Test ----------

    @Test
    void shouldOutputCorrectDeltaValues_whenPrinting() {
        // Arrange
        calculator.addTicker("VOO", 87434.0, 0.70);
        calculator.addTicker("VXUS", 45629.0, 0.20);
        calculator.addTicker("BND", 10000.0, 0.10);
        calculator.calculateDeltaWithoutSelling();

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(out));

        // Act
        calculator.outputDelta();

        // Restore
        System.setOut(originalOut);
        String output = out.toString();

        // Assert
        assertTrue(output.contains("VOO: 72267.5"));
        assertTrue(output.contains("VXUS: 0.0"));
        assertTrue(output.contains("BND: 12814.5"));
    }

    // ---------- Edge Cases ----------

    @Test
    void shouldCalculateDelta_whenTickerHasZeroWeightOrValue() {
        // Arrange
        calculator.addTicker("TEST1", 0.0, 0.50);
        calculator.addTicker("TEST2", 1000.0, 0.50);

        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertEquals(1000.0, calculator.getDelta("TEST1"), delta);
        assertEquals(0.0, calculator.getDelta("TEST2"), delta);
    }

    @Test
    void shouldHandleOverweightAllocationsWithoutCrash() {
        // Arrange
        calculator.addTicker("AAA", 1000.0, 0.80);
        calculator.addTicker("BBB", 1000.0, 0.40); // total 120%

        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertNotEquals(-1, calculator.getDelta("AAA"));
        assertNotEquals(-1, calculator.getDelta("BBB"));
    }

    @Test
    void shouldReturnEmptyDeltaMap_whenPortfolioIsEmpty() {
        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertEquals(-1, calculator.getDelta("SPMO"));
        assertTrue(calculator.getDeltaMap().isEmpty());
    }

    @Test
    void shouldHandleNegativeValuesCorrectly() {
        // Arrange
        calculator.addTicker("NEG", -1000.0, 0.50);
        calculator.addTicker("POS", 1000.0, 0.50);

        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertEquals(2000.0, calculator.getDelta("NEG"), delta);
    }

    @Test
    void shouldContainAllTickersInDeltaMap_afterCalculation() {
        // Arrange
        calculator.addTicker("SPMO", 2000.0, 0.70);
        calculator.addTicker("BRKB", 3000.0, 0.20);
        calculator.addTicker("IDMO", 1000.0, 0.10);

        // Act
        calculator.calculateDeltaWithoutSelling();

        // Assert
        assertTrue(calculator.getDeltaMap().keySet().containsAll(
                java.util.Arrays.asList("SPMO", "BRKB", "IDMO")
        ));
    }

    // ---------- Remove Ticker Tests ----------

    @Test
    void shouldRemoveTickerAndRebalance_whenTickerExists() {
        // Arrange
        calculator.addTicker("AAPL", 100.0, 0.5);
        calculator.addTicker("MSFT", 100.0, 0.5);

        // Act
        calculator.removeTicker("AAPL");

        // Assert
        assertFalse(calculator.calculateCurrentWeight().containsKey("AAPL"));
        assertNull(calculator.getDesiredWeight("AAPL"));
        assertEquals(1.0, calculator.getDesiredWeight("MSFT"), delta);
    }

    @Test
    void shouldDoNothing_whenRemovingNonexistentTicker() {
        // Arrange
        calculator.addTicker("AAPL", 100.0, 0.5);
        calculator.addTicker("MSFT", 100.0, 0.5);

        // Act
        calculator.removeTicker("GOOG");

        // Assert
        assertTrue(calculator.calculateCurrentWeight().containsKey("MSFT"));
        assertEquals(2, calculator.calculateCurrentWeight().size());
    }
}
