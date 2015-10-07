//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:46 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Text.EncodingSupport;
import CS2JNet.System.Xml.XmlWriter;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.Environment;

public class MLSSystem   
{
    public static final String META_DATA = "METADATA";
    public static final String METADATA_SYSTEM = "METADATA-SYSTEM";
    private static final String SYSTEM_NAME = "TCS";
    private Environment m_environment;
    private Board m_board = null;
    private StringBuilder m_xmlBuffer = new StringBuilder();
    private XmlWriter m_xmlWriter = null;
    private String m_metadataPath = "";
    public MLSSystem(Environment environment) throws Exception {
        m_environment = environment;
        m_board = new Board(this);
    }

    public XmlWriter getSystemXmlWriter() throws Exception {
        return m_xmlWriter;
    }

    public Environment getEnvironment() throws Exception {
        return m_environment;
    }

    public StringBuilder getXmlBuffer() throws Exception {
        return m_xmlBuffer;
    }

    public String getSystemName() throws Exception {
        return SYSTEM_NAME;
    }

    public String getMetadataPath() throws Exception {
        return m_metadataPath;
    }

    public void setMetadataPath(String value) throws Exception {
        m_metadataPath = value;
    }

    public MemoryStream toXml() throws Exception {
        MemoryStream ms = new MemoryStream();
        XmlWriterSettings setting = new XmlWriterSettings();
        setting.Encoding = EncodingSupport.GetEncoder("UTF-8");
        m_xmlWriter = XmlWriter.Create(ms, setting);
        m_xmlWriter.WriteStartDocument();
        m_xmlWriter.WriteStartElement(MC.META_DATA);
        m_xmlWriter.WriteStartElement(MC.METADATA_SYSTEM);
        m_xmlWriter.WriteAttributeString(MC.SYSTEM_NAME, SYSTEM_NAME);
        m_xmlWriter.WriteAttributeString(MC.SYSTEM_Version, net.toppro.components.mls.engine.MLSEngine.MLS_ENGINE_VERSION);
        m_xmlWriter.WriteStartElement(MC.METADATA_BOARD);
        m_board.toXml();
        m_xmlWriter.writeEndElement();
        m_xmlWriter.writeEndElement();
        m_xmlWriter.writeEndElement();
        m_xmlWriter.WriteEndDocument();
        m_xmlWriter.Flush();
        m_xmlWriter.Close();
        return ms;
    }

}


