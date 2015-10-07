//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:57 PM
//

package Tcs.Mls.Request;


public class STRINGS   
{
    //+-----------------------------------------------------------------------------------+
    //|                                  String constants                                 |
    //+-----------------------------------------------------------------------------------+
    public static final int NOT_SURPPORT_CREDENTIAL_CHECK = 0;
    //UPGRADE_NOTE: Final was removed from the declaration of 'ONE_OR_MORE_PICTURE_NOT_FOUND '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int ONE_OR_MORE_PICTURE_NOT_FOUND = NOT_SURPPORT_CREDENTIAL_CHECK + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED = ONE_OR_MORE_PICTURE_NOT_FOUND + 1;
    public static final int SEARCH_FIELD_DUPLICATE = MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED + 1;
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
    private static final String[] STRINGS_Renamed_Field = new String[]{ "MLS Credential Validation is not supported by the MLS.", "One or more of requested photos could not be found.", "Minimum request search parameter is not supplied: ", "The following standard search fields were duplicates of MLS-specific fields. The MLS-specific criteria will be ignored. " };
}


