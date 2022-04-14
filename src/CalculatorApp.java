import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    private JLabel catSelected;
    private JLabel weightSelected;
    private JLabel totalWeight;
    private JButton continueToOutput;
    private JTextField assignmentInput;
    private JTextField assignmentGrade;
    private JComboBox categorySelect;

    static CalculatorApp myGUI = new CalculatorApp();
    static JFrame frame;

    static HashMap<String, Integer> noCapitals = new HashMap<>();
    static ArrayList<Category> inputCategories = new ArrayList<>();;

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

    private String removeSubstring(String regex, String input) {
        //if no contain regex just return
        if(!input.contains(regex)) return input;

        //remove any instances of the specified regex and return a string without those
        String output = "";

        String[] parsed = input.split(regex, 0);
        for (String s : parsed) output += s;

        return output;
    }

    private int parsePercentage(String input) {
        int output = -1;
        String processing = "";
        boolean errorFound = false;

        //get rid of percentages, if exist
        processing = removeSubstring("%", input);
        System.out.println("Processing = " + processing);
      //  processing = removeSubstring("\\.", processing);
      //  System.out.println("Processing = " + processing);

        try {
            output = (int) Double.parseDouble(processing);
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid percentage input: " + processing.toUpperCase() + ". Please input your value as an integer in the format \"XX%\".",
                    "Percentage Parse Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
        }

        if(output < 0 && !errorFound) {
            JOptionPane.showMessageDialog(frame, "Invalid percentage input (negative input). Please input your value as an integer in the format \"XX%\".",
                    "Percentage Input Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
        } else if(output > 100 && !errorFound) {
            JOptionPane.showMessageDialog(frame, "Invalid percentage input (input over 100%). Please input your value as an integer in the format \"XX%\".",
                    "Percentage Input Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
        }

        if(errorFound) return -1;
        return output;
    }

    private void addComboItem(JComboBox list, String key, String val) {
        catList.addItem(new ComboItem(key, val));
    }

    private int searchForCat(String name, int weight) {
        boolean nameFlag = false;
        boolean weightFlag = false;
        int index = 0;

        for(int i = 0; i < inputCategories.size() && !nameFlag && !weightFlag; i++) {
            //name must == searched name and weight must == searched weight
            if(inputCategories.get(i).getName().equalsIgnoreCase(name)) {
                nameFlag = true;
                if(inputCategories.get(i).getWeight() == weight) weightFlag = true;

                index = i;
            }
        }

        if(nameFlag && weightFlag) return 2; //already in there
        else if(nameFlag) return index; //override weight in this case

        return -1; //nothing found
    }

    private int searchForCatName(String name) {
        int index = -1;

        for(int i = 0; i < inputCategories.size(); i++) {
            //name must == searched name and weight must == searched weight
            if(inputCategories.get(i).getName().equalsIgnoreCase(name)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private void resetCatList() {
        catList.removeAllItems();
        for(int i = 0; i < inputCategories.size(); i++) {
            String name = inputCategories.get(i).getName();
            addComboItem(catList, name, name);
        }
    }

    private void appendToLabel(JLabel label, String toAppend) {
        String temp = label.getText();
        if(toAppend.charAt(0) != ' ') temp += (" " + toAppend);
        else temp += toAppend;

        label.setText(temp);
    }

    private void resetTotalWeightLabel() {
        totalWeight.setText("Total Category Weight:");
    }

    private void resetDisplayLabels() {
        catSelected.setText("Selected Category:");
        weightSelected.setText("Weight (percent):");
    }

    private int getWeightSum() {
        int sum = 0;
        for(int i = 0; i < inputCategories.size(); i++) {
            sum += inputCategories.get(i).getWeight();
        }

        return sum;
    }

    private boolean checkCanAdd(int weight) {
        int sum = getWeightSum();
        return sum + weight <= 100;
    }

    public CalculatorApp() {
        confirmCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the category from the input box, trim padding, and fix caps
                String category = catTitle.getText().toLowerCase(Locale.ROOT);

                //create cat and set name/make adjustments to name
                Category cat = new Category();
                cat.setName(category);
                cat.removeSpacePadding();
                cat.capitalizeTitle(noCapitals);

                //get percentage weight, trim padding, check if right format
                String percentInput = weightInput.getText().toLowerCase(Locale.ROOT);
                int percent = parsePercentage(percentInput);

                //do not want to populate combo box if encounter error in parsing int
                if(percent == -1) return;

                //if no error in parsing int we can set weight
                cat.setWeight(percent);

                int searchState = searchForCat(cat.getName(), cat.getWeight());

                //populate catList combo box/arraylist if we've encountered no errors so far
                if(checkCanAdd(percent)) {
                    if (searchState == -1) {
                        inputCategories.add(cat);
                        addComboItem(catList, cat.getName(), cat.getName());
                    } else if (searchState != 2) {
                        //if we have found the name but with a different weight override that one
                        inputCategories.get(searchState).setWeight(percent);
                    }

                    resetTotalWeightLabel();
                    appendToLabel(totalWeight, Integer.toString(getWeightSum()) + "/100%");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid percentage input. This percent would make the total weight over 100% (with this addition, the sum would be " + Integer.toString(getWeightSum() + percent) + "%).",
                            "Percentage Input Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        deleteCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object temp = catList.getSelectedItem();
                String name = "";
                boolean noErrors = true;

                if (temp != null) {
                    name = ((ComboItem)temp).getValue();
                } else return;

                if(noErrors) {
                    //if no errors, proceed to delete category and reset combo box
                    //to not have that category
                    int i = searchForCatName(name);
                    if(i >= 0) inputCategories.remove(i);
                    resetTotalWeightLabel();
                    appendToLabel(totalWeight, Integer.toString(getWeightSum()) + "/100%");
                    resetCatList();
                }
            }
        });
        catList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object temp = catList.getSelectedItem();
                String name = "";
                boolean noErrors = true;

                if (temp != null) {
                    name = ((ComboItem)temp).getValue();
                } else return;

                int i = searchForCatName(name);
                if(i >= 0) {
                    resetDisplayLabels();
                    appendToLabel(catSelected, inputCategories.get(i).getName());
                    appendToLabel(weightSelected, Integer.toString(inputCategories.get(i).getWeight()) + "%");
                }
            }
        });
        contToInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputCategories.size() > 0) {
                    CardLayout cl = (CardLayout) Home.getLayout();
                    cl.show(Home, "gradeInput");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please input some categories before continuing!",
                            "Category Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
