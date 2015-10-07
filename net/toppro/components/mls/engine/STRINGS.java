//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:49 PM
//

package net.toppro.components.mls.engine;


public class STRINGS   
{
    //+-----------------------------------------------------------------------------------+
    //|                                  String constants                                 |
    //+-----------------------------------------------------------------------------------+
    // common
    public static final int COMMON_ERR_BAD_DEF_FILE_SYNTAX = 0;
    // 0 - section name in a def-file
    // BoardSetup
    //UPGRADE_NOTE: Final was removed from the declaration of 'BOARD_SETUP_LOGIN_SETUP_CAPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int BOARD_SETUP_LOGIN_SETUP_CAPTION = COMMON_ERR_BAD_DEF_FILE_SYNTAX + 1;
    // 0 -
    // ClientFactory
    //UPGRADE_NOTE: Final was removed from the declaration of 'CLIENT_FACTORY_ERR_UNKNOWN_CLIENT_TYPE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int CLIENT_FACTORY_ERR_UNKNOWN_CLIENT_TYPE = BOARD_SETUP_LOGIN_SETUP_CAPTION + 1;
    // 0 - connection id; 1 - vendor name
    // CommunicationClient
    //UPGRADE_NOTE: Final was removed from the declaration of 'COMM_CLIENT_LOGIN_SETUP_SERVER_INFO_CAPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int COMM_CLIENT_LOGIN_SETUP_SERVER_INFO_CAPTION = CLIENT_FACTORY_ERR_UNKNOWN_CLIENT_TYPE + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'COMM_CLIENT_LOGIN_SETUP_ADDRESS_CAPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int COMM_CLIENT_LOGIN_SETUP_ADDRESS_CAPTION = COMM_CLIENT_LOGIN_SETUP_SERVER_INFO_CAPTION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'COMM_CLIENT_LOGIN_SETUP_PORT_CAPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int COMM_CLIENT_LOGIN_SETUP_PORT_CAPTION = COMM_CLIENT_LOGIN_SETUP_ADDRESS_CAPTION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'COMM_CLIENT_LOGIN_SETUP_LOGIN_INFO_CAPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int COMM_CLIENT_LOGIN_SETUP_LOGIN_INFO_CAPTION = COMM_CLIENT_LOGIN_SETUP_PORT_CAPTION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'COMM_CLIENT_LOGIN_SETUP_PASSWORD_CAPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int COMM_CLIENT_LOGIN_SETUP_PASSWORD_CAPTION = COMM_CLIENT_LOGIN_SETUP_LOGIN_INFO_CAPTION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'COMM_CLIENT_PREPARING_RECORDS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int COMM_CLIENT_PREPARING_RECORDS = COMM_CLIENT_LOGIN_SETUP_PASSWORD_CAPTION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'COMM_CLIENT_PROPERTY_SEARCH_DEFAULT_TIP '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int COMM_CLIENT_PROPERTY_SEARCH_DEFAULT_TIP = COMM_CLIENT_PREPARING_RECORDS + 1;
    // MLSCommand
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_COMMAND_ERR_NO_PARAM '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_COMMAND_ERR_NO_PARAM = COMM_CLIENT_PROPERTY_SEARCH_DEFAULT_TIP + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_COMMAND_ERR_EXPECTING_INT_PARAM '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_COMMAND_ERR_EXPECTING_INT_PARAM = MLS_COMMAND_ERR_NO_PARAM + 1;
    // MLSEngine
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_ENGINE_ERR_GET_PORT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_ENGINE_ERR_GET_PORT = MLS_COMMAND_ERR_EXPECTING_INT_PARAM + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_ENGINE_ERR_PORT_RANGE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_ENGINE_ERR_PORT_RANGE = MLS_ENGINE_ERR_GET_PORT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_ENGINE_ERR_DOWNLOADING_ZIP '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_ENGINE_ERR_DOWNLOADING_ZIP = MLS_ENGINE_ERR_PORT_RANGE + 1;
    // 0 - zip-file name.
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_ENGINE_ERR_UNABLE_TO_INSTALL_ZIP '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_ENGINE_ERR_UNABLE_TO_INSTALL_ZIP = MLS_ENGINE_ERR_DOWNLOADING_ZIP + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_ENGINE_WAITING_FOR_PREV_SCRIPT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_ENGINE_WAITING_FOR_PREV_SCRIPT = MLS_ENGINE_ERR_UNABLE_TO_INSTALL_ZIP + 1;
    // MLSException
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_EXCEPTION_ERR_SCRIPT_TIMEOUT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_EXCEPTION_ERR_SCRIPT_TIMEOUT = MLS_ENGINE_WAITING_FOR_PREV_SCRIPT + 1;
    // MLSRecord
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_RECORD_DEFAULT_CUSTOM_ROOM_NAME '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_RECORD_DEFAULT_CUSTOM_ROOM_NAME = MLS_EXCEPTION_ERR_SCRIPT_TIMEOUT + 1;
    // MLSRecordSet
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_RECORDSET_LOADING_PREV_SEARCH_RESULTS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_RECORDSET_LOADING_PREV_SEARCH_RESULTS = MLS_RECORD_DEFAULT_CUSTOM_ROOM_NAME + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_RECORDSET_ERR_PREPARING_PREV_RECORDS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_RECORDSET_ERR_PREPARING_PREV_RECORDS = MLS_RECORDSET_LOADING_PREV_SEARCH_RESULTS + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_RECORDSET_ERR_SAVING_PREPARED_RECORDS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_RECORDSET_ERR_SAVING_PREPARED_RECORDS = MLS_RECORDSET_ERR_PREPARING_PREV_RECORDS + 1;
    // MLSScriptClient
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_NO_LOGIN_INFO '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_NO_LOGIN_INFO = MLS_RECORDSET_ERR_SAVING_PREPARED_RECORDS + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_ELSE_WITHOUT_IF '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_ELSE_WITHOUT_IF = MLS_SCRIPT_CLIENT_NO_LOGIN_INFO + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_ENDIF_WITHOUT_IF '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_ENDIF_WITHOUT_IF = MLS_SCRIPT_CLIENT_ERR_ELSE_WITHOUT_IF + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_SCRIPT_PARSING '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_SCRIPT_PARSING = MLS_SCRIPT_CLIENT_ERR_ENDIF_WITHOUT_IF + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_UNABLE_TO_CONNECT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_UNABLE_TO_CONNECT = MLS_SCRIPT_CLIENT_ERR_SCRIPT_PARSING + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_UNKNOWN_COMMAND '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_UNKNOWN_COMMAND = MLS_SCRIPT_CLIENT_ERR_UNABLE_TO_CONNECT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_UNKNOWN_ERROR '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_UNKNOWN_ERROR = MLS_SCRIPT_CLIENT_ERR_UNKNOWN_COMMAND + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_DISCONNECT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_DISCONNECT = MLS_SCRIPT_CLIENT_ERR_UNKNOWN_ERROR + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_TRANSMIT_IO_ERROR '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_TRANSMIT_IO_ERROR = MLS_SCRIPT_CLIENT_ERR_DISCONNECT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT = MLS_SCRIPT_CLIENT_ERR_TRANSMIT_IO_ERROR + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_READ_IO '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_READ_IO = MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_UNKNOWN_LABEL '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_UNKNOWN_LABEL = MLS_SCRIPT_CLIENT_ERR_READ_IO + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_IO_DATA_RECEIVE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_IO_DATA_RECEIVE = MLS_SCRIPT_CLIENT_ERR_UNKNOWN_LABEL + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_SCRIPT_CLIENT_ERR_NO_CURRENT_RECORD '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_SCRIPT_CLIENT_ERR_NO_CURRENT_RECORD = MLS_SCRIPT_CLIENT_ERR_IO_DATA_RECEIVE + 1;
    // MLSUtil
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_UTIL_UNABLE_TO_COPY_FILE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_UTIL_UNABLE_TO_COPY_FILE = MLS_SCRIPT_CLIENT_ERR_NO_CURRENT_RECORD + 1;
    // 0 - src file name; 1 - dst filename
    // PropertyField
    //UPGRADE_NOTE: Final was removed from the declaration of 'PROPERTY_FIELD_ERR_UNABLE_TO_SET_VALUE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int PROPERTY_FIELD_ERR_UNABLE_TO_SET_VALUE = MLS_UTIL_UNABLE_TO_COPY_FILE + 1;
    // 0 - property name; 1 - value
    //UPGRADE_NOTE: Final was removed from the declaration of 'EXCEED_MAX_PARSE_LISINGS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int EXCEED_MAX_PARSE_LISINGS = PROPERTY_FIELD_ERR_UNABLE_TO_SET_VALUE + 1;
    //+-----------------------------------------------------------------------------------+
    //|                               String array constants                              |
    //+-----------------------------------------------------------------------------------+
    // common
    public static final int COMMON_EXECUTING_SCRIPT = 0;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_RECORD_NOTES_PREFIXES '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MLS_RECORD_NOTES_PREFIXES = COMMON_EXECUTING_SCRIPT + 1;
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
    private static final String[] STRINGS_Renamed_Field = new String[]{ "Bad syntax in the section [%0%] of the def-file.\nPlease contact Top Producer Technical Support at tchelp@topproducer.com.", "Login setup", "Unable to create communication module. Unsupported connection type %0% for '%1%'.", "Server information", "IP address", "IP port", "Login information", "Password ", "Preparing records ...", "Click on any field to see the help text", "MLS script error: the required parameter is missing. The def-file syntax is bad.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "MLS script error: expecting integer as a command parameter. The def-file syntax is bad.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "IP port value must be a number. Please go to board setup and check if IP port is entered properly.", "Port number is invalid. Please make sure that the port number in MLS Setup is correct. Click {MLSProductName} setup menu, then 'Login setup' and check help text for the correct port number.", "Unable to download and install package %0%", "Unable to install helper software for the MLS board.", "Cancelling previous download...", "Cannot continue search.  Expected response was not received from MLS server. Server may be temporarily unavailable or is not able to interpret search request. Please try again later or try to use different search criteria.", "Room", "Loading previous search results...", "Unable to read previous search results", "Unable to save search results", "MLS Login information does not exist.  Please enter your MLS login information in {MLSProductName} setup.", "Error parsing script in the def-file. 'Else' without 'if'.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "Error parsing script in the def-file. 'endif' without 'if'.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "Error parsing script in def-file.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "Cannot connect to MLS server.  Server may be temporarily unavailable.  Please try again later or contact your MLS system for more details.", "MLS script error: Unknown command.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "Internal script error\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "Error closing MLS board connection.", "Write error. Connection to MLS server is lost.", "MLS script error: bad parameter format.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "Read error.", "MLS script error: unknown label.\nPlease contact contact Top Producer Technical Support at tchelp@topproducer.com.", "Error receiving data from the MLS board.", "MLS script error: no current record.", "Unable to copy the file '%0%'.\nPlease try to select another one.", "Unable to set property '%0%' to '%1%'.", "Maximum number of %0% listing records set for this MLS was exceeded." };
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
    private static final String[][] STRING_ARRAYS = new String[][]{ new String[]{ "Searching...", "Downloading pictures...", "Downloading notes...", "Downloading extra notes/data..." }, new String[]{ "Listing ID: ", "House #", "Street name: ", "Suite #", "Listing type: ", "House style: ", "Area: ", "Bedrooms: ", "Bathrooms: ", "List Price: ", "Sale Price: ", "Listing date: ", "Sale date:", "Square feet: ", "Lot size: ", "Year build / Age: ", "DOM: ", "Features: ", "Notes: ", "Levels: ", "Parking: ", "Taxes: ", "Tax year: ", "Tax Assess: ", "City: ", "State: ", "ZIP code: ", "Listing expiry date: ", "Seller terms: ", "How to show: ", "Legal description: ", "Property type: ", "Directions: ", "Floor: ", "Building: ", "PO box: ", "Street dir prefix: ", "Street dir suffix: ", "Street designator: ", "County: ", "Country: ", "Tax roll no: ", "Zoning: ", "RV parking: ", "Dist to transit: ", "Construction: ", "Basement: ", "Heating: ", "Exterior: ", "Roof: ", "Attached: ", "Flooring: ", "Fireplace: ", "Last sold: ", "Sewer: ", "Water: ", "Dist to school: ", "AC: ", "Living room: ", "Dining room: ", "Kitchen: ", "Family room: ", "Master bedroom: ", "2nd bedroom: ", "3rd bedroom: ", "4th bedroom: ", "5th bedroom: ", "Office: ", "Den: ", "Great room: ", "Library: ", "Laundry: ", "Workshop: ", "Room1: ", "Room2: ", "Room3: ", "Room4: ", "Room5: ", "Room6: ", "Rooms amenities: ", "Views: ", "Listing agent company: ", "Agent house no: ", "Agent street name: ", "Agent floor: ", "Agent building: ", "Agent PO box: ", "Agent street dir prefix: ", "Agent street dir suffix: ", "Agent street designator: ", "Agent unit: ", "Agent city: ", "Agent county: ", "Agent state: ", "Agent zip: ", "Agent country: ", "Agent title: ", "Agent Jr/Sr: ", "Agent Mr/Ms: ", "Agent first name: ", "Agent middle name: ", "Agent last name: ", "Agent SSN: ", "Agent designation: ", "Agent home phone: ", "Agent home phone ext: ", "Agent business phone: ", "Agent business phone ext: ", "Agent other phone: ", "Agent other phone ext: ", "Agent mobile phone: ", "Agent mobile phone ext: ", "Agent other fax: ", "Agent other fax ext: ", "Agent email: ", "Agent website: ", "Agent ID: ", "Seller company: ", "Seller house no: ", "Seller street name: ", "Seller floor: ", "Seller building: ", "Seller PO box: ", "Seller street dir prefix: ", "Seller street dir suffix: ", "Seller street designator: ", "Seller unit: ", "Seller city: ", "Seller county: ", "Seller state: ", "Seller zip: ", "Seller country: ", "Seller title: ", "Seller Jr/Sr: ", "Seller Mr/Ms: ", "Seller first name: ", "Seller middle name: ", "Seller last name: ", "Seller SSN: ", "Seller designation: ", "Seller home phone: ", "Seller home phone ext: ", "Seller business phone: ", "Seller business phone ext: ", "Seller other phone: ", "Seller other phone ext: ", "Seller mobile phone: ", "Seller mobile phone ext: ", "Seller other fax: ", "Seller other fax ext: ", "Seller email: ", "Seller website: ", "Listing note: ", "Office ID: " } };
}


