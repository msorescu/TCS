//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:42 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Collections.ArrayListSupport;
import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.STRINGS;
import Tcs.Mls.TCSStandardResultFields;

public class MLSCmaFields   
{
    private static final String MLSREC_CMAPAGESREC = "CMAPagesRec";
    private static final String MLSREC_FLDNAME = "FldName";
    private static final String MLSREC_INPLEN = "InpLength";
    private static final String MLSREC_CUTBY = "CutBy";
    private static final String MLSREC_FLDTYPE = "FldType";
    private static final String MLSREC_TAKEAFTER = "TakeAfter";
    private static final String MLSREC_NUMREPS = "NumReps";
    private static final String MLSREC_MULTRECS = "MultRecs";
    //UPGRADE_NOTE: Final was removed from the declaration of 'CMA_FIELD_PROPERTIES_NAMES '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    //UPGRADE_NOTE: The initialization of  'CMA_FIELD_PROPERTIES_NAMES' was moved to static method 'net.toppro.components.mls.engine.MLSCmaFields'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1005'"
    private static final String[] CMA_FIELD_PROPERTIES_NAMES;
    //UPGRADE_NOTE: Final was removed from the declaration of 'CMA_REC_PROPERTIES_NAMES '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    private static final String[] CMA_REC_PROPERTIES_NAMES = new String[]{ MLSREC_FLDNAME, MLSREC_INPLEN, MLSREC_CUTBY, MLSREC_FLDTYPE, MLSREC_TAKEAFTER, MLSREC_NUMREPS, MLSREC_MULTRECS };
    public CSList<String> FEATURE_GROUPS = new CSList<String>();
    //new string []{"ExtFeatures", "Exterior Features"},
    //new string []{"IntFeatures", "Interior Features"},
    //new string []{"Appliances", "Appliances"},
    //new string []{"Rooms", "Rooms"},
    //new string []{"OtherFeatures", "Other Features"},
    //new string []{"MiscPropInfo", "Misc Property Information"},
    //new string []{"CondoInfo", "Condo Townhome Coop Information"},
    //new string []{"MobileInfo", "Mobile Home Information"},
    //new string []{"RentalInfo", "Rental Information"},
    //new string []{"MultiUnitInfo", "Multi Unit Information"},
    //new string []{"LandInfo", "Land Information"},
    //new string []{"ComInfo", "Commercial Industrial Business Information"},
    //new string []{"ListingAgentOffice", "Listing Agent Office"},
    //new string []{"BuyerAgentOffice", "Buyer Agent Office"},
    //new string []{"ListingInfo", "Listing Information"}
    //};
    public static final int[] ReturnIDsOnlyFieldList = new int[]{ TCSStandardResultFields.STDF_RECORDID, TCSStandardResultFields.STDF_PICID, TCSStandardResultFields.STDF_STDFLASTMOD, TCSStandardResultFields.STDF_STDFPICMOD, TCSStandardResultFields.STDF_STDFNUMBEROFPHOTOS };
    private CSList<CSList<Integer>> m_featureGroupList = new CSList<CSList<Integer>>();
    public CSList<CSList<Integer>> getFeatureGroupList() throws Exception {
        return m_featureGroupList;
    }

    public void setFeatureGroupList(CSList<CSList<Integer>> value) throws Exception {
        m_featureGroupList = value;
    }

    //UPGRADE_NOTE: Final was removed from the declaration of 'STD_FIELD_NAME'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    //public static readonly System.String[]STD_FIELD_NAME = new System.String[]{
    //    "RecordID",
    //    "CMAHouseNo",
    //    "CMAStreetName",
    //    "CMASuiteNo",
    //    "CMAIdentifier",
    //    "CMAHouseStyle",
    //    "CMAArea",
    //    "CMABedrooms",
    //    "CMABathrooms",
    //    "CMAListingPrice",
    //    "CMASalePrice",
    //    "CMAListingDate",
    //    "CMASaleDate",
    //    "CMASquareFeet",
    //    "CMALotSize",
    //    "CMAAge",
    //    "CMADaysOnMarket",
    //    "CMAFeature",
    //    "Notes",
    //    "CMAStories",
    //    "CMAGarage",
    //    "CMATaxAmount",
    //    "CMATaxYear",
    //    "CMAAssessment",
    //    "TPOCity",
    //    "TPOState",
    //    "TPOZip",
    //    "PicID",
    //    "STDFPropertytypeMLS",
    //    "STDFNumUnits",
    //    "STDFFullBathrooms",
    //    "STDFHalfBathrooms",
    //    "STDFQBathrooms",
    //    "STDFSubdivision",
    //    "STDFDOM",
    //    "STDFSearchPrice",
    //    "STDFStatusDate",
    //    "STDFLat",
    //    "STDFLong",
    //    "STDFListAgentID",
    //    "STDFListBrokerID",
    //    "STDFListOfficeName",
    //    "STDFListAgentFName",
    //    "STDFListAgentLName",
    //    "STDFAgentRemarks",
    //    "STDFVirtualTourURL",
    //    "STDFElemSchool",
    //    "STDFMiddleSchool",
    //    "STDFHighSchool",
    //    "STDFSchoolDistrict",
    //    "STDFViews",
    //    "STDFRoomDim",
    //    "STDFWaterfrontYN",
    //    "STDFWaterfrontDesc",
    //    "STDFLotDesc",
    //    "STDFDirections",
    //    "STDFListAgentPhone",
    //    "STDFListAgentEmail",
    //    "STDFLastMod",
    //    "STDFSaleOrLease",
    //    "STDFPendingDate",
    //    "STDFExpiredDate",
    //    "STDFInactiveDate",
    //    "STDFBuyerAgentID",
    //    "STDFBuyerBrokerID",
    //    "STDFBuyerOfficeName",
    //    "STDFBuyerAgentFName",
    //    "STDFBuyerAgentLName",
    //    "STDFBuyerAgentPhone",
    //    "STDFBuyerAgentEmail",
    //    "STDFAPNNumber",
    //    "STDFStreetNameParsed",
    //    "STDFStreetType",
    //    "STDFStreetDirPrefix",
    //    "STDFStreetDirSuffix",
    //    "STDFListAgentIDDataAgg",
    //    "STDFListOfficeIDDataAgg",
    //    "STDFBuyerAgentIDDataAgg",
    //    "STDFBuyerOfficeIDDataAgg",
    //    "STDFPicMod",
    //    "STDFCounty",
    //    "STDFRentIncome",
    //    "STDFZoning",
    //    "STDFOwnerFName",
    //    "STDFOwnerLName",
    //    "AG_NRDSID",
    //    "AG_AgentID",
    //    "AG_OfficeID",
    //    "AG_Status",
    //    "AG_FirstName",
    //    "AG_LastName",
    //    "AG_OfficePhone",
    //    "AG_CellPhone",
    //    "AG_HomePhone",
    //    "AG_Email",
    //    "AG_StreetAddress",
    //    "AG_City",
    //    "AG_State",
    //    "AG_ZipCode",
    //    "OF_NRDSID",
    //    "OF_OfficeID",
    //    "OF_Status",
    //    "OF_OfficeName",
    //    "OF_OfficePhone",
    //    "OF_OfficeFax",
    //    "OF_Email",
    //    "OF_WebsiteURL",
    //    "OF_StreetAddress",
    //    "OF_City",
    //    "OF_State",
    //    "OF_ZipCode",
    //    "STDFDisplayListingOnRDC",
    //    "STDFExcludeAddressFromRDC",
    //    "PublicLisitingStatusNotUsed",
    //    "DEFTypeNotUsed",
    //    "STDFThreeQBathrooms",
    //    "RDCFireplaceFeatures",
    //    "RDCHeatingFeatures",
    //    "RDCRoofingFeatures",
    //    "RDCExteriorConstructionFeatures",
    //    "RDCExteriorFeatures",
    //    "STDFDistressedShortSale",
    //    "STDFDistressedAuction",
    //    "STDFDistressedForeclosedREO",
    //    "RDCInteriorFeatures",
    //    "STDFNumberofGarageSpaces",
    //    "STDFCarport",
    //    "STDFLaundryRoom",
    //    "STDFDiningRoom",
    //    "STDFGameRoom",
    //    "STDFFamilyRoom",
    //    "STDFDenOffice",
    //    "STDFBasement",
    //    "STDFCentralAir",
    //    "STDFCentralHeat",
    //    "STDFForcedAir",
    //    "STDFHardwoodFloors",
    //    "STDFFireplace",
    //    "STDFSwimmingPool",
    //    "STDFRVBoatParking",
    //    "STDFSpaHotTub",
    //    "STDFHorseFacilities",
    //    "STDFTennisCourts",
    //    "STDFDisabilityFeatures",
    //    "STDFPetsAllowed",
    //    "STDFEnergyEfficientHome",
    //    "AG_LastMod",
    //    "AG_StatusDate",
    //    "OF_LastMod",
    //    "OF_StatusDate",
    //    "OH_MLSNumber",
    //    "OH_AttendedYN",
    //    "OH_RefreshmentYN",
    //    "OH_Type",
    //    "OH_StartDateTime",
    //    "OH_EndDateTime",
    //    "OH_TimeRangeDescription",
    //    "OH_Remarks",
    //    "OH_GateCode",
    //    "ReferencingKey"
    //};
    //protected internal static readonly string[] DISPLAY_NAMES = new string[]{
    //    "MLS Number",
    //    "House No",
    //    "Street Name",
    //    "Suite/Unit No",
    //    "Listing Status",
    //    "House Style",
    //    "Area",
    //    "Bedrooms",
    //    "Total Bathrooms",
    //    "Listing Price",
    //    "Sale Price",
    //    "Listing Date",
    //    "Sale Date",
    //    "Square Feet Living",
    //    "Lot Size",
    //    "Year Built",
    //    "Days on Market",
    //    "",//Features
    //    "Public Remarks",
    //    "Levels or Stories",
    //    "Garage",
    //    "Tax Amount",
    //    "Tax Year",
    //    "Tax Assessment",
    //    "City",
    //    "State",
    //    "Zip/Postal Code",
    //    "Picture ID",
    //    "Property Type",
    //    "Number of Units",
    //    "Full Bathrooms",
    //    "Half Bathrooms",
    //    "1/4 Bathrooms",
    //    "Subdivision",
    //    "",//STDFDOM
    //    "Search Price",
    //    "Status Date",
    //    "Latitude",
    //    "Longitude",
    //    "Listing Agent MLS ID",
    //    "Listing Broker MLS ID",
    //    "Listing Office Name",
    //    "Listing Agent First Name",
    //    "Listing Agent Last Name",
    //    "Agent Remarks",
    //    "Virtual tour URL",
    //    "Elementary School",
    //    "Middle School",
    //    "High School",
    //    "School District",
    //    "Views",
    //    "",//RoomDimensions
    //    "Waterfront",
    //    "Waterfront Description",
    //    "Lot Description",
    //    "Directions",
    //    "Listing Agent Contact Phone",
    //    "Listing Agent Email",
    //    "Listing Last Modified Date-Time",
    //    "Sale or Lease",
    //    "Pending Date",
    //    "Expired Date",
    //    "Inactive Date",
    //    "Buyer Agent MLS ID",
    //    "Buyer Broker MLS ID",
    //    "Buyer Office Name",
    //    "Buyer Agent First Name",
    //    "Buyer Agent Last Name",
    //    "Buyer Agent Contact Phone",
    //    "Buyer Agent Email",
    //    "APN Number",
    //    "Street Name",
    //    "Street Type",
    //    "Street Dir Prefix",
    //    "Street Dir Suffix",
    //    "Listing Agent MLS ID",
    //    "Listing Office MLS ID",
    //    "Buyer Agent MLS ID",
    //    "Buyer Office MLS ID",
    //    "Picture last modified",
    //    "County",
    //    "Rental Income",
    //    "Zoning",
    //    "Owner First Name",
    //    "Owner Last Name",
    //    "", //AG_NRDSID
    //    "", //AG_AgentID
    //    "", //AG_OfficeID
    //    "", //AG_Status
    //    "", //AG_FirstName
    //    "", //AG_LastName
    //    "", //AG_OfficePhone
    //    "", //AG_CellPhone
    //    "", //AG_HomePhone
    //    "", //AG_Email
    //    "", //AG_StreetAddress
    //    "", //AG_City
    //    "", //AG_State
    //    "", //AG_ZipCode
    //    "", //OF_NRDSID
    //    "", //OF_OfficeID
    //    "", //OF_Status
    //    "", //OF_OfficeName
    //    "", //OF_OfficePhone
    //    "", //OF_OfficeFax
    //    "", //OF_Email
    //    "", //OF_WebsiteURL
    //    "", //OF_StreetAddress
    //    "", //OF_City
    //    "", //OF_State
    //    "", //OF_ZipCode
    //    "Display Listing on RDC",//STDFDisplayListingonRDC
    //    "Exclude Address from RDC", //STDFExcludeAddressfromRDC
    //    "Public Listing Status", //PublicListingStatusNotUsed
    //    "DEF Type",//DEFTypeNotUsed
    //    "3/4 Bathrooms",//STDFThreeQBathrooms
    //    "Fireplace Features",//RDCFireplaceFeatures
    //    "Heating Features",//RDCHeatingFeatures
    //    "Roofing Features",//RDCRoofingFeatures
    //    "Exterior Construction Features",//RDCExteriorConsturctoinFeatures
    //    "Exterior Features",//RDCExteriorFeatures
    //    "Notice of Default Short Sale",//STDFDistressedShortSale
    //    "Auction",//STDFDistressedAuction
    //    "Bank, Real Estate Owned",//STDFDistressedForeclosedREO
    //    "Interior Features",
    //    "Number of Garage Spaces",//STDFNumberofGarageSpaces
    //    "Carport",//STDFCarport
    //    "Laundry Room",//STDFLaundryRoom
    //    "Dining Room",//STDFDiningRoom
    //    "Game Room",//STDFGameRoom
    //    "Family Room",//STDFFamilyRoom
    //    "Den Office",//STDFDenOffice
    //    "Basement",//STDFBasement
    //    "Central Air",//STDFCentralAir
    //    "Central Heat",//STDFCentralHeat
    //    "Forced Air",//STDFForcedAir
    //    "Hardwood Floors",//STDFHardwoodFloors
    //    "Fireplace",//STDFFireplace
    //    "Swimming Pool",//STDFSwimmingPool
    //    "RV/Boat Parking",//STDFRVBoatParking
    //    "Spa/Hot Tub",//STDFSpaHotTub
    //    "Horse Facilities",//STDFHorseFacilities
    //    "Tennis Courts",//STDFTennisCourts
    //    "Disability Features",//STDFDisabilityFeatures
    //    "Pets Allowed",//STDFPetsAllowed
    //    "Energy Efficient Home",//STDFEnergyEfficientHome
    //    "Last Modified Date Time",//AG_LastMod
    //    "Status Date",//AG_StatusDate
    //    "Last Modified Date Time",//OF_LastMod
    //    "Status Date",//OF_StatusDate
    //    "MLS Number",//OH_MLSNumber
    //    "Attended",//OH_AttendedYN
    //    "Refreshments",//OH_RefreshmentYN
    //    "Type",//OH_Type
    //    "Start Date Time",//OH_StartDateTime
    //    "End Date Time",//OH_EndDateTime
    //    "Time Range Description",//OH_TimeRangeDescription
    //    "Remarks",//OH_Remarks
    //    "Gate Code",//OH_GateCode
    //    "ReferencingKey"
    //};
    //public static readonly string[] DISPLAY_RULES = new string[]{
    //    "4",//MLS Number
    //    "4",//House No
    //    "4",//Street Name
    //    "4",//Suite/Unit No
    //    "4",//Listing Status
    //    "4",//House Style
    //    "4",//Area
    //    "4",//Bedrooms
    //    "4",//Total Bathrooms
    //    "4",//Listing Price
    //    "4",//Sale Price
    //    "4",//Listing Date
    //    "4",//Sale Date
    //    "4",//Square Feet Living
    //    "4",//Lot Size
    //    "4",//Year Built
    //    "4",//Days on Market
    //    "4",////Features
    //    "4",//Public Remarks
    //    "4",//Levels or Stories
    //    "4",//Garage
    //    "4",//Tax Amount
    //    "4",//Tax Year
    //    "4",//Tax Assessment
    //    "4",//City
    //    "4",//State
    //    "4",//Zip/Postal Code
    //    "5",//Picture ID
    //    "4",//Property Type
    //    "4",//Number of Units
    //    "4",//Full Bathrooms
    //    "4",//Half Bathrooms
    //    "4",//1/4 Bathrooms
    //    "4",//Subdivision
    //    "4",////STDFDOM
    //    "4",//Search Price
    //    "4",//Status Date
    //    "4",//Latitude
    //    "4",//Longitude
    //    "3",//Listing Agent MLS ID
    //    "3",//Listing Broker MLS ID
    //    "4",//Listing Office Name
    //    "4",//Listing Agent First Name
    //    "4",//Listing Agent Last Name
    //    "3",//Agent Remarks
    //    "4",//Virtual tour URL
    //    "4",//Elementary School
    //    "4",//Middle School
    //    "4",//High School
    //    "4",//School District
    //    "4",//Views
    //    "4",////RoomDimensions
    //    "4",//Waterfront
    //    "4",//Waterfront Description
    //    "4",//Lot Description
    //    "4",//Directions
    //    "4",//Listing Agent Contact Phone
    //    "4",//Listing Agent Email
    //    "3",//Listing Last Modified Date-Time
    //    "4",//Sale or Lease
    //    "3",//Pending Date
    //    "3",//Expired Date
    //    "3",//Inactive Date
    //    "3",//Buyer Agent MLS ID
    //    "3",//Buyer Broker MLS ID
    //    "4",//Buyer Office Name
    //    "4",//Buyer Agent First Name
    //    "4",//Buyer Agent Last Name
    //    "4",//Buyer Agent Contact Phone
    //    "4",//Buyer Agent Email
    //    "4",//APN Number
    //    "4",//Street Name
    //    "4",//Street Type
    //    "4",//Street Dir Prefix
    //    "4",//Street Dir Suffix
    //    "3",//Listing Agent MLS ID
    //    "3",//Listing Office MLS ID
    //    "3",//Buyer Agent MLS ID
    //    "3",//Buyer Office MLS ID
    //    "3",//Picture last modified
    //    "4",//County
    //    "4",//Rental Income
    //    "4",//Zoning
    //    "3",//Owner First Name
    //    "3",//Owner Last Name"
    //    "4", //AG_NRDSID
    //    "4", //AG_AgentID
    //    "4", //AG_OfficeID
    //    "4", //AG_Status
    //    "4", //AG_FirstName
    //    "4", //AG_LastName
    //    "4", //AG_OfficePhone
    //    "4", //AG_CellPhone
    //    "4", //AG_HomePhone
    //    "4", //AG_Email
    //    "4", //AG_StreetAddress
    //    "4", //AG_City
    //    "4", //AG_State
    //    "4", //AG_ZipCode
    //    "4", //OF_NRDSID
    //    "4", //OF_OfficeID
    //    "4", //OF_Status
    //    "4", //OF_OfficeName
    //    "4", //OF_OfficePhone
    //    "4", //OF_OfficeFax
    //    "4", //OF_Email
    //    "4", //OF_WebsiteURL
    //    "4", //OF_StreetAddress
    //    "4", //OF_City
    //    "4", //OF_State
    //    "4", //OF_ZipCode
    //    "5",//STDFDisplayListingonRDC
    //    "5", //STDFExcludeAddressfromRDC
    //    "5", //PublicListingStatusNotUsed
    //    "5", //DEFTypeNotUsed
    //    "4", //STDFThreeQBathrooms
    //    "4", //RDCFireplaceFeatures
    //    "4",//RDCHeatingFeatures
    //    "4",//RDCRoofingFeatures
    //    "4",//RDCExteriorConsturctoinFeatures
    //    "4",//RDCExteriorFeatures
    //    "4",//STDFDistressedShortSale
    //    "4",//STDFDistressedAuction
    //    "4",//STDFDistressedForeclosedREO
    //    "4",//RDCInteriorFeatures
    //    "1",//STDFNumberofGarageSpaces
    //    "1",//STDFCarport
    //    "1",//STDFLaundryRoom
    //    "1",//STDFDiningRoom
    //    "1",//STDFGameRoom
    //    "1",//STDFFamilyRoom
    //    "1",//STDFDenOffice
    //    "1",//STDFBasement
    //    "1",//STDFCentralAir
    //    "1",//STDFCentralHeat
    //    "1",//STDFForcedAir
    //    "1",//STDFHardwoodFloors
    //    "1",//STDFFireplace
    //    "1",//STDFSwimmingPool
    //    "1",//STDFRVBoatParking
    //    "1",//STDFSpaHotTub
    //    "1",//STDFHorseFacilities
    //    "1",//STDFTennisCourts
    //    "1",//STDFDisabilityFeatures
    //    "1",//STDFPetsAllowed
    //    "1",//STDFEnergyEfficientHome
    //    "5",//AG_LastMod
    //    "5",//AG_StatusDate
    //    "5",//OF_LastMod
    //    "5",//OF_StatusDate
    //    "4",//OH_MLSNumber
    //    "4",//OH_AttendedYN
    //    "4",//OH_RefreshmentYN
    //    "4",//OH_Type
    //    "4",//OH_StartDateTime
    //    "4",//OH_EndDateTime
    //    "4",//OH_TimeRangeDescription
    //    "4",//OH_Remarks
    //    "4",//OH_GateCode
    //    "5"//ReferencingKey
    //};
    //public static readonly System.String[] STD_FIELD_XML_NAME = new System.String[]{
    //    "MLSNumber",
    //    "HouseNo",
    //    "StreetName",
    //    "SuiteNo",
    //    "Status_MLS",
    //    "HouseStyle",
    //    "Area",
    //    "BedRooms",
    //    "TotalBathRooms_STDF",
    //    "ListingPrice",
    //    "SalePrice",
    //    "ListingDate",
    //    "SaleDate",
    //    "SquareFeet_STDF",
    //    "LotSize_STDF",
    //    "YearBuilt_MLS",
    //    "DOM",
    //    "Features",
    //    "Comments",
    //    "Stories_STDF",
    //    "Garage",
    //    "TaxAmount_STDF",
    //    "TaxYear",
    //    "Assessment_STDF",
    //    "City",
    //    "State",
    //    "Zip",
    //    "PicID",
    //    "PropertyType_STDF",
    //    "NumberUnits",
    //    "FullBathRooms",
    //    "HalfBathRooms",
    //    "QuarterBathRooms",
    //    "SubDivision",
    //    "STDFDOM",
    //    "SearchPrice",
    //    "StatusDate",
    //    "Latitude",
    //    "Longitude",
    //    "ListAgentID",
    //    "ListBrokerID",
    //    "ListOfficeName",
    //    "ListAgentFName",
    //    "ListAgentLName",
    //    "AgentRemarks",
    //    "VirtualTourURL",
    //    "ElementSchool",
    //    "MiddleSchool",
    //    "HighSchool",
    //    "SchoolDistrict",
    //    "Views",
    //    "RoomDimensions",
    //    "WaterFrontYN",
    //    "WaterFrontDesc",
    //    "LotDesc",
    //    "Directions",
    //    "ListAgentPhone",
    //    "ListAgentEmail",
    //    "LastMod",
    //    "SaleOrLease",
    //    "PendingDate",
    //    "ExpiredDate",
    //    "InactiveDate",
    //    "BuyerAgentID",
    //    "BuyerBrokerID",
    //    "BuyerOfficeName",
    //    "BuyerAgentFName",
    //    "BuyerAgentLName",
    //    "BuyerAgentPhone",
    //    "BuyerAgentEmail",
    //    "APNNumber",
    //    "StreetNameParsed",
    //    "StreetTypeParsed",
    //    "StreetDirPrefixParsed",
    //    "StreetDirSuffixParsed",
    //    "ListAgentIDDataAgg",
    //    "ListOfficeIDDataAgg",
    //    "BuyerAgentIDDataAgg",
    //    "BuyerOfficeIDDataAgg",
    //    "PicMod",
    //    "County",
    //    "RentIncome",
    //    "Zoning",
    //    "OwnerFName",
    //    "OwnerLName",
    //    "AG_NRDSID",
    //    "AG_AgentID",
    //    "AG_OfficeID",
    //    "AG_Status",
    //    "AG_FirstName",
    //    "AG_LastName",
    //    "AG_OfficePhone",
    //    "AG_CellPhone",
    //    "AG_HomePhone",
    //    "AG_Email",
    //    "AG_StreetAddress",
    //    "AG_City",
    //    "AG_State",
    //    "AG_ZipCode",
    //    "OF_NRDSID",
    //    "OF_OfficeID",
    //    "OF_Status",
    //    "OF_OfficeName",
    //    "OF_OfficePhone",
    //    "OF_OfficeFax",
    //    "OF_Email",
    //    "OF_WebsiteURL",
    //    "OF_StreetAddress",
    //    "OF_City",
    //    "OF_State",
    //    "OF_ZipCode",
    //    "DisplayListingOnRDC",
    //    "ExcludeAddressFromRDC",
    //    "PublicListingStatus",//PublicListingStatusNotUsed
    //    "DEFType",
    //    "ThreeQuarterBathrooms",//STDFThreeQBathrooms
    //    "RDCFireplaceFeatures",
    //    "RDCHeatingFeatures",
    //    "RDCRoofingFeatures",
    //    "RDCExteriorConstructionFeatures",
    //    "RDCExteriorFeatures",
    //    "DistressedShortSale",
    //    "DistressedAuction",
    //    "DistressedForeclosedREO",
    //    "RDCInteriorFeatures",
    //    "NumberofGarageSpaces",//STDFNumberofGarageSpaces
    //    "Carport",//STDFCarport
    //    "LaundryRoom",//STDFLaundryRoom
    //    "DiningRoom",//STDFDiningRoom
    //    "GameRoom",//STDFGameRoom
    //    "FamilyRoom",//STDFFamilyRoom
    //    "DenOffice",//STDFDenOffice
    //    "Basement",//STDFBasement
    //    "CentralAir",//STDFCentralAir
    //    "CentralHeat",//STDFCentralHeat
    //    "ForcedAir",//STDFForcedAir
    //    "HardwoodFloors",//STDFHardwoodFloors
    //    "Fireplace",//STDFFireplace
    //    "SwimmingPool",//STDFSwimmingPool
    //    "RVBoatParking",//STDFRVBoatParking
    //    "SpaHotTub",//STDFSpaHotTub
    //    "HorseFacilities",//STDFHorseFacilities
    //    "TennisCourts",//STDFTennisCourts
    //    "DisabilityFeatures",//STDFDisabilityFeatures
    //    "PetsAllowed",//STDFPetsAllowed
    //    "EnergyEfficientHome",//STDFEnergyEfficientHome
    //    "AG_LastMod",
    //    "AG_StatusDate",
    //    "OF_LastMod",
    //    "OF_StatusDate",
    //    "OH_MLSNumber",
    //    "OH_AttendedYN",
    //    "OH_RefreshmentYN",
    //    "OH_Type",
    //    "OH_StartDateTime",
    //    "OH_EndDateTime",
    //    "OH_TimeRangeDescription",
    //    "OH_Remarks",
    //    "OH_GateCode",
    //    "ReferencingKey"
    //};
    //public const int CUSTOM_FIELD = - 1;
    //public const int STDF_RECORDID = 0;
    //public const int TCSStandardResultFields.STDF_CMAHOUSENO = 1;
    //public const int TCSStandardResultFields.STDF_CMASTREETNAME = 2;
    //public const int TCSStandardResultFields.STDF_CMASUITENO  = 3;
    //public const int STDF_IDENTIFIER = 4; // it is not used - we use getType instead - it seems that this field is not needed at all (the meaning is: active, saved, pending, or expired)
    //public const int TCSStandardResultFields.STDF_CMAHOUSESTYLE = 5;
    //public const int TCSStandardResultFields.STDF_CMAAREA = 6;
    //public const int TCSStandardResultFields.STDF_CMABEDROOMS = 7;
    //public const int TCSStandardResultFields.STDF_CMABATHROOMSNORM = 8;
    //public const int TCSStandardResultFields.STDF_CMALISTINGPRICE = 9;
    //public const int TCSStandardResultFields.STDF_CMASALEPRICE = 10;
    //public const int TCSStandardResultFields.STDF_CMALISTINGDATE = 11;
    //public const int TCSStandardResultFields.STDF_CMASALEDATE = 12;
    //public const int TCSStandardResultFields.STDF_CMASQUAREFEETNORM = 13;
    //public const int TCSStandardResultFieldsSTDF_CMALOTSIZENORM = 14;
    //public const int TCSStandardResultFields.STDF_CMAAGENORM = 15;
    //public const int TCSStandardResultFields.STDF_CMADAYSONMARKET = 16;
    //public const int TCSStandardResultFields.STDF_CMAFEATURE = 17;
    //public const int STDF_NOTES = 18;
    //public const int TCSStandardResultFields.STDF_CMASTORIESNORM = 19;
    //public const int TCSStandardResultFields.STDF_CMAGARAGE = 20;
    //public const int TCSStandardResultFields.STDF_CMATAXAMOUNTNORM = 21;
    //public const int TCSStandardResultFields.STDF_CMATAXYEAR = 22;
    //public const int TCSStandardResultFields.STDF_CMAASSESSMENTNORM = 23;
    //public const int TCSStandardResultFields.TPOCITY = 24;
    //public const int TCSStandardResultFields.STDF_TPOSTATE = 25;
    //public const int TCSStandardResultFields.STDF_TPOZIP = 26;
    //public const int STDF_PIC_ID = 27;
    //public const int STDF_PROPERTY_TYPE_MLS = 28;
    //public const int TCSStandardResultFields.STDF_STDFNUMUNITS = 29;
    //public const int TCSStandardResultFields.STDF_STDFFULLBATHROOMS = 30;
    //public const int TCSStandardResultFields.STDF_STDFHALFBATHROOMS = 31;
    //public const int TCSStandardResultFields.STDF_STDFQBATHROOMS = 32;
    //public const int TCSStandardResultFields.STDF_STDFSUBDIVISION = 33;
    //public const int STDF_DOM = 34;
    //public const int TCSStandardResultFields.STDF_STDFSEARCHPRICE = 35;
    //public const int TCSStandardResultFields.STDF_STDFSTATUSDATE = 36;
    //public const int TCSStandardResultFields.STDF_STDFLAT = 37;
    //public const int TCSStandardResultFields.STDF_STDFLONG = 38;
    //public const int TCSStandardResultFields.STDF_STDFLISTAGENTID = 39;
    //public const int TCSStandardResultFields.STDF_STDFLISTBROKERID = 40;
    //public const int TCSStandardResultFields.STDF_STDFLISTOFFICENAME = 41;
    //public const int TCSStandardResultFields.STDF_STDFLISTAGENTFNAME = 42;
    //public const int TCSStandardResultFields.STDF_STDFLISTAGENTLNAME = 43;
    //public const int TCSStandardResultFields.STDF_STDFAGENTREMARKS = 44;
    //public const int TCSStandardResultFields.STDF_STDFVIRTUALTOURURL = 45;
    //public const int TCSStandardResultFields.STDF_STDFELEMSCHOOL = 46;
    //public const int TCSStandardResultFields.STDF_STDFMIDDLESCHOOL = 47;
    //public const int TCSStandardResultFields.STDF_STDFHIGHSCHOOL = 48;
    //public const int TCSStandardResultFields.STDF_STDFSCHOOLDISTRICT = 49;
    //public const int TCSStandardResultFields.STDF_STDFVIEWS = 50;
    //public const int TCSStandardResultFields.STDF_STDFROOMDIM = 51;
    //public const int TCSStandardResultFields.STDF_STDFWATERFRONTYN = 52;
    //public const int TCSStandardResultFields.STDF_STDFWATERFRONTDESC = 53;
    //public const int TCSStandardResultFields.STDF_STDFLOTDESC = 54;
    //public const int TCSStandardResultFields.STDF_STDFDIRECTIONS = 55;
    //public const int TCSStandardResultFields.STDF_STDFLISTAGENTPHONE = 56;
    //public const int TCSStandardResultFields.STDF_STDFLISTAGENTEMAIL = 57;
    //public const int TCSStandardResultFields.STDF_STDFLASTMODIFIED = 58;
    //public const int TCSStandardResultFields.STDF_STDFSALEORLEASE = 59;
    //public const int STDF_PENDING_DATE = 60;
    //public const int STDF_EXPIRED_DATE = 61;
    //public const int STDF_INACTIVE_DATE = 62;
    //public const int TCSStandardResultFields.STDF_STDFBUYERAGENTID = 63;
    //public const int TCSStandardResultFields.STDF_STDFBUYERBROKERID = 64;
    //public const int TCSStandardResultFields.STDF_STDFBUYEROFFICENAME = 65;
    //public const int TCSStandardResultFields.STDF_STDFBUYERAGENTFNAME = 66;
    //public const int TCSStandardResultFields.STDF_STDFBUYERAGENTLNAME = 67;
    //public const int TCSStandardResultFields.STDF_STDFBUYERAGENTPHONE = 68;
    //public const int TCSStandardResultFields.STDF_STDFBUYERAGENTEMAIL = 69;
    //public const int TCSStandardResultFields.STDF_STDFAPNNUMBER = 70;
    //public const int TCSStandardResultFields.STDF_CMASTREETNAME_PARSED = 71;
    //public const int STDF_STREETTYPE_PARSED = 72;
    //public const int STDF_STREETDIRPREFIX_PARSED = 73;
    //public const int STDF_STREETDIRSUFFIX_PARSED = 74;
    //public const int TCSStandardResultFields.STDF_STDFLISTAGENTID_DATAAGG = 75;
    //public const int TCSStandardResultFields.STDF_STDFLISTBROKERID_DATAAGG = 76;
    //public const int TCSStandardResultFields.STDF_STDFBUYERAGENTID_DATAAGG = 77;
    //public const int TCSStandardResultFields.STDF_STDFBUYERBROKERID_DATAAGG = 78;
    //public const int TCSStandardResultFields.STDF_STDFPICMOD = 79;
    //public const int STDF_COUNTY1 = 80;
    //public const int TCSStandardResultFields.STDF_STDFRENTINCOME = 81;
    //public const int TCSStandardResultFields.STDF_STDFZONING = 82;
    //public const int TCSStandardResultFields.STDF_STDFOWNERFNAME = 83;
    //public const int TCSStandardResultFields.STDF_STDFOWNERLNAME = 84;
    //public const int STDF_AG_NRDSID = 85;
    //public const int STDF_AG_AGENTID = 86;
    //public const int STDF_AG_OFFICEID = 87;
    //public const int STDF_AG_STATUS = 88;
    //public const int STDF_AG_FIRSTNAME = 89;
    //public const int STDF_AG_LASTNAME = 90;
    //public const int STDF_AG_OFFICEPHONE = 91;
    //public const int STDF_AG_CELLPHONE = 92;
    //public const int STDF_AG_HOMEPHONE = 93;
    //public const int STDF_AG_EMAIL = 94;
    //public const int STDF_AG_STREETADDRESS = 95;
    //public const int STDF_AG_CITY = 96;
    //public const int STDF_AG_STATE = 97;
    //public const int STDF_AG_ZIPCODE = 98;
    //public const int STDF_OF_NRDSID = 99;
    //public const int STDF_OF_OFFICEID = 100;
    //public const int STDF_OF_STATUS = 101;
    //public const int STDF_OF_OFFICENAME = 102;
    //public const int STDF_OF_OFFICEPHONE = 103;
    //public const int STDF_OF_OFFICEFAX = 104;
    //public const int STDF_OF_EMAIL = 105;
    //public const int STDF_OF_WEBSITEURL = 106;
    //public const int STDF_OF_STREETADDRESS = 107;
    //public const int STDF_OF_CITY = 108;
    //public const int STDF_OF_STATE = 109;
    //public const int STDF_OF_ZIPCODE = 110;
    //public const int TCSStandardResultFields.STDF_STDFDISPLAYLISTINGONRDC = 111;
    //public const int TCSStandardResultFields.STDF_STDFEXCLUDEADDRESSFROMRDC = 112;
    //public const int STDF_PUBLICLISTING_STATUS_NOTUSED = 113;
    //public const int STDF_DEF_TYPE_NOT_USED = 114;
    //public const int TCSStandardResultFields.STDF_STDFTHREEQBATHROOMS = 115;
    //public const int TCSStandardResultFields.STDF_RDCFIREPLACEFEATURES = 116;
    //public const int TCSStandardResultFields.STDF_RDCHEATINGFEATURES = 117;
    //public const int TCSStandardResultFields.STDF_RDCROOFINGFEATURES = 118;
    //public const int TCSStandardResultFields.STDF_RDCEXTERIORCONSTRUCTIONFEATURES = 119;
    //public const int TCSStandardResultFields.STDF_RDCEXTERIORFEATURES = 120;
    //public const int TCSStandardResultFields.STDF_STDFDISTRESSEDSHORTSALE = 121;
    //public const int TCSStandardResultFields.STDF_STDFDISTRESSEDAUCTION = 122;
    //public const int TCSStandardResultFields.STDF_STDFDISTRESSEDFORECLOSEDREO = 123;
    //public const int TCSStandardResultFields.STDF_RDCINTERIORFEATURES = 124;
    //public const int TCSStandardResultFields.STDF_STDFNUMBEROFGARAGESPACES = 125;
    //public const int TCSStandardResultFields.STDF_STDFCARPORT = 126;
    //public const int TCSStandardResultFields.STDF_STDFLAUNDRYROOM = 127;
    //public const int TCSStandardResultFields.STDF_STDFDININGROOM = 128;
    //public const int TCSStandardResultFields.STDF_STDFGAMEROOM = 129;
    //public const int TCSStandardResultFields.STDF_STDFFAMILYROOM = 130;
    //public const int TCSStandardResultFields.STDF_STDFDENOFFICE = 131;
    //public const int STDF_BASEMENT = 132;
    //public const int TCSStandardResultFields.STDF_STDFCENTRALAIR = 133;
    //public const int TCSStandardResultFields.STDF_STDFCENTRALHEAT = 134;
    //public const int TCSStandardResultFields.STDF_STDFFORCEDAIR = 135;
    //public const int TCSStandardResultFields.STDF_STDFHARDWOODFLOORS = 136;
    //public const int TCSStandardResultFields.STDF_STDFFIREPLACE = 137;
    //public const int TCSStandardResultFields.STDF_STDFSWIMMINGPOOL = 138;
    //public const int TCSStandardResultFields.STDF_STDFRVBOATPARKING = 139;
    //public const int TCSStandardResultFields.STDF_STDFSPAHOTTUB = 140;
    //public const int TCSStandardResultFields.STDF_STDFHORSEFACILITIES = 141;
    //public const int TCSStandardResultFields.STDF_STDFTENNISCOURTS = 142;
    //public const int TCSStandardResultFields.STDF_STDFDISABILITYFEATURES = 143;
    //public const int TCSStandardResultFields.STDF_STDFPETSALLOWED = 144;
    //public const int TCSStandardResultFields.STDF_STDFENERGYEFFICIENTHOME = 145;
    //public const int STDF_AG_LASTMOD = 146;
    //public const int STDF_AG_STATUSDATE = 147;
    //public const int STDF_OF_LASTMOD = 148;
    //public const int STDF_OF_STATUSDATE = 149;
    //public const int STDF_OH_MLSNUMBER = 150;
    //public const int STDF_OH_ATTENDEDYN = 151;
    //public const int STDF_OH_REFRESHMENTYN = 152;
    //public const int STDF_OH_TYPE = 153;
    //public const int STDF_OH_STARTDATETIME = 154;
    //public const int STDF_OH_ENDDATETIME = 155;
    //public const int STDF_OH_TIMERANGEDESCRIPTION = 156;
    //public const int STDF_OH_REMARKS = 157;
    //public const int STDF_OH_GATECODE = 158;
    //public const int STDF_REFERENCINGKEY = 159;
    //public const int STANDARD_FIELDS_COUNT = 160;
    public static final String CMA_GRID_FIELD_DOWNLOAD_PIC = "DownloadPic";
    private net.toppro.components.mls.engine.MLSEngine m_engine;
    private ArrayList m_CmaFields;
    private int[] m_std_fields_indexes;
    private String[] m_FieldPositions = new String[0];
    private Hashtable m_stdfFieldIndex = new Hashtable();
    private int[] m_non_std_fields_indexes;
    private Hashtable m_allFeaturesInGroups = new Hashtable();
    public Hashtable UnitsList = new Hashtable();
    public Hashtable m_FeatureGroups = new Hashtable();
    public MLSCmaFields(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        m_engine = engine;
        m_CmaFields = ArrayList.Synchronized(new ArrayList());
    }

    public int size() throws Exception {
        return m_CmaFields.size();
    }

    public net.toppro.components.mls.engine.CmaField getField(int index) throws Exception {
        if (index >= 0 && index < m_CmaFields.size())
            return (net.toppro.components.mls.engine.CmaField)m_CmaFields.get(index);
         
        return null;
    }

    public net.toppro.components.mls.engine.CmaField getField(String name) throws Exception {
        for (int i = 0;i < m_CmaFields.size();i++)
        {
            net.toppro.components.mls.engine.CmaField field = (net.toppro.components.mls.engine.CmaField)m_CmaFields.get(i);
            if (StringSupport.Compare(field.getName(), name, true) == 0)
                return field;
             
        }
        return null;
    }

    public int getFieldIndex(String name) throws Exception {
        name = name.toUpperCase();
        if (m_stdfFieldIndex.containsKey(name))
            return (Integer)m_stdfFieldIndex.get(name);
         
        return -1;
    }

    //for (int i = 0; i < m_CmaFields.Count; i++)
    //{
    //    CmaField field = (CmaField) m_CmaFields[i];
    //    if (field.getName().ToUpper().Equals(name.ToUpper()))
    //        return i;
    //}
    //return - 1;
    public int getStdIndex(int index) throws Exception {
        for (int i = 0;i < m_std_fields_indexes.length;i++)
        {
            if (m_std_fields_indexes[i] == index)
            {
                return i;
            }
             
        }
        return -1;
    }

    public String getXmlName(int index) throws Exception {
        int result = getStdIndex(index);
        if (result > -1)
        {
            return TCSStandardResultFields.getXmlName(result);
        }
         
        return "";
    }

    public net.toppro.components.mls.engine.CmaField getFieldByRecordPosition(String recordPos) throws Exception {
        for (int i = 0;i < m_CmaFields.size();i++)
        {
            net.toppro.components.mls.engine.CmaField field = (net.toppro.components.mls.engine.CmaField)m_CmaFields.get(i);
            if (field.getRecordPosition().toUpperCase().equals(recordPos.toUpperCase()))
                return field;
             
        }
        return null;
    }

    public int getStdFieldIndex(int std_field) throws Exception {
        return m_std_fields_indexes[std_field];
    }

    public static boolean isStdField(net.toppro.components.mls.engine.CmaField field, int std_field) throws Exception {
        String array = TCSStandardResultFields.getDEFName(std_field);
        if (field.getName().toUpperCase().equals(array.toUpperCase()))
            return true;
         
        return false;
    }

    public net.toppro.components.mls.engine.CmaField getStdField(int std_field) throws Exception {
        int index = m_std_fields_indexes[std_field];
        if (index >= 0)
            return (net.toppro.components.mls.engine.CmaField)m_CmaFields.get(index);
         
        return null;
    }

    public int[] getNonStdFields() throws Exception {
        if (m_non_std_fields_indexes != null)
            return m_non_std_fields_indexes;
         
        m_non_std_fields_indexes = new int[m_CmaFields.size()];
        ArrayList nonStdfields = new ArrayList();
        for (int i = 0, n = 0;i < m_CmaFields.size();i++)
        {
            net.toppro.components.mls.engine.CmaField field = (net.toppro.components.mls.engine.CmaField)m_CmaFields.get(i);
            if (!StringSupport.isNullOrEmpty(field.displayname) && field.getStdId() == TCSStandardResultFields.CUSTOM_FIELD)
            {
                String fname = field.name.toLowerCase();
                if (fname.startsWith("un") && (fname.endsWith("actualrent") || fname.endsWith("furnishedyn") || fname.endsWith("numberbaths") || fname.endsWith("numberbeds") || fname.endsWith("numberunits") || fname.endsWith("totalrent")))
                    continue;
                 
                if (fname.startsWith("rmroom") && (fname.endsWith("type") || fname.endsWith("dimensions") || fname.endsWith("length") || fname.endsWith("width") || fname.endsWith("Features")))
                    continue;
                 
                if (!m_allFeaturesInGroups.containsKey(i))
                {
                    nonStdfields.add(i);
                    n++;
                }
                 
            }
             
        }
        m_non_std_fields_indexes = (int[])ArrayListSupport.toArray(nonStdfields, new int[0]);
        return m_non_std_fields_indexes;
    }

    public String[] getSubFieldList(int stdField) throws Exception {
        net.toppro.components.mls.engine.CmaField cmaField = getStdField(stdField);
        if (cmaField == null)
            return new String[0];
         
        String cont_fields = cmaField.getRecordPosition();
        int index = cont_fields.indexOf('\\', 1);
        if (index < 0)
        {
            return new String[0];
        }
         
        String[] field_names = StringSupport.Split(cont_fields.substring(1, (1) + (index - 1)).toUpperCase(), ',');
        for (int i = 0;i < field_names.length;i++)
        {
            net.toppro.components.mls.engine.CmaField field = getField(field_names[i]);
            if (field != null)
                field_names[i] = field.getPrefix();
            else
                field_names[i] = ""; 
        }
        return field_names;
    }

    public void initCmaFields(String buffer) throws Exception {
        if (StringSupport.isNullOrEmpty(buffer))
            return ;
         
        StringBuilder recTmpStr = new StringBuilder();
        HashMap<String,String[]> methodList = getEngine().getResultScriptMethodList();
        net.toppro.components.mls.engine.CmaField cmaField = null;
        //cmaFieldsList
        int j, i = 0, k;
        int dataType = 0;
        char c;
        boolean newLine = true;
        boolean joinFlag = false;
        boolean marksFlag = false;
        boolean withinFieldPos = false;
        int commas = 0;
        String TmpStr = "";
        String SubstStr = "";
        String SubstWith = "";
        String SubstTable = "";
        ArrayList FieldPositions = ArrayList.Synchronized(new ArrayList(10));
        Hashtable htSubstTable = Hashtable.Synchronized(new Hashtable());
        IEnumerator enumA = null;
        String SubstTableKey = "";
        String SubstTableValue = "";
        String ScriptStr = "";
        String ReplaceDelimVal = "";
        boolean cmaPagesRec = false;
        DefParser parser = m_engine.getDefParser();
        String fldName = "";
        StringBuilder recordPosition = new StringBuilder();
        String commonDateFormat = parser.getValue("TPOnline","DateFormat");
        m_CmaFields.clear();
        try
        {
            while (i < buffer.length())
            {
                c = (char)buffer.charAt(i);
                switch(dataType)
                {
                    case 1: 
                        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                        j = SupportClass.getIndex(buffer,"\"",i);
                        if (j == -1)
                        {
                            String s = buffer.substring(i);
                            System.out.println("MLSCmaFields [initCmaFields] - Wrong records");
                            m_engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_MLSRECORDS,s);
                            throw m_engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_MLSRECORDS),s);
                        }
                         
                        TmpStr = buffer.Substring(i, (j)-(i));
                        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                        i = SupportClass.getIndex(buffer,"\r\n",i);
                        if (i == -1)
                            i = j + 2;
                        else
                            i++; 
                        //
                        cmaPagesRec = false;
                        //CMAPagesRec
                        if ((j = TmpStr.indexOf(MLSREC_CMAPAGESREC)) == -1)
                            cmaPagesRec = false;
                        else
                            cmaPagesRec = true; 
                        //
                        cmaField = new net.toppro.components.mls.engine.CmaField();
                        String TmpStrNotUpperCase = TmpStr;
                        TmpStr = TmpStr.toUpperCase();
                        for (k = 0;k < CMA_REC_PROPERTIES_NAMES.length;k++)
                        {
                            CMA_REC_PROPERTIES_NAMES[k] = CMA_REC_PROPERTIES_NAMES[k].toUpperCase();
                            if ((j = TmpStr.indexOf(CMA_REC_PROPERTIES_NAMES[k])) == -1 && k == 0)
                            {
                                System.out.println("MLSCmaFields [initCmaFields] - " + CMA_REC_PROPERTIES_NAMES[k]);
                                m_engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_MLSRECORDS,TmpStr);
                                throw m_engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_MLSRECORDS),TmpStr);
                            }
                            else
                            {
                                //j += CMA_REC_PROPERTIES_NAMES[k].length();
                                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                                if ((j = TmpStr.indexOf(CMA_REC_PROPERTIES_NAMES[k])) != -1 && (j = SupportClass.getIndex(TmpStr,"=",j)) == -1)
                                {
                                    System.out.println("ScriptEngine [initCmaFields] - symbol '=' after " + CMA_REC_PROPERTIES_NAMES[k] + " not found");
                                    m_engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_MLSRECORDS,TmpStr);
                                    throw m_engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_MLSRECORDS),TmpStr);
                                }
                                else if (j != -1)
                                {
                                    //recTmpStr = new StringBuffer();
                                    StringBuilderSupport.setLength(recTmpStr, 0);
                                    j++;
                                    while (j != TmpStr.length() && TmpStr.charAt(j) != ',')
                                    {
                                        recTmpStr.append(TmpStr.charAt(j));
                                        j++;
                                    }
                                    //while
                                    if (k == 0)
                                    {
                                        fldName = StringSupport.Trim(recTmpStr.toString());
                                        cmaField.setXmlName(TmpStrNotUpperCase.Substring(TmpStrNotUpperCase.IndexOf(fldName, StringComparison.CurrentCultureIgnoreCase), fldName.length()));
                                        cmaField.setName(fldName);
                                    }
                                    else if (k == 3)
                                    {
                                        String __dummyScrutVar1 = cmaField.name.toLowerCase();
                                        if (__dummyScrutVar1.equals("cmalistingdate") || __dummyScrutVar1.equals("cmasaledate") || __dummyScrutVar1.equals("stdfstatusdate") || __dummyScrutVar1.equals("stdfinactivedate") || __dummyScrutVar1.equals("stdfexpireddate") || __dummyScrutVar1.equals("stdfpendingdate"))
                                        {
                                            cmaField.setFieldType("D");
                                        }
                                        else
                                        {
                                            cmaField.setFieldType(StringSupport.Trim(recTmpStr.toString()));
                                        } 
                                    }
                                    else
                                        cmaField.setPropertyValue(CMA_REC_PROPERTIES_NAMES[k],StringSupport.Trim(recTmpStr.toString()));  
                                }
                                  
                            } 
                        }
                        if (buffer.charAt(i) != '\r' && buffer.charAt(i) != '\n')
                        {
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            i = SupportClass.getIndex(buffer,"\r\n",i);
                            if (i != -1)
                                i++;
                             
                        }
                         
                        //
                        cmaField.setVisible(false);
                        cmaField.setRecordPosition(StringSupport.Trim(recordPosition.toString()).replace('~', ' '));
                        FieldPositions.add(StringSupport.Trim(recordPosition.toString()).replace('~', ' '));
                        if (cmaPagesRec)
                            cmaField.setVisible(true);
                         
                        cmaField.setName(StringSupport.Trim(fldName.toString()));
                        TmpStr = "CMAfield_";
                        TmpStr += StringSupport.Trim(fldName.toString());
                        j = 1;
                        SubstStr = " ";
                        while (SubstStr.length() != 0)
                        {
                            SubstStr = parser.getValue(TmpStr,"SubstString" + String.valueOf((((int)(j)))));
                            if (SubstStr.length() != 0)
                            {
                                SubstWith = parser.getValue(TmpStr,"SubstWith" + String.valueOf((((int)(j)))));
                                if (SubstWith.length() != 0)
                                {
                                    cmaField.addSubstWith(SubstWith);
                                    cmaField.addSubstString(SubstStr);
                                }
                                 
                            }
                             
                            j++;
                        }
                        j = 1;
                        ScriptStr = " ";
                        while (ScriptStr.length() != 0)
                        {
                            ScriptStr = parser.getValue(TmpStr,"Script" + String.valueOf(j));
                            if (ScriptStr.length() != 0)
                                cmaField.addScriptString(ScriptStr);
                             
                            j++;
                        }
                        j = 1;
                        ReplaceDelimVal = " ";
                        while (ReplaceDelimVal.length() != 0)
                        {
                            ReplaceDelimVal = parser.getValue(TmpStr,"ReplaceDelimVal" + String.valueOf(j));
                            if (ReplaceDelimVal.length() != 0)
                                cmaField.addReplaceDelimVal(ReplaceDelimVal);
                             
                            j++;
                        }
                        cmaField.setSubstTableName(parser.getValue(TmpStr,"SubstTable"));
                        cmaField.defaultvalue = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_DEFAULT);
                        cmaField.unitofarea = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_UNITOFAREA);
                        cmaField.setColumnCaption(parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_CAPTION));
                        cmaField.setDelimiter(parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_DELIMITER));
                        cmaField.setColumnWidth(parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_WIDTH));
                        cmaField.displayrule = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_DISPLAYRULE);
                        cmaField.displayname = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_DISPLAYNAME);
                        cmaField.retsLongName = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_RETSLONGNAME);
                        cmaField.category = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_CATEGORY);
                        cmaField.orderincategory = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_ORDERINCATEGORY);
                        cmaField.displayformatstring = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_DISPLAYMASK);
                        cmaField.attributeexposure = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_NEWDISPLAYRULE);
                        //cmaField.displaylistasbullets = parser.getValue(TmpStr, CmaField.CMA_FIELD_DISPLAYLISTASBULLETS);
                        cmaField.displaybehaviorbitmask = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_DISPLAY_BEHAVIOR);
                        cmaField.dynamicname = parser.getValue(TmpStr,net.toppro.components.mls.engine.CmaField.CMA_FIELD_DYNAMIC_NAME);
                        if (methodList != null && methodList.containsKey(cmaField.name))
                            cmaField.methodParams = methodList.get(cmaField.name);
                         
                        for (k = 0;k < CMA_FIELD_PROPERTIES_NAMES.length;k++)
                        {
                            if (CMA_FIELD_PROPERTIES_NAMES[k].equals(net.toppro.components.mls.engine.CmaField.CMA_FIELD_DATEFORMAT) && parser.getValue(TmpStr,CMA_FIELD_PROPERTIES_NAMES[k]).length() != 0 && commonDateFormat.length() != 0)
                                cmaField.setPropertyValue(CMA_FIELD_PROPERTIES_NAMES[k],commonDateFormat);
                            else
                                cmaField.setPropertyValue(CMA_FIELD_PROPERTIES_NAMES[k],parser.getValue(TmpStr,CMA_FIELD_PROPERTIES_NAMES[k])); 
                        }
                        String tempFieldName = cmaField.getName().toLowerCase();
                        if (tempFieldName.startsWith("un") && (tempFieldName.endsWith("actualrent") || tempFieldName.endsWith("furnishedyn") || tempFieldName.endsWith("numberbaths") || tempFieldName.endsWith("numberbeds") || tempFieldName.endsWith("numberunits") || tempFieldName.endsWith("totalrent")))
                        {
                            UnitsList.put(tempFieldName.substring(2, (2) + (1)), null);
                        }
                         
                        m_CmaFields.add(cmaField);
                        if (!m_stdfFieldIndex.containsKey(cmaField.getName()))
                            m_stdfFieldIndex.put(cmaField.getName(), m_CmaFields.size() - 1);
                        else
                            m_stdfFieldIndex.put(cmaField.getName() + (m_CmaFields.size() - 1), m_CmaFields.size() - 1); 
                        //
                        dataType = 0;
                        newLine = true;
                        marksFlag = false;
                        joinFlag = false;
                        withinFieldPos = false;
                        recordPosition = new StringBuilder();
                        commas = 0;
                        break;
                    case 0: 
                        if ((newLine == true && c == ';') || (newLine == true && c == 10))
                        {
                            if (c == 10)
                                return ;
                             
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            i = SupportClass.getIndex(buffer,"\r\n",i + 1);
                            if (i == -1)
                                return ;
                             
                            i++;
                            if (i > buffer.length())
                                return ;
                             
                        }
                        else
                        {
                            if (newLine == true)
                            {
                                if (c == '"')
                                {
                                    withinFieldPos = true;
                                    marksFlag = true;
                                    commas = 1;
                                }
                                else if (c == '@' && i + 1 < buffer.length() && buffer.charAt(i + 1) == '"')
                                {
                                    withinFieldPos = true;
                                    marksFlag = true;
                                    commas = 0;
                                }
                                  
                                newLine = false;
                            }
                            else if (newLine != true && c == '"')
                            {
                                if (marksFlag == false && (commas == 0 || commas == 2))
                                {
                                    dataType++;
                                    break;
                                }
                                else
                                {
                                    commas++;
                                    if (commas == 2 && withinFieldPos)
                                        withinFieldPos = false;
                                     
                                    marksFlag = false;
                                } 
                            }
                              
                            if (c != ' ')
                                recordPosition.append(c);
                            else if (c == ' ' && withinFieldPos)
                                recordPosition.append(c);
                              
                            if (c == '\\')
                                joinFlag = true;
                             
                        } 
                        break;
                
                }
                i++;
            }
        }
        catch (Exception e)
        {
            throw m_engine.createException(e);
        }

        //while
        // Init standard fields indexes cashe
        m_std_fields_indexes = new int[TCSStandardResultFields.STANDARD_FIELDS_COUNT];
        for (i = 0;i < m_std_fields_indexes.length;i++)
        {
            m_std_fields_indexes[i] = -1;
            String array = TCSStandardResultFields.getDEFName(i);
            if (StringSupport.isNullOrEmpty(array))
                continue;
            else
                array = array.toUpperCase(); 
            for (k = m_CmaFields.size() - 1;k >= 0;k--)
            {
                net.toppro.components.mls.engine.CmaField field = (net.toppro.components.mls.engine.CmaField)m_CmaFields.get(k);
                if (field.getName().toUpperCase().equals(array))
                {
                    field.setStdId(i);
                    m_std_fields_indexes[i] = k;
                    break;
                }
                 
            }
        }
        if (FieldPositions.size() > 0)
        {
            m_FieldPositions = new String[FieldPositions.size()];
            for (i = 0;i < FieldPositions.size();i++)
                m_FieldPositions[i] = ((String)FieldPositions.get(i));
        }
         
    }

    //end of initCmaFields
    public void initFeatureGroups() throws Exception {
        for (int i = 0;i < m_engine.getDefParser().getCategorizationGroups().length;i++)
        {
            getFeatureGroupList().add(new CSList<Integer>());
            String[] featureGroup = m_engine.getDefParser().getAttributiesFor("ResultFieldGroup_" + (String)m_engine.getDefParser().getCategorizationGroups()[i]);
            if (featureGroup != null)
            {
                for (int m = 0;m < featureGroup.length;m++)
                {
                    int index = getFieldIndex(featureGroup[m]);
                    if (index > -1 && !getFeatureGroupList().get(i).contains(index))
                    {
                        getFeatureGroupList().get(i).add(index);
                        net.toppro.components.mls.engine.CmaField cmFd = getField(index);
                        cmFd.category = (String)m_engine.getDefParser().getCategorizationGroups()[i];
                    }
                     
                    if (index > -1 && !m_allFeaturesInGroups.containsKey(index))
                        m_allFeaturesInGroups.put(index, index);
                     
                }
            }
             
        }
    }

    public net.toppro.components.mls.engine.MLSEngine getEngine() throws Exception {
        return m_engine;
    }

    public String[] getFieldPositions() throws Exception {
        return m_FieldPositions;
    }

    public String getFieldNamesForReturnIDsOnly() throws Exception {
        String result = "";
        for (int i = 0;i < ReturnIDsOnlyFieldList.length;i++)
        {
            net.toppro.components.mls.engine.CmaField cf = getStdField(ReturnIDsOnlyFieldList[i]);
            if (cf != null)
            {
                String pos = cf.getRecordPosition();
                if (!StringSupport.isNullOrEmpty(pos))
                    result += pos + ",";
                 
            }
             
        }
        if (result.endsWith(","))
            result = result.substring(0, (0) + (result.length() - 1));
         
        return result.replace("\"", "");
    }

    public String getFieldNameForReturnMlsNumberOnly() throws Exception {
        String result = "";
        CmaField cf = getStdField(TCSStandardResultFields.STDF_RECORDID);
        if (cf != null)
        {
            result = cf.getRecordPosition();
        }
         
        return result.replace("\"", "");
    }

    //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
    public void save(System.IO.BinaryWriter out_Renamed) throws Exception {
        try
        {
            int i;
            out_Renamed.Write(m_CmaFields.size());
            for (i = 0;i < m_CmaFields.size();i++)
            {
                //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
                SupportClass.serialize(out_Renamed,m_CmaFields.get(i));
            }
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            SupportClass.serialize(out_Renamed,m_std_fields_indexes);
            out_Renamed.Write(m_FieldPositions.length);
            for (i = 0;i < m_FieldPositions.length;i++)
            {
                //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
                SupportClass.serialize(out_Renamed,m_FieldPositions[i]);
            }
            SupportClass.serialize(out_Renamed,m_stdfFieldIndex);
            SupportClass.serialize(out_Renamed,UnitsList);
        }
        catch (Exception e)
        {
            throw m_engine.createException(e);
        }
    
    }

    //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
    public void load(System.IO.BinaryReader in_Renamed) throws Exception {
        try
        {
            int i;
            m_CmaFields.clear();
            int size = in_Renamed.ReadInt32();
            for (i = 0;i < size;i++)
            {
                //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                m_CmaFields.add((net.toppro.components.mls.engine.CmaField)SupportClass.deserialize(in_Renamed));
            }
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            m_std_fields_indexes = (int[])SupportClass.deserialize(in_Renamed);
            int field_positions_size = in_Renamed.ReadInt32();
            m_FieldPositions = new String[field_positions_size];
            for (i = 0;i < field_positions_size;i++)
            {
                //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                m_FieldPositions[i] = ((String)SupportClass.deserialize(in_Renamed));
            }
            m_stdfFieldIndex = (Hashtable)SupportClass.deserialize(in_Renamed);
            UnitsList = (Hashtable)SupportClass.deserialize(in_Renamed);
        }
        catch (Exception e)
        {
            throw m_engine.createException(e);
        }
    
    }

    static {
        try
        {
            CMA_FIELD_PROPERTIES_NAMES = new String[]{ net.toppro.components.mls.engine.CmaField.CMA_FIELD_TRIMSPACES, net.toppro.components.mls.engine.CmaField.CMA_FIELD_ALIGN, net.toppro.components.mls.engine.CmaField.CMA_FIELD_TPOALIGN, net.toppro.components.mls.engine.CmaField.CMA_FIELD_CHARSTRANSF, net.toppro.components.mls.engine.CmaField.CMA_FIELD_CHARSTRIP, net.toppro.components.mls.engine.CmaField.CMA_FIELD_COMBINE, net.toppro.components.mls.engine.CmaField.CMA_FIELD_SUBSTTABLE, net.toppro.components.mls.engine.CmaField.CMA_FIELD_PREFIX, net.toppro.components.mls.engine.CmaField.CMA_FIELD_DATEFORMAT, net.toppro.components.mls.engine.CmaField.CMA_FIELD_POSTFIX };
        }
        catch (Exception __dummyStaticConstructorCatchVar1)
        {
            throw new ExceptionInInitializerError(__dummyStaticConstructorCatchVar1);
        }
    
    }

    public static boolean isCMAFeild(int standardId) throws Exception {
        boolean result = false;
        switch(standardId)
        {
            case TCSStandardResultFields.STDF_RECORDID: 
            case TCSStandardResultFields.STDF_CMAHOUSENO: 
            case TCSStandardResultFields.STDF_CMASTREETNAME: 
            case TCSStandardResultFields.STDF_CMASUITENO: 
            case TCSStandardResultFields.STDF_TPOCITY: 
            case TCSStandardResultFields.STDF_TPOSTATE: 
            case TCSStandardResultFields.STDF_TPOZIP: 
            case TCSStandardResultFields.STDF_CMABEDROOMS: 
            case TCSStandardResultFields.STDF_CMABATHROOMS: 
            case TCSStandardResultFields.STDF_CMASQUAREFEET: 
            case TCSStandardResultFields.STDF_CMAGARAGE: 
            case TCSStandardResultFields.STDF_CMALOTSIZE: 
            case TCSStandardResultFields.STDF_CMAHOUSESTYLE: 
            case TCSStandardResultFields.STDF_CMADAYSONMARKET: 
            case TCSStandardResultFields.STDF_CMALISTINGPRICE: 
            case TCSStandardResultFields.STDF_CMASALEPRICE: 
            case TCSStandardResultFields.STDF_CMAAREA: 
            case TCSStandardResultFields.STDF_CMASTORIES: 
            case TCSStandardResultFields.STDF_CMAAGE: 
            case TCSStandardResultFields.STDF_CMATAXYEAR: 
            case TCSStandardResultFields.STDF_CMATAXAMOUNT: 
            case TCSStandardResultFields.STDF_CMAASSESSMENT: 
            case TCSStandardResultFields.STDF_CMALISTINGDATE: 
            case TCSStandardResultFields.STDF_CMASALEDATE: 
            case TCSStandardResultFields.STDF_CMAFEATURE: 
            case TCSStandardResultFields.STDF_NOTES: 
                result = true;
                break;
        
        }
        return result;
    }

}


