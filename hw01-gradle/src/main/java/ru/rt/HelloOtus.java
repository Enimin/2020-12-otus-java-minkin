package ru.rt;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.util.List;

public class HelloOtus{

    public static void main(String... args) {
        final char splitterSymbol = ',';
        final char joinerSymbol = '_';
        final String inputData = "Hello, ,Otus,,!,";

        String joinedWords = joinWordsList(inputData, splitterSymbol, joinerSymbol);

        System.out.println(joinedWords);
    }

    public static String joinWordsList(String wordsList, char splitterSymbol, char joinerSymbol){
        List<String> words = Splitter.on(splitterSymbol).trimResults().omitEmptyStrings().splitToList(wordsList);
        String joinedWords = Joiner.on(joinerSymbol).join(words);

        return joinedWords;
    }
}
