import java.util.HashMap;
import java.util.Locale;

public class Category {
    private int weight;
    private String name;

    public Category() {
        name = "";
        weight = 0;
    }

    public Category(String title, int weight) {
        this.name = title;
        this.weight = weight;
    }

    public void setName(String input) {
        name = input;
    }
    public void setWeight(int input) {
        weight = input;
    }
    public String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }

    public void parseName(String regex) {
        //remove any instances of the specified regex and sets name to string without that regex in there
        String output = "";

        String[] parsed = name.split(regex);
        for(int i = 0; i < parsed.length; i++) output += parsed[i];

        name = output;
    }

    public void removeSpacePadding() {
        //this function takes the name, checks for space at beginning and end,
        //and if it exists removes that padding to trim to the "real" input
        String edited = name;
        boolean spaceAtEnd = ( edited.charAt(edited.length() - 1) == ' ' );
        boolean spaceAtBegin = ( edited.charAt(0) == ' ' );

        while(spaceAtBegin || spaceAtEnd) {
            //check for only space at begin
            if(spaceAtBegin && !spaceAtEnd) edited = edited.substring(1);
                //check for only space at end
            else if(spaceAtEnd && !spaceAtBegin) edited = edited.substring(0, name.length() - 1);
                //check for space at beginning AND end
            else if(spaceAtBegin && spaceAtEnd) edited = edited.substring(1, name.length() - 1);

            //reset flags to see if there's still spaces
            spaceAtEnd = ( edited.charAt(name.length() - 1) == ' ' );
            spaceAtBegin = ( edited.charAt(0) == ' ' );
        }

        this.name = edited;
    }

    public void capitalizeTitle(HashMap<String, Integer> noCapitals) {
        //function that correctly capitalizes a category's name
        String[] separatedBySpace = name.split(" ");

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

        name = output;
    }

}
