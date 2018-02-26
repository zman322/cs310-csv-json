package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and other whitespace
        have been added for clarity).  Note the curly braces, square brackets, and double-quotes!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject jsonObject = new JSONObject();
            
            JSONArray colHeaders = new JSONArray();
            JSONArray rowHeaders = new JSONArray();
            JSONArray bigData = new JSONArray();
            JSONArray lilData = new JSONArray();
            String[] lines;
            
            lines = full.get(0);
            
            for (String str : lines){ 
                colHeaders.add(str);
            }
            for(int i=1; i<full.size(); i++){
                lines = full.get(i);
                rowHeaders.add(lines[0]);
                lilData = new JSONArray();
                
                for(int j=1; j<lines.length; j++){
                    lilData.add(Integer.parseInt(lines[j])); 
                }
            }
            jsonObject.put("colHeaders", colHeaders);
            jsonObject.put("rowHeaders", rowHeaders);
            jsonObject.put("data", bigData);
            
            results = JSONValue.toJSONString(jsonObject);


            
        }
        
        catch(IOException e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            JSONArray colHeaders = (JSONArray) jsonObject.get("colHeaders");
            JSONArray rowHeaders = (JSONArray) jsonObject.get("rowHeaders");
            JSONArray data = (JSONArray) jsonObject.get("data");
            
            for(int i = 0; i < colHeaders.size(); i++){
                if(i == colHeaders.size()-1)
                    writer.append("\"" + colHeaders.get(i) + "\"");
                else
                    writer.append("\"" + colHeaders.get(i) + "\",");
            }
            writer.append("\n");
            
            for(int i = 0; i < rowHeaders.size(); i++){
                writer.append("\"" + rowHeaders.get(i) + "\",");
                JSONArray subData = (JSONArray) data.get(i);
                for(int j = 0; j < subData.size(); j++){
                    if(j == subData.size()-1)
                        writer.append("\"" + subData.get(j) + "\"");
                    else
                        writer.append("\"" + subData.get(j) + "\",");
                }
                writer.append("\n");
            }
            results += writer.toString();
        }
        
        
        catch(ParseException e) { return e.toString(); }
        
        return results.trim();
        
    }
	
}