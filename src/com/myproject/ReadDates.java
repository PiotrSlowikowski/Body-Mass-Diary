package com.myproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadDates {

    public static List<String> readDates() {

        List<String> listOfDates = new ArrayList<>();
        int i = 0;


        try {
            Scanner scanner = new Scanner(new File("weight.txt"));

            while (scanner.hasNext()) {
                String line = scanner.next();
                i++;
                if (i % 3 == 1) {
                    listOfDates.add(line);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listOfDates;
    }

}
