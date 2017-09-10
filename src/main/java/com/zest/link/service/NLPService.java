package com.zest.link.service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
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

    public List<String> getPersonInformation(String name) throws IOException, ClassNotFoundException {

        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
        String facebookResult = googleSearchService.getGoogleSearchResult(name, "all");

        DocumentContext jsonContext = JsonPath.parse(facebookResult);
        List<LinkedHashMap> facebookItems = jsonContext.read("$.items[*]");

        //go through favorite links if exist


        for (LinkedHashMap link : facebookItems){
            String display = (String) link.get("displayLink");
            System.out.print(display);
            if (!display.contains("linkedin")) {
                String fileContents = googleSearchService.fetchHtmlByURL((String) link.get("link"));
//                List<List<CoreLabel>> out = classifier.classify(fileContents);
//                for (List<CoreLabel> sentence : out) {
//                    for (CoreLabel word : sentence) {
//                        System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
//                    }
//                    System.out.println();
//                }

                List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);
                for (Triple<String, Integer, Integer> item : list) {
                    System.out.println(item.first() + ": " + fileContents.substring(item.second(), item.third()));
                }
            }
        }

//        String fileContents = googleSearchService.fetchHtmlByURL("https://www.linkedin.com/in/okrasova");
//        List<List<CoreLabel>> out = classifier.classify(fileContents);
//        for (List<CoreLabel> sentence : out) {
//            for (CoreLabel word : sentence) {
//                System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
//            }
//            System.out.println();
//        }

//        System.out.println("---");
//        List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);
//        for (Triple<String, Integer, Integer> item : list) {
//            System.out.println(item.first() + ": " + fileContents.substring(item.second(), item.third()));
//        }

        List<String> persons = null;
        return persons;
    }
}
