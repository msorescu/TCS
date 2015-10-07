//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;


public interface IPropertyFieldValidator   
{
    /**
    * The implementation has to validate one of the MLSRecord standard fields here.
    *  @param value field value to validate.
    * 
    *  @param field field id. One of the MLSCmaFields.STDF_xxx constants.
    * 
    *  @return  corrected field value. If validation failed the function should return
    * null - MLSRecords will set the field empty, and add some info into notes, so users
    * could see the value in notes even if it's not valid.
    */
    String validateField(String value_Renamed, net.toppro.components.mls.engine.CmaField field) throws Exception ;

}


