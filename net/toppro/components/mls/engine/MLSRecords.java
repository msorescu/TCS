//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:44 PM
//

package net.toppro.components.mls.engine;

import java.util.ArrayList;
import net.toppro.components.mls.engine.IPropertyFieldValidator;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecordSet;

public class MLSRecords   
{
    /*	public static final int FILTER_ALL                    = 0;
    		public static final int FILTER_ACTIVE                 = 1;
    		public static final int FILTER_SOLD                   = 2;
    		public static final int FILTER_PENDING                = 3;
    		public static final int FILTER_EXPIRED                = 4;
    		public static final int FILTER_CHECKED                = 5;
    		public static final int FILTER_CHECKED_PICTURELES     = 6;
    		public static final int FILTER_PICTURELES             = 7;*/
    public static final int FILTER_FLAG_ACTIVE = 0x0001;
    public static final int FILTER_FLAG_SOLD = 0x0002;
    public static final int FILTER_FLAG_PENDING = 0x0004;
    public static final int FILTER_FLAG_EXPIRED = 0x0008;
    public static final int FILTER_FLAG_UNKNOWN_TYPE = 0x0010;
    public static final int FILTER_FLAG_CHECKED = 0x0020;
    public static final int FILTER_FLAG_UNCHECKED = 0x0040;
    public static final int FILTER_FLAG_PICTURE_NOT_DOWNLOADED = 0x0080;
    public static final int FILTER_FLAG_PICTURE_DOWNLOADED = 0x0100;
    public static final int FILTER_FLAG_PICTURE_DOWNLOAD_FAILED = 0x0200;
    public static final int FILTER_FLAG_PICTURE_DOWNLOADED_FINALY = 0x0400;
    public static final int FILTER_FLAG_PICTURE_DOWNLOAD_FAILED_FINALY = 0x0800;
    public static final int FILTER_FLAG_MISSING_PICTURES = 0x0A80;
    public static final int FILTER_FLAG_DOWNLOADED_PICTURES = 0x0500;
    public static final int FILTER_FLAG_FAILED_PICTURES = 0x0A00;
    public static final int FILTER_FLAG_ANY_TYPE = 0x001F;
    public static final int FILTER_FLAG_ANY_CHECK_STATE = 0x0060;
    public static final int FILTER_FLAG_ANY_PICTURE_STATE = 0x0F80;
    public static final int FILTER_ALL = 0x0FFF;
    public static final int FILTER_ACTIVE = 0x0FE1;
    public static final int FILTER_SOLD = 0x0FE2;
    public static final int FILTER_PENDING = 0x0FE4;
    public static final int FILTER_EXPIRED = 0x0FE8;
    public static final int FILTER_UNKNOWN_TYPE = 0x0FF0;
    public static final int FILTER_CHECKED = 0x0FBF;
    public static final int FILTER_UNCHECKED = 0x0FDF;
    public static final int FILTER_PICTURE_NOT_DOWNLOADED = 0x00FF;
    public static final int FILTER_PICTURE_DOWNLOADED = 0x01EF;
    public static final int FILTER_PICTURE_DOWNLOAD_FAILED = 0x02EF;
    public static final int FILTER_PICTURE_DOWNLOADED_FINALY = 0x04EF;
    public static final int FILTER_PICTURE_DOWNLOAD_FAILED_FINALY = 0x08EF;
    public static final int FILTER_MISSING_PICTURES = 0x0AFF;
    public static final int FILTER_DOWNLOADED_PICTURES = 0x05EF;
    public static final int FILTER_FAILED_PICTURES = 0x0AEF;
    /*	public static final String FILTER[] =
    		{
    		"ALL",
    		"Active",
    		"Sold",
    		"Pending",
    		"Expired",
    		null,
    		null,
    		null
    		};*/
    private MLSRecord[] m_data;
    private int m_size;
    private int m_current_record;
    private int m_index;
    private int m_filterFlags;
    private IPropertyFieldValidator m_validator;
    public MLSRecords(MLSRecordSet recordset, int filter, IPropertyFieldValidator validator) throws Exception {
        m_data = recordset.getArray();
        m_validator = validator;
        setFilterFlags(filter);
    }

    public int getFilterFlags() throws Exception {
        return m_filterFlags;
    }

    public void setFilterFlags(int filterFlags) throws Exception {
        m_filterFlags = filterFlags;
        if ((filterFlags & FILTER_ALL) == FILTER_ALL)
            m_size = m_data.length;
        else
            m_size = -1; 
        m_index = m_current_record = -1;
    }

    public void addFilterFlags(int filterFlags) throws Exception {
        setFilterFlags(m_filterFlags | filterFlags);
    }

    public void removeFilterFlags(int filterFlags) throws Exception {
        setFilterFlags(m_filterFlags & ~filterFlags);
    }

    public MLSRecord getCurrentRecord() throws Exception {
        if (m_current_record >= 0 && m_current_record < m_data.length)
            return m_data[m_current_record];
         
        return null;
    }

    public MLSRecord getFirstRecord() throws Exception {
        for (m_current_record = 0;m_current_record < m_data.length;m_current_record++)
        {
            MLSRecord rec = m_data[m_current_record];
            if (filter(rec))
            {
                m_index = 0;
                rec.setValidator(m_validator);
                return rec;
            }
             
        }
        m_index = -1;
        return null;
    }

    public MLSRecord getNextRecord() throws Exception {
        if (m_current_record < 0 || m_current_record >= m_data.length)
            return getFirstRecord();
         
        for (m_current_record++;m_current_record < m_data.length;m_current_record++)
        {
            MLSRecord rec = m_data[m_current_record];
            if (filter(rec))
            {
                m_index++;
                rec.setValidator(m_validator);
                return rec;
            }
             
        }
        m_index = -1;
        return null;
    }

    public MLSRecord getLastRecord() throws Exception {
        for (m_current_record = m_data.length - 1;m_current_record >= 0;m_current_record--)
        {
            MLSRecord rec = m_data[m_current_record];
            if (filter(rec))
            {
                m_index = size() - 1;
                rec.setValidator(m_validator);
                return rec;
            }
             
        }
        m_index = -1;
        return null;
    }

    public MLSRecord getPrevRecord() throws Exception {
        if (m_current_record < 0 || m_current_record >= m_data.length)
            return getLastRecord();
         
        for (m_current_record--;m_current_record >= 0;m_current_record--)
        {
            MLSRecord rec = m_data[m_current_record];
            if (filter(rec))
            {
                m_index = size() - 1;
                rec.setValidator(m_validator);
                return rec;
            }
             
        }
        m_index = -1;
        return null;
    }

    public int size() throws Exception {
        if (m_size < 0)
        {
            m_size = 0;
            for (int i = m_data.length - 1;i >= 0;i--)
            {
                MLSRecord rec = m_data[i];
                if (filter(rec))
                    m_size++;
                 
            }
        }
         
        return m_size;
    }

    public int index() throws Exception {
        return m_index;
    }

    private boolean filter(MLSRecord rec) throws Exception {
        // filter by type
        int flag;
        switch(rec.getMLSRecordType())
        {
            case MLSRecord.TYPE_ACTIVE: 
                flag = FILTER_FLAG_ACTIVE;
                break;
            case MLSRecord.TYPE_EXPIRED: 
                flag = FILTER_FLAG_EXPIRED;
                break;
            case MLSRecord.TYPE_SOLD: 
                flag = FILTER_FLAG_SOLD;
                break;
            case MLSRecord.TYPE_PENDING: 
                flag = FILTER_FLAG_PENDING;
                break;
            default: 
                flag = FILTER_FLAG_UNKNOWN_TYPE;
                break;
        
        }
        if ((m_filterFlags & flag) != flag)
            return false;
         
        // filter by checked state
        if (rec.isChecked())
        {
            if ((m_filterFlags & FILTER_FLAG_CHECKED) != FILTER_FLAG_CHECKED)
                return false;
             
        }
        else if ((m_filterFlags & FILTER_FLAG_UNCHECKED) != FILTER_FLAG_UNCHECKED)
            return false;
          
        // filter by picture download state
        switch(rec.getPictureDownloadState())
        {
            case MLSRecord.PICTURE_NOT_DOWNLOADED: 
                flag = FILTER_FLAG_PICTURE_NOT_DOWNLOADED;
                break;
            case MLSRecord.PICTURE_DOWNLOADED: 
                flag = FILTER_FLAG_PICTURE_DOWNLOADED;
                break;
            case MLSRecord.PICTURE_DOWNLOAD_FAILED: 
                flag = FILTER_FLAG_PICTURE_DOWNLOAD_FAILED;
                break;
            case MLSRecord.PICTURE_DOWNLOADED_FINALY: 
                flag = FILTER_FLAG_PICTURE_DOWNLOADED_FINALY;
                break;
            case MLSRecord.PICTURE_DOWNLOAD_FAILED_FINALY: 
                flag = FILTER_FLAG_PICTURE_DOWNLOAD_FAILED_FINALY;
                break;
            default: 
                flag = 0;
                break;
        
        }
        // should never happen
        if ((m_filterFlags & flag) != flag)
            return false;
         
        return true;
    }

    public void sort(boolean SortAscending, String[] column_names) throws Exception {
        int i, j, len, result;
        int[] sort_columns = null;
        len = m_data.length;
        if (len <= 1)
            return ;
         
        ArrayList columnIndexes = ArrayList.Synchronized(new ArrayList(10));
        try
        {
            MLSCmaFields columns = m_data[0].getFields();
            if (column_names != null)
                for (j = 0;j < column_names.length;j++)
                {
                    result = columns.getFieldIndex(column_names[j]);
                    if (result != -1)
                        columnIndexes.add(((int)(result)));
                     
                }
             
            if (columnIndexes.size() != 0)
            {
                sort_columns = new int[columnIndexes.size()];
                for (i = 0;i < columnIndexes.size();i++)
                    sort_columns[i] = ((int)columnIndexes.get(i));
            }
             
            MLSRecord r1, r2, r3;
            for (i = 0;i < len - 1;i++)
                for (j = 1;j < len - i;j++)
                {
                    r1 = m_data[j - 1];
                    r2 = m_data[j];
                    try
                    {
                        result = r1.compareRecords(r2,sort_columns);
                        if (SortAscending && result < 0)
                        {
                            r3 = m_data[j - 1];
                            m_data[j - 1] = m_data[j];
                            m_data[j] = r3;
                        }
                        else if (!SortAscending && result > 0)
                        {
                            r3 = m_data[j];
                            m_data[j] = m_data[j - 1];
                            m_data[j - 1] = r3;
                        }
                          
                    }
                    catch (Exception e)
                    {
                        SupportClass.WriteStackTrace(e, Console.Error);
                    }
                
                }
        }
        catch (Exception e)
        {
            SupportClass.WriteStackTrace(e, Console.Error);
        }
    
    }

}


//end of void sort