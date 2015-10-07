//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:45 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import CS2JNet.System.Xml.XmlWriter;
import java.util.ArrayList;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.Rule;

public class MlsRules   
{
    private ArrayList m_ruleList = new ArrayList();
    private Board m_board;
    private net.toppro.components.mls.engine.MLSEngine m_mlsEngine;
    private static final String StringMlsRules = "MLSRules";
    private static final String RuleID = "RuleID";
    private static final String RuleDescription = "RuleDescription";
    public MlsRules(Board board, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        m_board = board;
        m_mlsEngine = mlsEngine;
        populateData();
    }

    public String populateData() throws Exception {
        for (int i = 1;i < 30;i++)
        {
            String id = m_mlsEngine.getDefParser().getValue(StringMlsRules,RuleID + i);
            String description = m_mlsEngine.getDefParser().getValue(StringMlsRules,RuleDescription + i);
            if (StringSupport.isNullOrEmpty(id))
                continue;
            else
            {
                Rule rule = new Rule();
                rule.setID(id);
                rule.setDescription(description);
                m_ruleList.add(rule);
            } 
        }
        return "";
    }

    public void toXml() throws Exception {
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        w.WriteStartElement(MC.METADATA_MLS_RULES);
        for (int i = 0;i < m_ruleList.size();i++)
        {
            w.WriteStartElement(MC.RULE);
            w.WriteElementString(MC.ID, ((Rule)m_ruleList.get(i)).getID());
            w.WriteElementString(MC.DESCRIPTION, ((Rule)m_ruleList.get(i)).getDescription());
            w.writeEndElement();
        }
        w.writeEndElement();
    }

}


