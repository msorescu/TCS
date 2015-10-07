//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:32 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.RegularExpressions.GroupCollection;
import CS2JNet.System.Text.RegularExpressions.Match;
import CS2JNet.System.Xml.XmlNode;
import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;
import net.toppro.components.mls.engine.DataAggResultFieldType;
import net.toppro.components.mls.engine.DataAggResultTable;
import net.toppro.components.mls.engine.HelpInfoTable;
import net.toppro.components.mls.engine.Lookup;
import net.toppro.components.mls.engine.LookupTable;
import net.toppro.components.mls.engine.MLSCredentialsTable;
import net.toppro.components.mls.engine.MlsRules;
import net.toppro.components.mls.engine.MLSSystem;
import net.toppro.components.mls.engine.OpenHouseResutlTable;
import net.toppro.components.mls.engine.PropertyClass;
import net.toppro.components.mls.engine.PropertyStandardToMLS;
import net.toppro.components.mls.engine.ResultFieldType;
import net.toppro.components.mls.engine.ResultTable;
import net.toppro.components.mls.engine.RosterResultTable;
import net.toppro.components.mls.engine.SearchFieldType;
import net.toppro.components.mls.engine.SearchTable;

public class Board   
{
    private String m_boardID = "";
    private String m_boardName = "";
    private String m_moduleName = "";
    private String m_moduleID = "";
    private String m_helpPageUrl = "";
    private String m_boardType = "";
    private String m_statusID = "";
    private String m_statusDescription = "";
    private String m_boardVersion = "";
    private String m_maxRecordsLimit = "";
    private String m_overrideAllowed = "";
    private String m_mlsSearchTimeZone = "";
    private String m_mlsResultsTimeZone = "";
    private MLSSystem m_mlsSystem;
    private PropertyClass[] m_propertyClass;
    private net.toppro.components.mls.engine.MLSEngine[] m_mlsEngine;
    private SearchTable m_searchTable;
    private ResultTable m_resultTable;
    private DataAggResultTable m_dataAggResultTable;
    private MLSCredentialsTable m_mslCredentialsTable;
    private HelpInfoTable m_helpInfo;
    private LookupTable m_lookupTable;
    private MlsRules m_mlsRules;
    private RosterResultTable m_agentRosterResultTable;
    private RosterResultTable m_officeRosterResultTable;
    private OpenHouseResutlTable m_openHouseResultTable;
    private PropertyStandardToMLS m_propertyStandardToMLS;
    private Pattern m_reg = Pattern.compile("(\\w+)_(\\d+)$");
    private static final String BOARD_SEARCH = "Search";
    private static final String BOARD_IMPORT = "Import";
    private boolean m_isLastDefFile = false;
    private HashMap<String,HashMap<String,String>> m_categoryOrder = new HashMap<String,HashMap<String,String>>();
    public HashMap<String,HashMap<String,String>> getCategoryOrder() throws Exception {
        return m_categoryOrder;
    }

    public void setCategoryOrder(HashMap<String,HashMap<String,String>> value) throws Exception {
        m_categoryOrder = value;
    }

    private HashMap<String,Integer> m_categoryCount = new HashMap<String,Integer>();
    public HashMap<String,Integer> getCategoryCount() throws Exception {
        return m_categoryCount;
    }

    public void setCategoryCount(HashMap<String,Integer> value) throws Exception {
        m_categoryCount = value;
    }

    public String getMLSSearchTimeZone() throws Exception {
        return m_mlsSearchTimeZone;
    }

    public void setMLSSearchTimeZone(String value) throws Exception {
        m_mlsSearchTimeZone = value;
    }

    public String getMLSResultsTimeZone() throws Exception {
        return m_mlsResultsTimeZone;
    }

    public void setMLSResultsTimeZone(String value) throws Exception {
        m_mlsResultsTimeZone = value;
    }

    public boolean getIsLastDefFile() throws Exception {
        return m_isLastDefFile;
    }

    public void setIsLastDefFile(boolean value) throws Exception {
        m_isLastDefFile = value;
    }

    public Board(MLSSystem mlsSystem) throws Exception {
        m_mlsSystem = mlsSystem;
        createChildNodes();
    }

    public void createChildNodes() throws Exception {
        XmlNode defList = getMLSSystem().getEnvironment().getMetaDataDEFList();
        XmlNode agentRoster = null;
        XmlNode officeRoster = null;
        setStatusID(defList.getAttributes()[MLSConnector.ATTRIBUTE_BOARD_STATUS].Value);
        setStatusDescription(getStatusDescription(getStatusID()));
        m_searchTable = new SearchTable(this);
        m_resultTable = new ResultTable(this);
        m_dataAggResultTable = new DataAggResultTable(this);
        m_lookupTable = new LookupTable(this);
        m_propertyStandardToMLS = new PropertyStandardToMLS(this);
        for (int i = 0;i < defList.getChildNodes().size();i++)
        {
            String defPath = defList.getChildNodes().get(i).getAttributes()[MLSConnector.ATTRIBUTE_DEFINITION_FILE].Value.ToLower();
            if (defPath.endsWith(".sql"))
            {
                if (defPath.endsWith("ag.sql"))
                {
                    m_agentRosterResultTable = new RosterResultTable(this,new net.toppro.components.mls.engine.MLSEngine(getMLSSystem().getEnvironment(), Util.loadFile(defPath), null),true);
                }
                else if (defPath.endsWith("of.sql"))
                {
                    m_officeRosterResultTable = new RosterResultTable(this,new net.toppro.components.mls.engine.MLSEngine(getMLSSystem().getEnvironment(), Util.loadFile(defPath), null),false);
                }
                else if (defPath.endsWith("oh.sql"))
                {
                    m_openHouseResultTable = new OpenHouseResutlTable(this,new net.toppro.components.mls.engine.MLSEngine(getMLSSystem().getEnvironment(), Util.loadFile(defPath), null),false);
                }
                else if (defPath.endsWith("da.sql"))
                {
                    continue;
                }
                    
                // m_openHouseResultTable = new OpenHouseResutlTable(this, new MLSEngine(MLSSystem.Environment, Util.loadFile(defPath), null), false);
                defList.removeChild(defList.getChildNodes().get(i));
                i--;
            }
             
        }
        m_propertyClass = new PropertyClass[defList.getChildNodes().size()];
        m_mlsEngine = new net.toppro.components.mls.engine.MLSEngine[defList.getChildNodes().size()];
        for (int i = 0;i < defList.getChildNodes().size();i++)
        {
            XmlNode ele = defList.getChildNodes().get(i);
            String defPath = defList.getChildNodes().get(i).getAttributes()[MLSConnector.ATTRIBUTE_DEFINITION_FILE].Value;
            m_mlsEngine[i] = new net.toppro.components.mls.engine.MLSEngine(getMLSSystem().getEnvironment(), Util.loadFile(defPath), null);
            if (i == 0)
            {
                setBoardName(ele.getAttributes()[MLSConnector.ATTRIBUTE_BOARD_NAME].Value);
                setBoardID(ele.getAttributes()[MLSConnector.ATTRIBUTE_BOARD_ID_DB].Value);
                setModuleName(ele.getAttributes()[MLSConnector.ATTRIBUTE_MODULE_NAME_DB].Value);
                setModuleID(ele.getAttributes()[MLSConnector.ATTRIBUTE_MODULE_ID].Value);
                setHelpPageUrl(ele.getAttributes()[MLSConnector.ATTRIBUTE_HELP_URL].Value);
                if (m_mlsEngine[i].isFileImport())
                    setBoardType(BOARD_IMPORT);
                else
                    setBoardType(BOARD_SEARCH); 
                getMLSSystem().setMetadataPath(defPath.substring(0, (0) + (defPath.lastIndexOf("\\"))));
                setBoardVersion(ele.getAttributes()[MLSConnector.ATTRIBUTE_VERSION].Value);
                setMaxRecordsLimit(String.valueOf(m_mlsEngine[i].getMaxRecordsLimit()));
                setOverrideAllowed(m_mlsEngine[i].isOverrideAllowed() ? "1" : "0");
                setMLSSearchTimeZone(m_mlsEngine[i].getTimeZoneDisplayName(m_mlsEngine[i].getMLSSearchTimeZone()));
                setMLSResultsTimeZone(m_mlsEngine[i].getTimeZoneDisplayName(m_mlsEngine[i].getMLSResultsTimeZone()));
            }
             
            if (i == (defList.getChildNodes().size() - 1))
                setIsLastDefFile(true);
             
            m_propertyClass[i] = new PropertyClass();
            m_propertyClass[i].setClassName(ele.getAttributes()[MLSConnector.ATTRIBUTE_CONNECTION_TYPE].Value);
            m_propertyClass[i].setDefFileName(ele.getAttributes()[MLSConnector.ATTRIBUTE_CONNECTION_NAME].Value);
            m_propertyClass[i].setDefFilePath(defPath);
            m_propertyClass[i].initPropertyClass(this,m_mlsEngine[i]);
        }
        m_mslCredentialsTable = new MLSCredentialsTable(this,m_mlsEngine[0]);
        m_helpInfo = new HelpInfoTable(this,m_mlsEngine[0]);
        m_mlsRules = new MlsRules(this,m_mlsEngine[0]);
    }

    public static String getStatusDescription(String statusID) throws Exception {
        String result = "";
        String __dummyScrutVar0 = statusID;
        if (__dummyScrutVar0.equals("1"))
        {
            result = "Available";
        }
        else if (__dummyScrutVar0.equals("2"))
        {
            result = "Temp unavailable";
        }
        else if (__dummyScrutVar0.equals("3"))
        {
            result = "Unavailable";
        }
           
        return result;
    }

    public String addToSearchTable(SearchFieldType searchFieldType) throws Exception {
        return m_searchTable.addType(searchFieldType);
    }

    public String addToResultTable(ResultFieldType resultFieldType) throws Exception {
        return m_resultTable.addType(resultFieldType);
    }

    public String addToDataAggResultTable(DataAggResultFieldType resultFieldType) throws Exception {
        return m_dataAggResultTable.addType(resultFieldType);
    }

    public String addToLookupTable(Lookup lookup) throws Exception {
        return m_lookupTable.addLookup(lookup);
    }

    public void addSearchFieldByDef(String defPath, String searchField) throws Exception {
        m_propertyStandardToMLS.addSearchFieldByDef(defPath,searchField);
    }

    public String getIncrementalFieldName(String name) throws Exception {
        if (m_reg.matcher(name).matches())
        {
            Match match = Match.mk(m_reg, name);
            name = GroupCollection.mk(match).get(1) + "_" + (int.Parse(GroupCollection.mk(match).get(2).toString(), Locale.CurrentCulture) + 1).ToString(Locale.CurrentCulture);
        }
        else
        {
            name = name + "_1";
        } 
        return name;
    }

    public MLSSystem getMLSSystem() throws Exception {
        return m_mlsSystem;
    }

    public String getModuleName() throws Exception {
        return m_moduleName;
    }

    public void setModuleName(String value) throws Exception {
        m_moduleName = value;
    }

    public String getModuleID() throws Exception {
        return m_moduleID;
    }

    public void setModuleID(String value) throws Exception {
        m_moduleID = value;
    }

    public String getBoardName() throws Exception {
        return m_boardName;
    }

    public void setBoardName(String value) throws Exception {
        m_boardName = value;
    }

    public String getBoardID() throws Exception {
        return m_boardID;
    }

    public void setBoardID(String value) throws Exception {
        m_boardID = value;
    }

    public String getHelpPageUrl() throws Exception {
        return m_helpPageUrl;
    }

    public void setHelpPageUrl(String value) throws Exception {
        m_helpPageUrl = value;
    }

    public String getBoardType() throws Exception {
        return m_boardType;
    }

    public void setBoardType(String value) throws Exception {
        m_boardType = value;
    }

    public String getStatusID() throws Exception {
        return m_statusID;
    }

    public void setStatusID(String value) throws Exception {
        m_statusID = value;
    }

    public String getStatusDescription() throws Exception {
        return m_statusDescription;
    }

    public void setStatusDescription(String value) throws Exception {
        m_statusDescription = value;
    }

    public String getBoardVersion() throws Exception {
        return m_boardVersion;
    }

    public void setBoardVersion(String value) throws Exception {
        m_boardVersion = value;
    }

    public String getMaxRecordsLimit() throws Exception {
        return m_maxRecordsLimit;
    }

    public void setMaxRecordsLimit(String value) throws Exception {
        m_maxRecordsLimit = value;
    }

    public String getOverrideAllowed() throws Exception {
        return m_overrideAllowed;
    }

    public void setOverrideAllowed(String value) throws Exception {
        m_overrideAllowed = value;
    }

    private String getMetadataVersion() throws Exception {
        String version = "1" + StringSupport.PadLeft(net.toppro.components.mls.engine.MLSEngine.MLS_ENGINE_VERSION, 3, '0');
        version += "1" + StringSupport.PadLeft(getBoardVersion(), 3, '0');
        return version;
    }

    public void toXml() throws Exception {
        XmlWriter w = getMLSSystem().getSystemXmlWriter();
        w.WriteStartElement(MC.BOARD);
        w.WriteElementString(MC.BOARD_ID, getBoardID());
        w.WriteElementString(MC.NAME, getBoardName());
        w.WriteElementString(MC.MODULE_ID, getModuleID());
        w.WriteElementString(MC.MODULE_NAME, getModuleName());
        w.WriteElementString(MC.HELP_PAGE_URL, getHelpPageUrl());
        w.WriteElementString(MC.TYPE, getBoardType());
        w.WriteElementString(MC.STATUS_ID, getStatusID());
        w.WriteElementString(MC.STATUS_DESCRIPTION, getStatusDescription());
        w.WriteElementString(MC.VERSION, getMetadataVersion());
        w.WriteElementString(MC.MLSSEARCH_TIMEZONE, getMLSSearchTimeZone());
        w.WriteElementString(MC.MLSRESULTS_TIMEZONE, getMLSResultsTimeZone());
        if (!m_mlsEngine[0].isFileImport())
        {
            w.WriteElementString(MC.MAX_RECORDS_LIMIT, getMaxRecordsLimit());
            w.WriteElementString(MC.OVERRIDE_ALLOWED, getOverrideAllowed());
        }
         
        w.WriteStartElement(MC.METADATA_PROPERTY_CLASS);
        for (int i = 0;i < m_propertyClass.length;i++)
        {
            if (!m_propertyClass[i].getDefFilePath().toLowerCase().endsWith("da.sql"))
                m_propertyClass[i].toXml();
             
        }
        w.writeEndElement();
        m_searchTable.toXml();
        m_resultTable.toXml();
        m_dataAggResultTable.toXml();
        if (m_agentRosterResultTable != null)
            m_agentRosterResultTable.toXml();
         
        if (m_officeRosterResultTable != null)
            m_officeRosterResultTable.toXml();
         
        if (m_openHouseResultTable != null)
            m_openHouseResultTable.toXml();
         
        m_mslCredentialsTable.toXml();
        m_helpInfo.toXml();
        m_mlsRules.toXml();
        m_lookupTable.toXml();
        m_propertyStandardToMLS.toXml();
        w.writeEndElement();
    }

}


