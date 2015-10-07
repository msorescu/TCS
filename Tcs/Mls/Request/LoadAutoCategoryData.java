//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:55 PM
//

package Tcs.Mls.Request;

import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Xml.XmlDocument;
import CS2JNet.System.Xml.XmlNode;
import CS2JNet.System.Xml.XmlNodeList;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.BoardSetupField;
import net.toppro.components.mls.engine.MLSCmaFields;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.MLSEnvironment;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class LoadAutoCategoryData  extends TPAppRequest 
{
    //TCServer m_tcs;
    MapperTool m_mapperTool = new MapperTool();
    public LoadAutoCategoryData(TCServer tcs) throws Exception {
        super(tcs);
    }

    public void run() throws Exception {
        long time;
        try
        {
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            m_mapperTool = new MapperTool();
            createChildNodes();
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        }
        catch (Exception exc)
        {
            TCSException e = TCSException.produceTCSException(exc);
            String outErrXml = e.getOutputErrorXml();
            m_connector.WriteLine(outErrXml);
        }
    
    }

    public void createChildNodes() throws Exception {
        XmlNode defList = m_tcs.getConnector().getMetaDataDEFList();
        //for (int i = 0; i < defList.ChildNodes.Count; i++)
        //{
        //    string defPath = defList.ChildNodes[i].Attributes[MLSConnector.ATTRIBUTE_DEFINITION_FILE].Value.ToLower();
        //    if (defPath.EndsWith(".sql"))
        //    {
        //        defList.RemoveChild(defList.ChildNodes[i]);
        //        i--;
        //    }
        //}
        MLSEnvironment mlsEnvironment = new MLSEnvironment(m_tcs.getConnector());
        net.toppro.components.mls.engine.MLSEngine[] m_mlsEngine;
        m_mlsEngine = new net.toppro.components.mls.engine.MLSEngine[defList.getChildNodes().size()];
        String defName = "";
        for (int i = 0;i < defList.getChildNodes().size();i++)
        {
            XmlNode ele = defList.getChildNodes().get(i);
            m_tcs.getConnector().setCurrentDEF(ele);
            String defPath = defList.getChildNodes().get(i).getAttributes().get(MLSConnector.ATTRIBUTE_DEFINITION_FILE).getValue();
            m_mlsEngine[i] = new net.toppro.components.mls.engine.MLSEngine(mlsEnvironment, Tcs.Mls.Util.loadFile(defPath), this);
            defName = (new File(defPath)).getName();
            m_mapperTool.DeleteResultFieldsByDefName(defName);
            if (i == 0)
            {
                String moduleID = ele.getAttributes().get(MLSConnector.ATTRIBUTE_MODULE_ID).getValue();
                m_mapperTool.DeleteStandardResultFieldsByModuleID(moduleID);
                try
                {
                    getMLSMetadata(m_mlsEngine[i]);
                }
                catch (Exception exc)
                {
                    m_mlsEngine[i].writeLine(exc.getMessage());
                }
            
            }
             
            populateDataAggResultLayout(m_mlsEngine[i],StringSupport.Trim(defName.toLowerCase()));
        }
    }

    private int[] getStandardResultFields(String defName) throws Exception {
        if (defName.endsWith(".def") || defName.endsWith("da.sql"))
            return Tcs.Mls.TCSStandardResultFields.getDataAggResultFields();
        else if (defName.endsWith("ag.sql"))
            return Tcs.Mls.TCSStandardResultFields.getAgentRosterResultFields();
        else if (defName.endsWith("of.sql"))
            return Tcs.Mls.TCSStandardResultFields.getOfficeRosterResultFields();
        else if (defName.endsWith("oh.sql"))
            return Tcs.Mls.TCSStandardResultFields.getOpenHouseResultFields();
            
        return null;
    }

    private void getMLSMetadata(net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        DBAccess dba = new DBAccess();
        String dataSourceID = "";
        IDataReader dr = dba.GetDataReader("select data_source_id from dbo.tcs_module_id_to_data_source_id where module_id=" + m_tcs.ModuleID);
        try
        {
            {
                if (dr.Read())
                    dataSourceID = dr["data_source_id"].toString();
                 
            }
        }
        finally
        {
            if (dr != null)
                Disposable.mkDisposable(dr).dispose();
             
        }
        CategorizationDal cd = new CategorizationDal("DACount");
        String userName = "";
        String password = "";
        String userAgentPassword = "";
        String userAgent = "";
        IDataReader dr = cd.SpDARETSConnectionInfoSel(dataSourceID);
        try
        {
            {
                if (dr.Read())
                {
                    userName = dr["RETSUserName"].toString();
                    password = dr["RETSPassword"].toString();
                    userAgent = dr["RETSUserAgent"].toString();
                    userAgentPassword = dr["RETSUserAgentPassword"].toString();
                }
                 
            }
        }
        finally
        {
            if (dr != null)
                Disposable.mkDisposable(dr).dispose();
             
        }
        BoardSetup setup = mlsEngine.getBoardSetup();
        net.toppro.components.mls.engine.BoardSetup.SecFieldIterator iterator = setup.getSecFieldIterator();
        BoardSetupField field = iterator.first();
        ArrayList passwords = ArrayList.Synchronized(new ArrayList(10));
        passwords.add(userName);
        passwords.add(password);
        int i2 = 0;
        while (field != null)
        {
            if (field.getInputType() != BoardSetupField.INPUT_TYPE_FINAL)
            {
                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Object.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                field.setValue(passwords.get(i2++).toString());
            }
             
            field = iterator.next();
        }
        mlsEngine.setUserAgent(userAgent);
        mlsEngine.setRETSUAPwd(userAgentPassword);
        mlsEngine.setBypassAgentRosterAuth(1);
        m_connector.ModuleID = m_tcs.ModuleID;
        mlsEngine.runMainScript(net.toppro.components.mls.engine.MLSEngine.MSFLAG_METADATA_ONLY | net.toppro.components.mls.engine.MLSEngine.MSFLAG_DATAAGG_METADATA_ONLY);
        String path = mlsEngine.getResultsFolder() + "Metadata.xml";
        dba.ExecuteNonQuery("delete from dbo.tcs_mls_metadata_fields where module_id=" + m_tcs.ModuleID);
        parseMLSMetadataPropertyClass(path,mlsEngine);
    }

    public void parseMLSMetadataPropertyClass(String path, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        String data = Tcs.Mls.Util.loadXmlFile(path);
        //string resource = mlsEngine.getDefParser().getValue(MLSEngine.SECTION_TCPIP, MLSEngine.ATTRIBUTE_PROPERTYRESOURCE);
        //if (string.IsNullOrEmpty(resource))
        //    resource = "Property";
        //if (!ParseMLSMetadataPropertyClassCompactFormat(data, path, resource, "0"))
        //    if (!ParseMLSMetadataPropertyClassCompactFormat(data, path, "PROPERTY", "0"))
        //        if (!ParseMLSMetadataPropertyClassCompactFormat(data, path, "PropertyResource", "0"))
        //            if (!ParseMLSMetadataPropertyClassCompactFormat(data, path, "PROPERTYRESOURCE", "0"))
        //                addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing Property resource");
        String resource = mlsEngine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_AGENTRESOURCE);
        if (StringSupport.isNullOrEmpty(resource))
            resource = "ActiveAgent";
         
        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,resource,"1"))
            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"ACTIVEAGENT","1"))
                if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"Agent","1"))
                    if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"AGENT","1"))
                        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"User","1"))
                            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"USER","1"))
                                addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing Agent resource");
                             
                         
                     
                 
             
         
        resource = mlsEngine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_OFFICERESOURCE);
        if (StringSupport.isNullOrEmpty(resource))
            resource = "Office";
         
        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,resource,"2"))
            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"OFFICE","2"))
                addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing Office resource");
             
         
        resource = mlsEngine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_OPENHOUSERESOURCE);
        if (StringSupport.isNullOrEmpty(resource))
            resource = "OpenHouse";
         
        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,resource,"3"))
            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"OPEN_HOUSE","3"))
                if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"open_house","3"))
                    if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"OPENHOUSE","3"))
                        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"openhouse","3"))
                            addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing OpenHouse resource");
                         
                     
                 
             
         
    }

    public boolean parseMLSMetadataPropertyClassCompactFormat(String data, String path, String resource, String resourceType) throws Exception {
        MapperTool mt = new MapperTool();
        XmlDocument doc = new XmlDocument();
        doc.PreserveWhitespace = true;
        doc.loadXml(data);
        XmlNode rets = doc.selectSingleNode("RETS");
        XmlNode prop = rets.selectSingleNode(".//METADATA-CLASS[@Resource='" + resource + "']");
        if (prop == null)
            return false;
         
        XmlNode columnNode = prop.selectSingleNode("./COLUMNS");
        if (columnNode == null)
            columnNode = prop.selectSingleNode("./Columns");
         
        String[] columns = StringSupport.Split(StringSupport.Trim(columnNode.getInnerXml()), '\t');
        int i = 0;
        HashMap<String,Integer> ColumnDict = new HashMap<String,Integer>();
        for (i = 0;i < columns.length;i++)
        {
            ColumnDict.put(columns[i], i);
        }
        XmlNodeList classNodes = prop.selectNodes("./DATA");
        String[][] matrix = new String[classNodes.size(), i];
        int n = 0;
        for (Object __dummyForeachVar1 : classNodes)
        {
            XmlNode node = (XmlNode)__dummyForeachVar1;
            String[] classAttributes = StringSupport.Split(StringSupport.Trim(node.getInnerXml()), '\t');
            for (int m = 0;m < classAttributes.length;m++)
            {
                matrix[n, m] = classAttributes[m];
            }
            String xpath = ".//METADATA-TABLE[@Class='" + matrix[n, ColumnDict.get("ClassName")] + "']";
            XmlNode classNode = rets.selectSingleNode(xpath);
            if (classNode == null)
                continue;
            else
            {
                XmlNode columnNode1 = classNode.selectSingleNode("./COLUMNS");
                if (columnNode1 == null)
                    columnNode1 = classNode.selectSingleNode("./Columns");
                 
                String[] columns1 = StringSupport.Split(StringSupport.Trim(columnNode1.getInnerXml()), '\t');
                int i1 = 0;
                HashMap<String,Integer> ColumnDict1 = new HashMap<String,Integer>();
                for (i1 = 0;i1 < columns1.length;i1++)
                {
                    ColumnDict1.put(columns1[i1].toLowerCase(), i1);
                }
                XmlNodeList classNodes1 = classNode.selectNodes("./DATA");
                String[][] matrix1 = new String[classNodes1.size(), i];
                int n1 = 0;
                for (Object __dummyForeachVar0 : classNodes1)
                {
                    XmlNode node1 = (XmlNode)__dummyForeachVar0;
                    String[] classAttributes1 = StringSupport.Split(StringSupport.Trim(node1.getInnerXml()), '\t');
                    for (int m = 0;m < classAttributes1.length;m++)
                    {
                        mt.PRTcsMlsMetadataFieldsInsert(null, m_tcs.ModuleID, ColumnDict1.containsKey("systemname") ? classAttributes1[ColumnDict1.get("systemname")] : "", ColumnDict1.containsKey("longname") ? classAttributes1[ColumnDict1.get("longname")] : "", ColumnDict1.containsKey("standardname") ? classAttributes1[ColumnDict1.get("standardname")] : "", ColumnDict1.containsKey("maximumLength") ? classAttributes1[ColumnDict1.get("maximumLength")] : "", ColumnDict1.containsKey("datatype") ? classAttributes1[ColumnDict1.get("datatype")] : "", resource, matrix[n, ColumnDict.get("ClassName")], resourceType);
                    }
                }
            } 
            //string filePath = path.Insert(path.Length - 4, "_" + resource + "_" + matrix[n, ColumnDict["ClassName"]]).Replace("_System", "");
            //Util.WriteFile(filePath, classNode.OuterXml);
            //MetadataFiles.Add(new string[] { matrix[n, ColumnDict["ClassName"]], matrix[n, ColumnDict["VisibleName"]], matrix[n, ColumnDict["Description"]], resource, filePath });
            n++;
        }
        return true;
    }

    private void populateDataAggResultLayout(net.toppro.components.mls.engine.MLSEngine mlsEngine, String defName) throws Exception {
        int groupSize = 1;
        //m_mlsEngine.getPropertyFieldGroups().Length;
        MLSCmaFields mlsCmaFields = mlsEngine.getCmaFields();
        String position = "";
        String displayName = "";
        String retsLongName = "";
        String property_type = "";
        String attributeExposure = "";
        String category = "";
        String systemName = "";
        String vendorName = mlsEngine.getDefParser().getValue("Common","NameOfOrigin");
        StringBuilder sb = new StringBuilder();
        boolean isPublicField = false;
        net.toppro.components.mls.engine.CmaField cfLease = mlsCmaFields.getStdField(Tcs.Mls.TCSStandardResultFields.STDF_STDFSALEORLEASE);
        boolean isRental = false;
        if (cfLease != null)
        {
            if (StringSupport.Compare(cfLease.defaultvalue, "lease", true) == 0)
                isRental = true;
             
        }
         
        int[] resultFieldList = getStandardResultFields(defName);
        if (resultFieldList == null)
            System.Diagnostics.Debug.Assert(resultFieldList == null, "Exception found：" + defName);
         
        for (int x = 0;x < resultFieldList.length;x++)
        {
            position = "";
            displayName = "";
            retsLongName = "";
            property_type = "";
            attributeExposure = "";
            category = "";
            systemName = "";
            if (resultFieldList[x] == Tcs.Mls.TCSStandardResultFields.STDF_CMAFEATURE || resultFieldList[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFROOMDIM)
                continue;
             
            net.toppro.components.mls.engine.CmaField cmaField = mlsCmaFields.getStdField(resultFieldList[x]);
            boolean isStandardField = false;
            switch(resultFieldList[x])
            {
                case Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPENORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIERNORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIESNORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNTNORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENTNORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM: 
                case Tcs.Mls.TCSStandardResultFields.STDF_PUBLICLISTINGSTATUS_NODEFNAME: 
                case Tcs.Mls.TCSStandardResultFields.STDF_DEFTYPE_NODEFNAME: 
                    isStandardField = true;
                    break;
                case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM1: 
                case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM2: 
                case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM3: 
                case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM4: 
                case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM5: 
                    continue;
            
            }
            if (cmaField != null)
            {
                systemName = cmaField.getRecordPosition().replace("\"", "");
                if (systemName.startsWith("\\"))
                {
                    if (systemName.indexOf(",") < 0)
                    {
                        net.toppro.components.mls.engine.CmaField cfd = mlsCmaFields.getField(systemName.replace("\\", ""));
                        if (cfd != null)
                        {
                            systemName = cfd.getRecordPosition().replace("\"", "");
                                ;
                            if (systemName.startsWith("\\"))
                            {
                                if (systemName.indexOf(",") < 0)
                                {
                                    net.toppro.components.mls.engine.CmaField cfd1 = mlsCmaFields.getField(systemName.replace("\\", ""));
                                    if (cfd1 != null)
                                    {
                                        systemName = cfd1.getRecordPosition().replace("\"", "");
                                            ;
                                        retsLongName = cfd1.retsLongName;
                                    }
                                     
                                }
                                 
                            }
                             
                            if (StringSupport.isNullOrEmpty(retsLongName))
                                retsLongName = cfd.retsLongName;
                             
                        }
                         
                    }
                     
                }
                 
                //if (position.StartsWith("@"))
                //{
                //    systemName = position.Replace("@", "").Replace("\\", "");
                //}
                if (StringSupport.isNullOrEmpty(retsLongName))
                    retsLongName = cmaField.retsLongName;
                 
                if (isRental)
                    property_type = "RNT";
                else
                    property_type = null; 
                if (!StringSupport.isNullOrEmpty(systemName) && StringSupport.isNullOrEmpty(retsLongName) && !systemName.equals("\\\\"))
                    retsLongName = Tcs.Mls.Util.getNewGuid();
                 
                if (!StringSupport.isNullOrEmpty(retsLongName) && MLSConnector.RUN_LOCAL == 0)
                {
                    m_mapperTool.PrTcsStandardResultFieldsInsert(null, null, defName, cmaField.name, retsLongName, property_type, systemName);
                }
                 
                if (MLSConnector.RUN_LOCAL > 0)
                    sb.append(defName + "," + cmaField.name + "," + cmaField.getRecordPosition().replace(",", "!#!") + "\r\n");
                 
            }
            else
            {
                if (MLSConnector.RUN_LOCAL > 0 && !StringSupport.isNullOrEmpty(Tcs.Mls.TCSStandardResultFields.getDEFName(resultFieldList[x])))
                    sb.append(defName + "," + Tcs.Mls.TCSStandardResultFields.getDEFName(resultFieldList[x]) + "," + "" + "," + "" + "," + "" + "\r\n");
                 
            } 
        }
        if (MLSConnector.RUN_LOCAL > 0)
        {
            Tcs.Mls.Util.writeFile("c:\\temp\\" + defName.substring(0, (0) + (defName.length() - 4)) + ".csv",sb.toString());
            return ;
        }
         
        //    string defPath = defList.ChildNodes[i].Attributes[MLSConnector.ATTRIBUTE_DEFINITION_FILE].Value.ToLower();
        if (defName.endsWith(".sql"))
            return ;
         
        for (int m = 0;m < mlsEngine.getDefParser().getCategorizationGroups().length;m++)
        {
            position = "";
            displayName = "";
            retsLongName = "";
            property_type = "";
            attributeExposure = "";
            category = "";
            systemName = "";
            for (int fieldIndex : mlsCmaFields.getFeatureGroupList().get(m))
            {
                net.toppro.components.mls.engine.CmaField cf = mlsCmaFields.getField(fieldIndex);
                if (cf != null)
                    displayName = cf.getDisplayName();
                 
                if (StringSupport.isNullOrEmpty(displayName))
                    displayName = cf.getRetsLongName();
                 
                retsLongName = cf.getRetsLongName();
                category = cf.category;
                if ((cf.displayrule.equals("4") || StringSupport.isNullOrEmpty(cf.displayrule)) && (cf.attributeexposure.equals("60") || StringSupport.isNullOrEmpty(cf.attributeexposure)))
                    isPublicField = true;
                 
                if (isPublicField)
                {
                    if (!StringSupport.isNullOrEmpty(displayName) && !StringSupport.isNullOrEmpty(category))
                    {
                        String propertyType = null;
                        String __dummyScrutVar1 = category;
                        if (__dummyScrutVar1.equals("condoinfo"))
                        {
                            propertyType = "CON";
                        }
                        else if (__dummyScrutVar1.equals("landinfo"))
                        {
                            propertyType = "LND";
                        }
                        else if (__dummyScrutVar1.equals("mobileinfo"))
                        {
                            propertyType = "MOB";
                        }
                        else if (__dummyScrutVar1.equals("rentalinfo"))
                        {
                            propertyType = "RNT";
                        }
                        else if (__dummyScrutVar1.equals("cominfo"))
                        {
                            propertyType = "COM";
                        }
                        else if (__dummyScrutVar1.equals("farminfo"))
                        {
                            propertyType = "FRM";
                        }
                        else if (__dummyScrutVar1.equals("multiunitinfo"))
                        {
                            propertyType = "MFM";
                        }
                               
                        if (isRental)
                            propertyType = "RNT";
                         
                        m_mapperTool.PrTcsResultFieldsInsert(null, null, defName, cf.name, null, propertyType, displayName, retsLongName, null, category, null, null, null, null, null);
                    }
                     
                }
                 
            }
        }
    }

}


