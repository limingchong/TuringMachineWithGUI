package reader;
import utm.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;

/**
 * The class to control to show Universal Turing Machine.
 * @author Mingchong Li
 * @version 0.0.1
 */
public class ExtendedUTM extends Thread {

    private UniversalTuringMachine UTM;

    private ExtendedTM ETM;
    private boolean animation = true;

    private String currentState;
    private String currentCell;

    private JPanel controlPad;
    private JTable rulesTable;
    private JButton pause;
    private JButton next;
    private JButton reset;

    /**
     * If the turing machine is running.
     */
    private boolean running = false;

    /**
     * Initialization of extended Universal Turing Machine.
     * @param tmr Turing Machine Reader
     */
    public ExtendedUTM(TMReader tmr){

        UTM = new UniversalTuringMachine();
        animation = tmr.isAnimation();
        UTM.setTitle(UTM.getTitle() + " - " + tmr.getDeltaFunction());

        ETM = tmr.getETM();
        UTM.loadTuringMachine(ETM);
        UTM.loadInput(tmr.getInput());

        controlPad = new JPanel();

        pause = new JButton("CONTINUE");
        pause.setBackground(new Color(131,175, 155));
        pause.setForeground(Color.white);

        next = new JButton("NEXT");
        next.setBackground(new Color(249,205,173));
        next.setForeground(Color.white);

        reset = new JButton("RESET");
        reset.setBackground(new Color(254,67, 101));
        reset.setForeground(Color.white);

        controlPad.add(pause);
        controlPad.add(next);
        controlPad.add(reset);

        controlPad.setVisible(true);
        controlPad.setLayout(new GridLayout(1,3));
        controlPad.setBounds(Config.TAPE_X_START, Config.RULES_Y_START+Config.RULES_HEIGHT + 20, Config.TAPE_WIDTH, Config.TAPE_HEIGHT);

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = !running;
                if (pause.getText().equals("PAUSE")) pause.setText("CONTINUE");
                else pause.setText("PAUSE");
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Step();
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause.setVisible(true);
                pause.setText("CONTINUE");
                running = false;
                UTM.loadTuringMachine(ETM);
                UTM.loadInput(tmr.getInput());
            }
        });

        UTM.display();

        JLayeredPane mainContainer = UTM.getLayeredPane();
        mainContainer.add(controlPad);
        rulesTable =((JTable)((JViewport)((JScrollPane)((JLayeredPane)((JPanel)mainContainer.getComponent(1)).getComponent(0)).getComponent(0)).getComponent(0)).getComponent(0));
        rulesTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                ETM.getHead().reset();
                UTM.loadInput(tmr.getInput());
            }
        });

        JOptionPane.showMessageDialog(new JFrame("GUIDANCE"),
                "PAUSE: Show or pause the animation. It is pause now.\n" +
                         "NEXT: Turing Machine go a step further.\n" +
                         "RESET: Restart the Turing Machine.\n" +
                         "Table: Edit the table to change the rules, and the Turing Machine will be restarted automatically.");
    }

    /**
     * Thread function, call by start().
     * Forever run until the halt.
     * Pause when running is false.
     */
    public void run() {
        super.run();

        while (true){
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (running)
                if (!Step()){
                    running = false;
                    pause.setVisible(false);
                }
        }
    }

    /**
     * get current cell
     * @return current cell
     */
    public String getCurrentCell() {
        return currentCell;
    }

    /**
     * get current state
     * @return current state
     */
    public String getCurrentState() {
        return currentState;
    }

    /**
     * get Universal Turing Machine
     * @return Universal Turing Machine
     */
    public UniversalTuringMachine getUTM() {
        return UTM;
    }

    /**
     * go one step
     * @return this step is not the final step? true or false.
     */
    public boolean Step(){
        setCurrentState(ETM.getHead().getCurrentState());
        setCurrentCell(String.valueOf(ETM.getTape().get(ETM.getHead().getCurrentCell())));

        // record
        //System.out.println(getCurrentState() + "," + getCurrentCell());

        // check rules
        for (String rule[] : ETM.getRules()){
            if (rule[0].equals(getCurrentState()) && rule[1].equals(getCurrentCell())){
                getUTM().updateHeadState(rule[2]);
                getUTM().writeOnCurrentCell(rule[3].toCharArray()[0]);
                if (rule[4].equals("RIGHT"))
                    getUTM().moveHead(MoveClassical.RIGHT,animation);

                if (rule[4].equals("LEFT"))
                    getUTM().moveHead(MoveClassical.LEFT,animation);

                if (rule[4].equals("RESET"))
                    ETM.getHead().reset();
                break;
            }
        }

        if (ETM.getHead().getCurrentState().equals("qa")){
            End(HaltState.ACCEPTED);
            return false;
        }else if(ETM.getHead().getCurrentState().equals("qr")){
            End(HaltState.REJECTED);
            return false;
        }

        return true;
    }

    /**
     * set current cell
     * @param currentCell current cell
     */
    public void setCurrentCell(String currentCell) {
        this.currentCell = currentCell;
    }

    /**
     * set current state
     * @param currentState current state
     */
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    /**
     * is the turing machine shows animation
     * @return show or not show animation
     */
    public boolean isAnimation() {
        return animation;
    }

    /**
     * show the halt state
     * @param hs halt state
     */
    public void End(HaltState hs){
        UTM.displayHaltState(hs);
    }

}
