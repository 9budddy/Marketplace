import com.mysql.cj.jdbc.Driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;


public class Main {
    static String[][] storeTable;
    public static void main(String[] args) {
        System.out.println();
        //createTable();

        //getUserInputs();

        //printTable();

    }



    private static void getUserInputs() {

        Scanner scan = new Scanner(System.in);
        System.out.println("""
                Welcome to the Store and Item search!
                Would you like to search by store or by an item?""");
        String input = scan.nextLine();
        while(!input.equalsIgnoreCase("store") && !input.equalsIgnoreCase("item")) {

            System.out.println("""

                    Invalid Input! (valid inputs: store | item)
                    Would you like to search by store or by an item?""");
            input = scan.nextLine();
        }

        if (input.equalsIgnoreCase("store")) {

            boolean found = false;
            int number = 0;
            do {
                System.out.println("\n" +
                        "Give the number for the store would you like to search for? (");
                for(int i = 1; i < storeTable[0].length; i++) {
                    System.out.print("\t" + storeTable[0][i] + " [" + i + "]");

                    if (storeTable[0].length-1 != i) {
                        System.out.print(",\n");
                    }
                }
                System.out.println(")");

                number = scan.nextInt();
                if (number >= 1 && number <= storeTable[0].length-1) {
                    found = true;
                }
            } while (!found);


            printItemSpacing();
            for (int i = 0; i < storeTable.length; i++) {
                if (!storeTable[i][number].equals("-1.0")) {
                    System.out.printf("| %-35s | %-35s |", storeTable[i][0], storeTable[i][number]);
                    printItemSpacing();
                }
            }
        }
        else if (input.equalsIgnoreCase("item")) {
            boolean found = false;
            do {
                System.out.println("\nPlease enter the name of the item you'd like to search for.");
                input = scan.nextLine();

                for (int i = 1; i < storeTable.length; i++) {
                    if (storeTable[i][0].toLowerCase().contains(input.toLowerCase())) {
                        if (!found) {
                            printTableSpacing();
                            for(int j = 0; j < storeTable[0].length; j++) {
                                if (j == 0) {
                                    System.out.printf("| %-35s |", storeTable[0][j]);
                                } else {
                                    System.out.printf(" %-35s | ", storeTable[0][j]);
                                }
                            }
                            printTableSpacing();
                        }
                        found = true;
                        for(int j = 0; j < storeTable[i].length; j++) {
                            String item = storeTable[i][j];
                            if (item.equalsIgnoreCase("-1.0")) {
                                item = "Not sold.";
                            }
                            if (j == 0) {
                                System.out.printf("| %-35s |", item);
                            } else {
                                System.out.printf(" %-35s | ", item);
                            }
                        }
                        printTableSpacing();
                    }
                }
            } while (!found);
        }
    }


    private static void createTable() {
        storeTable = new String[1][1];
        String stringData = readData();
        List<String> itemNames = new ArrayList<>();
        List<String> storeNames = new ArrayList<>();
        HashMap<String, HashMap<String, Double>> totalStoreMap = new HashMap<>();

        String[] storeMulData = stringData.split(";");
        for(String storeSingleData : storeMulData) {
            int separate = storeSingleData.indexOf(":");
            String storeName = storeSingleData.substring(separate+1).trim();
            String[] storeItemsData = storeSingleData.substring(0, separate).split(",");

            if (!storeNames.contains(storeName)) {
                storeNames.add(storeName);
            }

            HashMap<String, Double> storeItemMap = new HashMap<>();
            for(int i = 0; i < storeItemsData.length; i += 2) {
                if (!itemNames.contains(storeItemsData[i].trim())) {
                    itemNames.add(storeItemsData[i].trim());
                }
                storeItemMap.put(storeItemsData[i].trim(), Double.parseDouble(storeItemsData[i+1].trim()));
            }

            totalStoreMap.put(storeName.trim(), storeItemMap);
        }

        storeTable = new String[itemNames.size()+1][storeNames.size()+1];
        storeTable[0][0] = "Store & Items";
        for(int i = 1; i <= storeNames.size(); i++) {
            storeTable[0][i] = storeNames.get(i-1);
        }
        for(int i = 1; i <= itemNames.size(); i++) {
            storeTable[i][0] = itemNames.get(i-1);
        }

        for(int i = 1; i <= storeNames.size(); i++) {
            for (int j = 1; j <= itemNames.size(); j++) {
                storeTable[j][i] = totalStoreMap.get(storeNames.get(i-1)).getOrDefault(itemNames.get(j-1), -1.0).toString();
            }
        }
    }

    private static void printTable() {
        printTableSpacing();
        for (String[] strings : storeTable) {
            for (int j = 0; j < strings.length; j++) {
                if (j == 0) {
                    System.out.printf("| %-35s |", strings[j]);
                } else {
                    System.out.printf(" %-35s | ", strings[j]);
                }
            }
            printTableSpacing();
        }
    }

    private static void printTableSpacing() {
        System.out.printf("%n---------------------------------------------------------");
        System.out.print("---------------------------------------------------------");
        System.out.printf("-----------------------------------------%n");
    }

    private static void printItemSpacing() {
        System.out.printf("%n---------------------------------------------------------");
        System.out.printf("--------------------%n");
    }


    private static String readData() {
        StringBuilder strData = new StringBuilder();
        try {
            File file = new File("res/formattedData.txt");
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                strData.append(scan.nextLine());
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not read file.");
        }

        return strData.toString();
    }

}