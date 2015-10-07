//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:57 PM
//

package Tcs.Mls;


public class STRINGS   
{
    //+-----------------------------------------------------------------------------------+
    //|                                  String constants                                 |
    //+-----------------------------------------------------------------------------------+
    // MLSException
    public static final int MLS_EXCEPTION_ERR_SCRIPT_TIMEOUT = 0;
    // TCSException
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_TIMEOUT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_TIMEOUT = MLS_EXCEPTION_ERR_SCRIPT_TIMEOUT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_SOURCE_NULL '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_SOURCE_NULL = TCS_EXCEPTION_ERR_SCRIPT_TIMEOUT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_SOURCE_EXCEPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_SOURCE_EXCEPTION = TCS_EXCEPTION_ERR_SCRIPT_SOURCE_NULL + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_SOURCE_MLSEXCEPTION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_SOURCE_MLSEXCEPTION = TCS_EXCEPTION_ERR_SCRIPT_SOURCE_EXCEPTION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_SOURCE_APPLICATION '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_SOURCE_APPLICATION = TCS_EXCEPTION_ERR_SCRIPT_SOURCE_MLSEXCEPTION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_NUL_ERROR_CODE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_NUL_ERROR_CODE = TCS_EXCEPTION_ERR_SCRIPT_SOURCE_APPLICATION + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_GET_LOOK_UP_TABLE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_GET_LOOK_UP_TABLE = TCS_EXCEPTION_ERR_SCRIPT_NUL_ERROR_CODE + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_STATUS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_STATUS = TCS_EXCEPTION_ERR_SCRIPT_GET_LOOK_UP_TABLE + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_PROGRESS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_PROGRESS = TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_STATUS + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_SAVE_TASK_RESULT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_SAVE_TASK_RESULT = TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_PROGRESS + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_GET_MESSAGE_BODY '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_GET_MESSAGE_BODY = TCS_EXCEPTION_ERR_SCRIPT_SAVE_TASK_RESULT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_GET_DEF '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_GET_DEF = TCS_EXCEPTION_ERR_SCRIPT_GET_MESSAGE_BODY + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXCEPTION_ERR_SCRIPT_SAVE_TASK_FILE_RESULT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXCEPTION_ERR_SCRIPT_SAVE_TASK_FILE_RESULT = TCS_EXCEPTION_ERR_SCRIPT_GET_DEF + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'NOT_SURPPORT_CREDENTIAL_CHECK '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int NOT_SURPPORT_CREDENTIAL_CHECK = TCS_EXCEPTION_ERR_SCRIPT_SAVE_TASK_FILE_RESULT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'INVALID_SEARCH_FIELD_FORMAT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int INVALID_SEARCH_FIELD_FORMAT = NOT_SURPPORT_CREDENTIAL_CHECK + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'NO_DEF_FILE_MATCH '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int NO_DEF_FILE_MATCH = INVALID_SEARCH_FIELD_FORMAT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'NOT_SURPPORT_REQUEST_SEARCH_FIELD '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int NOT_SURPPORT_REQUEST_SEARCH_FIELD = NO_DEF_FILE_MATCH + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'REQUIRED_FIELD_NOT_SUPPLY '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int REQUIRED_FIELD_NOT_SUPPLY = NOT_SURPPORT_REQUEST_SEARCH_FIELD + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'NOT_SURPPORT_RECORD_COUNT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int NOT_SURPPORT_RECORD_COUNT = REQUIRED_FIELD_NOT_SUPPLY + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'INVALID_USERNAME_PASSWORD '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int INVALID_USERNAME_PASSWORD = NOT_SURPPORT_RECORD_COUNT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED = INVALID_USERNAME_PASSWORD + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED = MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'BOARD_TEMP_UNAVAILABLE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int BOARD_TEMP_UNAVAILABLE = MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'BOARD_UNAVAILABLE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int BOARD_UNAVAILABLE = BOARD_TEMP_UNAVAILABLE + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'LOOKUP_CODE_FORMAT_NOT_EXIST '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int LOOKUP_CODE_FORMAT_NOT_EXIST = BOARD_UNAVAILABLE + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'RESULT_EXCEED_LIMITED_SIZE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int RESULT_EXCEED_LIMITED_SIZE = LOOKUP_CODE_FORMAT_NOT_EXIST + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MINIMUM_MLS_CREDENTIAL_NOT_SUPPLIED '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MINIMUM_MLS_CREDENTIAL_NOT_SUPPLIED = RESULT_EXCEED_LIMITED_SIZE + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'TCS_EXECUTIVE_TIMEOUT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int TCS_EXECUTIVE_TIMEOUT = MINIMUM_MLS_CREDENTIAL_NOT_SUPPLIED + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'EXCEED_MAX_PARSE_LISTINGS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int EXCEED_MAX_PARSE_LISTINGS = TCS_EXECUTIVE_TIMEOUT + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'INTERNAL_ERROR '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int INTERNAL_ERROR = EXCEED_MAX_PARSE_LISTINGS + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'NOT_SURPPORT_SEARCH_FIELD '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int NOT_SURPPORT_SEARCH_FIELD = INTERNAL_ERROR + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MISCELLANEOUS_MLS_ERROR '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MISCELLANEOUS_MLS_ERROR = NOT_SURPPORT_SEARCH_FIELD + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'NO_RECORD_FOUND '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int NO_RECORD_FOUND = MISCELLANEOUS_MLS_ERROR + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'BOARD_NOT_COMPATIBLE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int BOARD_NOT_COMPATIBLE = NO_RECORD_FOUND + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'DATA_STANDARDIZATION_APPLIED '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int DATA_STANDARDIZATION_APPLIED = BOARD_NOT_COMPATIBLE + 1;
    public static final int NOT_ALLOW_MULTIPLE_VALUES = DATA_STANDARDIZATION_APPLIED + 1;
    public static final int BOARD_NOT_EXIST = NOT_ALLOW_MULTIPLE_VALUES + 1;
    public static final int RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT = BOARD_NOT_EXIST + 1;
    public static final int BOTH_RECORDLIMIT_OVERRIDERECORDLIMIT_USED = RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT + 1;
    public static final int OVERRIDERECLIMIT_ISLOWERTHAN_MAXRECLIMIT = BOTH_RECORDLIMIT_OVERRIDERECORDLIMIT_USED + 1;
    public static final int OVERRIDERECLIMIT_ISNOTALLOWED = OVERRIDERECLIMIT_ISLOWERTHAN_MAXRECLIMIT + 1;
    public static final int PARSINGLIMIT_MAXRECLIMIT_BOTH_USED = OVERRIDERECLIMIT_ISNOTALLOWED + 1;
    public static final int OVERRIDERECLIMIT_DATENOTSUPPLIED = PARSINGLIMIT_MAXRECLIMIT_BOTH_USED + 1;
    public static final int OVERRIDERECLIMIT_DEFNOTHAVEDATEFIELD = OVERRIDERECLIMIT_DATENOTSUPPLIED + 1;
    public static final int NONMLS_SEARCH_PARA_IN_STANDARD_SEARCH = OVERRIDERECLIMIT_DEFNOTHAVEDATEFIELD + 1;
    public static final int METADATA_VERSION_NOT_MATCH = NONMLS_SEARCH_PARA_IN_STANDARD_SEARCH + 1;
    public static final int NOT_SURPPORT_POSTALFSA_FIELD = METADATA_VERSION_NOT_MATCH + 1;
    public static final int BOTH_LASTMODIFIED_PICMODIFIED_USED = NOT_SURPPORT_POSTALFSA_FIELD + 1;
    public static final int BOTH_LASTMODIFIED_PICMODIFIED_EMPTY = BOTH_LASTMODIFIED_PICMODIFIED_USED + 1;
    public static final int EXCEED_MAX_PIC_NUMBER_100 = BOTH_LASTMODIFIED_PICMODIFIED_EMPTY + 1;
    public static final int EXCEED_MAX_PIC_NUMBER_1000 = EXCEED_MAX_PIC_NUMBER_100 + 1;
    public static final int CANNOT_SEARCH_BYBOTH_MODULEID_BOARDID = EXCEED_MAX_PIC_NUMBER_1000 + 1;
    public static final int CODE_QUERY_LIMIT_EXCEEDED = CANNOT_SEARCH_BYBOTH_MODULEID_BOARDID + 1;
    public static final int EXCEED_MAX_LISITNG_NUMBER = CODE_QUERY_LIMIT_EXCEEDED + 1;
    public static final int LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH = EXCEED_MAX_LISITNG_NUMBER + 1;
    public static final int MLS_METADATA_VERSION_NOT_MATCH = LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH + 1;
    public static final int EXCEED_MAX_MLSNO_SEARCH_NUMBER = MLS_METADATA_VERSION_NOT_MATCH + 1;
    public static final int AGENT_OFFICE_ROSTER_DEF_NOT_EXIST = EXCEED_MAX_MLSNO_SEARCH_NUMBER + 1;
    public static final int CHUNKING_FIELD_NOTDEFINED = AGENT_OFFICE_ROSTER_DEF_NOT_EXIST + 1;
    public static final int BOTH_STATUS_PUBLICSTATUS_USED = CHUNKING_FIELD_NOTDEFINED + 1;
    public static final int OFFMARKET_STATUS_NOT_DEFINED_INDEF = BOTH_STATUS_PUBLICSTATUS_USED + 1;
    public static final int CHUNKING_FAILED = OFFMARKET_STATUS_NOT_DEFINED_INDEF + 1;
    public static final int DUPLICATE_SEARCH_CODE_RETS_PARAMETER = CHUNKING_FAILED + 1;
    public static final int BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED = DUPLICATE_SEARCH_CODE_RETS_PARAMETER + 1;
    public static final int CONDITIONCODE_NOT_IMPLEMENTED_INDEF = BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED + 1;
    public static final int OPENHOUSE_DEF_NOT_EXIST = CONDITIONCODE_NOT_IMPLEMENTED_INDEF + 1;
    public static final int WRITE_RESULT_FLAG_FILE_FAILED = OPENHOUSE_DEF_NOT_EXIST + 1;
    public static final int RETS_CLASS_NOT_EXISTS = WRITE_RESULT_FLAG_FILE_FAILED + 1;
    public static final int RETS_CLASS_NOT_AVAILABLE = RETS_CLASS_NOT_EXISTS + 1;
    public static final int UNABLE_TO_USE_RETS_CLASS_NON_RETS = RETS_CLASS_NOT_AVAILABLE + 1;
    public static final int OpenHouse_MLSNumber_NotFound = UNABLE_TO_USE_RETS_CLASS_NON_RETS + 1;
    public static final int OpenHouse_NotAvailableTo_MlsNumberSearch = OpenHouse_MLSNumber_NotFound + 1;
    public static final int INVALID_AGENTID = OpenHouse_NotAvailableTo_MlsNumberSearch + 1;
    public static final int INVALID_PROPERTY_TYPE = INVALID_AGENTID + 1;
    public static final int NoMappingForResidentailClassFound = INVALID_PROPERTY_TYPE + 1;
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
    private static final String[] STRINGS_Renamed_Field = new String[]{ "Timeout Error from MLS Exception.", "Timeout Error from TCS Exception.", "TCS Exception with unkown source.", "TCS Exception re-thrown from Exception.", "TCS Exception re-thrown from MLSException.", "TCS Exception thrown in TSCExcutive.", "See Message. (This error thrown from MLSException or Exception)", "TCS Error when calling BLL getLookUpTable().", "TCS Error when calling BLL updateTaskStatus().", "TCS Error when calling BLL updateTaskProgress().", "TCS Error when calling BLL SaveTaskResult().", "TCS Error when calling BLL getMessageBody().", "TCS Error when calling BLL getDEF().", "TCS Error when calling BLL SaveTaskFileResult()).", "MLS Credential Validation is not supported", "Invalid search field: ", "Requested Property Type does not exist on the MLS: ", "Required search field does not exist in DEF file: %0% - %1%.", "Missing required search fields: ", "Record count is not supported by the MLS", "Authorization failed: Invalid MLS username or password or incorrect vendor authorization", "Minimum search criteria required by the MLS were not supplied in the request - ", "Minimum request search parameter is not supplied: ", "Board/Module is temporarily unavailable. Board ID: ", "Board/Module is unavailable. Board ID: ", "DEF Syntax Error – Format does not exist for a field that requires lookup code - ", "The size of MLS data or pictures exceeded 3000KB limit", "The minimum credentials required by the MLS were not provided in the request", "TCS timeout 2hr exceeded by the request", "Maximum number of %0% listing records set for this MLS was exceeded", "Internal Error - Critical ", "Supplied search field(non-required) does not exist in the DEF file: ", "Miscellaneous MLS Error: ", "No Records Found", "The specified board/module is not compatible with this request", "Data standardization rule was used in field: %0%", "This Top Connector module does not allow searching by multiple values in field", "Board ID supplied in the request does not exist", "Records Limit is greater than Maximum Records Limit for this MLS", "Using both Records Limit and Override Records Limit in the same request is not allowed", "Override is lower than Maximum Records Limit for this MLS. Maximum Records Limit will be returned", "Override records limit search parameter was specified but override is not allowed by this MLS", "Critical DEF Syntax error. Parsing Limit and Maximum Records Limit are both used in the DEF file", "A date search criteria required for request chunking was not supplied. By default 10 years back was searched.", "Internal error Critical - DEF file does not have a {0} date required for request chunking", "Invalid search request. Non-MLS search parameter(s) were supplied in Standard search group.", "The metadata version parameter in this request does not match the metadata version in TCS", "Supplied Postal FSA search field does not exist in the DEF file.", "Cannot search by both Last Modified Date and Picture Modified date.", "To search, Last Modified Date OR Picture Modified Date must be used.", "When requesting ALL pictures per listing, the maximum number of listings is 100", "When requesting ONE picture per listing, the maximum number of listings is 1000", "Cannot search by both BoardID and ModuleID", "Query limit exceeded", "The total number of records in search results exceeded the maximum limit of 10,000 records", "Chunking failed. The total number of records in the smallest interval possible, exceeded the maximum number of records allowed by MLS for download.", "MLS RETS Metadata version mismatch. Client version: {0}. MLS version: {1}", "The maximum of 100 MLS numbers per request was exceeded. Please submit several requests instead", "Unable to download Agent or Office Roster - the required DEF file is not available/does not exist in TCS.", "Advanced Chunking field could not be found in DEF file.", "Cannot use both PublicListingStatus and Status in search request, please specify only one criteria.", "Cannot search by Off Market and Other(O) - status is not defined in the DEF file.", "Chunked search failed. One or more sub-searches did not complete successfully.", "RETS Query Parameter conflicts with the existing RETS query in the DEF file.", "Cannot have both RETS Query Parameter and Condition Code in the same request.", "Condition code is not implemented in the DEF file", "Unable to download Open House - the required DEF file is not available/does not exist in TCS.", "TCS was unsuccessful to deliver the Flag File on request completion - cannot write flag file", "One or more RETSClass could not be found", "One or more of the specified RETSClass is not available to this client", "Unable to use RETSClass parameter -- non-RETS module", "No matching MLSNumbers are found for those Property IDs:", "This TCS module does not support MLS Number search in Open House request", "MLS Authorization successful but Agent ID is invalid", "Invalid property type in the search fields", "No matching residential class is found" };
}


//METADATA_VERSION_NOT_MATCH
//NOT_SURPPORT_POSTALFSA_FIELD
//BOTH_LASTMODIFIED_PICMODIFIED_USED
//BOTH_LASTMODIFIED_PICMODIFIED_EMPTY
//EXCEED_MAX_PIC_NUMBER
//EXCEED_MAX_PIC_NUMBER_1000
//CANNOT_SEARCH_BYBOTH_MODULEID_BOARDID
//CODE_QUERY_LIMIT_EXCEEDED
//EXCEED_MAX_LISITNG_NUMBER
//LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH
//MLS_METADATA_VERSION_NOT_MATCH
//EXCEED_MAX_MLSNO_SEARCH_NUMBER
//AGENT_OFFICE_ROSTER_DEF_NOT_EXIST
//CANNOT_SEARCH_BYBOTH_MODULEID_BOARDID
//CHUNKING_FIELD_NOTDEFINED
//BOTH_STATUS_PUBLICSTATUS_USED
//CHUNKING_FAILED
//DUPLICATE_SEARCH_CODE_RETS_PARAMETER
//BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED
//CONDITIONCODE_NOT_IMPLEMENTED_INDEF
//OPENHOUSE_DEF_NOT_EXIST
//WRITE_RESULT_FLAG_FILE_FAILED
//RETS_CLASS_NOT_EXISTS
//REST_CLASS_NOT_AVAILABLE
//UNABLE_TO_USE_RETS_CLASS_NON_RETS
//OpenHouse_MLSNumber_NotFound
//OpenHouse_MLSNumber_NotFound
//INVALID_AGENTID
//INVALID_PROPERTY_TYPE
//NoMappingForResidentailClassFound