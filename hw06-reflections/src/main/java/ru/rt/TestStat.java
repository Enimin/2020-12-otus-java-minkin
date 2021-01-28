package ru.rt;

import java.util.HashMap;
import java.util.Map;

public class TestStat {
    private final HashMap<String, Boolean> testsList = new HashMap<>();

    public void saveTestResult(String testName, Boolean testStatus){
        testsList.put(testName,testStatus);
    }

    public void printStats(){
        int total = testsList.size(), passed = 0;

        for( Map.Entry<String, Boolean> test : testsList.entrySet()){
            String statTest = test.getKey();

            if (test.getValue()){
                statTest += " PASSED";
                passed++;
            }
            else
            {
                statTest += " FAILED";
            }

            System.out.println(statTest);
        }
        String summary = total + " tests completed";
        if (passed != total){
            summary +=  ", " + passed + " passed" + ", " + (total-passed) + " failed";
        }

        System.out.println(summary);
    }
}
