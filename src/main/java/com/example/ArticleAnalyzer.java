package com.example;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.StringUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.example.BoilerPipeExtractor.extractFromUrl;

public class ArticleAnalyzer {
    private HashSet<String> article;
    private static final String modelPath = "edu\\stanford\\nlp\\models\\pos-tagger\\english-left3words\\english-left3words-distsim.tagger";
    private MaxentTagger tagger;

    public ArticleAnalyzer() {
        tagger = new MaxentTagger(modelPath);
        article = new HashSet<>();
    }

    public String tagPos(String input) {
        return tagger.tagString(input);
    }

    public static HashSet<String> extractProperNouns(String taggedOutput) {
        HashSet<String> propNounSet = new HashSet<>();
        String[] split = taggedOutput.split(" ");
        List<String> propNounList = new ArrayList<>();
        for (String token : split) {
            String[] splitTokens = token.split("_");
            if (splitTokens.length >= 2 && splitTokens[1].equals("NNP")) {
                propNounList.add(splitTokens[0]);
            } else {
                if (!propNounList.isEmpty()) {
                    propNounSet.add(StringUtils.join(propNounList, " "));
                    propNounList.clear();
                }
            }
        }
        if (!propNounList.isEmpty()) {
            propNounSet.add(StringUtils.join(propNounList, " "));
            propNounList.clear();
        }
        return propNounSet;
    }

    public void addKeyword(String keyword) {
        article.add(keyword);
    }

    public boolean areKeywordsMentioned(HashSet<String> articleProperNouns) {
        HashSet<String> lowercaseArticle = new HashSet<>();
        for (String topics : article) {
            lowercaseArticle.add(topics.toLowerCase());
        }
        for (String properNoun : articleProperNouns) {
            if (lowercaseArticle.contains(properNoun.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    public boolean analyzeArticle(String urlString) throws IOException, SAXException, BoilerpipeProcessingException {
        String articleText = extractFromUrl(urlString);
        String tagged = tagPos(articleText);
        HashSet<String> properNounsSet = extractProperNouns(tagged);
        return areKeywordsMentioned(properNounsSet);
    }
}
