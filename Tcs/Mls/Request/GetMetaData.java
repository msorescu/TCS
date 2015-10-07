//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:53 PM
//

package Tcs.Mls.Request;

import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.LCC.Disposable;
import java.util.Calendar;
import java.util.Date;
import net.toppro.components.mls.engine.MLSSystem;
import Tcs.Mls.MLSEnvironment;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class GetMetaData  extends TPAppRequest 
{
    public GetMetaData(TCServer tcs) throws Exception {
        super(tcs);
    }

    public void run() throws Exception {
        long time;
        try
        {
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            MLSSystem mlsSystem = new MLSSystem(new MLSEnvironment(m_connector));
            MemoryStream ms = mlsSystem.toXml();
            try
            {
                {
                    FileStreamSupport fs = new FileStreamSupport(mlsSystem.getMetadataPath() + "\\" + m_connector.getBoardID() + "_metadata.xml", FileMode.Create);
                    try
                    {
                        {
                            ms.WriteTo(fs);
                        }
                    }
                    finally
                    {
                        if (fs != null)
                            Disposable.mkDisposable(fs).dispose();
                         
                    }
                }
            }
            finally
            {
                if (ms != null)
                    Disposable.mkDisposable(ms).dispose();
                 
            }
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        }
        catch (Exception exc)
        {
            TCSException e = TCSException.produceTCSException(exc);
            String outErrXml = e.getOutputErrorXml();
            m_connector.WriteLine(outErrXml);
        }
    
    }

}


