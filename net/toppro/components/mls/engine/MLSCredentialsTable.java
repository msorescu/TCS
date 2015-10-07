//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:42 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Xml.XmlWriter;
import java.util.ArrayList;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.BoardSetupField;
import net.toppro.components.mls.engine.MLSCredentials;

public class MLSCredentialsTable   
{
    private ArrayList m_credentialList = new ArrayList();
    private Board m_board;
    private net.toppro.components.mls.engine.MLSEngine m_mlsEngine;
    public MLSCredentialsTable(Board board, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        m_board = board;
        m_mlsEngine = mlsEngine;
        populateData();
    }

    public String populateData() throws Exception {
        net.toppro.components.mls.engine.BoardSetup.SecFieldIterator iterator = m_mlsEngine.getBoardSetup().getSecFieldIterator();
        BoardSetupField field = iterator.first();
        int i = 0;
        while (field != null)
        {
            if (field.getInputType() != BoardSetupField.INPUT_TYPE_FINAL)
            {
                MLSCredentials credentials = new MLSCredentials();
                credentials.setDisplayName(field.getCaption());
                credentials.setSystemName(field.getName());
                if (!field.isVisible())
                    credentials.setStoreFlag("1");
                else if (m_mlsEngine.isSavePassLocal())
                    credentials.setStoreFlag("2");
                else if (field.getMaskPassword())
                    credentials.setStoreFlag("3");
                else
                    credentials.setStoreFlag("0");   
                credentials.setStoreFlagDesc(getStoreFlagDesc(credentials.getStoreFlag()));
                m_credentialList.add(credentials);
            }
             
            field = iterator.next();
        }
        if (m_mlsEngine.isSafeMLS())
        {
            MLSCredentials credentials = new MLSCredentials();
            credentials.setDisplayName("Safe MLS Password");
            credentials.setSystemName("SafeMLSPassword");
            credentials.setStoreFlag("1");
            credentials.setStoreFlagDesc(getStoreFlagDesc(credentials.getStoreFlag()));
            m_credentialList.add(credentials);
        }
         
        return "";
    }

    private String getStoreFlagDesc(String storeFlag) throws Exception {
        String result = "";
        String __dummyScrutVar0 = storeFlag;
        if (__dummyScrutVar0.equals("0"))
        {
            result = "Store on server";
        }
        else if (__dummyScrutVar0.equals("1"))
        {
            result = "Do not store on server. Prompt every login session.";
        }
        else if (__dummyScrutVar0.equals("2"))
        {
            result = "Store on the client, encrypted";
        }
        else if (__dummyScrutVar0.equals("3"))
        {
            result = "Store on the server, masked in the UI";
        }
            
        return result;
    }

    public void toXml() throws Exception {
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        w.WriteStartElement(MC.METADATA_MLS_CREDENTIALS);
        for (int i = 0;i < m_credentialList.size();i++)
        {
            w.WriteStartElement(MC.MLS_CREDENTIALS);
            w.WriteElementString(MC.SYSTEM_NAME, ((MLSCredentials)m_credentialList.get(i)).getSystemName());
            w.WriteElementString(MC.DISPLAY_NAME, ((MLSCredentials)m_credentialList.get(i)).getDisplayName());
            w.WriteElementString(MC.MLS_STORE_PERMISSION, ((MLSCredentials)m_credentialList.get(i)).getStoreFlag());
            w.WriteElementString(MC.MLS_STORE_PERMISSION_DESCRIPTION, ((MLSCredentials)m_credentialList.get(i)).getStoreFlagDesc());
            w.writeEndElement();
        }
        w.writeEndElement();
    }

}


