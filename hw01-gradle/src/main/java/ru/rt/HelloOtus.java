package ru.rt;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.util.List;

public class HelloOtus{

    public static void main(String... args) {
        final char splitterSymbol = ',';
        final char joinerSymbol = '_';
        final String inputData = "Hello, ,Otus,,!,";

        List<String> words = Splitter.on(splitterSymbol).trimResults().omitEmptyStrings().splitToList(inputData);
        String joinedWords = Joiner.on(joinerSymbol).join(words);

        System.out.println(joinedWords);
    }
}
