package com.flipkart.qa.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class to represent Test Parameters
 * 
 * @author Harish K
 *
 */
public class TestParams {

    private String testName;
    private HashMap<String, Object> map;
    
    public static TestParams create(String testName, Map<String, Object> parameterMap) {
        return new TestParams(testName, parameterMap);
    }
    
    private TestParams(String testName, Map<String, Object> parameterMap) {
        this.testName = testName;
        this.map = (HashMap<String, Object>) parameterMap;
    }
    
    /**
     * Retrieve the names of the parameters
     * 
     * @return
     */
    public Set<String> names() {
        return map.keySet();
    }
    
    /**
     * Retrieve the test name associated with the parameters
     * 
     * @return
     */
    public String getTestName() {
        return testName;
    }

    /**
     * Remove an object in the test parameters and return the object
     * 
     * @param name
     * @return
     */
    public Object remove(String name) {
        if(this.map.containsKey(name)) {
            return this.map.remove(name);
        } else {
            return String.format("The key (%s) was not found", name);
        }
    }
    
    /**
     * Add an key-value pair into the map
     * 
     * @param name
     * @param value
     */
    public void add(String name, Object value) {
        if(!this.map.containsKey(name)) {
            this.map.put(name, value);   
        }
    }
    
    public Map<String,Object> getMap(){
        return this.map;
    }
    /**
     * Retrieve the optional parameter with the provided name.
     * 
     * @param name
     * @return value or null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) map.get(name);
    }
    
}
