package com.zest.link.controller;

import com.zest.link.service.GoogleSearchService;
import com.zest.link.service.NLPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by KrasovaO on 8/28/2017.
 */
@RestController
@RequestMapping(value = "/search")
public class GoogleSearchController {

    @Autowired
    private GoogleSearchService googleSearchService;

    @Autowired
    private NLPService nlpService;

    @RequestMapping(value = "/{name}/{source}",
            method = RequestMethod.GET)
    public @ResponseBody
    String getGoogleSearchResult(@PathVariable("name") String name, @PathVariable("source") String source) throws Exception{
        return googleSearchService.getGoogleSearchResult(name, source);
    }

    @RequestMapping(value = "/{name}/save",
            method = RequestMethod.GET)
    public @ResponseBody
    String getPersonInformation(@PathVariable("name") String name) throws Exception {
        return nlpService.getPersonInformation(name);
    }

}
