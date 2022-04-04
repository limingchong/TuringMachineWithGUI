package reader;

import reader.ExtendedUTM;
import utm.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import editor.*;

import javax.swing.*;

/**
 * The class to read desc files and save data.
 * @author Mingchong Li
 * @version 0.0.1
 */
public class TMReader implements UTMController{

    private String[] raw;
    private String[] rowRules;
    private String initialState;
    private String acceptState;
    private String rejectState;
    private String variant;
    private String DeltaFunction = "Untitled";

    private ExtendedTM ETM;
    private String input;
    private boolean animation = true;

    /**
     * Load Turing Machine from a file path.
     * Check the file is a desc file, and read its String to the raw.
     * @param filePath A string representing the absolute path to the TM
     */
    @Override
    public void loadTuringMachineFrom(String filePath){
        if (!filePath.endsWith(".desc")){
            JOptionPane.showMessageDialog(new JFrame(), "ERROR: Not a desc file.");
            return;
        }

        InputStream in = null;
        int size = 0;
        try {
            in = new FileInputStream(filePath);
            size = in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = "";

        for (int i = 1; i <= size; i++) {
            try {
                str += (char) in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        raw = str.split("\n");

        setParameters();
    }

    /**
     * Set if it show animation.
     * @param animation show animation or not.
     */
    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    /**
     * Check if it shows animation.
     * @return show animation or not.
     */
    public boolean isAnimation() {
        return animation;
    }

    /**
     * Read the input, create a polymorphic instance of controller to run teh Universal Turing Machine.
     * @param input A string representing the input for the loaded TM.
     */
    @Override
    public void runUTM(String input){
        this.input = input;

        ExtendedUTM controller = null;

        char[] ints = input.toCharArray();
        for (char x : ints){
            if (x != '0' && x != '1' && !variant.equals("BUSY_BEAVER")) {
                JOptionPane.showMessageDialog(new JFrame(), "ERROR: 0 or 1 only.");
                return;
            }
        }

        if (variant.equals("BUSY_BEAVER")){
            controller = new BusyBeaver(this);
            this.input = "00000000000000000000";
        }
        else if (variant.equals("LEFT_RESET")) controller = new LeftRest(this);
        else controller = new ExtendedUTM(this);

        controller.start();
    }

    /**
     * Set input.
     * @param input input
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Get input
     * @return input
     */
    public String getInput() {
        return input;
    }

    /**
     * set all parameters from desc file to the Turing Machine.
     */
    public void setParameters() {
        for (String str : raw){

            if (str.startsWith("initialState="))
                initialState = str.substring(13);

            if (str.startsWith("acceptState="))
                acceptState = str.substring(12);

            if (str.startsWith("rejectState="))
                rejectState = str.substring(12);

            if (str.startsWith("rules="))
                rowRules = str.substring(6).split("<>");

            if (str.startsWith("variant="))
                variant = str.substring(8);

            if (str.startsWith("#"))
                DeltaFunction = str.substring(1);

        }

        ETM = new ExtendedTM(rowRules.length,initialState,acceptState,rejectState);

        for (String rule : rowRules)
            ETM.addRule(rule.split(","));

    }

    /**
     * Get Extended Turing Machine.
     * @return Extended Turing Machine
     */
    public ExtendedTM getETM() {
        return ETM;
    }

    /**
     * Get name of the function
     * @return Delta Function
     */
    public String getDeltaFunction() {
        return DeltaFunction;
    }

}
