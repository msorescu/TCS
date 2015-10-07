//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:59 PM
//

package Tcs.Mls;

import java.util.HashMap;
import java.util.Map;

public class TCSStandardResultFields   
{
    public static final int CUSTOM_FIELD = -1;
    public static final int STDF_STDFPROPERTYTYPENORM = 0;
    public static final int STDF_STDFPROPERTYTYPEMLS = 1;
    public static final int STDF_STDFPROPERTYSUBTYPE = 2;
    public static final int STDF_DEFTYPE_NODEFNAME = 3;
    public static final int STDF_RECORDID = 4;
    public static final int STDF_CMAIDENTIFIERNORM = 5;
    public static final int STDF_CMAIDENTIFIER = 6;
    public static final int STDF_PUBLICLISTINGSTATUS_NODEFNAME = 7;
    public static final int STDF_CMAHOUSENO = 8;
    public static final int STDF_CMASTREETNAME = 9;
    public static final int STDF_STDFSTREETNAMEPARSED = 10;
    public static final int STDF_STDFSTREETTYPE = 11;
    public static final int STDF_STREETTYPEPARSEDSHORT_NODEFNAME = 12;
    public static final int STDF_STDFSTREETDIRPREFIX = 13;
    public static final int STDF_STREETDIRPREFIXPARSEDSHORT_NODEFNAME = 14;
    public static final int STDF_STDFSTREETDIRSUFFIX = 15;
    public static final int STDF_STREETDIRSUFFIXPARSEDSHORT_NODEFNAME = 16;
    public static final int STDF_CMASUITENO = 17;
    public static final int STDF_TPOCITY = 18;
    public static final int STDF_TPOSTATE = 19;
    public static final int STDF_TPOZIP = 20;
    public static final int STDF_STDFNUMUNITS = 21;
    public static final int STDF_CMABEDROOMS = 22;
    public static final int STDF_CMABATHROOMS = 23;
    public static final int STDF_CMABATHROOMSNORM = 24;
    public static final int STDF_STDFFULLBATHROOMS = 25;
    public static final int STDF_STDFTHREEQBATHROOMS = 26;
    public static final int STDF_STDFHALFBATHROOMS = 27;
    public static final int STDF_STDFQBATHROOMS = 28;
    public static final int STDF_CMALOTSIZE = 29;
    public static final int STDF_CMALOTSIZENORM = 30;
    public static final int STDF_STDFLOTDESC = 31;
    public static final int STDF_CMASQUAREFEET = 32;
    public static final int STDF_CMASQUAREFEETNORM = 33;
    public static final int STDF_CMASTORIES = 34;
    public static final int STDF_CMASTORIESNORM = 35;
    public static final int STDF_CMAHOUSESTYLE = 36;
    public static final int STDF_CMAGARAGE = 37;
    public static final int STDF_STDFNUMBEROFGARAGESPACES = 38;
    public static final int STDF_CMAAREA = 39;
    public static final int STDF_STDFSUBDIVISION = 40;
    public static final int STDF_STDFDEVELOPMENTNAME = 41;
    public static final int STDF_STDFMLSNEIGHBORHOOD = 42;
    public static final int STDF_CMAAGE = 43;
    public static final int STDF_CMAAGENORM = 44;
    public static final int STDF_CMATAXAMOUNT = 45;
    public static final int STDF_CMATAXAMOUNTNORM = 46;
    public static final int STDF_CMATAXYEAR = 47;
    public static final int STDF_CMAASSESSMENT = 48;
    public static final int STDF_CMAASSESSMENTNORM = 49;
    public static final int STDF_CMALISTINGDATE = 50;
    public static final int STDF_CMADAYSONMARKET = 51;
    public static final int STDF_CMALISTINGPRICE = 52;
    public static final int STDF_CMASALEDATE = 53;
    public static final int STDF_CMASALEPRICE = 54;
    public static final int STDF_STDFSEARCHPRICE = 55;
    public static final int STDF_STDFSTATUSDATE = 56;
    public static final int STDF_STDFLASTMOD = 57;
    public static final int STDF_STDFPICMOD = 58;
    public static final int STDF_STDFPENDINGDATE = 59;
    public static final int STDF_STDFEXPIREDDATE = 60;
    public static final int STDF_STDFINACTIVEDATE = 61;
    public static final int STDF_STDFLAT = 62;
    public static final int STDF_STDFLONG = 63;
    public static final int STDF_CMAFEATURE = 64;
    public static final int STDF_NOTES = 65;
    public static final int STDF_STDFAGENTREMARKS = 66;
    public static final int STDF_STDFLISTAGENTID = 67;
    public static final int STDF_STDFLISTAGENTIDDATAAGG = 68;
    public static final int STDF_STDFLISTAGENTIDTIGERLEAD = 69;
    public static final int STDF_STDFLISTAGENTIDSHOWINGTIME = 70;
    public static final int STDF_STDFLISTBROKERID = 71;
    public static final int STDF_STDFLISTOFFICEIDDATAAGG = 72;
    public static final int STDF_STDFLISTOFFICEIDTIGERLEAD = 73;
    public static final int STDF_STDFLISTOFFICEIDSHOWINGTIME = 74;
    public static final int STDF_STDFLISTOFFICENAME = 75;
    public static final int STDF_STDFLISTAGENTFNAME = 76;
    public static final int STDF_STDFLISTAGENTLNAME = 77;
    public static final int STDF_STDFLISTAGENTPHONE = 78;
    public static final int STDF_STDFLISTINGAGENTASSOCIATION = 79;
    public static final int STDF_STDFLISTAGENTEMAIL = 80;
    public static final int STDF_STDFBUYERAGENTID = 81;
    public static final int STDF_STDFSALEAGENTASSOCIATION = 82;
    public static final int STDF_STDFBUYERAGENTIDDATAAGG = 83;
    public static final int STDF_STDFBUYERBROKERID = 84;
    public static final int STDF_STDFBUYEROFFICEIDDATAAGG = 85;
    public static final int STDF_STDFBUYEROFFICENAME = 86;
    public static final int STDF_STDFBUYERAGENTFNAME = 87;
    public static final int STDF_STDFCOOLINGFEATURES = 88;
    public static final int STDF_STDFBUYERAGENTLNAME = 89;
    public static final int STDF_STDFBUYERAGENTPHONE = 90;
    public static final int STDF_STDFBUYERAGENTEMAIL = 91;
    public static final int STDF_STDFBUYERCOAGENTMLSIDDATAAGG = 92;
    public static final int STDF_STDFBUYERCOOFFICEMLSIDDATAAGG = 93;
    public static final int STDF_STDFLISTINGCOOFFICEMLSIDDATAAGG = 94;
    public static final int STDF_STDFLISTINGCOAGENTMLSIDDATAAGG = 95;
    public static final int STDF_STDFNEWCONSTRUCTION = 96;
    public static final int STDF_STDFCOMMUNITYFEATURES = 97;
    public static final int STDF_STDFPARKINGFEATURES = 98;
    public static final int STDF_STDFHEATINGFEATURES = 99;
    public static final int STDF_STDFBASEMENTFEATURES = 100;
    public static final int STDF_STDFINTERIORFEATURES = 101;
    public static final int STDF_STDFEXTERIORFEATURES = 102;
    public static final int STDF_STDFFIREPLACEFEATURES = 103;
    public static final int STDF_STDFNUMBEROFFIREPLACES = 104;
    public static final int STDF_STDFDISABILITYFEATURES = 105;
    public static final int STDF_STDFPETFEATURES = 106;
    public static final int STDF_STDFENERGYFEATURES = 107;
    public static final int STDF_STDFVIRTUALTOURURL = 108;
    public static final int STDF_STDFLISTINGDETAILSPAGEURL = 109;
    public static final int STDF_STDFLISTINGOFFICEURL = 110;
    public static final int STDF_STDFLISTINGBROKERAGEURL = 111;
    public static final int STDF_STDFLISTINGFRANCHISEURL = 112;
    public static final int STDF_STDFELEMSCHOOL = 113;
    public static final int STDF_STDFMIDDLESCHOOL = 114;
    public static final int STDF_STDFHIGHSCHOOL = 115;
    public static final int STDF_STDFSCHOOLDISTRICT = 116;
    public static final int STDF_STDFVIEWS = 117;
    public static final int STDF_STDFROOMDIM = 118;
    public static final int STDF_STDFWATERFRONTYN = 119;
    public static final int STDF_STDFWATERFRONTDESC = 120;
    public static final int STDF_STDFDIRECTIONS = 121;
    public static final int STDF_PICID = 122;
    public static final int STDF_STDFNUMBEROFPHOTOS = 123;
    public static final int STDF_STDFSALEORLEASE = 124;
    public static final int STDF_STDFAPNNUMBER = 125;
    public static final int STDF_STDFCOUNTY = 126;
    public static final int STDF_STDFZONING = 127;
    public static final int STDF_STDFDISPLAYLISTINGONRDC = 128;
    public static final int STDF_STDFEXCLUDEADDRESSFROMRDC = 129;
    public static final int STDF_SRCHCARPORT = 130;
    public static final int STDF_SRCHLAUNDRYROOM = 131;
    public static final int STDF_SRCHDININGROOM = 132;
    public static final int STDF_SRCHGAMEROOM = 133;
    public static final int STDF_SRCHFAMILYROOM = 134;
    public static final int STDF_SRCHDEN = 135;
    public static final int STDF_SRCHOFFICE = 136;
    public static final int STDF_SRCHBASEMENT = 137;
    public static final int STDF_SRCHCENTRALAIR = 138;
    public static final int STDF_SRCHCENTRALHEAT = 139;
    public static final int STDF_SRCHFORCEDAIR = 140;
    public static final int STDF_SRCHHARDWOODFLOORS = 141;
    public static final int STDF_SRCHFIREPLACE = 142;
    public static final int STDF_SRCHSWIMMINGPOOL = 143;
    public static final int STDF_SRCHRVBOATPARKING = 144;
    public static final int STDF_SRCHSPAHOTTUB = 145;
    public static final int STDF_SRCHHORSEFACILITIES = 146;
    public static final int STDF_SRCHTENNISCOURTS = 147;
    public static final int STDF_SRCHDISABILITYFEATURES = 148;
    public static final int STDF_SRCHPETSALLOWED = 149;
    public static final int STDF_SRCHENERGYEFFICIENTHOME = 150;
    public static final int STDF_STDFROOMSTOTAL = 151;
    public static final int STDF_SRCHOCEANVIEW = 152;
    public static final int STDF_SRCHANYVIEW = 153;
    public static final int STDF_SRCHWATERVIEW = 154;
    public static final int STDF_SRCHCOMMUNITYSWIMMINGPOOL = 155;
    public static final int STDF_SRCHLAKEVIEW = 156;
    public static final int STDF_SRCHGOLFCOURSEVIEW = 157;
    public static final int STDF_SRCHCOMMUNITYSECURITYFEATURES = 158;
    public static final int STDF_SRCHSENIORCOMMUNITY = 159;
    public static final int STDF_SRCHGOLFCOURSELOTORFRONTAGE = 160;
    public static final int STDF_SRCHCULDESAC = 161;
    public static final int STDF_SRCHCITYVIEW = 162;
    public static final int STDF_SRCHHILLMOUNTAINVIEW = 163;
    public static final int STDF_SRCHRIVERVIEW = 164;
    public static final int STDF_SRCHCOMMUNITYSPAHOTTUB = 165;
    public static final int STDF_SRCHCOMMUNITYCLUBHOUSE = 166;
    public static final int STDF_SRCHCOMMUNITYRECREATIONFACILITIES = 167;
    public static final int STDF_SRCHCOMMUNITYTENNISCOURTS = 168;
    public static final int STDF_SRCHCORNERLOT = 169;
    public static final int STDF_SRCHCOMMUNITYGOLF = 170;
    public static final int STDF_SRCHCOMMUNITYHORSEFACILITIES = 171;
    public static final int STDF_SRCHCOMMUNITYBOATFACILITIES = 172;
    public static final int STDF_SRCHCOMMUNITYPARK = 173;
    public static final int STDF_SRCHLEASEOPTION = 174;
    public static final int STDF_SRCHDISTRESSEDSHORTSALE = 175;
    public static final int STDF_SRCHDISTRESSEDAUCTION = 176;
    public static final int STDF_SRCHDISTRESSEDFORECLOSEDREO = 177;
    public static final int STDF_STDFSHORTSALE = 178;
    public static final int STDF_STDFAUCTION = 179;
    public static final int STDF_STDFFORECLOSURE = 180;
    public static final int STDF_RDCFIREPLACEFEATURES = 181;
    public static final int STDF_RDCHEATINGFEATURES = 182;
    public static final int STDF_RDCROOFINGFEATURES = 183;
    public static final int STDF_RDCEXTERIORCONSTRUCTIONFEATURES = 184;
    public static final int STDF_RDCEXTERIORFEATURES = 185;
    public static final int STDF_RDCINTERIORFEATURES = 186;
    public static final int STDF_STDFLISTINGCOAGENTFIRSTNAME = 187;
    public static final int STDF_STDFLISTINGCOAGENTLASTNAME = 188;
    public static final int STDF_STDFLISTINGCOAGENTCONTACTPHONE = 189;
    public static final int STDF_STDFLISTINGCOAGENTASSOCIATION = 190;
    public static final int STDF_STDFLISTINGCOAGENTEMAIL = 191;
    public static final int STDF_STDFBUYERCOAGENTFIRSTNAME = 192;
    public static final int STDF_STDFBUYERCOAGENTLASTNAME = 193;
    public static final int STDF_STDFBUYERCOAGENTCONTACTPHONE = 194;
    public static final int STDF_STDFBUYERCOAGENTASSOCIATION = 195;
    public static final int STDF_STDFBUYERCOAGENTEMAIL = 196;
    public static final int STDF_STDFLISTINGCOOFFICENAME = 197;
    public static final int STDF_STDFBUYERCOOFFICENAME = 198;
    public static final int STDF_STDFLISTINGAGENTDESIGNATION = 199;
    public static final int STDF_STDFLISTINGCOAGENTDESIGNATION = 200;
    public static final int STDF_STDFBUYERAGENTDESIGNATION = 201;
    public static final int STDF_STDFBUYERCOAGENTDESIGNATION = 202;
    public static final int STDF_STDFIDXCUSTOM1 = 203;
    public static final int STDF_STDFIDXCUSTOM2 = 204;
    public static final int STDF_STDFIDXCUSTOM3 = 205;
    public static final int STDF_STDFIDXCUSTOM4 = 206;
    public static final int STDF_STDFIDXCUSTOM5 = 207;
    public static final int STDF_STDFLISTINGRID = 208;
    public static final int STDF_AG_NRDSID = 209;
    public static final int STDF_AG_AGENTID = 210;
    public static final int STDF_AG_OFFICEID = 211;
    public static final int STDF_AG_AGENTIDTIGERLEAD = 212;
    public static final int STDF_AG_OFFICEIDTIGERLEAD = 213;
    public static final int STDF_AG_AGENTIDSHOWINGTIME = 214;
    public static final int STDF_AG_OFFICEIDSHOWINGTIME = 215;
    public static final int STDF_AG_AGTMLSSYSTEMID = 216;
    public static final int STDF_AG_OFFICEMLSSYSTEMID = 217;
    public static final int STDF_AG_STATUS = 218;
    public static final int STDF_AG_FIRSTNAME = 219;
    public static final int STDF_AG_LASTNAME = 220;
    public static final int STDF_AG_OFFICEPHONE = 221;
    public static final int STDF_AG_CELLPHONE = 222;
    public static final int STDF_AG_HOMEPHONE = 223;
    public static final int STDF_AG_EMAIL = 224;
    public static final int STDF_AG_STREETADDRESS = 225;
    public static final int STDF_AG_CITY = 226;
    public static final int STDF_AG_STATE = 227;
    public static final int STDF_AG_ZIPCODE = 228;
    public static final int STDF_AG_LASTMOD = 229;
    public static final int STDF_AG_STATUSDATE = 230;
    public static final int STDF_AG_ASSOCIATION = 231;
    public static final int STDF_AG_MEMBERTYPE = 232;
    public static final int STDF_AG_AGENTREFID = 233;
    public static final int STDF_AG_MIDDLENAME = 234;
    public static final int STDF_AG_FULLNAME = 235;
    public static final int STDF_AG_PREFERREDPHONE = 236;
    public static final int STDF_AG_PREFERREDPHONEEXT = 237;
    public static final int STDF_AG_OFFICEPHONEEXT = 238;
    public static final int STDF_AG_DIRECTPHONE = 239;
    public static final int STDF_AG_FAX = 240;
    public static final int STDF_AG_TOLLFREEPHONE = 241;
    public static final int STDF_AG_AGENTWEBSITEURL = 242;
    public static final int STDF_AG_OFFICEURL = 243;
    public static final int STDF_AG_BROKERAGEURL = 244;
    public static final int STDF_AG_FRANCHISEURL = 245;
    public static final int STDF_AG_AGENTSTATELICENSE = 246;
    public static final int STDF_AG_DESIGNATION = 247;
    public static final int STDF_AG_TEAMNAME = 248;
    public static final int STDF_AG_ISBROKERYN = 249;
    public static final int STDF_AG_BROKERID = 250;
    public static final int STDF_AG_NICKNAME = 251;
    public static final int STDF_AG_FACEBOOKURL = 252;
    public static final int STDF_AG_TWITTERURL = 253;
    public static final int STDF_AG_LINKEDINURL = 254;
    public static final int STDF_AG_OTHERSOCIALMEDIAURL = 255;
    public static final int STDF_AG_SALUTATION = 256;
    public static final int STDF_AG_NAMESUFFIX = 257;
    public static final int STDF_AG_AGENTPHOTOURL = 258;
    public static final int STDF_OF_NRDSID = 259;
    public static final int STDF_OF_OFFICEID = 260;
    public static final int STDF_OF_OFFICEREFID = 261;
    public static final int STDF_OF_OFFICEIDTIGERLEAD = 262;
    public static final int STDF_OF_OFFICEIDSHOWINGTIME = 263;
    public static final int STDF_OF_OFFMLSSYSTEMID = 264;
    public static final int STDF_OF_MAINOFFICEID = 265;
    public static final int STDF_OF_STATUS = 266;
    public static final int STDF_OF_OFFICENAME = 267;
    public static final int STDF_OF_OFFICEPHONE = 268;
    public static final int STDF_OF_OFFICEFAX = 269;
    public static final int STDF_OF_EMAIL = 270;
    public static final int STDF_OF_WEBSITEURL = 271;
    public static final int STDF_OF_BROKERAGEURL = 272;
    public static final int STDF_OF_FRANCHISEURL = 273;
    public static final int STDF_OF_STREETADDRESS = 274;
    public static final int STDF_OF_CITY = 275;
    public static final int STDF_OF_STATE = 276;
    public static final int STDF_OF_ZIPCODE = 277;
    public static final int STDF_OF_LASTMOD = 278;
    public static final int STDF_OF_STATUSDATE = 279;
    public static final int STDF_OF_ASSOCIATION = 280;
    public static final int STDF_OF_OFFICEPHONEEXT = 281;
    public static final int STDF_OF_COMPANY = 282;
    public static final int STDF_OF_FRANCHISE = 283;
    public static final int STDF_OF_BROKERID = 284;
    public static final int STDF_OF_BROKERNAME = 285;
    public static final int STDF_OF_FACEBOOKURL = 286;
    public static final int STDF_OF_TWITTERURL = 287;
    public static final int STDF_OF_LINKEDINURL = 288;
    public static final int STDF_OF_OTHERSOCIALMEDIAURL = 289;
    public static final int STDF_OH_OPENHOUSEID = 290;
    public static final int STDF_OH_MLSNUMBER = 291;
    public static final int STDF_OH_ATTENDEDYN = 292;
    public static final int STDF_OH_REFRESHMENTYN = 293;
    public static final int STDF_OH_TYPE = 294;
    public static final int STDF_OH_STARTDATETIME = 295;
    public static final int STDF_OH_ENDDATETIME = 296;
    public static final int STDF_OH_DATE = 297;
    public static final int STDF_OH_STARTTIME = 298;
    public static final int STDF_OH_ENDTIME = 299;
    public static final int STDF_OH_TIMERANGEDESCRIPTION = 300;
    public static final int STDF_OH_STARTDATETIME2 = 301;
    public static final int STDF_OH_ENDDATETIME2 = 302;
    public static final int STDF_OH_DATE2 = 303;
    public static final int STDF_OH_STARTTIME2 = 304;
    public static final int STDF_OH_ENDTIME2 = 305;
    public static final int STDF_OH_TIMERANGEDESCRIPTION2 = 306;
    public static final int STDF_OH_STARTDATETIME3 = 307;
    public static final int STDF_OH_ENDDATETIME3 = 308;
    public static final int STDF_OH_DATE3 = 309;
    public static final int STDF_OH_STARTTIME3 = 310;
    public static final int STDF_OH_ENDTIME3 = 311;
    public static final int STDF_OH_TIMERANGEDESCRIPTION3 = 312;
    public static final int STDF_OH_REMARKS = 313;
    public static final int STDF_OH_GATECODE = 314;
    public static final int STANDARD_FIELDS_COUNT = 315;
    public static Map<Integer,String[]> StandardResultDictionary;
    public static Map<String,String> TcsFieldsToCommonCodes;
    public static Map<String,Integer> TcsFieldXmlNameToIndex;
    public static int[] DataAggResultList;
    public static int[] AgentRosterResultList;
    public static int[] OfficeRosterResultList;
    public static int[] OpenHouseResultList;
    static {
        try
        {
            StandardResultDictionary = new HashMap<Integer,String[]>();
            TcsFieldsToCommonCodes = new HashMap<String,String>();
            TcsFieldXmlNameToIndex = new HashMap<String,Integer>();
            //STDF_CMADAYSONMARKET,
            DataAggResultList = new int[]{ STDF_STDFPROPERTYTYPENORM, STDF_STDFPROPERTYTYPEMLS, STDF_STDFPROPERTYSUBTYPE, STDF_DEFTYPE_NODEFNAME, STDF_RECORDID, STDF_CMAIDENTIFIERNORM, STDF_CMAIDENTIFIER, STDF_PUBLICLISTINGSTATUS_NODEFNAME, STDF_CMAHOUSENO, STDF_CMASTREETNAME, STDF_STDFSTREETNAMEPARSED, STDF_STDFSTREETTYPE, STDF_STREETTYPEPARSEDSHORT_NODEFNAME, STDF_STDFSTREETDIRPREFIX, STDF_STREETDIRPREFIXPARSEDSHORT_NODEFNAME, STDF_STDFSTREETDIRSUFFIX, STDF_STREETDIRSUFFIXPARSEDSHORT_NODEFNAME, STDF_CMASUITENO, STDF_TPOCITY, STDF_TPOSTATE, STDF_TPOZIP, STDF_STDFNUMUNITS, STDF_CMABEDROOMS, STDF_CMABATHROOMS, STDF_CMABATHROOMSNORM, STDF_STDFFULLBATHROOMS, STDF_STDFTHREEQBATHROOMS, STDF_STDFHALFBATHROOMS, STDF_STDFQBATHROOMS, STDF_CMALOTSIZE, STDF_CMALOTSIZENORM, STDF_STDFLOTDESC, STDF_CMASQUAREFEET, STDF_CMASQUAREFEETNORM, STDF_CMASTORIES, STDF_CMASTORIESNORM, STDF_CMAHOUSESTYLE, STDF_CMAGARAGE, STDF_STDFNUMBEROFGARAGESPACES, STDF_CMAAREA, STDF_STDFSUBDIVISION, STDF_STDFDEVELOPMENTNAME, STDF_STDFMLSNEIGHBORHOOD, STDF_CMAAGE, STDF_CMAAGENORM, STDF_CMATAXAMOUNT, STDF_CMATAXAMOUNTNORM, STDF_CMATAXYEAR, STDF_CMAASSESSMENT, STDF_CMAASSESSMENTNORM, STDF_CMALISTINGDATE, STDF_CMALISTINGPRICE, STDF_CMASALEDATE, STDF_CMASALEPRICE, STDF_STDFSEARCHPRICE, STDF_STDFSTATUSDATE, STDF_STDFLASTMOD, STDF_STDFPICMOD, STDF_STDFPENDINGDATE, STDF_STDFEXPIREDDATE, STDF_STDFINACTIVEDATE, STDF_STDFLAT, STDF_STDFLONG, STDF_CMAFEATURE, STDF_NOTES, STDF_STDFAGENTREMARKS, STDF_STDFLISTAGENTID, STDF_STDFLISTAGENTIDDATAAGG, STDF_STDFLISTAGENTIDTIGERLEAD, STDF_STDFLISTAGENTIDSHOWINGTIME, STDF_STDFLISTBROKERID, STDF_STDFLISTOFFICEIDDATAAGG, STDF_STDFLISTOFFICEIDTIGERLEAD, STDF_STDFLISTOFFICEIDSHOWINGTIME, STDF_STDFLISTOFFICENAME, STDF_STDFLISTAGENTFNAME, STDF_STDFLISTAGENTLNAME, STDF_STDFLISTAGENTPHONE, STDF_STDFLISTINGAGENTASSOCIATION, STDF_STDFLISTAGENTEMAIL, STDF_STDFBUYERAGENTID, STDF_STDFSALEAGENTASSOCIATION, STDF_STDFBUYERAGENTIDDATAAGG, STDF_STDFBUYERBROKERID, STDF_STDFBUYEROFFICEIDDATAAGG, STDF_STDFBUYEROFFICENAME, STDF_STDFBUYERAGENTFNAME, STDF_STDFCOOLINGFEATURES, STDF_STDFBUYERAGENTLNAME, STDF_STDFBUYERAGENTPHONE, STDF_STDFBUYERAGENTEMAIL, STDF_STDFBUYERCOAGENTMLSIDDATAAGG, STDF_STDFBUYERCOOFFICEMLSIDDATAAGG, STDF_STDFLISTINGCOOFFICEMLSIDDATAAGG, STDF_STDFLISTINGCOAGENTMLSIDDATAAGG, STDF_STDFNEWCONSTRUCTION, STDF_STDFCOMMUNITYFEATURES, STDF_STDFPARKINGFEATURES, STDF_STDFHEATINGFEATURES, STDF_STDFBASEMENTFEATURES, STDF_STDFINTERIORFEATURES, STDF_STDFEXTERIORFEATURES, STDF_STDFFIREPLACEFEATURES, STDF_STDFNUMBEROFFIREPLACES, STDF_STDFDISABILITYFEATURES, STDF_STDFPETFEATURES, STDF_STDFENERGYFEATURES, STDF_STDFVIRTUALTOURURL, STDF_STDFLISTINGDETAILSPAGEURL, STDF_STDFLISTINGOFFICEURL, STDF_STDFLISTINGBROKERAGEURL, STDF_STDFLISTINGFRANCHISEURL, STDF_STDFELEMSCHOOL, STDF_STDFMIDDLESCHOOL, STDF_STDFHIGHSCHOOL, STDF_STDFSCHOOLDISTRICT, STDF_STDFVIEWS, STDF_STDFROOMDIM, STDF_STDFWATERFRONTYN, STDF_STDFWATERFRONTDESC, STDF_STDFDIRECTIONS, STDF_PICID, STDF_STDFNUMBEROFPHOTOS, STDF_STDFSALEORLEASE, STDF_STDFAPNNUMBER, STDF_STDFCOUNTY, STDF_STDFZONING, STDF_STDFDISPLAYLISTINGONRDC, STDF_STDFEXCLUDEADDRESSFROMRDC, STDF_SRCHCARPORT, STDF_SRCHLAUNDRYROOM, STDF_SRCHDININGROOM, STDF_SRCHGAMEROOM, STDF_SRCHFAMILYROOM, STDF_SRCHDEN, STDF_SRCHOFFICE, STDF_SRCHBASEMENT, STDF_SRCHCENTRALAIR, STDF_SRCHCENTRALHEAT, STDF_SRCHFORCEDAIR, STDF_SRCHHARDWOODFLOORS, STDF_SRCHFIREPLACE, STDF_SRCHSWIMMINGPOOL, STDF_SRCHRVBOATPARKING, STDF_SRCHSPAHOTTUB, STDF_SRCHHORSEFACILITIES, STDF_SRCHTENNISCOURTS, STDF_SRCHDISABILITYFEATURES, STDF_SRCHPETSALLOWED, STDF_SRCHENERGYEFFICIENTHOME, STDF_STDFROOMSTOTAL, STDF_SRCHOCEANVIEW, STDF_SRCHANYVIEW, STDF_SRCHWATERVIEW, STDF_SRCHCOMMUNITYSWIMMINGPOOL, STDF_SRCHLAKEVIEW, STDF_SRCHGOLFCOURSEVIEW, STDF_SRCHCOMMUNITYSECURITYFEATURES, STDF_SRCHSENIORCOMMUNITY, STDF_SRCHGOLFCOURSELOTORFRONTAGE, STDF_SRCHCULDESAC, STDF_SRCHCITYVIEW, STDF_SRCHHILLMOUNTAINVIEW, STDF_SRCHRIVERVIEW, STDF_SRCHCOMMUNITYSPAHOTTUB, STDF_SRCHCOMMUNITYCLUBHOUSE, STDF_SRCHCOMMUNITYRECREATIONFACILITIES, STDF_SRCHCOMMUNITYTENNISCOURTS, STDF_SRCHCORNERLOT, STDF_SRCHCOMMUNITYGOLF, STDF_SRCHCOMMUNITYHORSEFACILITIES, STDF_SRCHCOMMUNITYBOATFACILITIES, STDF_SRCHCOMMUNITYPARK, STDF_SRCHLEASEOPTION, STDF_SRCHDISTRESSEDSHORTSALE, STDF_SRCHDISTRESSEDAUCTION, STDF_SRCHDISTRESSEDFORECLOSEDREO, STDF_STDFSHORTSALE, STDF_STDFAUCTION, STDF_STDFFORECLOSURE, STDF_RDCFIREPLACEFEATURES, STDF_RDCHEATINGFEATURES, STDF_RDCROOFINGFEATURES, STDF_RDCEXTERIORCONSTRUCTIONFEATURES, STDF_RDCEXTERIORFEATURES, STDF_RDCINTERIORFEATURES, STDF_STDFLISTINGCOAGENTFIRSTNAME, STDF_STDFLISTINGCOAGENTLASTNAME, STDF_STDFLISTINGCOAGENTCONTACTPHONE, STDF_STDFLISTINGCOAGENTASSOCIATION, STDF_STDFLISTINGCOAGENTEMAIL, STDF_STDFBUYERCOAGENTFIRSTNAME, STDF_STDFBUYERCOAGENTLASTNAME, STDF_STDFBUYERCOAGENTCONTACTPHONE, STDF_STDFBUYERCOAGENTASSOCIATION, STDF_STDFBUYERCOAGENTEMAIL, STDF_STDFLISTINGCOOFFICENAME, STDF_STDFBUYERCOOFFICENAME, STDF_STDFLISTINGAGENTDESIGNATION, STDF_STDFLISTINGCOAGENTDESIGNATION, STDF_STDFBUYERAGENTDESIGNATION, STDF_STDFBUYERCOAGENTDESIGNATION, STDF_STDFIDXCUSTOM1, STDF_STDFIDXCUSTOM2, STDF_STDFIDXCUSTOM3, STDF_STDFIDXCUSTOM4, STDF_STDFIDXCUSTOM5, STDF_STDFLISTINGRID };
            AgentRosterResultList = new int[]{ STDF_AG_NRDSID, STDF_AG_AGENTID, STDF_AG_OFFICEID, STDF_AG_AGENTIDTIGERLEAD, STDF_AG_OFFICEIDTIGERLEAD, STDF_AG_AGENTIDSHOWINGTIME, STDF_AG_OFFICEIDSHOWINGTIME, STDF_AG_AGTMLSSYSTEMID, STDF_AG_OFFICEMLSSYSTEMID, STDF_AG_STATUS, STDF_AG_FIRSTNAME, STDF_AG_LASTNAME, STDF_AG_OFFICEPHONE, STDF_AG_CELLPHONE, STDF_AG_HOMEPHONE, STDF_AG_EMAIL, STDF_AG_STREETADDRESS, STDF_AG_CITY, STDF_AG_STATE, STDF_AG_ZIPCODE, STDF_AG_LASTMOD, STDF_AG_STATUSDATE, STDF_AG_ASSOCIATION, STDF_AG_MEMBERTYPE, STDF_AG_AGENTREFID, STDF_AG_MIDDLENAME, STDF_AG_FULLNAME, STDF_AG_PREFERREDPHONE, STDF_AG_PREFERREDPHONEEXT, STDF_AG_OFFICEPHONEEXT, STDF_AG_DIRECTPHONE, STDF_AG_FAX, STDF_AG_TOLLFREEPHONE, STDF_AG_AGENTWEBSITEURL, STDF_AG_OFFICEURL, STDF_AG_BROKERAGEURL, STDF_AG_FRANCHISEURL, STDF_AG_AGENTSTATELICENSE, STDF_AG_DESIGNATION, STDF_AG_TEAMNAME, STDF_AG_ISBROKERYN, STDF_AG_BROKERID, STDF_AG_NICKNAME, STDF_AG_FACEBOOKURL, STDF_AG_TWITTERURL, STDF_AG_LINKEDINURL, STDF_AG_OTHERSOCIALMEDIAURL, STDF_AG_SALUTATION, STDF_AG_NAMESUFFIX, STDF_AG_AGENTPHOTOURL };
            OfficeRosterResultList = new int[]{ STDF_OF_NRDSID, STDF_OF_OFFICEID, STDF_OF_OFFICEREFID, STDF_OF_OFFICEIDTIGERLEAD, STDF_OF_OFFICEIDSHOWINGTIME, STDF_OF_OFFMLSSYSTEMID, STDF_OF_MAINOFFICEID, STDF_OF_STATUS, STDF_OF_OFFICENAME, STDF_OF_OFFICEPHONE, STDF_OF_OFFICEFAX, STDF_OF_EMAIL, STDF_OF_WEBSITEURL, STDF_OF_BROKERAGEURL, STDF_OF_FRANCHISEURL, STDF_OF_STREETADDRESS, STDF_OF_CITY, STDF_OF_STATE, STDF_OF_ZIPCODE, STDF_OF_LASTMOD, STDF_OF_STATUSDATE, STDF_OF_ASSOCIATION, STDF_OF_OFFICEPHONEEXT, STDF_OF_COMPANY, STDF_OF_FRANCHISE, STDF_OF_BROKERID, STDF_OF_BROKERNAME, STDF_OF_FACEBOOKURL, STDF_OF_TWITTERURL, STDF_OF_LINKEDINURL, STDF_OF_OTHERSOCIALMEDIAURL };
            OpenHouseResultList = new int[]{ STDF_OH_OPENHOUSEID, STDF_OH_MLSNUMBER, STDF_OH_ATTENDEDYN, STDF_OH_REFRESHMENTYN, STDF_OH_TYPE, STDF_OH_STARTDATETIME, STDF_OH_ENDDATETIME, STDF_OH_DATE, STDF_OH_STARTTIME, STDF_OH_ENDTIME, STDF_OH_TIMERANGEDESCRIPTION, STDF_OH_STARTDATETIME2, STDF_OH_ENDDATETIME2, STDF_OH_DATE2, STDF_OH_STARTTIME2, STDF_OH_ENDTIME2, STDF_OH_TIMERANGEDESCRIPTION2, STDF_OH_STARTDATETIME3, STDF_OH_ENDDATETIME3, STDF_OH_DATE3, STDF_OH_STARTTIME3, STDF_OH_ENDTIME3, STDF_OH_TIMERANGEDESCRIPTION3, STDF_OH_REMARKS, STDF_OH_GATECODE };
        }
        catch (Exception __dummyStaticConstructorCatchVar0)
        {
            throw new ExceptionInInitializerError(__dummyStaticConstructorCatchVar0);
        }
    
    }

    public static String[] getStandardField(int stdfIndex) throws Exception {
        return StandardResultDictionary.get(stdfIndex);
    }

    public static int[] getDataAggResultFields() throws Exception {
        return DataAggResultList;
    }

    public static int[] getAgentRosterResultFields() throws Exception {
        return AgentRosterResultList;
    }

    public static int[] getOfficeRosterResultFields() throws Exception {
        return OfficeRosterResultList;
    }

    public static int[] getOpenHouseResultFields() throws Exception {
        return OpenHouseResultList;
    }

    public static String getXmlName(int stdfIndex) throws Exception {
        try
        {
            return StandardResultDictionary.get(stdfIndex)[0];
        }
        catch (Exception __dummyCatchVar0)
        {
            return "";
        }
    
    }

    public static String getDEFName(int stdfIndex) throws Exception {
        try
        {
            return StandardResultDictionary.get(stdfIndex)[1];
        }
        catch (Exception __dummyCatchVar1)
        {
            return "";
        }
    
    }

    public static String getDisplayName(int stdfIndex) throws Exception {
        try
        {
            return StandardResultDictionary.get(stdfIndex)[2];
        }
        catch (Exception __dummyCatchVar2)
        {
            return "";
        }
    
    }

    public static String getDisplayRule(int stdfIndex) throws Exception {
        try
        {
            return StandardResultDictionary.get(stdfIndex)[3];
        }
        catch (Exception __dummyCatchVar3)
        {
            return "";
        }
    
    }

    public static String getDataType(int stdfIndex) throws Exception {
        try
        {
            return StandardResultDictionary.get(stdfIndex)[4];
        }
        catch (Exception __dummyCatchVar4)
        {
            return "";
        }
    
    }

    public static String getNormalized(int stdfIndex) throws Exception {
        try
        {
            return StandardResultDictionary.get(stdfIndex)[5];
        }
        catch (Exception __dummyCatchVar5)
        {
            return "";
        }
    
    }

    public static String getAttributeExposure(int stdfIndex) throws Exception {
        try
        {
            return StandardResultDictionary.get(stdfIndex)[6];
        }
        catch (Exception __dummyCatchVar6)
        {
            return "";
        }
    
    }

    public static String getCommonCode(String fieldName) throws Exception {
        try
        {
            if (TcsFieldsToCommonCodes.containsKey(fieldName))
                return TcsFieldsToCommonCodes.get(fieldName);
            else
                return ""; 
        }
        catch (Exception __dummyCatchVar7)
        {
            return "";
        }
    
    }

    public static int getFieldIndex(String xmlName) throws Exception {
        try
        {
            if (TcsFieldXmlNameToIndex.containsKey(xmlName))
                return TcsFieldXmlNameToIndex.get(xmlName);
            else
                return -1; 
        }
        catch (Exception __dummyCatchVar8)
        {
            return -1;
        }
    
    }

}


