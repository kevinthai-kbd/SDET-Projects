import java.util.HashMap;
import java.util.Map;

public class RebalanceCalculator
{
    private Map<String, Double> currentPrices;
    private Map<String, Double> currentWeights;
    private Map<String, Double> desiredWeights;
    private Map<String, Double> mapDeltas;
    // static Scanner scanner;

    public RebalanceCalculator()
    {
        currentPrices = new HashMap<>();
        currentWeights = new HashMap<>();
        desiredWeights = new HashMap<>();
        mapDeltas = new HashMap<>();

        currentPrices.put("SPMO",(double) 40000);
        currentPrices.put("IDMO",(double) 12400);
        currentPrices.put("BRK.B",(double) 5000);
        currentWeights = calculateWeights();
        desiredWeights.put("SPMO",0.70);
        desiredWeights.put("IDMO",0.20);
        desiredWeights.put("BRK.B",0.10);
        calculateDeltaWithoutSelling();
        //outputDelta();
    }

    public Map<String, Double> calculateWeights()
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

        // gets price with the greatest delta, we use this as an anchor
        double newTotal = currentPrices.get(maxKey) / desiredWeights.get(maxKey);
        // total * desired weight = current price
        newTotal = Math.round(newTotal * 100.0) / 100.0;
        System.out.println(maxKey);

        // calculate the delta between current cost basis and cost basis for desired allocation
        for(Map.Entry<String, Double> entry : currentWeights.entrySet())
        {
            String key = entry.getKey();
            mapDeltas.put(key, (newTotal * desiredWeights.get(key)) - currentPrices.get(key));
        }
    }


}