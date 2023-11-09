package ui;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            new GuiApp();
        } else if (args[0].equals("--cli")) {
            new ConsoleApp();
        } else {
            System.out.println("Unknown argument");
        }
    }
}
