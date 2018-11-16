package com.myproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadMass {

    public static List<String> readMass() {

        List<String> listOfMass = new ArrayList<>();
        int i = 0;


        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\Piotrek\\Desktop\\Projekty\\BodyMassDiary\\src\\waga.txt"));

            while (scanner.hasNext()) {
                String line = scanner.next();
                i++;
                if (i % 2 == 0) {
                    listOfMass.add(line);

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listOfMass;
    }

}
