//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.ObjectSupport;
import CS2JNet.System.Xml.XmlWriter;
import java.util.ArrayList;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.HelpInfo;

public class HelpInfoTable   
{
    private ArrayList m_credentialList = new ArrayList();
    private Board m_board;
    private net.toppro.components.mls.engine.MLSEngine m_mlsEngine;
    public HelpInfoTable(Board board, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        m_board = board;
        m_mlsEngine = mlsEngine;
        populateData();
    }

    public String populateData() throws Exception {
        if (ObjectSupport.Equals("import", StringComparison.CurrentCultureIgnoreCase))
        {
            HelpInfo fileImportHelpInfo = new HelpInfo();
            fileImportHelpInfo.setSystemName(MC.FILE_IMPORT_SYSTEM_NAME);
            fileImportHelpInfo.setDisplayName(MC.FILE_IMPORT_DISPLAY_NAME);
            fileImportHelpInfo.setHelpText(m_mlsEngine.getBoardSetup().getNotes());
            m_credentialList.add(fileImportHelpInfo);
        }
        else
        {
            HelpInfo helpInfo = new HelpInfo();
            helpInfo.setSystemName(MC.LOGIN_HELP_SYSTEM_NAME);
            helpInfo.setDisplayName(MC.LOGIN_HELP_DISPLAY_NAME);
            helpInfo.setHelpText(m_mlsEngine.getBoardSetup().getNotes());
            if (helpInfo.getHelpText().length() > 0)
                m_credentialList.add(helpInfo);
             
        } 
        HelpInfo disclaimerHelpInfo = new HelpInfo();
        disclaimerHelpInfo.setSystemName(MC.MLS_DISCLAIMER_SYSTEM_NAME);
        disclaimerHelpInfo.setDisplayName(MC.MLS_DISCLAIMER_DISPLAY_NAME);
        disclaimerHelpInfo.setHelpText(m_mlsEngine.getDisclaimer());
        if (disclaimerHelpInfo.getHelpText().length() > 0)
            m_credentialList.add(disclaimerHelpInfo);
         
        return "";
    }

    public void toXml() throws Exception {
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        w.WriteStartElement(MC.METADATA_HELP_INFO);
        for (int i = 0;i < m_credentialList.size();i++)
        {
            w.WriteStartElement(MC.HELP_INFO);
            w.WriteElementString(MC.SYSTEM_NAME, ((HelpInfo)m_credentialList.get(i)).getSystemName());
            w.WriteElementString(MC.DISPLAY_NAME, ((HelpInfo)m_credentialList.get(i)).getDisplayName());
            w.WriteElementString(MC.HELP_TEXT, ((HelpInfo)m_credentialList.get(i)).getHelpText());
            w.writeEndElement();
        }
        w.writeEndElement();
    }

}


