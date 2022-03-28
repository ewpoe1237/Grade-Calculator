import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;

public class CalculatorApp {
    private JPanel Home;
    private JPanel catInput;
    private JTextField weightInput;
    private JTextField catTitle;
    private JButton confirmCat;
    private JComboBox catList;
    private JButton deleteCat;
    private JButton contToInput;
    private JPanel gradeInput;

    static CalculatorApp myGUI = new CalculatorApp();
    static JFrame frame;

    static HashMap<String, Integer> noCapitals = new HashMap<>();

    public static void main(String[] args) {
        frame = new JFrame("CalculatorApp");
        frame.setContentPane(myGUI.Home);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        populateCapitalizationList();

        myGUI.Home.add(myGUI.catInput, "catInput");
        myGUI.Home.add(myGUI.gradeInput, "gradeInput");
    }

    private static void populateCapitalizationList() {
        noCapitals.put("and", 1);
        noCapitals.put("a", 1);
        noCapitals.put("an", 1);
        noCapitals.put("but", 1);
        noCapitals.put("for", 1);
        noCapitals.put("at", 1);
        noCapitals.put("by", 1);
        noCapitals.put("to", 1);
        noCapitals.put("nor", 1);
        noCapitals.put("so", 1);
        noCapitals.put("or", 1);
        noCapitals.put("after", 1);
        noCapitals.put("from", 1);
        noCapitals.put("with", 1);
        noCapitals.put("without", 1);
        noCapitals.put("along", 1);
        noCapitals.put("around", 1);
        noCapitals.put("the", 1);
    }

    private String removeSpacePadding(String input) {
        //this function takes an input string, checks for space at beginning and end,
        //and if it exists removes that padding to trim to the "real" input
        String edited = input;
        boolean spaceAtEnd = ( edited.charAt(edited.length() - 1) == ' ' );
        boolean spaceAtBegin = ( edited.charAt(0) == ' ' );

        while(spaceAtBegin || spaceAtEnd) {
            //check for only space at begin
            if(spaceAtBegin && !spaceAtEnd) edited = edited.substring(1);
            //check for only space at end
            else if(spaceAtEnd && !spaceAtBegin) edited = edited.substring(0, input.length() - 1);
            //check for space at beginning AND end
            else if(spaceAtBegin && spaceAtEnd) edited = edited.substring(1, input.length() - 1);

            //reset flags to see if there's still spaces
            spaceAtEnd = ( edited.charAt(input.length() - 1) == ' ' );
            spaceAtBegin = ( edited.charAt(0) == ' ' );
        }

        return edited;
    }

    private String capitalizeTitle(String input) {
        //function that correctly capitalizes a title (input) and returns it
        String[] separatedBySpace = input.split(" ");

        //loop through each word,
        //check if word is article/conjunction
        //capitalize first letter if not
        boolean noCapitalize = false;
        String observed;
        String output = "";

        for(int i = 0; i < separatedBySpace.length; i++) {
            observed = separatedBySpace[i].toLowerCase(Locale.ROOT);
            noCapitalize = (i != 0) && (i != separatedBySpace.length - 1)
                    && (noCapitals.containsKey(observed) || observed.length() <= 4);

            //if we NEED to capitalize, capitalize the first letter
            if(!noCapitalize) observed = Character.toUpperCase(observed.charAt(0)) + observed.substring(1);

            //add to output
            output += observed;

            //if we aren't looking at the last index add a space to separate each word in title
            if(i != separatedBySpace.length - 1) output += " ";
        }

        return output;
    }

    private String removeSubstring(String regex, String input) {
        //remove any instances of the specified regex and return a string without those
        String output = "";

        String[] parsed = input.split(regex);
        for(int i = 0; i < parsed.length; i++) output += parsed[i];

        return output;
    }

    private int parsePercentage(String input) {
        int output = -1;
        String processing = "";
        boolean errorFound = false;

        //get rid of percentages and decimals, if those exist
        processing = removeSubstring("%", input);
        processing = removeSubstring(".", processing);

        try {
            output = Integer.parseInt(processing);
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid percentage input. Please input your value in the format \"XX%\".",
                    "Percentage Parse Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
        }

        if(output < 0 && !errorFound) {
            JOptionPane.showMessageDialog(frame, "Invalid percentage input (negative input). Please input your value in the format \"XX%\".",
                    "Percentage Input Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
        } else if(output > 100 && !errorFound) {
            JOptionPane.showMessageDialog(frame, "Invalid percentage input (input over 100%). Please input your value in the format \"XX%\".",
                    "Percentage Input Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
        }

        if(errorFound) return -1;
        return output;
    }

    public CalculatorApp() {
        contToInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) Home.getLayout();
                cl.show(Home, "gradeInput");
            }
        });
        confirmCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the category from the input box, trim padding, and fix caps
                String category = catTitle.getText().toLowerCase(Locale.ROOT);
                category = removeSpacePadding(category);
                category = capitalizeTitle(category);

                //get percentage weight, trim padding, check if right format
                String percentInput = weightInput.getText().toLowerCase(Locale.ROOT);
                int percent = parsePercentage(percentInput);

                //do not want to populate combo box if encounter error in parsing int
                if(percent == -1) return;

                //populate catList combo box if we've encountered no errors so far
            }
        });
    }
}
