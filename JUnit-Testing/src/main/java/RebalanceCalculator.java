import java.util.HashMap;
import java.util.Map;

public class RebalanceCalculator
{
    private Map<String, Double> currentPrices;
    private Map<String, Double> currentWeights;
    private Map<String, Double> desiredWeights;
    private Map<String, Double> mapDeltas;
    private Map<String, Double> percentDeltas;
    // static Scanner scanner;

    public RebalanceCalculator()
    {
        currentPrices = new HashMap<>();
        currentWeights = new HashMap<>();
        desiredWeights = new HashMap<>();
        mapDeltas = new HashMap<>();
        percentDeltas = new HashMap<>();
    }

    public Map<String, Double> getDeltaMap()
    {
        return this.mapDeltas;
    }

    public Double getDelta(String ticker)
    {
        if(this.mapDeltas.containsKey(ticker))
            return this.mapDeltas.get(ticker);
        return -1.0;
    }
    public void addTicker(String name, Double costBasis, Double desiredWeight)
    {
        currentPrices.put(name, costBasis);
        desiredWeights.put(name, desiredWeight);
        currentWeights = calculateCurrentWeight(); // recalculate current weights after every addition
    }

    public boolean checkKeys()
    {
        for(Map.Entry<String, Double> entry : currentWeights.entrySet())
        {
            if(!desiredWeights.keySet().contains(entry.getKey()))
                return false;
        }
        return true;
    }

    public Map<String, Double> calculateCurrentWeight()
    {
        double total = 0;
        Map<String, Double> desiredWeightsTemp = new HashMap<>();
        for(Map.Entry<String, Double> entry : currentPrices.entrySet())
        {
            total += entry.getValue();
        }

        for(Map.Entry<String, Double> entry : currentPrices.entrySet())
        {
            desiredWeightsTemp.put(entry.getKey(), entry.getValue() / total);
        }
        return desiredWeightsTemp;
    }

    public void outputDelta()
    {
        // outputDelta results
        for(Map.Entry<String, Double> entry : mapDeltas.entrySet())
        {
            System.out.print(entry.getKey() + ": ");
            System.out.println(entry.getValue());
        }
    }

    public void calculateDeltaWithoutSelling()
    {
        // non-sell version

        // calculate delta to desired allocation weights
        double currentMax = 0; // negatives included
        String maxKey = "";
        for(Map.Entry<String, Double> entry : currentWeights.entrySet())
        {
            if(entry.getValue()-desiredWeights.get(entry.getKey()) > currentMax){
                currentMax = entry.getValue()-desiredWeights.get(entry.getKey());
                maxKey = entry.getKey();
            }
        }

        if(maxKey == "")
            return;

        // gets price with the greatest delta, we use this as an anchor
        double newTotal = currentPrices.get(maxKey) / desiredWeights.get(maxKey);
        // total * desired weight = current price
        newTotal = Math.round(newTotal * 100.0) / 100.0;


        // calculate the delta between current cost basis and cost basis for desired allocation
        for(Map.Entry<String, Double> entry : currentWeights.entrySet())
        {
            String key = entry.getKey();
            mapDeltas.put(key, (newTotal * desiredWeights.get(key)) - currentPrices.get(key));
        }
    }

    public void removeTicker(String name) {
        currentPrices.remove(name);
        desiredWeights.remove(name);
        currentWeights = calculateCurrentWeight();
        rebalanceDesiredWeights();
    }

    public void updateTickerValue(String name, Double newCostBasis) {
        if (currentPrices.containsKey(name)) {
            currentPrices.put(name, newCostBasis);
            currentWeights = calculateCurrentWeight();
        }
    }
    void calculateDeltaPercentages() {
        for (String key : mapDeltas.keySet()) {
            double current = currentPrices.get(key);
            if (current != 0) {
                percentDeltas.put(key, mapDeltas.get(key) / current);
            } else {
                percentDeltas.put(key, 0.0);
            }
        }
    }
    public Map<String, Double> getDeltaPercentages() {
        return percentDeltas;
    }

    public Double getDesiredWeight(String key) {
        return desiredWeights.get(key);
    }

    public void rebalanceDesiredWeights()
    {
        double total = 0;
        for(Map.Entry<String, Double> entry : desiredWeights.entrySet())
        {
            total += entry.getValue();
        }

        for(Map.Entry<String, Double> entry : desiredWeights.entrySet())
        {
            entry.setValue(entry.getValue()/total);
        }

    }


}