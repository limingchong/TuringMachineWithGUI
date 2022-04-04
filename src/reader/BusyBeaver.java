package reader;

import utm.MoveClassical;

/**
 * Class extends ExtendedUTM to realize polymorphic.
 */
public class BusyBeaver extends ExtendedUTM {
    /**
     * Construct a busy beaver object, load all zeros input and move head to the middle.
     * @param tmr Turing Machine reader
     */
    public BusyBeaver(TMReader tmr){
        super(tmr);
        getUTM().loadInput("00000000000000000000");
        for (int i = 0; i < 10; i++)
            getUTM().moveHead(MoveClassical.RIGHT,false);
    }

}
