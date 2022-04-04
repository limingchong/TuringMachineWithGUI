package utm;

/**
 * The class implements more elegant variable rules.
 * @author Mingchong Li
 * @version 0.0.1
 */
public class ExtendedTM extends TuringMachine{

    private String rules[][];
    private int ruleCount;

    /**
     * Creates a TM with no rules.
     *
     * @param rulesNumber  The number of rules that the machine will have.
     * @param initialState A string representing the TM's initial state.
     * @param acceptState  A string representing the TM's accept state.
     * @param rejectState  A string representing the TM's reject state.
     */
    public ExtendedTM(int rulesNumber, String initialState, String acceptState, String rejectState) {
        super(rulesNumber, initialState, acceptState, rejectState);
        rules = new String[rulesNumber + 1][5];
    }


    /**
     * Add a rule.
     * @param rule the rule to be added.
     */
    public void addRule(String rule[]) {
        rules[ruleCount] = rule;
        rules[++ruleCount] = new String[5];
    }

    /**
     * Set rules.
     * @param rules the rules.
     */
    public void setRules(String[][] rules) {
        this.rules = rules;
        ruleCount = rules.length;
    }

    /**
     * Get the count of rules.
     * @return rule count
     */
    public int getRuleCount() {
        return ruleCount;
    }

    /**
     * Get all rules.
     * @return all rules
     */
    @Override
    public String[][] getRules() {
        return rules;
    }
}
