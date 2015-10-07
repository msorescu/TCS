//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:39 PM
//

package net.toppro.components.mls.engine.client;


public class STRINGS   
{
    public static final int ERROR_DOWNLOAD_PIC = 0;
    public static final int ERROR_BAD_USERNAME_OR_PWD = 1;
    //+-----------------------------------------------------------------------------------+
    //|                                    RETSClient                                     |
    //+-----------------------------------------------------------------------------------+
    //Login
    public static final int ERROR_RETS_20003 = 2;
    public static final int ERROR_RETS_20012 = 3;
    public static final int ERROR_RETS_20013 = 4;
    public static final int ERROR_RETS_20050 = 5;
    //Search
    public static final int ERROR_RETS_20200 = 6;
    public static final int ERROR_RETS_20201 = 7;
    public static final int ERROR_RETS_20202 = 8;
    public static final int ERROR_RETS_20206 = 9;
    public static final int ERROR_RETS_20207 = 10;
    public static final int ERROR_RETS_20208 = 11;
    public static final int ERROR_RETS_20209 = 12;
    public static final int ERROR_RETS_20210 = 13;
    public static final int ERROR_RETS_20514 = 14;
    //GetObject
    public static final int ERROR_RETS_20400 = 15;
    public static final int ERROR_RETS_20401 = 16;
    public static final int ERROR_RETS_20402 = 17;
    public static final int ERROR_RETS_20403 = 18;
    public static final int ERROR_RETS_20406 = 19;
    public static final int ERROR_RETS_20407 = 20;
    public static final int ERROR_RETS_20408 = 21;
    public static final int ERROR_RETS_20409 = 22;
    public static final int ERROR_RETS_20410 = 23;
    public static final int ERROR_RETS_20411 = 24;
    public static final int ERROR_RETS_20412 = 25;
    //+-----------------------------------------------------------------------------------+
    //|                                       Misc                                        |
    //+-----------------------------------------------------------------------------------+
    // Sergei. These strings require more ordering.
    // Notes:
    // 1. S7_0 is the same as net.toppro.components.mls.engine.ERR_SCRIPT_TIMEOUT
    //    we need to investigate, how to use it directly
    // 2. EXECUTING_SCRIPT is the same as net.toppro.components.mls.engine.EXECUTING_SCRIPT
    //    we need to investigate, how to use it directly
    // 3. May be some more duplicates exist. Should be investigated too.
    public static final int S31 = 26;
    public static final int S36 = 27;
    public static final int ERR_BAD_RESULTS_FORMAT = 28;
    public static final int S11 = 29;
    public static final int S37 = 30;
    public static final int S20 = 31;
    public static final int S32 = 32;
    public static final int S29 = 33;
    public static final int S28 = 34;
    public static final int S7_0 = 35;
    public static final int S10 = 36;
    public static final int WYLDFYRE_DEFAULT_TOOLTIP = 37;
    public static final int S21 = 38;
    public static final int EXECUTING_SCRIPT = 0;
    public static final int DB_CLIENT_LOGIN_SETUP_CAPTION = 39;
    public static final int DBF_CLIENT_DATABASE_INFO_CAPTION = 40;
    public static final int DBF_CLIENT_SELECT_DATABASE_CAPTION = 41;
    public static final int DBF_CLIENT_SELECT_PICTURES_FOLDER_CAPTION = 42;
    public static final int ERR_DEMO_CLIENT_UNABLE_TO_GET_RECORDS = 43;
    public static final int ERR_ERROR_WRONGURL = 44;
    public static final int MSSQL_SERVER_NAME_CAPTION = 45;
    public static final int RECEIVING = 46;
    public static final int RECEIVING_NO_TOTAL = 47;
    public static final int ERR_WYLD_FYRE_SEARCH_CANCELED = 48;
    public static final int WYLDFYRE_RUNNING = 49;
    public static final int WYLDFYRE_DOWNLOADING = 50;
    public static final int SCRIPT_CLIENT_CANNOT_CONNECT = 51;
    public static final int ERR_DATA_FILE_NOT_FOUND = 52;
    public static final int ERR_BAD_PICTURE_FORMAT = 53;
    public static final int ERR_MSSQL_TOO_MANY_RECORDS = 54;
    public static final int ERR_MSSQL_BAD_TABLE = 55;
    public static final int ERR_HTTP_404 = 56;
    public static final int ERR_HTTP_405 = 57;
    public static final int ERR_WRONG_PICTURE_SIZE = 58;
    public static final int ERR_EXCEED_MAX_DOWNLOAD_SIZE = 59;
    //+-----------------------------------------------------------------------------------+
    //|                                      STRINGS                                      |
    //+-----------------------------------------------------------------------------------+
    /// <summary> We don't referense strings directly, because optimizer links them constantly and staticly
    /// to the class which uses them. So when we replace STRINGS with another class conrtaining
    /// different strings (for example using another language), we don't achive our goal.
    /// Strings remain the same unless we recompile all the classes that use them.
    /// <p>To avoid static linkage we access strings indirectly  - through this function, which
    /// simply gets them from strings array by index.
    /// <p>Some parts of iTP use the inheritance from java.util.ListResourceBundle to achive the same
    /// goal (for example net.toppro.tponline.itp.ServiceReports.Common.STRINGS). We think it takes
    /// too much resources for such a simple problem. It actually puts all the strings to Hashtable
    /// using string keys. So the number of strings doubles.
    /// <p>Another disadvantage of resource bundles is late linkage. If we misspell resource name, compiler
    /// can't help us to find an error, and it goes to runtime. This solution uses early linkage. The parameter
    /// is variable name and compiler can check it, preventing misspellinig errors at the compile time.
    /// </summary>
    /// <param name="id">one of the integer constants specified in this class. Id of the string.
    /// </param>
    /// <returns> string by id.
    /// </returns>
    public static String get_Renamed(int id) throws Exception {
        return STRINGS_Renamed_Field[id];
    }

    //UPGRADE_NOTE: Final was removed from the declaration of 'STRINGS'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    private static final String[] STRINGS_Renamed_Field = new String[]{ "The following error occured when downloading picture for \nMLS record.\n\n", "The username or password entered in the Login Setup screen is not valid.  Please verify your username and password.  If the problem still persists, please contact your MLS.\n", "Zero Balance.", "Broker Code Required.", "Broker Code Invalid.", "Server Temporarily Disabled.", "Unknown Query Field.", "No Records Found.", "Invalid Select.", "Invalid Query Syntax.", "Unauthorized Query.", "Maximum Records Exceeded.", "Timeout.", "Too many outstanding queries.", "Requested DTD version unavailable.", "Invalid Resource.", "Invalid Type.", "Invalid Identifier.", "No Object Found. \nPictures for one or more of selected properties could not be downloaded.  Please check if pictures are available for the listings and try again.", "Unsupported MIME type.", "Unauthorized Retrieval.", "Resource Unavailable.", "Object Unavailable.", "Request Too Large.", "Timeout.", "Too many outstanding requests.", "The location of the MLS data file is required for file import.  Please select the location.", "MLS data file cannot be found. Please select the correct file by clicking 'Select MLS data file'.", "Data cannot be found in the exported MLS file.  Changes by your MLS board may be a possible reason.  If the problem persists, please contact Top Producer Technical Support at tchelp@topproducer.com.", "Connecting...", "MLS picture folder cannot be found. Please select the correct folder by clicking 'Select MLS pictures folder'.", "Result file not found", "Can not complete search or no matches were found. The following error was received from the MLS board. Please modify your search criteria and try again or contact your MLS board for more information.\n", "Cannot update client configurations.", "Updating client configurations...", "Cannot continue search.  Expected response was not received from MLS server. Server may be temporarily unavailable or is not able to interpret search request. Please try again later or try to use different search criteria.", "No matches found. Please change search parameters and try again.", "{ProductName} will now download listings from your WyldFyre software. Please make sure you have the WyldFyre software installed before you attempt to import information.\nClick Next to be brought to the WyldFyre search window.  After the search is finished, you will see the results in {ProductName}.", "File is corrupted", "Database setup for %0%", "MLS database information", "Select database folder", "Select pictures folder", "Unable to get demo records.", "Invalid URL string for this request.", "Server name", "Receiving %0% records...", "Receiving...", "WyldFyre import was canceled", "Running WyldFyre...", "Checking WyldFyre version...", "Cannot connect to MLS server.  Server may be temporarily unavailable.  Please try again later or contact your MLS system for more details.", "Unable to open the data file with the received data.\nPlease close all other instances of {ApplicationName} and try again.", "Bad picture format", "This download exceeds the 1500 record limit. \nPlease revise your search criteria and try again.", "Bad table format", "Object not found", "Resource not allowed", "Pictures for one or more of selected listings were not fully received.\nYou may have a firewall installed on your computer or network.\n Another possible reason is interruption in Internet conntection. Please disable firewall \nwhen getting pictures or simply retry getting the same pictures again.", "The size of MLS data file exceeded 50 MB limit." };
    /**
    * Works similar to get ( id ), but gets values from string arrays. Sometimes it's necessary
    * to keep resources in string arrays and access strings by index.
    * 
    *  @param id one of the integer constants specified in this class. Id of the string array.
    * 
    *  @param index index in the array.
    * 
    *  @return  string from the string array with the specified id by the specified index.
    */
    public static String get_Renamed(int id, int index) throws Exception {
        return STRING_ARRAYS[id][index];
    }

    /**
    * Works similar to get ( id ), but gets string array. Sometimes it's necessary
    * to keep resources in string arrays and access strings by index.
    * 
    *  @param id one of the integer constants specified in this class. Id of the string array.
    * 
    *  @return  string array with the specified id.
    */
    public static String[] getArray(int id) throws Exception {
        return STRING_ARRAYS[id];
    }

    //UPGRADE_NOTE: Final was removed from the declaration of 'STRING_ARRAYS'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    private static final String[][] STRING_ARRAYS = new String[][]{ new String[]{ "Searching...", "Downloading pictures...", "Downloading notes..." } };
}


