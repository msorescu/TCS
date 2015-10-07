//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:39 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.STRINGS;

//using StellarClient = net.toppro.components.mls.engine.client.StellarClient;
//using BorisMaestroClient = net.toppro.components.mls.engine.client.BorisMaestroClient;
//using REInfoLinkClient = net.toppro.components.mls.engine.client.REInfoLinkClient;
//using MSSQLClient = net.toppro.components.mls.engine.client.MSSQLClient;
//using DBFClient = net.toppro.components.mls.engine.client.DBFClient;
public class ClientFactory   
{
    //+-----------------------------------------------------------------------------------+
    //|                                    Vendor ids                                     |
    //+-----------------------------------------------------------------------------------+
    private static final int VDR_UNKNOWN = -1;
    private static final int VDR_BORIS_MAESTRO = 0;
    // Telnet (4)
    private static final int VDR_BURGDORFF = 1;
    // Telnet (4)
    private static final int VDR_INTERREALTY_STELLAR = 2;
    // Telnet (4)
    private static final int VDR_RE_INFO_LINK = 3;
    // Telnet (8)
    private static final int VDR_RE_SEARCH = 4;
    // Telnet (4); Helper DLL; ASCII Import
    private static final int VDR_GTE = 5;
    // HTTP (5)
    private static final int VDR_HOMESEEKERS = 6;
    // HTTP (2)
    private static final int VDR_MARKET_LINX = 7;
    // HTTP (6)
    private static final int VDR_RAPATTONI = 8;
    // HTTP (2)
    private static final int VDR_RE_XPLORER = 9;
    // HTTP (2)
    private static final int VDR_REAL_GO = 10;
    // HTTP (2)
    private static final int VDR_REALTY_PLUS = 11;
    // HTTP (2)
    private static final int VDR_SOLIDEARTH = 12;
    // HTTP (2)
    private static final int VDR_WYLDFYRE = 13;
    // API; ASCII IMPORT (97)
    private static final int VDR_ADVANCED_MARKETING = 14;
    // ASCII IMPORT (98); ZIP
    private static final int VDR_CTI_NAVIGATOR_2000 = 15;
    // ASCII IMPORT (98)
    private static final int VDR_FBS = 16;
    // ASCII IMPORT (98)
    private static final int VDR_INTERREALTY_NET_MLS = 17;
    // ASCII IMPORT (98)
    private static final int VDR_KINNEXUS = 18;
    // ASCII IMPORT (98)
    private static final int VDR_OFFUTT_INNOVIA = 19;
    // ASCII IMPORT (98); ZIP
    private static final int VDR_PROMATCH_2000 = 20;
    // ASCII IMPORT (98)
    private static final int VDR_REALMATRIX_HOMESCAPE = 21;
    // ASCII IMPORT (98); ZIP
    private static final int VDR_RISCO_GALAXY = 22;
    // ASCII IMPORT (98)
    private static final int VDR_RISCO_VOYAGER = 23;
    // ASCII IMPORT (98)
    private static final int VDR_SEI = 24;
    // ASCII IMPORT (98)
    private static final int VDR_STRATUS = 25;
    // ASCII IMPORT (98)
    private static final int VDR_T_III = 26;
    // ASCII IMPORT (98); Helper DLL
    private static final int VDR_ULTREX = 27;
    // ASCII IMPORT (98)
    private static final int VDR_DEMO = 28;
    // DEMO (999)
    private static final int VDR_TREB = 29;
    // HTTP (2) Support picture download via FTP
    //+-----------------------------------------------------------------------------------+
    //|                                     Data source                                   |
    //+-----------------------------------------------------------------------------------+
    private static final int DATA_SOURCE_UNKNOWN = -1;
    public static final int DATA_SOURCE_ASCII = 0;
    public static final int DATA_SOURCE_WYLDFYRE = 1;
    //+-----------------------------------------------------------------------------------+
    //|                                  Client creation                                  |
    //+-----------------------------------------------------------------------------------+
    public static net.toppro.components.mls.engine.CommunicationClient createClient(net.toppro.components.mls.engine.MLSEngine engine, String connection, String data_source, String vendor) throws Exception {
        //For DB import client
        DefParser objDefParser = engine.getDefParser();
        String sDataImport = objDefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_COMMON,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DATA_IMPORT);
        //if (sDataImport != null && sDataImport.Trim().Length != 0)
        //{
        //    if (sDataImport.ToUpper().Equals("SQL SERVER".ToUpper()))
        //    {
        //        //SQL SERVER
        //        return new MSSQLClient(engine);
        //    }
        //    else if (sDataImport.ToUpper().Equals("FOXPRO 2.6".ToUpper()))
        //    {
        //        //FOXPRO 2.6
        //        return new DBFClient(engine);
        //    }
        //}
        //End
        switch(getConnectionType(connection))
        {
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_DEMO: 
                return new DemoClient(engine);
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_HTTP_2: 
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_HTTP_3: 
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_HTTP_5: 
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_HTTP_6: 
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_HTTP_7: 
                return new HttpClient(engine);
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_RETS: 
                return new RETSClient(engine);
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_MRIS: 
                return new MRISClient(engine);
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_GARDENSTATE: 
                return new GardenStateClient(engine);
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_SAFEMLS: 
                return new SafeMLSClient(engine);
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_MRISRETS: 
                return new MRISRETSClient(engine);
            case net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_REInfoLinkRETS: 
                return new REInfoLinkRETSClient(engine);
            default: 
                //case MLSEngine.CLIENT_TYPE_TELNET:
                //    switch (getVendorId(vendor))
                //    {
                //        case VDR_BORIS_MAESTRO:
                //        case VDR_BURGDORFF:
                //            return new BorisMaestroClient(engine);
                //        case VDR_INTERREALTY_STELLAR:
                //            return new StellarClient(engine);
                //        }
                //    break;
                //case MLSEngine.CLIENT_TYPE_REINFOLINK:
                //    return new REInfoLinkClient(engine);
                //if( objDefParser.getValue( MLSEngine.SECTION_PICTURES, MLSEngine.ATTRIBUTE_PICDWNFLAG).equals( "14" ) )
                //	return new TREBClient ( engine );
                switch(getDataSource(data_source))
                {
                    case DATA_SOURCE_ASCII: 
                        return new ASCIIClient(engine);
                    case DATA_SOURCE_WYLDFYRE: 
                        return new WyldFyreClient(engine);
                
                }
                break;
        
        }
        engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_UNKNOWN_CONNECTION_TYPE);
        throw engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.CLIENT_FACTORY_ERR_UNKNOWN_CLIENT_TYPE),connection,vendor));
    }

    private static int getConnectionType(String connection) throws Exception {
        int result = net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_UNKNOWN;
        connection = StringSupport.Trim(connection);
        if (connection != null && connection.length() > 0)
        {
            try
            {
                result = Integer.valueOf(connection);
            }
            catch (Exception exc)
            {
            }
        
        }
         
        return result;
    }

    /// <returns> vendor id. The functionality sometimes is different for different vendors.
    /// For example, z-modem works different for Boris Maestro boards in TelnetClient.
    /// Unfortunally def-files provide only vendor name (Common.NameOfOrigin section), which may
    /// change. So this function must be flexible enough to determine vendor using only it's name.
    /// All the changes in names must be reflected here.
    /// <p>Probably the better solution is to provide vendor id in def-files, but this puts too much work
    /// on MLS department. At least we're gonna keep the algorythm determining board id in one place: here.
    /// </returns>
    private static int getVendorId(String name) throws Exception {
        name = name.toUpperCase();
        // The performance can be improved by doing all string searches in one loop,
        // but 30 searches work fast enough.
        if (name.indexOf("SYSTEMS ENGINEERING") >= 0)
            return VDR_SEI;
        else if (name.indexOf("TOP PRODUCER") >= 0)
            return VDR_DEMO;
        else if (name.indexOf("HOMESEEKERS") >= 0)
            return VDR_HOMESEEKERS;
        else if (name.indexOf("MARKETLINX") >= 0)
            return VDR_MARKET_LINX;
        else if (name.indexOf("REALMATRIX") >= 0)
            return VDR_REALMATRIX_HOMESCAPE;
        else if (name.indexOf("RAPATTONI") >= 0)
            return VDR_RAPATTONI;
        else if (name.indexOf("BURGDORFF") >= 0)
            return VDR_BURGDORFF;
        else if (name.indexOf("KINNEXUS") >= 0)
            return VDR_KINNEXUS;
        else if (name.indexOf("WYLDFYRE") >= 0)
            return VDR_WYLDFYRE;
        else if (name.indexOf("PROMATCH") >= 0)
            return VDR_PROMATCH_2000;
        else if (name.indexOf("INFOLINK") >= 0)
            return VDR_RE_INFO_LINK;
        else if (name.indexOf("STELLAR") >= 0)
            return VDR_INTERREALTY_STELLAR;
        else if (name.indexOf("LIST-IT") >= 0)
            return VDR_SOLIDEARTH;
        else if (name.indexOf("XPLORER") >= 0)
            return VDR_RE_XPLORER;
        else if (name.indexOf("VOYAGER") >= 0)
            return VDR_RISCO_VOYAGER;
        else if (name.indexOf("STRATUS") >= 0)
            return VDR_STRATUS;
        else if (name.indexOf("NET MLS") >= 0)
            return VDR_INTERREALTY_NET_MLS;
        else if (name.indexOf("FUSION") >= 0)
            return VDR_REALTY_PLUS;
        else if (name.indexOf("OFFUTT") >= 0)
            return VDR_OFFUTT_INNOVIA;
        else if (name.indexOf("REALGO") >= 0)
            return VDR_REAL_GO;
        else if (name.indexOf("GALAXY") >= 0)
            return VDR_RISCO_GALAXY;
        else if (name.indexOf("ULTREX") >= 0)
            return VDR_ULTREX;
        else if (name.indexOf("SEARCH") >= 0)
            return VDR_RE_SEARCH;
        else if (name.indexOf("BORIS") >= 0)
            return VDR_BORIS_MAESTRO;
        else if (name.indexOf("T-III") >= 0)
            return VDR_T_III;
        else if (name.indexOf("GTE") >= 0)
            return VDR_GTE;
        else if (name.indexOf("AMS") >= 0)
            return VDR_ADVANCED_MARKETING;
        else if (name.indexOf("CTI") >= 0)
            return VDR_CTI_NAVIGATOR_2000;
        else if (name.indexOf("FBS") >= 0)
            return VDR_FBS;
        else if (name.indexOf("SEI") >= 0)
            return VDR_SEI;
                                      
        return VDR_UNKNOWN;
    }

    public static int getDataSource(String name) throws Exception {
        name = name.toUpperCase();
        if (name.indexOf("ASCII") >= 0)
            return DATA_SOURCE_ASCII;
        else if (name.indexOf("WYLDFYRE") >= 0)
            return DATA_SOURCE_WYLDFYRE;
          
        return DATA_SOURCE_UNKNOWN;
    }

}


