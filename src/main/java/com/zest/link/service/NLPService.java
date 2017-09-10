package com.zest.link.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import edu.stanford.nlp.util.Triple;
import org.springframework.stereotype.Service;


/**
 * Created by KrasovaO on 8/30/2017.
 */
@Service
public class NLPService {

    String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";

    @Autowired
    private GoogleSearchService googleSearchService;

    public String getPersonInformation(String name) throws IOException, ClassNotFoundException {

        fetchAndSave(name, "facebook");
        fetchAndSave(name, "twitter");
        fetchAndSave(name, "all");
        fetchAndSave(name, "instagram");
        fetchAndSave(name, "sec");

        return "ok";
    }

    private void fetchAndSave(String name, String source) throws IOException, ClassNotFoundException {
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

        String facebookResult = googleSearchService.getGoogleSearchResult(name,source);

        DocumentContext jsonContext = JsonPath.parse(facebookResult, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));
        List<LinkedHashMap> facebookItems = jsonContext.read("$.items[*]");
        Path path = Paths.get("./" +name+"_"+source+".txt");
        for (LinkedHashMap link : facebookItems){
            String display = (String) link.get("displayLink");

            if (!display.contains("linkedin")) {
                String fileContents = googleSearchService.fetchHtmlByURL((String) link.get("link"));
                List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);

                for (Triple<String, Integer, Integer> item : list) {
                    String s = item.first() + ": " + fileContents.substring(item.second(), item.third()) + "\n";
                    Files.write(path, s.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                }
            }
        }
    }
}
