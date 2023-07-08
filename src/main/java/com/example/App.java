package com.example;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, SAXException, BoilerpipeProcessingException {
        ArticleAnalyzer analyzer = new ArticleAnalyzer();
        analyzer.addKeyword("chicken");
        boolean mentioned = analyzer.analyzeArticle("https://en.wikipedia.org/wiki/Chicken");
        if (mentioned) {
            System.out.println("Article is relevant! you might gain learn something.");
        } else {
            System.out.println("Article is irrelevant!");
        }
    }
}
