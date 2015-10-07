//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:57 PM
//

package Tcs.Mls;

import java.net.InetAddress;
import Tcs.Mls.TCSException;

public class TCSException  extends MLSException 
{
    public String getMessage() throws Exception {
        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
        String msg = super.Message;
        if (msg == null)
            msg = "";
         
        return msg;
    }

    public String getErrorMessage() throws Exception {
        return m_errorMessage;
    }

    public void setErrorMessage(String value) throws Exception {
        m_errorMessage = value;
    }

    // Sources of TCSException
    public static final int TCSEXCEPTION_SOURCE_NULL = 0;
    public static final int TCSEXCEPTION_SOURCE_EXCEPTION = 1;
    public static final int TCSEXCEPTION_SOURCE_MLSEXCEPTION = 2;
    public static final int TCSEXCEPTION_SOURCE_APPLICATION = 3;
    private String m_errorMessage = "";
    // Error Codes for TCSEXCEPTION_SOURCE_APPLICATION
    public static final int NUL_ERROR_CODE = 0;
    public static final int BLL_ERROR_GET_LOOK_UP_TABLE = 60020;
    public static final int BLL_ERROR_UPDATE_TASK_STATUS = 60030;
    public static final int BLL_ERROR_UPDATE_TASK_PROGRESS = 60040;
    public static final int BLL_ERROR_SAVE_TASK_RESULT = 60050;
    public static final int BLL_ERROR_GET_MESSAGE_BODY = 60060;
    public static final int BLL_ERROR_GET_DEF = 60070;
    public static final int BLL_ERROR_SAVE_TASK_FILE_RESULT = 60080;
    public static final int PARSINGLIMIT_MAXRECLIMIT_BOTH_USED = 60150;
    public static final int ERROR_MLS_SEARCH = 60160;
    public static final int MLS_ERROR_UNABLE_TO_CONNECT = 60170;
    public static final int MLS_ERROR_DEF_PARSING_ERROR = 60180;
    public static final int ERROR_CODE_JAVA_EXCEPTION = 60190;
    public static final int INTERNAL_ERROR = 60200;
    public static final int MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED = 60210;
    public static final int INVALID_PROPERTY_TYPE = 60211;
    public static final int NOT_SURPPORT_CREDENTIAL_CHECK = 60220;
    public static final int INVALID_AGENTID = 60221;
    public static final int NOT_SURPPORT_RECORD_COUNT = 60230;
    public static final int NOT_SURPPORT_REQUEST_SEARCH_FIELD = 60240;
    public static final int BOARD_TEMP_UNAVAILABLE = 60250;
    public static final int BOARD_UNAVAILABLE = 60260;
    public static final int MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED = 60270;
    public static final int RESULT_EXCEED_LIMITED_SIZE = 60280;
    public static final int MLS_ERROR_TIMEOUT = 60290;
    public static final int EXCEED_MAX_PIC_NUMBER = 60291;
    public static final int MINIMUM_MLS_CREDENTIAL_NOT_SUPPLIED = 60300;
    public static final int MLS_ERROR_INVALID_USERNAME_PASSWORD = 60310;
    public static final int MISCELLANEOUS_MLS_ERROR = 60320;
    public static final int NO_DEF_FILE_MATCH = 60330;
    public static final int TCS_EXECUTIVE_TIMEOUT = 60340;
    public static final int WRITE_RESULT_FLAG_FILE_FAILED = 60341;
    public static final int EXCEED_MAX_DOWNLOAD_SIZE = 60350;
    public static final int BOARD_NOT_COMPATIBLE = 60360;
    public static final int DATA_STANDARDIZATION_APPLIED = 60370;
    public static final int RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT = 60380;
    public static final int BOTH_RECORDLIMIT_OVERRIDERECORDLIMIT_USED = 60390;
    public static final int BOTH_LASTMODIFIED_PICMODIFIED_USED = 60392;
    public static final int BOTH_LASTMODIFIED_PICMODIFIED_EMPTY = 60393;
    public static final int CANNOT_SEARCH_BYBOTH_MODULEID_BOARDID = 60394;
    public static final int EXCEED_MAX_PIC_NUMBER_100 = 60395;
    public static final int EXCEED_MAX_PIC_NUMBER_1000 = 60396;
    public static final int EXCEED_MAX_LISITNG_NUMBER = 60397;
    public static final int LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH = 60398;
    public static final int EXCEED_MAX_MLSNO_SEARCH_NUMBER = 60399;
    public static final int AGENT_OFFICE_ROSTER_DEF_NOT_EXIST = 60321;
    public static final int OPENHOUSE_DEF_NOT_EXIST = 60322;
    public static final int BOTH_STATUS_PUBLICSTATUS_USED = 60381;
    public static final int OFFMARKET_STATUS_NOT_DEFINED_INDEF = 60382;
    public static final int CHUNKING_FAILED = 60383;
    public static final int DUPLICATE_SEARCH_CODE_RETS_PARAMETER = 60384;
    public static final int BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED = 60385;
    public static final int CONDITIONCODE_NOT_IMPLEMENTED_INDEF = 60386;
    public static final int RETS_CLASS_NOT_EXISTS = 60387;
    public static final int OpenHouse_NotAvailableTo_MlsNumberSearch = 60388;
    public static final int RETS_CLASS_NOT_AVAILABLE = 60838;
    public static final int UNABLE_TO_USE_RETS_CLASS_NON_RETS = 60839;
    public static final int MLS_ERROR_NO_USERNAME_PASSWORD = 60405;
    public static final int MLS_ERROR_NO_RECORD_FOUND = 60510;
    public static final int MLS_ERROR_UNABLE_COMPLETE_SEARCH = 60510;
    public static final int ONE_OR_MORE_PICTURE_NOT_FOUND = 60520;
    public static final int RETRY_FAILED = 60521;
    public static final int OpenHouse_MLSNumber_NotFound = 60522;
    public static final int NOT_SURPPORT_SEARCH_FIELD = 60530;
    public static final int MLS_ENGINE_NOTE = 60540;
    public static final int OVERRIDERECLIMIT_ISLOWERTHAN_MAXRECLIMIT = 60550;
    public static final int OVERRIDERECLIMIT_ISNOTALLOWED = 60560;
    public static final int OVERRIDERECLIMIT_DATENOTSUPPLIED = 60570;
    public static final int NOT_SURPPORT_POSTALFSA_FIELD = 60580;
    public static final int INVALID_REQUEST = 60600;
    public static final int INVALID_SEARCH_FIELD_FORMAT = 60601;
    public static final int LOOKUP_CODE_FORMAT_NOT_EXIST = 60610;
    public static final int BOARD_NOT_EXIST = 60620;
    public static final int NOT_ALLOW_MULTIPLE_VALUES = 60630;
    public static final int SEARCH_FIELD_DUPLICATE = 60910;
    public static final int NONMLS_SEARCH_PARA_IN_STANDARD_SEARCH = 60920;
    public static final int METADATA_VERSION_NOT_MATCH = 60930;
    public static final int MLS_METADATA_VERSION_NOT_MATCH = 60931;
    public static final int MLS_METADATA_NOT_FOUND = 60932;
    public static final int CHUNKING_FIELD_NOTDEFINED = 60933;
    public static final int NoMappingForResidentailClassFound = 60934;
    //public const int CODE_QUERY_LIMIT_EXCEEDED = 20210;
    private int m_source;
    private int m_errorCode;
    public TCSException(Exception exc, String message, String developer_message) throws Exception {
        super(exc, message, developer_message, 0);
        //text format
        m_source = TCSEXCEPTION_SOURCE_NULL;
        m_errorCode = NUL_ERROR_CODE;
    }

    public TCSException(Exception exc, String message, String developer_message, int source, int errorCode) throws Exception {
        super(exc, message, developer_message, 0);
        //text format
        m_source = source;
        m_errorCode = errorCode;
    }

    public static TCSException produceTCSException(String message, String developer_message, int errorCode) throws Exception {
        return new TCSException(null,message,developer_message,TCSEXCEPTION_SOURCE_APPLICATION,errorCode);
    }

    public static TCSException produceTCSException(Exception exc) throws Exception {
        if (exc instanceof TCSException)
            return (TCSException)exc;
         
        if (exc instanceof MLSException)
        {
            //StringWriter sw = new StringWriter ();
            //PrintWriter pw = new PrintWriter ( sw );
            //exc.printStackTrace ( pw );
            MLSException mlsExc = (MLSException)exc;
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            String errMsg = exc.getMessage();
            int code = 0;
            MLSException.APPLY __dummyScrutVar0 = mlsExc.getCode();
            if (__dummyScrutVar0.equals(2))
            {
                code = MLS_ERROR_TIMEOUT;
            }
            else if (__dummyScrutVar0.equals(4) || __dummyScrutVar0.equals(7))
            {
                code = MLS_ERROR_NO_RECORD_FOUND;
            }
            else //errMsg = STRINGS.get( STRINGS.NO_RECORD_FOUND );
            //case 4:
            //code = MLS_ERROR_UNABLE_COMPLETE_SEARCH;
            //break;
            if (__dummyScrutVar0.equals(5))
            {
                code = MLS_ERROR_UNABLE_TO_CONNECT;
            }
            else if (__dummyScrutVar0.equals(8))
            {
                code = MLS_ERROR_NO_USERNAME_PASSWORD;
            }
            else if (__dummyScrutVar0.equals(9))
            {
                code = MLS_ERROR_INVALID_USERNAME_PASSWORD;
                errMsg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INVALID_USERNAME_PASSWORD);
            }
            else if (__dummyScrutVar0.equals(10))
            {
                code = MLS_ERROR_DEF_PARSING_ERROR;
            }
            else if (__dummyScrutVar0.equals(11))
            {
                code = EXCEED_MAX_DOWNLOAD_SIZE;
            }
            else if (__dummyScrutVar0.equals(20210))
            {
                code = CODE_QUERY_LIMIT_EXCEEDED;
                errMsg = getTCSMessage(code);
            }
            else if (__dummyScrutVar0.equals(CODE_DUPLICATE_SEARCH_CODE_RETS_PARAMETER))
            {
                code = DUPLICATE_SEARCH_CODE_RETS_PARAMETER;
                errMsg = getTCSMessage(code);
            }
            else
            {
                code = INTERNAL_ERROR;
            }         
            TCSException tcsExc = new TCSException(null, errMsg, mlsExc.getDeveloperMessage(), TCSEXCEPTION_SOURCE_MLSEXCEPTION, code);
            return tcsExc;
        }
         
        if (exc instanceof Exception)
        {
            //System.IO.StringWriter sw = new System.IO.StringWriter();
            //UPGRADE_ISSUE: Constructor 'java.io.PrintWriter.PrintWriter' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javaioPrintWriterPrintWriter_javaioWriter'"
            //System.IO.StreamWriter pw = new PrintWriter(sw);
            //SupportClass.WriteStackTrace(exc, pw);
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            TCSException tcsExc = new TCSException(null,exc.getMessage(),exc.getStackTrace().toString(),TCSEXCEPTION_SOURCE_EXCEPTION,ERROR_CODE_JAVA_EXCEPTION);
            return tcsExc;
        }
         
        return null;
    }

    public static TCSException produceTCSException(Exception exc, int errorCode) throws Exception {
        //System.IO.StringWriter sw = new System.IO.StringWriter();
        //UPGRADE_ISSUE: Constructor 'java.io.PrintWriter.PrintWriter' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javaioPrintWriterPrintWriter_javaioWriter'"
        //System.IO.StreamWriter pw = new PrintWriter(sw);
        //SupportClass.WriteStackTrace(exc, pw);
        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
        TCSException tcsExc = new TCSException(null,exc.getMessage(),exc.getStackTrace().toString(),TCSEXCEPTION_SOURCE_APPLICATION,errorCode);
        return tcsExc;
    }

    public static TCSException produceTCSException(int errorCode) throws Exception {
        TCSException tcsExc = new TCSException(null,getTCSMessage(errorCode),"",TCSEXCEPTION_SOURCE_APPLICATION,errorCode);
        return tcsExc;
    }

    public int getErrorCode() throws Exception {
        return m_errorCode;
    }

    public static String getTCSMessage(int code) throws Exception {
        String message = "";
        switch(code)
        {
            case BLL_ERROR_GET_LOOK_UP_TABLE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_GET_LOOK_UP_TABLE);
                break;
            case BLL_ERROR_UPDATE_TASK_STATUS: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_STATUS);
                break;
            case BLL_ERROR_UPDATE_TASK_PROGRESS: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_UPDATE_TASK_PROGRESS);
                break;
            case BLL_ERROR_SAVE_TASK_RESULT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_SAVE_TASK_RESULT);
                break;
            case BLL_ERROR_GET_MESSAGE_BODY: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_GET_MESSAGE_BODY);
                break;
            case BLL_ERROR_GET_DEF: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_GET_DEF);
                break;
            case BLL_ERROR_SAVE_TASK_FILE_RESULT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_GET_DEF);
                break;
            case PARSINGLIMIT_MAXRECLIMIT_BOTH_USED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.PARSINGLIMIT_MAXRECLIMIT_BOTH_USED);
                break;
            case ERROR_MLS_SEARCH: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INTERNAL_ERROR);
                break;
            case MLS_ERROR_UNABLE_TO_CONNECT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INTERNAL_ERROR);
                break;
            case MLS_ERROR_DEF_PARSING_ERROR: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INTERNAL_ERROR);
                break;
            case ERROR_CODE_JAVA_EXCEPTION: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INTERNAL_ERROR);
                break;
            case INTERNAL_ERROR: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INTERNAL_ERROR);
                break;
            case MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED);
                break;
            case NOT_SURPPORT_CREDENTIAL_CHECK: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_CREDENTIAL_CHECK);
                break;
            case NOT_SURPPORT_RECORD_COUNT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_RECORD_COUNT);
                break;
            case NOT_SURPPORT_REQUEST_SEARCH_FIELD: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
                break;
            case BOARD_TEMP_UNAVAILABLE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOARD_TEMP_UNAVAILABLE);
                break;
            case BOARD_UNAVAILABLE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOARD_UNAVAILABLE);
                break;
            case MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED);
                break;
            case RESULT_EXCEED_LIMITED_SIZE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.RESULT_EXCEED_LIMITED_SIZE);
                break;
            case MLS_ERROR_TIMEOUT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXECUTIVE_TIMEOUT);
                break;
            case MINIMUM_MLS_CREDENTIAL_NOT_SUPPLIED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MINIMUM_MLS_CREDENTIAL_NOT_SUPPLIED);
                break;
            case MLS_ERROR_INVALID_USERNAME_PASSWORD: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INVALID_USERNAME_PASSWORD);
                break;
            case MISCELLANEOUS_MLS_ERROR: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MISCELLANEOUS_MLS_ERROR);
                break;
            case NO_DEF_FILE_MATCH: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_DEF_FILE_MATCH);
                break;
            case TCS_EXECUTIVE_TIMEOUT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXECUTIVE_TIMEOUT);
                break;
            case EXCEED_MAX_DOWNLOAD_SIZE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.EXCEED_MAX_PARSE_LISTINGS);
                break;
            case BOARD_NOT_COMPATIBLE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOARD_NOT_COMPATIBLE);
                break;
            case DATA_STANDARDIZATION_APPLIED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.DATA_STANDARDIZATION_APPLIED);
                break;
            case MLS_ERROR_NO_USERNAME_PASSWORD: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INVALID_USERNAME_PASSWORD);
                break;
            case MLS_ERROR_NO_RECORD_FOUND: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND);
                break;
            case ONE_OR_MORE_PICTURE_NOT_FOUND: 
                //case MLS_ERROR_UNABLE_COMPLETE_SEARCH:
                //    message = STRINGS.get_Renamed(STRINGS.INTERNAL_ERROR);
                //    break;
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_GET_DEF);
                break;
            case NOT_SURPPORT_SEARCH_FIELD: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_SEARCH_FIELD);
                break;
            case NOT_SURPPORT_POSTALFSA_FIELD: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_POSTALFSA_FIELD);
                break;
            case INVALID_SEARCH_FIELD_FORMAT: 
                //case MLS_ENGINE_NOTE:
                //    message = STRINGS.get_Renamed(STRINGS.MLS_ENGINE_NOTE);
                //    break;
                //case INVALID_REQUEST:
                //    message = STRINGS.get_Renamed(STRINGS.INVALID_REQUEST);
                //    break;
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INVALID_SEARCH_FIELD_FORMAT);
                break;
            case LOOKUP_CODE_FORMAT_NOT_EXIST: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.LOOKUP_CODE_FORMAT_NOT_EXIST);
                break;
            case NOT_ALLOW_MULTIPLE_VALUES: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_ALLOW_MULTIPLE_VALUES);
                break;
            case BOARD_NOT_EXIST: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOARD_NOT_EXIST);
                break;
            case RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT);
                break;
            case BOTH_RECORDLIMIT_OVERRIDERECORDLIMIT_USED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOTH_RECORDLIMIT_OVERRIDERECORDLIMIT_USED);
                break;
            case OVERRIDERECLIMIT_ISLOWERTHAN_MAXRECLIMIT: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_ISLOWERTHAN_MAXRECLIMIT);
                break;
            case OVERRIDERECLIMIT_ISNOTALLOWED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_ISNOTALLOWED);
                break;
            case OVERRIDERECLIMIT_DATENOTSUPPLIED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_DATENOTSUPPLIED);
                break;
            case METADATA_VERSION_NOT_MATCH: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.METADATA_VERSION_NOT_MATCH);
                break;
            case BOTH_LASTMODIFIED_PICMODIFIED_USED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOTH_LASTMODIFIED_PICMODIFIED_USED);
                break;
            case BOTH_LASTMODIFIED_PICMODIFIED_EMPTY: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOTH_LASTMODIFIED_PICMODIFIED_EMPTY);
                break;
            case EXCEED_MAX_PIC_NUMBER_100: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.EXCEED_MAX_PIC_NUMBER_100);
                break;
            case EXCEED_MAX_PIC_NUMBER_1000: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.EXCEED_MAX_PIC_NUMBER_1000);
                break;
            case CANNOT_SEARCH_BYBOTH_MODULEID_BOARDID: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.CANNOT_SEARCH_BYBOTH_MODULEID_BOARDID);
                break;
            case CODE_QUERY_LIMIT_EXCEEDED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.CODE_QUERY_LIMIT_EXCEEDED);
                break;
            case EXCEED_MAX_LISITNG_NUMBER: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.EXCEED_MAX_LISITNG_NUMBER);
                break;
            case LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH);
                break;
            case MLS_METADATA_VERSION_NOT_MATCH: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MLS_METADATA_VERSION_NOT_MATCH);
                break;
            case AGENT_OFFICE_ROSTER_DEF_NOT_EXIST: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.AGENT_OFFICE_ROSTER_DEF_NOT_EXIST);
                break;
            case OPENHOUSE_DEF_NOT_EXIST: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OPENHOUSE_DEF_NOT_EXIST);
                break;
            case CHUNKING_FIELD_NOTDEFINED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.CHUNKING_FIELD_NOTDEFINED);
                break;
            case BOTH_STATUS_PUBLICSTATUS_USED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOTH_STATUS_PUBLICSTATUS_USED);
                break;
            case OFFMARKET_STATUS_NOT_DEFINED_INDEF: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OFFMARKET_STATUS_NOT_DEFINED_INDEF);
                break;
            case CHUNKING_FAILED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.CHUNKING_FAILED);
                break;
            case DUPLICATE_SEARCH_CODE_RETS_PARAMETER: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.DUPLICATE_SEARCH_CODE_RETS_PARAMETER);
                break;
            case BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED);
                break;
            case CONDITIONCODE_NOT_IMPLEMENTED_INDEF: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.CONDITIONCODE_NOT_IMPLEMENTED_INDEF);
                break;
            case RETS_CLASS_NOT_EXISTS: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.RETS_CLASS_NOT_EXISTS);
                break;
            case RETS_CLASS_NOT_AVAILABLE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.RETS_CLASS_NOT_AVAILABLE);
                break;
            case UNABLE_TO_USE_RETS_CLASS_NON_RETS: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.UNABLE_TO_USE_RETS_CLASS_NON_RETS);
                break;
            case WRITE_RESULT_FLAG_FILE_FAILED: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.WRITE_RESULT_FLAG_FILE_FAILED);
                break;
            case OpenHouse_MLSNumber_NotFound: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OpenHouse_MLSNumber_NotFound);
                break;
            case OpenHouse_NotAvailableTo_MlsNumberSearch: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OpenHouse_NotAvailableTo_MlsNumberSearch);
                break;
            case INVALID_AGENTID: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INVALID_AGENTID);
                break;
            case INVALID_PROPERTY_TYPE: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INVALID_PROPERTY_TYPE);
                break;
            default: 
                message = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_NUL_ERROR_CODE);
                break;
        
        }
        return message;
    }

    public String getOutputErrorXml() throws Exception {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"1\" ReplyText=\"Failure\">\r\n");
        sb.append("<Notes>\r\n");
        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
        String msg = getMessage();
        if (msg == null || msg.length() == 0)
            msg = getDeveloperMessage();
         
        //else
        //   msg += getDeveloperMessage().Length > 0 ? ". " + getDeveloperMessage():"";
        if (m_errorCode <= INTERNAL_ERROR && m_errorCode > 60000)
        {
            m_errorCode = INTERNAL_ERROR;
            msg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INTERNAL_ERROR) + msg;
            msg += getErrorMessage();
            msg += " Application server: " + InetAddress.getLocalHost().getHostName();
        }
         
        if (m_errorCode == MLS_ERROR_NO_RECORD_FOUND)
            msg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND);
         
        sb.append("<Note ID=\"1\" ReplyCode=\"" + m_errorCode + "\"");
        sb.append(" ReplyText=\"" + Tcs.Mls.Util.convertStringToXML(msg != null ? msg : "No message") + "\" Comment=\"\"/>\r\n");
        sb.append("</Notes>\r\n");
        sb.append("</TCResult>\r\n");
        String result = sb.toString();
        return result;
    }

    public String getErrorMessage() throws Exception {
        String msg = getMessage();
        if (msg == null || msg.length() == 0)
            msg = getDeveloperMessage();
         
        //else
        //   msg += getDeveloperMessage().Length > 0 ? ". " + getDeveloperMessage():"";
        if (m_errorCode <= INTERNAL_ERROR && m_errorCode > 60000)
        {
            m_errorCode = INTERNAL_ERROR;
            msg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.INTERNAL_ERROR) + msg;
            msg += getErrorMessage();
        }
         
        if (m_errorCode == MLS_ERROR_NO_RECORD_FOUND)
            msg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND);
         
        return msg;
    }

    // Output error log
    public void writeErrLog() throws Exception {
        StringBuilder errorInfo = new StringBuilder();
        errorInfo.append("\r\n");
        errorInfo.append("Source:");
        switch(m_source)
        {
            case TCSEXCEPTION_SOURCE_EXCEPTION: 
                errorInfo.append(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_SOURCE_EXCEPTION));
                break;
            case TCSEXCEPTION_SOURCE_MLSEXCEPTION: 
                errorInfo.append(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_SOURCE_MLSEXCEPTION));
                break;
            case TCSEXCEPTION_SOURCE_APPLICATION: 
                errorInfo.append(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_SOURCE_APPLICATION));
                break;
            default: 
                errorInfo.append(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.TCS_EXCEPTION_ERR_SCRIPT_SOURCE_NULL));
                break;
        
        }
        //	errorInfo.append(m_source);
        errorInfo.append("\r\nError:");
        errorInfo.append(getTCSMessage(m_errorCode));
        //		errorInfo.append(m_errorCode);
        errorInfo.append("\r\nMessage:");
        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
        errorInfo.append(getMessage());
        errorInfo.append("\r\nDeveloper Message:\r\n");
        errorInfo.append(getDeveloperMessage());
        System.out.println(errorInfo.toString());
    }

    //Namespaces needed
    public boolean findAndKillProcess(String name) throws Exception {
        for (Object __dummyForeachVar0 : Process.GetProcesses())
        {
            //here we're going to get a list of all running processes on
            //the computer
            Process clsProcess = (Process)__dummyForeachVar0;
            //now we're going to see if any of the running processes
            //match the currently running processes by using the StartsWith Method,
            //this prevents us from incluing the .EXE for the process we're looking for.
            //. Be sure to not
            //add the .exe to the name you provide, i.e: NOTEPAD,
            //not NOTEPAD.EXE or false is always returned even if
            //notepad is running
            if (clsProcess.ProcessName.StartsWith(name))
            {
                //since we found the proccess we now need to use the
                //Kill Method to kill the process. Remember, if you have
                //the process running more than once, say IE open 4
                //times the loop thr way it is now will close all 4,
                //if you want it to just close the first one it finds
                //then add a return; after the Kill
                clsProcess.Kill();
                return true;
            }
             
        }
        return false;
    }

    //process killed, return true
    //process not found, return false
    public void runCommandFile(String fileName) throws Exception {
        System.Diagnostics.Process proc = new System.Diagnostics.Process();
        proc.EnableRaisingEvents = false;
        proc.StartInfo.FileName = fileName;
        proc.Start();
    }

}


