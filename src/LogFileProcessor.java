import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * LogFileProcessor takes into two arguments via command line and uses Regular Expression to store the number of instances of
 * IP address and users. IP address and users are stored in two respective HashMaps where:
 *      Key - The IP address or user name of type String
 *      Value - The count of the given key.
 *          For example, if a user is referenced 3 times in the .log, the value for the user key (count) would be 3.
 *          The same structure is followed for IP addresses.
 *
 * The nature of the HashMap does not allow for duplicate key's so, when a already present key is encountered again,
 * a new value is calculated using the previous state of said value.
 *      For example, if the count of user "bob" was 3, and the pattern detects "bob" again, the new count is 3+1 = 4.
 *
 * The HashMap's lack of duplicate keys also allows easy counting of unique users and IP addresses.
 * The number of unique IPs and users is the number of rows in the HashMap. This can be accessed via the keySet().size() call.
 * </pre>
 * @author Zachary Cowan
 * @version 1.0
 * Assignment 4
 * CS322 - Compiler Construction
 * Spring 2024
 */
public class LogFileProcessor {

    /**
     * Main method
     * @param args first command line argument to be file name to process, second argument to be printing method.
     */
    public static void main(String[] args) throws IOException {
        new LogFileProcessor(args);
    }


    /**
     * Hash maps used to store the pattern matches for IPv4 addresses.
     *      Keys : String of unique ip address
     *      Value : Integer count of instances of given key
     */
    private final HashMap<String, Integer> ipHashMap;
    /**
     * Hash maps used to store the pattern matches for usernames.
     *      Keys : String of unique username.
     *      Value : Integer count of instances of given key.
     */
    private final HashMap<String, Integer> userHashMap;

    /**
     * Name of file processed by the program.
     */
    private String fileName;

    /**
     * Total count of lines processed by the program for specified file
     */
    private int numLinesProcessed;
    /**
     * Total count of all references to an IP address.
     */
    private int numIPv4References;
    /**
     * Total count of all references to a username.
     */
    private int numUserReferences;

    /**
     * Constructor that takes in arguments from the command line, passed in through main and processes a log file.
     * @param args Command line arguments array of size 2, where element 1 is the file name and element 2 is the type of print method to perform.
     */
    LogFileProcessor(String [] args) throws IOException {
        // Gets first argument from command line call.
        // By Design the user needs to present the name of the file they wish to process.
        //
        fileName = args[0];

        // Conditional allows user to neglect the .log ending when entering the file name.
        // if it is absent from user input, append it to the end of the string.
        if(!fileName.endsWith(".log")) {
            fileName = fileName.concat(".log");
        }

        // Hash maps used to store the pattern matches
        // IP address and usernames found in log file are the keys of their respective maps
        // Count is the value of both maps
        ipHashMap = new HashMap<>();
        userHashMap = new HashMap<>();

        // Create a file reference to the .log file specified by user
        File logFile = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(fileName)); // user to read the file

        // Pattern uses to detect IPv4 Address
        // Such address come in the very general form:   w.x.y.z
        // Where w, x, y, and z take the place of a number with anywhere from 1 to 3 digits
        Pattern ipv4Pattern = Pattern.compile("([0-9]{1,3}\\.){3}([0-9]{1,3})");

        // Pattern used to detect users referenced in the log file
        // All user references follow the term "user "
        // The following pattern matches the entirety of "user <user_name>"
        // The prefix "user " will have to be stripped before storing.
        Pattern userPattern = Pattern.compile("user [a-zA-Z]+");

        // Counter for number of lines processed
        numLinesProcessed = 0 ;

        // Extra counts to find the number of references of both IP address and users
        // These are not the same as the unique counts because duplicates are present
        numIPv4References = 0;
        numUserReferences = 0;

        String line = br.readLine();
        while( line != null) {
            numLinesProcessed++;
            Matcher matcher = ipv4Pattern.matcher(line);

            // Find all IPv4 matches in line
            while ( matcher.find()) {
                String ipInstance = matcher.group();

                // Add instance to hash map with respect to already existing entries
                if( ipHashMap.containsKey(ipInstance)) ipHashMap.computeIfPresent(ipInstance, (key, value) -> value + 1); // recalculate value for key
                else ipHashMap.put(ipInstance, 1);
                numIPv4References++;
            }

            matcher = userPattern.matcher(line);

            // Find all user matches in line
            while ( matcher.find()) {
                String userInstance = matcher.group();
                String userName = userInstance.substring(5); // to strip prefix "user " from pattern match
                if( userName.equals("unknown")) continue; // do not add user "unknown"
                // "user unknown" does not reference a user in the system but the lack of one

                // Add instance to hash map with respect to already existing entries
                if( userHashMap.containsKey(userName)) userHashMap.computeIfPresent(userName, (key, value) -> value + 1); // recalculate value for key
                else userHashMap.put(userName, 1);

                numUserReferences++;
            }

            line = br.readLine();
        }

        /*
         * Design of command line arguments allows the user to use one of three output modes. If argument two is:
         *      0 - default output with general information about the file and its counts are printed.
         *      1 - unique IP addresses and their respective counts are printed
         *      2 - unique users and their respective counts are printed
         */

        if( args[1].equals("1")) printIPHashMap();
        else if( args[1].equals("2")) printUserHashMap();
        else printHashMapsOverview();

    }

    /**
     * Get the file name processed by the program.
     * @return fileName which is the file name passed in by user
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the total number of rows for the ip hash map (unique ip addresses from file).
     * @return the number of rows in the IP Hash Map
     */
    public int getIPHashSize() {
        return ipHashMap.keySet().size();
    }
    /**
     * Get the total number of rows for the user hash map (unique usernames from file).
     * @return the number of rows in the user Hash Map
     */
    public int getUserHashSize() {
        return userHashMap.keySet().size();
    }


    /**
     * Get the total number of lines processed for the designated file.
     * @return The total number of lines processed.
     */
    public int getNumLinesProcessed() {
        return numLinesProcessed;
    }
    /**
     * Get the total number of references to an IPv4 address in the file.
     * @return The total number of ip address references.
     */
    public int getNumIPv4References() {
        return numIPv4References;
    }
    /**
     * Get the total number of references to a username in the file.
     * @return The total number of username references.
     */
    public int getNumUserReferences() {
        return numUserReferences;
    }


    /**
     * Print all the unique IP address and their count.
     */
    public void printIPHashMap() {
        for( String key : ipHashMap.keySet()) {
            System.out.println(key +": " + ipHashMap.get(key));
        }
    }
    /**
     * Print all the unique usernames and their count.
     */
    public void printUserHashMap() {
        for( String key : userHashMap.keySet()) {
            System.out.println(key +": " + userHashMap.get(key));
        }
    }

    /**
     * Print general information about a file such as:
     *      number of lines processed,
     *      number of total ip references,
     *      number of total user references,
     *      number of unique ip address, and
     *      number of unique usernames.
     */
    public void printHashMapsOverview() {
        System.out.println(getNumLinesProcessed() + " lines in the log file (\"" + getFileName() + "\") were parsed.\n"+
                "Found " + getNumIPv4References() + " total ipv4 address references.\n" +
                "Found " + getNumUserReferences() + " total user references.\n" +

                "There are " + getIPHashSize() + " unique IPV4 addresses in the log.\n" +
                "There are " + getUserHashSize() + " unique users in the log.");
    }


}
