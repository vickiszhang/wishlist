package ui;

import java.io.FileNotFoundException;

//represents a new wish app
public class Main {

    // EFFECTS: runs the GUI app
    public static void main(String[] args) {
        new WishAppGUI();
    }

    // EFFECTS: makes a new WishApp (console)
//    public static void main(String[] args) {
//        try {
//            new WishApp();
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to run application...");
//        }
//    }


}
