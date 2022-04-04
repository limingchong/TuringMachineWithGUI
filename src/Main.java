import editor.UTMEditor;
import reader.*;

/**
 * @author Mingchong Li
 * @version 0.0.1
 */
public class Main {

    /**
     * @param args [0]: the absolute path of desc file.
     *             [1]: the input.
     *             [2]: animation or not.
     *             EX: java -jar practical4-37847244.jar D:\Advance\practical4-37847244\src\simple-tm.desc 10101 --animation
     */
    public static void main(String[] args) {

        UTMEditor UTME = new UTMEditor();
        TMReader TMR = new TMReader();

        UTME.setUTMController(TMR);

        if (args.length == 3) {
            TMR.loadTuringMachineFrom(args[0]);
            if (args[1].length() > 20) {
                System.out.println("ERROR: The input is too long.");
                return;
            }
            TMR.setInput(args[1]);
            if (args[2].equals("--animation")) {
                TMR.setAnimation(true);
            } else if (args[2].equals("--noanimation")) {
                TMR.setAnimation(false);
            } else {
                System.out.println("ERROR: The available options are --animation or --noanimation.");
                return;
            }
            TMR.runUTM(args[1]);
        }else if(args.length != 0){
            System.out.println(
                    "ERROR: The input is not available." +
                    "[0]: the absolute path of desc file.\n" +
                    "[1]: the input.\n" +
                    "[2]: animation or not.\n" +
                    "EX: java -jar practical4-37847244.jar D:\\Advance\\practical4-37847244\\src\\simple-tm.desc 10101 --animation");
            return;
        }
    }
}
