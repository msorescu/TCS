//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:44 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import java.util.ArrayList;
import java.util.Hashtable;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.STRINGS;

//UPGRADE_NOTE: The access modifier for this class or class field has been changed in order to prevent compilation errors due to the visibility level. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1296'"
public class MLSRecordSet   
{
    private Hashtable m_records = Hashtable.Synchronized(new Hashtable());
    private MLSRecord[] m_array = null;
    public void clear() throws Exception {
        m_records.Clear();
        m_array = null;
    }

    public MLSRecord[] getArray() throws Exception {
        if (m_array == null)
        {
            m_array = new MLSRecord[m_records.size()];
            IEnumerator enumA = EnumeratorSupport.mk(m_records.keySet().iterator());
            for (int i = 0;enumA.moveNext();i++)
            {
                //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
                //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
                m_array[i] = (MLSRecord)m_records.get(enumA.getCurrent());
            }
        }
         
        return m_array;
    }

    public MLSRecord getRecord(String id) throws Exception {
        return (MLSRecord)m_records.get(id);
    }

    public void addRecord(MLSRecord record) throws Exception {
        m_records.put(record.getRecordID(), record);
        m_array = null;
    }

    public int getRecordCount() throws Exception {
        return m_records.size();
    }

    public void load(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        engine.setMessage(STRINGS.get_Renamed(STRINGS.MLS_RECORDSET_LOADING_PREV_SEARCH_RESULTS));
        engine.setProgress(0,1);
        m_records.Clear();
        FileStreamSupport f = null;
        //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
        System.IO.BinaryReader s = null;
        MLSCmaFields fields = null;
        fields = engine.getCmaFields();
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            f = new FileStreamSupport(engine.getRecordsFilename(), FileMode.Open, FileAccess.Read);
            //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
            s = new System.IO.BinaryReader(f);
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            ArrayList array = (ArrayList)SupportClass.deserialize(s);
            int size = array.size();
            for (int i = 0;i < size;i += 2)
            {
                //engine.setProgress(i, size);
                MLSRecord rec = new MLSRecord(fields,(String[])array.get(i));
                //rec.setPictureDownloadState(((System.Int32) array[i + 1]));
                addRecord(rec);
            }
        }
        catch (Exception e)
        {
            throw engine.createException(e,STRINGS.get_Renamed(STRINGS.MLS_RECORDSET_ERR_PREPARING_PREV_RECORDS));
        }
        finally
        {
            //engine.setProgress(size, size);
            if (s != null)
            {
                try
                {
                    s.Close();
                }
                catch (Exception e2)
                {
                        ;
                }
            
            }
             
            if (f != null)
            {
                try
                {
                    f.close();
                }
                catch (Exception e3)
                {
                        ;
                }
            
            }
             
        }
    }

    public void save(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        return ;
    }

}


//System.IO.FileStream f = null;
////UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
//System.IO.BinaryWriter s = null;
//try
//{
//    System.Collections.ArrayList array = System.Collections.ArrayList.Synchronized(new System.Collections.ArrayList(10));
//    System.Collections.IEnumerator enumA = m_records.Keys.GetEnumerator();
//    //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
//    int statuA = 0;
//    int statuS = 0;
//    int statuE = 0;
//    int statuP = 0;
//    while (enumA.MoveNext())
//    {
//        //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
//        MLSRecord rec = (MLSRecord) m_records[enumA.Current];
//        if (rec.getType() == MLSRecord.TYPE_ACTIVE)
//        {
//            statuA++;
//            if (statuA > 120)
//                continue;
//        }
//        else if (rec.getType() == MLSRecord.TYPE_SOLD)
//        {
//            statuS++;
//            if (statuS > 120)
//                continue;
//        }
//        else if (rec.getType() == MLSRecord.TYPE_EXPIRED)
//        {
//            statuE++;
//            if (statuE > 30)
//                continue;
//        }
//        else if (rec.getType() == MLSRecord.TYPE_PENDING)
//        {
//            statuP++;
//            if (statuP > 30)
//                continue;
//        }
//        array.Add(rec.getData());
//        array.Add((System.Int32) rec.getPictureDownloadState());
//    }
//    //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
//    f = new System.IO.FileStream(engine.getRecordsFilename(), System.IO.FileMode.Create);
//    //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
//    s = new System.IO.BinaryWriter(f);
//    //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
//    SupportClass.Serialize(s, array);
//}
//catch (System.Exception e)
//{
//    throw engine.createException(e, STRINGS.get_Renamed(STRINGS.MLS_RECORDSET_ERR_SAVING_PREPARED_RECORDS));
//}
//finally
//{
//    if (s != null)
//    {
//        try
//        {
//            s.Close();
//        }
//        catch (System.Exception e2)
//        {
//            ;
//        }
//    }
//    if (f != null)
//    {
//        try
//        {
//            f.Close();
//        }
//        catch (System.Exception e3)
//        {
//            ;
//        }
//    }
//}