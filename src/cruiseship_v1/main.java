package cruiseship_v1;

import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class main {
    
    private static Scanner input = new Scanner(System.in);
    private static String[] ship = new String[14];
    private static String[] cabinReorder = new String[14]; /* For ordering */
    
    public static void main(String[] args) {
        initialise();
        menu();
    }
    
    private static void initialise() { /* Adapted from the study notes PDF in Blackboard */
        for (int x = 1; x < 13; x++) ship[x] = "e";
        for (int x = 1; x < 13; x++) cabinReorder[x] = "e";
        System.out.println("Initialise \n");
    }
    
    private static void menu() {
        Boolean valid = false;
        
        String choice;
        System.out.println("Menu: \n");
        System.out.println("""
                           A: Add a customer to a cabin
                           V: View all cabins
                           
                           E: Display Empty cabins
                           D: Delete customer from cabin
                           F: Find cabin from customer name
                           S: Store program data into file
                           L: Load program data from file
                           O: View passengers Ordered alphabetically by name
                           
                           Q: Exit
                           """);
        System.out.print("Enter choice: ");
        choice = input.next();
        /* Loop until valid input */
        while (valid.equals(false))
        {
            switch (choice.toUpperCase()) {
                case "A":
                    /* Add a passenger to cabin */
                    addPassenger();
                    break;
                case "V":
                    /* View all cabins with passengers */
                    viewCabins();
                    break;
                case "E":
                    /* Display empty cabins */
                    displayEmpty();
                    break;
                case "D":
                    /* Removes passenger from cabin */
                    deleteCustomer();
                    break;
                case "F":
                    /* Enter name of passenger to find their cabin number */
                    findCabin();
                    break;
                case "S":
                    /* Store details into "cruiseship.txt" */
                    store();
                    break;
                case "L":
                    /* Load data from "cruiseShip.txt" */
                    load();
                    break;
                case "O":
                    /* Orders passengers alphabetically */
                    order();
                    break;
                case "Q":
                    /* Terminates program */
                    System.out.println("Exiting \n");
                    System.exit(0);
                default:
                    /* This is displayed if user enters invalid input */
                    System.out.print("Please enter a valid option: ");
                    choice = input.next();
                    break;
            }
        }
    }

    private static void addPassenger() {
        String choice;
        String cabinName;
        int cabinNum;
        System.out.print("Enter cabin number (1-12) or 13 to stop: ");
        choice = input.next();
        try {
            cabinNum = Integer.parseInt(choice);
            if (cabinNum == 13){
                option(); /* Options to return to menu or terminate program */
            } else if (cabinNum < 1 || cabinNum > 13) {
                System.out.println("Out of range (1-13)");
                addPassenger();
            }
            while (cabinNum >= 1 && cabinNum <= 12) {
                if (ship[cabinNum] == "e")
                {
                    System.out.print("Enter name for cabin " +cabinNum+ ": ");
                    cabinName = input.next().toUpperCase();

                    ship[cabinNum] = cabinName;
                    cabinReorder[cabinNum] = cabinName;
                    addPassenger();
                }
                else {
                    System.out.println("Cabin is already occupied");
                    addPassenger();
                }
            }
        } catch(NumberFormatException nfe) {
            System.out.println("Enter a valid cabin number (integer) or 13 to quit");
            addPassenger();
        }
    }
    
    private static void viewCabins() {
        for (int x = 1; x < 13; x++)
        {
            /* Displays cabin number with occupant */
            System.out.println("Cabin " +x+ " is occupied by " +ship[x]);
        }
        option();
    }
    
    private static void displayEmpty() {
        for (int x = 1; x < 13; x++)
        {
            /* Only displays cabin without occupant */
            if (ship[x].equals("e"))System.out.println("Cabin " +x+ " is empty");
        }
        option();
    }
    
    private static void deleteCustomer() {
        String choice;
        int cabinNum;
        System.out.print("Enter cabin number to delete customer or enter Q to quit: ");
        choice = input.next();
        if (choice.toUpperCase().equals("Q")) menu();
        try {
            cabinNum = Integer.parseInt(choice);
            if (cabinNum >= 1 && cabinNum <= 12)
            {
                /* Sets same value as when first initialised */
                ship[cabinNum] = "e";
                System.out.println("Customer deleted");
                option();
            }
            else {
                /* Shown if input is out of range */
                System.out.println("Out of range");
                deleteCustomer();
            }
        } catch(NumberFormatException nfe) {
            /* Shown on invalid input */
            System.out.println("Enter valid cabin number (integer) or Q to quit");
            deleteCustomer();
        }
    }
    
    private static void findCabin() {
        String name;
        System.out.print("Enter name of customer: ");
        name = input.next();
        for (int x = 1; x < 13; x++)
        {
            /* Checks if input is the same as occupant name in specific cabin number */
            if (name.toUpperCase().equals(ship[x]))
            {
                /* Displays what cabin the occupant is located in */
                System.out.println(name +" is in cabin "+x);
                option();
            }
        }
        System.out.println("Customer not found.");
        option();
    }
    
    private static void store() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("cruiseShip.txt"));
            
            /* Storing added passengers to cabins */
            writer.write("Cruise Ship:\n\n");
            for (int x = 1; x < 13; x++)
            {
                writer.write("Cabin " +x+ " is occupied by " +ship[x] + "\n");
            }
            writer.write("\n");
            
            /* Storing passenger names in
            alphabetical order in file */
            writer.write("Passengers:\n\n");
            for (int x = 1; x < 13; x++)
            {
                if (cabinReorder[x] != "e") {
                    writer.write(cabinReorder[x] + "\n");
                }
            }
            
            /* Display empty cabins in file */
            writer.write("\nEmpty cabins:\n");
            for (int x = 1; x < 13; x++)
            {
                if (ship[x].equals("e")){
                    writer.write("Cabin " +x+ " is empty\n");
                }
            }
            
            writer.close();
            System.out.println("\nData stored");
            
            option();
        } catch (IOException e) {
            /* File error */
            System.out.println("Error IOException is " + e);
            option();
        }
    }
    
    private static void load() {
        try {
            /* Reader for file */
            BufferedReader reader = new BufferedReader(new FileReader("cruiseShip.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            
            option();
        } catch (IOException e) {
            /* File error */
            System.out.println("Error IOException is " + e);
            menu();
        }
    }
    
    private static void order() {
        int size = 13;
        String temp = "";
        System.out.println("Passengers:\n");
        /* Perform bubble sort to
        order passengers alphabetically
        
        Revised from PGOnline OCR A-level textbook */
        for (int x = 1; x < size; x++) {
            cabinReorder[x] = ship[x];
        }
        for (int i = 1; i < size; i++) {
            for (int j = i+1; j < size; j++)
            {
                if (cabinReorder[i].charAt(0) > cabinReorder[j].charAt(0))
                {
                    temp = cabinReorder[i];
                    cabinReorder[i] = cabinReorder[j];
                    cabinReorder[j] = temp;
                }
            }
            System.out.println(cabinReorder[i]);
        }
        option();
    }
    
    private static void option() {
        /* User will be given the option to 
        exit the program or return to the menu */
        Boolean valid = false;
        System.out.println("");
        System.out.println("""
                           M: Menu
                           Q: Quit
                           """);
        System.out.print("Enter choice: ");
        String choice = input.next();
        while (valid.equals(false))
        {
            if (choice.toUpperCase().equals("M"))
            {
                menu();
            }
            else if (choice.toUpperCase().equals("Q"))
            {
                System.exit(0);
            }
            else
            {
                System.out.print("Enter either Q to quit or M to return to menu: ");
                choice = input.next();
            }
        }
    }

}