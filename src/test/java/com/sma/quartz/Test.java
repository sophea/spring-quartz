package com.sma.quartz;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Mak Sophea
 * Date: 4/14/2020
 */
public class Test {

    public static void main(String[] args) throws URISyntaxException, IOException {

        String line = "update from table where xxxxxeev card_number='1234560000001234' sesers ksfalfs ";

        line = "update from table where card_number='1122330000001234' sesers ksfalfs'''' ";

        System.out.println(maskCardNumber(line));

        final Path path = Paths.get(Test.class.getClassLoader().getResource("text.txt").toURI());
        try ( Stream<String> lines = Files.lines(path)) {
            List<String> list = lines.map(s->maskCardNumber(s)).collect(Collectors.toList());
            Files.write(Paths.get("d:/tmp/output.txt"), list, StandardCharsets.UTF_8);
        }
    }


    public static  String maskCardNumber(String line) {
        int index=line.indexOf("card_number='");
        if (index <0) {
           // System.out.println(" no found "+ index);
            return line;
        }
        int lengthCardNumber = "card_number='".length();
        //String value = line.substring(index + lengthCardNumber, index + lengthCardNumber+16);
        String value = line.substring(index + lengthCardNumber);
       // System.out.println(value);
        int lastIndex=value.indexOf("'");
        value = value.substring(0, lastIndex);
       // System.out.println(value);
        if (value.length() != 16) {
            return  line;
        }

        final String sixDigit = value.substring(0,6);
        //System.out.println(sixDigit);
        String last4Digit = value.substring(value.length() -4);
        //System.out.println(last4Digit);
        final String maskValue = String.format("%sXXXXXX%s", sixDigit, last4Digit);
        //System.out.println(maskValue);
        String newLine = line.substring(0, index+lengthCardNumber) + String.format("%s", maskValue) + line.substring(index + lengthCardNumber+16);
       // System.out.println(newLine);
        return newLine;
    }
}
