package com.myproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadAdditional {


    public static List<String> readAdditional() {


        List<String> listOfAdditional = new ArrayList<>();
        int i = 0;

        try {
            Scanner scanner = new Scanner(new File("waga.txt"));
            while (scanner.hasNext()) {
                String line = scanner.next();
                i++;
                if (i % 3 == 0) {

                    listOfAdditional.add(line);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listOfAdditional;
    }

}
