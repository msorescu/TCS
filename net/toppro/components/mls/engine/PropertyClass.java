//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:47 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.DataAggResultFieldType;
import net.toppro.components.mls.engine.Layout;
import net.toppro.components.mls.engine.LayoutType;
import net.toppro.components.mls.engine.Lookup;
import net.toppro.components.mls.engine.LookupType;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.PropertyFieldGroup;
import net.toppro.components.mls.engine.ResultFieldType;
import net.toppro.components.mls.engine.SearchFieldType;
import Tcs.Mls.TCSStandardResultFields;

public class PropertyClass   
{
    public static final String CONTROL_TYPE_NUMBER = "Number";
    public static final String CONTROL_TYPE_TEXTBOX = "Text Box";
    public static final String CONTROL_TYPE_PICKLIST_TYPEABLE = "Picklist Typeable";
    public static final String CONTROL_TYPE_PICKLIST = "Dropdown list";
    public static final String CONTROL_TYPE_CHECKBOX = "Check Box";
    public static final String CONTROL_TYPE_LABELPANEL = "Label Panel";
    public static final String CONTROL_TYPE_FILESELECT_PANEL = "File Select Panel";
    public static final String CONTROL_TYPE_FOLDERSELECT_PANEL = "Folder Select Panel";
    public static final String CONTROL_TYPE_PICTUREFILESELECT_PANEL = "Picture File Select Panel";
    public static final String CONTROL_TYPE_CALENDAR = "Calendar";
    public static final String RESULT_MLS_LAYOUT_CAPTION = "MLS Search";
    private String className = "";
    private String defFileName = "";
    private String defFilePath = "";
    private net.toppro.components.mls.engine.MLSEngine m_mlsEngine;
    private Board m_board;
    private Layout[] m_searchLayout;
    private Layout[] m_resultLayout;
    public PropertyClass() throws Exception {
    }

    public void initPropertyClass(Board board, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        m_board = board;
        m_mlsEngine = mlsEngine;
        if (!defFilePath.toLowerCase().endsWith("da.sql"))
        {
            populateSearchLayout();
            populateResultLayout();
        }
         
        populateDataAggResultLayout();
    }

    public Board getBoard() throws Exception {
        return m_board;
    }

    private void populateSearchLayout() throws Exception {
        int groupSize = m_mlsEngine.getPropertyFieldGroups().length;
        m_searchLayout = new Layout[groupSize];
        String[] standardSearchNames = m_mlsEngine.getDefParser().getAttributiesFor("Standard_Search");
        HashMap<String,String> standardNameDic = new HashMap<String,String>();
        if (standardSearchNames != null)
        {
            for (int i = 0;i < standardSearchNames.length;i++)
            {
                String val = m_mlsEngine.getDefParser().getValue("Standard_Search",standardSearchNames[i]);
                if (StringSupport.isNullOrEmpty(val))
                    continue;
                 
                val = val.toUpperCase();
                if (standardNameDic.containsKey(val))
                    continue;
                 
                // this case should be an def file issue;
                standardNameDic.put(val, standardSearchNames[i]);
                if (standardSearchNames[i].toUpperCase().equals("ST_PType".toUpperCase()))
                {
                    m_board.addSearchFieldByDef(defFilePath,val);
                }
                 
            }
        }
         
        for (int i = 0;i < groupSize;i++)
        {
            PropertyFieldGroup propertyFieldGroup = m_mlsEngine.getPropertyFieldGroups()[i];
            m_searchLayout[i] = new Layout(this,propertyFieldGroup.getCaption(),Layout.LAYOUT_TYPE_SEARCH);
            int n = 0;
            for (int j = 0;j < propertyFieldGroup.size();j++)
            {
                net.toppro.components.mls.engine.PropertyField propertyField = propertyFieldGroup.getField(j);
                SearchFieldType searchFieldType = new SearchFieldType();
                searchFieldType.setSystemName(propertyField.getName());
                searchFieldType.setDisplayName(propertyField.getCaption());
                searchFieldType.setStandardName((standardNameDic.containsKey(searchFieldType.getSystemName())) ? standardNameDic.get(searchFieldType.getSystemName()) : "");
                searchFieldType.setControlTypeID(String.valueOf(propertyField.getControlType()));
                if (propertyField.getControlType() == net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_LABELPANEL)
                    continue;
                 
                searchFieldType.setControlTypeDescription(getControlTypeDescription(propertyField.getControlType()));
                searchFieldType.setLookupName(addLookup(propertyField));
                searchFieldType.setDelimeter(propertyField.getInitDelimiter());
                searchFieldType.setHelpText(propertyField.getTip());
                searchFieldType.setRequiredFlag(propertyField.isRequired() ? "Y" : "N");
                String searchFieldName = m_board.addToSearchTable(searchFieldType);
                LayoutType searchLayoutType = new LayoutType();
                searchLayoutType.setFieldName(searchFieldName);
                searchLayoutType.setDisplyOrder(String.valueOf((n + 1)));
                m_searchLayout[i].addLayoutType(searchLayoutType);
                n++;
            }
        }
    }

    private void populateResultLayout() throws Exception {
        int groupSize = 1;
        //m_mlsEngine.getPropertyFieldGroups().Length;
        m_resultLayout = new Layout[groupSize];
        String caption = "";
        for (int i = 0;i < groupSize;i++)
        {
            MLSCmaFields mlsCmaFields = m_mlsEngine.getCmaFields();
            m_resultLayout[i] = new Layout(this,RESULT_MLS_LAYOUT_CAPTION,Layout.LAYOUT_TYPE_RESULT);
            int n = 0;
            //Add Standard Status field to Result layout
            ResultFieldType resultFieldType;
            //resultFieldType = new ResultFieldType();
            //resultFieldType.SystemName = "Status_STDF";
            //resultFieldType.DisplayName = "";
            //resultFieldType.DataTypeID = CmaField.CMA_FLDTYPE_TEXT.ToString();
            //resultFieldType.DataTypeDescription = GetDataTypeDescription(CmaField.CMA_FLDTYPE_TEXT);
            //resultFieldType.StandardName = "Status_STDF";
            //resultFieldType.VisibleFlag = "N";
            String resultFieldName;
            //resultFieldName = m_board.AddToResultTable(resultFieldType);
            LayoutType resultLayoutType;
            //resultLayoutType = new LayoutType();
            //resultLayoutType.FieldName = resultFieldName;
            //resultLayoutType.DisplyOrder = (n + 1).ToString();
            //n++;
            //m_resultLayout[i].AddLayoutType(resultLayoutType);
            int statusIndex = mlsCmaFields.getStdFieldIndex(TCSStandardResultFields.STDF_CMAIDENTIFIER);
            for (int j = -1;j < mlsCmaFields.size();j++)
            {
                net.toppro.components.mls.engine.CmaField cmaField = null;
                String displayRule = "";
                if (statusIndex == j)
                    continue;
                 
                if (j != -1)
                    cmaField = mlsCmaFields.getField(j);
                else
                    cmaField = mlsCmaFields.getField(statusIndex); 
                System.Diagnostics.Trace.Assert(cmaField == null);
                caption = cmaField.getCaption();
                String fName = cmaField.getName();
                int fieldID = cmaField.getStdId();
                displayRule = cmaField.getDisplayRule();
                if (StringSupport.isNullOrEmpty(displayRule))
                {
                    if (fieldID > -1)
                        displayRule = TCSStandardResultFields.getDisplayRule(fieldID);
                     
                }
                 
                if (StringSupport.isNullOrEmpty(displayRule))
                    displayRule = "4";
                 
                if (caption.length() == 0 && !MLSCmaFields.isCMAFeild(fieldID) && !(StringSupport.Compare(fName, "YearBuilt_STDF", true) == 0 || StringSupport.Compare(fName, "TPOSTATE", true) == 0 || StringSupport.Compare(fName, "Status_STDF", true) == 0 || fieldID == TCSStandardResultFields.STDF_STDFSTREETNAMEPARSED || fieldID == TCSStandardResultFields.STDF_STDFSTREETTYPE || fieldID == TCSStandardResultFields.STDF_STDFSTREETDIRPREFIX || fieldID == TCSStandardResultFields.STDF_STDFSTREETDIRSUFFIX))
                    continue;
                 
                //&& !cmaField.getVisible())
                resultFieldType = new ResultFieldType();
                if (fieldID == TCSStandardResultFields.STDF_CMAFEATURE)
                {
                    resultFieldType.setSystemName(cmaField.getName());
                    resultFieldType.setDisplayName("Features");
                    resultFieldType.setDataTypeID(String.valueOf(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_RICHTEXT));
                    resultFieldType.setDataTypeDescription(getDataTypeDescription(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_RICHTEXT));
                    resultFieldType.setVisibleFlag("Y");
                }
                else if (fieldID == TCSStandardResultFields.STDF_NOTES)
                {
                    resultFieldType.setSystemName(cmaField.getName());
                    resultFieldType.setDisplayName("Comments");
                    resultFieldType.setDataTypeID(String.valueOf(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_RICHTEXT));
                    resultFieldType.setDataTypeDescription(getDataTypeDescription(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_RICHTEXT));
                    resultFieldType.setVisibleFlag("Y");
                }
                else
                {
                    resultFieldType.setSystemName(cmaField.getName());
                    resultFieldType.setDisplayName(caption);
                    resultFieldType.setDataTypeID(String.valueOf(cmaField.getFieldType()));
                    resultFieldType.setDataTypeDescription(getDataTypeDescription(cmaField.getFieldType()));
                    resultFieldType.setVisibleFlag(caption.length() == 0 ? "N" : "Y");
                }  
                resultFieldType.setDisplayRule(displayRule);
                if (j != -1)
                    resultFieldType.setStandardName(mlsCmaFields.getXmlName(j));
                else
                    resultFieldType.setStandardName(mlsCmaFields.getXmlName(statusIndex)); 
                String __dummyScrutVar0 = cmaField.getName().toUpperCase();
                if (__dummyScrutVar0.equals("YEARBUILT_STDF"))
                {
                    resultFieldType.setStandardName("YearBuilt_STDF");
                }
                else if (__dummyScrutVar0.equals("STATUS_STDF"))
                {
                    resultFieldType.setStandardName("Status_STDF");
                }
                else if (__dummyScrutVar0.equals("CMABATHROOMS") || __dummyScrutVar0.equals("CMASQUAREFEET") || __dummyScrutVar0.equals("CMASTORIES") || __dummyScrutVar0.equals("CMATAXAMOUNT") || __dummyScrutVar0.equals("CMAASSESSMENT") || __dummyScrutVar0.equals("CMALOTSIZE"))
                {
                    resultFieldType.setStandardName(resultFieldType.getStandardName().replace("_MLS", "_STDF"));
                }
                   
                switch(fieldID)
                {
                    case TCSStandardResultFields.STDF_STDFSTREETTYPE: 
                    case TCSStandardResultFields.STDF_STDFSTREETDIRPREFIX: 
                    case TCSStandardResultFields.STDF_STDFSTREETDIRSUFFIX: 
                        resultFieldType.setSystemName(cmaField.getName() + "LONG");
                        String stName = resultFieldType.getStandardName();
                        resultFieldType.setStandardName(stName);
                        resultFieldName = m_board.addToResultTable(resultFieldType);
                        resultLayoutType = new LayoutType();
                        resultLayoutType.setFieldName(resultFieldName);
                        resultLayoutType.setDisplyOrder(String.valueOf((n + 1)));
                        n++;
                        m_resultLayout[i].addLayoutType(resultLayoutType);
                        ResultFieldType resultFieldTypeShort = new ResultFieldType();
                        resultFieldTypeShort.setSystemName(cmaField.getName() + "SHORT");
                        resultFieldTypeShort.setStandardName(stName.replace("Long", "Short"));
                        resultFieldTypeShort.setDisplayName(caption);
                        resultFieldTypeShort.setDataTypeID(String.valueOf(cmaField.getFieldType()));
                        resultFieldTypeShort.setDataTypeDescription(getDataTypeDescription(cmaField.getFieldType()));
                        resultFieldTypeShort.setVisibleFlag(caption.length() == 0 ? "N" : "Y");
                        resultFieldTypeShort.setDisplayRule(displayRule);
                        resultFieldName = m_board.addToResultTable(resultFieldTypeShort);
                        resultLayoutType = new LayoutType();
                        resultLayoutType.setFieldName(resultFieldName);
                        resultLayoutType.setDisplyOrder(String.valueOf((n + 1)));
                        n++;
                        m_resultLayout[i].addLayoutType(resultLayoutType);
                        continue;
                
                }
                resultFieldName = m_board.addToResultTable(resultFieldType);
                resultLayoutType = new LayoutType();
                resultLayoutType.setFieldName(resultFieldName);
                resultLayoutType.setDisplyOrder(String.valueOf((n + 1)));
                n++;
                m_resultLayout[i].addLayoutType(resultLayoutType);
            }
            resultFieldType = new ResultFieldType();
            resultFieldType.setSystemName("PicID");
            resultFieldType.setDisplayName("PicID");
            resultFieldType.setDataTypeID(String.valueOf(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT));
            resultFieldType.setDataTypeDescription(getDataTypeDescription(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT));
            resultFieldType.setVisibleFlag("N");
            resultFieldType.setStandardName("PicID");
            resultFieldType.setDisplayRule("5");
            resultFieldName = m_board.addToResultTable(resultFieldType);
            resultLayoutType = new LayoutType();
            resultLayoutType.setFieldName(resultFieldName);
            resultLayoutType.setDisplyOrder(String.valueOf((n + 1)));
            n++;
            m_resultLayout[i].addLayoutType(resultLayoutType);
            if (mlsCmaFields.getStdField(TCSStandardResultFields.STDF_TPOSTATE) == null)
            {
                resultFieldType = new ResultFieldType();
                resultFieldType.setSystemName("TPOSTATE");
                resultFieldType.setDisplayName("");
                resultFieldType.setDataTypeID(String.valueOf(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT));
                resultFieldType.setDataTypeDescription(getDataTypeDescription(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT));
                resultFieldType.setVisibleFlag("N");
                resultFieldType.setStandardName("State");
                resultFieldType.setDisplayRule("4");
                resultFieldName = m_board.addToResultTable(resultFieldType);
                resultLayoutType = new LayoutType();
                resultLayoutType.setFieldName(resultFieldName);
                resultLayoutType.setDisplyOrder(String.valueOf((n + 1)));
                n++;
                m_resultLayout[i].addLayoutType(resultLayoutType);
            }
             
            if (mlsCmaFields.getField("Status_STDF") == null)
            {
                resultFieldType = new ResultFieldType();
                resultFieldType.setSystemName("Status_STDF");
                resultFieldType.setDisplayName("");
                resultFieldType.setDataTypeID(String.valueOf(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT));
                resultFieldType.setDataTypeDescription(getDataTypeDescription(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT));
                resultFieldType.setVisibleFlag("N");
                resultFieldType.setStandardName("Status_STDF");
                resultFieldType.setDisplayRule("4");
                resultFieldName = m_board.addToResultTable(resultFieldType);
                resultLayoutType = new LayoutType();
                resultLayoutType.setFieldName(resultFieldName);
                resultLayoutType.setDisplyOrder(String.valueOf((n + 1)));
                n++;
                m_resultLayout[i].addLayoutType(resultLayoutType);
            }
             
            if (mlsCmaFields.getField("YearBuilt_STDF") == null)
            {
                resultFieldType = new ResultFieldType();
                resultFieldType.setSystemName("YearBuilt_STDF");
                resultFieldType.setDisplayName("");
                resultFieldType.setDataTypeID(String.valueOf(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_NUMBER));
                resultFieldType.setDataTypeDescription(getDataTypeDescription(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_NUMBER));
                resultFieldType.setVisibleFlag("N");
                resultFieldType.setStandardName("YearBuilt_STDF");
                resultFieldType.setDisplayRule("4");
                resultFieldName = m_board.addToResultTable(resultFieldType);
                resultLayoutType = new LayoutType();
                resultLayoutType.setFieldName(resultFieldName);
                resultLayoutType.setDisplyOrder(String.valueOf((n + 1)));
                n++;
                m_resultLayout[i].addLayoutType(resultLayoutType);
            }
             
        }
    }

    private void populateDataAggResultLayout() throws Exception {
        int groupSize = 1;
        //m_mlsEngine.getPropertyFieldGroups().Length;
        MLSCmaFields mlsCmaFields = m_mlsEngine.getCmaFields();
        String caption = "";
        String displayName = "";
        String typeID = "2";
        String displayRule = "";
        String attributeExposure = "";
        for (int m = 0;m < m_mlsEngine.getDefParser().getCategorizationGroups().length;m++)
        {
            //string[] fieldsPrefix = mlsCmaFields.GetSubFieldList(stdFeatures[m]);
            if (!m_board.getCategoryOrder().containsKey((String)m_mlsEngine.getDefParser().getCategorizationGroups()[m]))
            {
                m_board.getCategoryOrder().put((String)m_mlsEngine.getDefParser().getCategorizationGroups()[m], new HashMap<String,String>());
                m_board.getCategoryCount().put((String)m_mlsEngine.getDefParser().getCategorizationGroups()[m], 0);
            }
             
        }
        for (int x = 0;x < TCSStandardResultFields.getDataAggResultFields().length;x++)
        {
            if (TCSStandardResultFields.getDataAggResultFields()[x] == TCSStandardResultFields.STDF_CMAFEATURE || TCSStandardResultFields.getDataAggResultFields()[x] == TCSStandardResultFields.STDF_STDFROOMDIM)
                continue;
             
            net.toppro.components.mls.engine.CmaField cmaField = mlsCmaFields.getStdField(TCSStandardResultFields.getDataAggResultFields()[x]);
            boolean isStandardField = false;
            switch(TCSStandardResultFields.getDataAggResultFields()[x])
            {
                case TCSStandardResultFields.STDF_STDFPROPERTYTYPENORM: 
                case TCSStandardResultFields.STDF_CMAIDENTIFIERNORM: 
                case TCSStandardResultFields.STDF_CMABATHROOMSNORM: 
                case TCSStandardResultFields.STDF_CMALOTSIZENORM: 
                case TCSStandardResultFields.STDF_CMASQUAREFEETNORM: 
                case TCSStandardResultFields.STDF_CMASTORIESNORM: 
                case TCSStandardResultFields.STDF_CMATAXAMOUNTNORM: 
                case TCSStandardResultFields.STDF_CMAASSESSMENTNORM: 
                case TCSStandardResultFields.STDF_CMAAGENORM: 
                case TCSStandardResultFields.STDF_STDFLISTAGENTIDTIGERLEAD: 
                case TCSStandardResultFields.STDF_STDFLISTAGENTIDSHOWINGTIME: 
                case TCSStandardResultFields.STDF_STDFLISTOFFICEIDTIGERLEAD: 
                case TCSStandardResultFields.STDF_STDFLISTOFFICEIDSHOWINGTIME: 
                case TCSStandardResultFields.STDF_PUBLICLISTINGSTATUS_NODEFNAME: 
                    isStandardField = true;
                    break;
                case TCSStandardResultFields.STDF_STDFIDXCUSTOM1: 
                case TCSStandardResultFields.STDF_STDFIDXCUSTOM2: 
                case TCSStandardResultFields.STDF_STDFIDXCUSTOM3: 
                case TCSStandardResultFields.STDF_STDFIDXCUSTOM4: 
                case TCSStandardResultFields.STDF_STDFIDXCUSTOM5: 
                    continue;
            
            }
            if (cmaField != null)
            {
                if (TCSStandardResultFields.getDataAggResultFields()[x] != TCSStandardResultFields.STDF_DEFTYPE_NODEFNAME)
                    caption = cmaField.getCaption();
                else
                    caption = "DEF type"; 
                displayName = cmaField.getDisplayName();
                if (StringSupport.isNullOrEmpty(displayName))
                    displayName = TCSStandardResultFields.getDisplayName(TCSStandardResultFields.getDataAggResultFields()[x]);
                 
                if (StringSupport.isNullOrEmpty(displayName))
                    displayName = StringSupport.Trim(caption);
                 
                typeID = String.valueOf(cmaField.type);
                displayRule = cmaField.getDisplayRule();
                if (StringSupport.isNullOrEmpty(displayRule))
                    displayRule = TCSStandardResultFields.getDisplayRule(TCSStandardResultFields.getDataAggResultFields()[x]);
                 
                if (StringSupport.isNullOrEmpty(displayRule))
                    displayRule = "4";
                 
                attributeExposure = cmaField.attributeexposure;
                if (StringSupport.isNullOrEmpty(attributeExposure))
                    attributeExposure = TCSStandardResultFields.getAttributeExposure(TCSStandardResultFields.getDataAggResultFields()[x]);
                 
                if (StringSupport.isNullOrEmpty(attributeExposure))
                    attributeExposure = "60";
                 
            }
            else
            {
                if (m_board.getIsLastDefFile() && isStandardField)
                {
                    displayName = TCSStandardResultFields.getDisplayName(TCSStandardResultFields.getDataAggResultFields()[x]);
                    typeID = "2";
                    displayRule = TCSStandardResultFields.getDisplayRule(TCSStandardResultFields.getDataAggResultFields()[x]);
                    attributeExposure = TCSStandardResultFields.getAttributeExposure(TCSStandardResultFields.getDataAggResultFields()[x]);
                }
                else
                    continue; 
            } 
            if (m_board.getIsLastDefFile())
            {
                if (StringSupport.isNullOrEmpty(displayName))
                {
                    displayName = TCSStandardResultFields.getDisplayName(TCSStandardResultFields.getDataAggResultFields()[x]);
                }
                 
            }
             
            DataAggResultFieldType resultFieldType = new DataAggResultFieldType();
            resultFieldType.setSystemName(TCSStandardResultFields.getXmlName(TCSStandardResultFields.getDataAggResultFields()[x]));
            resultFieldType.setReferenceName(resultFieldType.getSystemName().toUpperCase());
            resultFieldType.setDisplayName(displayName);
            resultFieldType.setRetsLongName(((cmaField != null) ? cmaField.retsLongName : ""));
            resultFieldType.setNormalized(TCSStandardResultFields.getNormalized(TCSStandardResultFields.getDataAggResultFields()[x]));
            if (StringSupport.Compare(Tcs.Mls.TCSStandardResultFields.getXmlName(Tcs.Mls.TCSStandardResultFields.getDataAggResultFields()[x]), Tcs.Mls.TCSStandardResultFields.getXmlName(Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD), true) == 0)
                typeID = String.valueOf(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME);
             
            resultFieldType.setDataTypeID(typeID);
            resultFieldType.setDataTypeDescription(getDataTypeDescription(Integer.valueOf(typeID)));
            //if (x != TCSStandardResultFields.STDF_CMAFEATURE && x != TCSStandardResultFields.STDF_STDFROOMDIM)
            resultFieldType.setIsStandard("1");
            //else
            //    resultFieldType.IsStandard = "0";
            //resultFieldType.VisibleFlag = caption.Length == 0 ? "N" : "Y";
            resultFieldType.setDisplayRule(displayRule);
            resultFieldType.setAttributeExposure(attributeExposure);
            addCategoryAttributes(resultFieldType,cmaField);
            m_board.addToDataAggResultTable(resultFieldType);
        }
        for (int i = 0;i < groupSize;i++)
        {
            int[] nonStdFields = mlsCmaFields.getNonStdFields();
            int n = 0;
            //Add Standard Status field to Result layout
            DataAggResultFieldType resultFieldType = new DataAggResultFieldType();
            String resultFieldName;
            for (int j = 0;j < nonStdFields.length;j++)
            {
                net.toppro.components.mls.engine.CmaField cmaField = mlsCmaFields.getField(nonStdFields[j]);
                displayName = cmaField.getDisplayName();
                if (StringSupport.isNullOrEmpty(displayName))
                    displayName = cmaField.getCaption();
                 
                displayRule = cmaField.getDisplayRule();
                if (StringSupport.isNullOrEmpty(displayRule))
                    displayRule = "4";
                 
                resultFieldType = new DataAggResultFieldType();
                resultFieldType.setSystemName(cmaField.getName() + "NS");
                resultFieldType.setReferenceName(resultFieldType.getSystemName().toUpperCase());
                resultFieldType.setDisplayName(displayName);
                resultFieldType.setRetsLongName(cmaField.retsLongName);
                resultFieldType.setDisplayRule(displayRule);
                resultFieldType.setNormalized("0");
                resultFieldType.setDataTypeID(String.valueOf(cmaField.getFieldType()));
                resultFieldType.setDataTypeDescription(getDataTypeDescription(cmaField.getFieldType()));
                resultFieldType.setIsStandard("0");
                addCategoryAttributes(resultFieldType,cmaField);
                //resultFieldType.VisibleFlag = caption.Length == 0 ? "N" : "Y";
                m_board.addToDataAggResultTable(resultFieldType);
            }
        }
        //For features and dimensions
        int[] stdFeatures = new int[]{ Tcs.Mls.TCSStandardResultFields.STDF_CMAFEATURE, Tcs.Mls.TCSStandardResultFields.STDF_STDFROOMDIM };
        for (int m = 0;m < m_mlsEngine.getDefParser().getCategorizationGroups().length;m++)
        {
            //string[] prefix = new string[] { "Feature_", "Room_" };
            //string[] fieldsPrefix = mlsCmaFields.GetSubFieldList(stdFeatures[m]);
            HashMap<String,String> cateOrder = null;
            String category = m_mlsEngine.getDefParser().getCategorizationGroups()[m].toString();
            if (m_board.getCategoryOrder().containsKey(category))
                cateOrder = m_board.getCategoryOrder().get(category);
            else
                cateOrder = new HashMap<String,String>(); 
            for (int fieldIndex : mlsCmaFields.getFeatureGroupList().get(m))
            {
                net.toppro.components.mls.engine.CmaField cf = mlsCmaFields.getField(fieldIndex);
                if (cf == null)
                    continue;
                 
                if (cf.getStdId() > -1)
                {
                    String xmlNameForStandardField = Tcs.Mls.TCSStandardResultFields.getXmlName(cf.getStdId());
                    if (!cateOrder.containsKey(xmlNameForStandardField))
                    {
                        cateOrder.put(xmlNameForStandardField, String.valueOf((m_board.getCategoryCount().get(category) + 1)));
                        m_board.getCategoryCount().put(category, m_board.getCategoryCount().get(category) + 1);
                    }
                     
                    continue;
                }
                 
                String featDisplayName = cf.displayname;
                if (StringSupport.isNullOrEmpty(featDisplayName))
                    continue;
                 
                if (StringSupport.isNullOrEmpty(featDisplayName))
                    featDisplayName = StringSupport.Trim(cf.getPrefix().replace(":", ""));
                 
                displayRule = cf.getDisplayRule();
                if (StringSupport.isNullOrEmpty(displayRule))
                    displayRule = "4";
                 
                DataAggResultFieldType resultFieldType = new DataAggResultFieldType();
                resultFieldType.setSystemName("MLS_" + Tcs.Mls.Util.convertStringToXMLNode(cf.xmlname));
                if (StringSupport.isNullOrEmpty(featDisplayName))
                    continue;
                 
                resultFieldType.setReferenceName(resultFieldType.getSystemName().toUpperCase());
                resultFieldType.setDisplayName(featDisplayName);
                resultFieldType.setRetsLongName(cf.retsLongName);
                resultFieldType.setDataTypeID(String.valueOf(cf.getFieldType()));
                resultFieldType.setDataTypeDescription(getDataTypeDescription(cf.getFieldType()));
                resultFieldType.setIsStandard("0");
                resultFieldType.setDisplayRule(displayRule);
                resultFieldType.setNormalized("0");
                addCategoryAttributes(resultFieldType,cf);
                String isAdded = m_board.addToDataAggResultTable(resultFieldType);
                if (!cateOrder.containsKey(resultFieldType.getSystemName()) && !StringSupport.isNullOrEmpty(isAdded))
                {
                    cateOrder.put(resultFieldType.getSystemName(), String.valueOf((m_board.getCategoryCount().get(category) + 1)));
                    m_board.getCategoryCount().put(category, m_board.getCategoryCount().get(category) + 1);
                }
                 
            }
            //resultFieldType.VisibleFlag = caption.Length == 0 ? "N" : "Y";
            m_board.getCategoryOrder().put(category, cateOrder);
        }
    }

    //Add standard fileds
    private void addCategoryAttributes(DataAggResultFieldType resultFieldType, net.toppro.components.mls.engine.CmaField cf) throws Exception {
        if (cf != null)
        {
            if (!StringSupport.isNullOrEmpty(cf.category))
                resultFieldType.setCategory(cf.category);
             
            if (!StringSupport.isNullOrEmpty(cf.orderincategory))
                resultFieldType.setOrderInCategory(cf.orderincategory);
             
            // to build a hash talbe for the order
            if (!StringSupport.isNullOrEmpty(cf.displayformatstring))
                resultFieldType.setDisplayFormatString(cf.displayformatstring);
             
            if (StringSupport.isNullOrEmpty(cf.attributeexposure))
            {
                if (StringSupport.isNullOrEmpty(resultFieldType.getAttributeExposure()))
                {
                    if (cf.displayrule.equals("4") || StringSupport.isNullOrEmpty(cf.displayrule))
                        resultFieldType.setAttributeExposure("60");
                     
                }
                 
            }
            else
                resultFieldType.setAttributeExposure(cf.attributeexposure); 
            //resultFieldType.DisplayListAsBullets = cf.displaylistasbullets;
            if (!StringSupport.isNullOrEmpty(cf.displaybehaviorbitmask))
                resultFieldType.setDisplayBehaviorBitmask(cf.displaybehaviorbitmask);
             
        }
         
    }

    private String addLookup(net.toppro.components.mls.engine.PropertyField propertyField) throws Exception {
        String result = "";
        if (propertyField.getControlType() == 2 || propertyField.getControlType() == 3)
        {
            //Todo: Add code
            Lookup lookup = new Lookup();
            lookup.setLookupName(propertyField.getName());
            for (int i = 0;i < propertyField.getInitValueCount();i++)
            {
                LookupType lookupType = new LookupType();
                lookupType.setLookupValue(propertyField.getUIInitValue(i));
                lookupType.setDisplayOrder(String.valueOf((i + 1)));
                lookup.addType(lookupType);
            }
            result = getBoard().addToLookupTable(lookup);
        }
         
        return result;
    }

    public static String getDataTypeDescription(int controlType) throws Exception {
        String typeDes = "";
        switch(controlType)
        {
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT: 
                typeDes = "Text";
                break;
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE: 
                typeDes = "Date";
                break;
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_CURRENCY: 
                typeDes = "Currency";
                break;
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_NUMBER: 
                typeDes = "Number";
                break;
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_BOOLEAN: 
                typeDes = "Boolean";
                break;
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_RICHTEXT: 
                typeDes = "Rich Text";
                break;
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME: 
                typeDes = "Date time";
                break;
            default: 
                typeDes = "Text";
                break;
        
        }
        return typeDes;
    }

    public static int getDataTypeID(String dataType) throws Exception {
        int typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT;
        String __dummyScrutVar4 = dataType;
        if (__dummyScrutVar4.equals("Text"))
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT;
        }
        else if (__dummyScrutVar4.equals("Date"))
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE;
        }
        else if (__dummyScrutVar4.equals("Currency"))
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_CURRENCY;
        }
        else if (__dummyScrutVar4.equals("Number") || __dummyScrutVar4.equals("Num"))
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_NUMBER;
        }
        else if (__dummyScrutVar4.equals("Boolean"))
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_BOOLEAN;
        }
        else if (__dummyScrutVar4.equals("RichText"))
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_RICHTEXT;
        }
        else if (__dummyScrutVar4.equals("DateTime") || __dummyScrutVar4.equals("Date:Time"))
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME;
        }
        else
        {
            typeID = net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT;
        }       
        return typeID;
    }

    private String getControlTypeDescription(int controlType) throws Exception {
        String result = CONTROL_TYPE_TEXTBOX;
        switch(controlType)
        {
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_NUMBER: 
                result = CONTROL_TYPE_NUMBER;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_TEXTBOX: 
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST_TYPEABLE: 
                result = CONTROL_TYPE_PICKLIST_TYPEABLE;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST: 
                result = CONTROL_TYPE_PICKLIST;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_CHECKBOX: 
                result = CONTROL_TYPE_CHECKBOX;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_LABELPANEL: 
                result = CONTROL_TYPE_LABELPANEL;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_FILESELECT_PANEL: 
                result = CONTROL_TYPE_FILESELECT_PANEL;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_FOLDERSELECT_PANEL: 
                result = CONTROL_TYPE_FOLDERSELECT_PANEL;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICTUREFILESELECT_PANEL: 
                result = CONTROL_TYPE_PICTUREFILESELECT_PANEL;
                break;
            case net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_CALENDAR: 
                result = CONTROL_TYPE_CALENDAR;
                break;
        
        }
        return result;
    }

    public String getClassName() throws Exception {
        return className;
    }

    public void setClassName(String value) throws Exception {
        className = value;
    }

    public String getDefFileName() throws Exception {
        return defFileName;
    }

    public void setDefFileName(String value) throws Exception {
        defFileName = value;
    }

    public String getDefFilePath() throws Exception {
        return defFilePath;
    }

    public void setDefFilePath(String value) throws Exception {
        defFilePath = value;
    }

    public void toXml() throws Exception {
        XmlWriter w = getBoard().getMLSSystem().getSystemXmlWriter();
        w.WriteStartElement(MC.PROPERTY_CLASS);
        w.WriteAttributeString(MC.CLASS_NAME, getClassName());
        w.WriteAttributeString(MC.DEF_FILE_NAME, getDefFileName());
        w.WriteStartElement(MC.METADATA_SEARCH_LAYOUT);
        for (int i = 0;i < m_searchLayout.length;i++)
        {
            m_searchLayout[i].toXml();
        }
        w.writeEndElement();
        w.WriteStartElement(MC.METADATA_RESULT_LAYOUT);
        for (int i = 0;i < m_resultLayout.length;i++)
        {
            m_resultLayout[i].toXml();
        }
        w.writeEndElement();
        w.writeEndElement();
    }

}


