//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import java.io.File;
import java.util.ArrayList;
import net.toppro.components.mls.engine.FileFilter;
import net.toppro.components.mls.engine.MLSUtil;

public class FileFilter   
{
    public static final int FLAG_FIND_FILES = 0x01;
    public static final int FLAG_FIND_FOLDERS = 0x02;
    public static final int FLAG_SEARCH_IN_SUBFOLDERS = 0x04;
    public static final int FLAG_PUT_FULL_PATH = 0x08;
    //UPGRADE_NOTE: Final was removed from the declaration of 'FLAG_FIND_ALL '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int FLAG_FIND_ALL = FLAG_FIND_FILES | FLAG_FIND_FOLDERS;
    private static final char WILDCARD_ALL = '*';
    private static final char WILDCARD_ONE = '?';
    private String[] m_cache = null;
    private int m_flags;
    private String m_folder;
    public FileFilter(String folder, int flags) throws Exception {
        m_folder = folder;
        m_flags = flags;
    }

    public FileFilter(String folder) throws Exception {
        this(folder, FLAG_FIND_ALL);
    }

    private void buildTmpStorage(ArrayList storage, String folder) throws Exception {
        String[] list = getFullFileList(folder,FLAG_FIND_ALL);
        for (int i = list.length - 1;i >= 0;i--)
        {
            String name = folder + list[i];
            File file = new File(name);
            if (File.Exists(file.FullName))
            {
                if ((m_flags & FLAG_FIND_FOLDERS) != 0)
                {
                    if ((m_flags & FLAG_PUT_FULL_PATH) != 0)
                        storage.add(name);
                    else
                        storage.add(list[i]); 
                }
                 
                buildTmpStorage(storage,name + "\\");
            }
            else if ((m_flags & FLAG_FIND_FILES) != 0)
            {
                if ((m_flags & FLAG_PUT_FULL_PATH) != 0)
                    storage.add(name);
                else
                    storage.add(list[i]); 
            }
              
        }
    }

    private static String[] getFullFileList(String folder, int flags) throws Exception {
        File dir = new File(folder + ".");
        //UPGRADE_TODO: The equivalent in .NET for method 'java.io.File.list' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
        String[] list = File.GetFileSystemEntries(dir.FullName);
        String[] tmp;
        int size, i;
        if (list == null)
            list = new String[0];
         
        switch(flags & FLAG_FIND_ALL)
        {
            case 0: 
                if (list.length > 0)
                    list = new String[0];
                 
                break;
            case FLAG_FIND_FILES: 
                tmp = new String[list.length];
                size = 0;
                for (i = list.length - 1;i >= 0;i--)
                    if ((new File(list[i])).exists())
                    {
                        tmp[size] = list[i];
                        size++;
                    }
                     
                list = new String[size];
                for (i = size - 1;i >= 0;i--)
                    list[i] = tmp[i];
                break;
            case FLAG_FIND_FOLDERS: 
                tmp = new String[list.length];
                size = 0;
                for (i = list.length - 1;i >= 0;i--)
                    if ((new File(list[i])).exists())
                    {
                        tmp[size] = list[i];
                        size++;
                    }
                     
                list = new String[size];
                for (i = size - 1;i >= 0;i--)
                    list[i] = tmp[i];
                break;
        
        }
        return list;
    }

    //UPGRADE_NOTE: Synchronized keyword was removed from method 'buildCache'. Lock expression was added. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1027'"
    private void buildCache() throws Exception {
        if (m_cache == null)
        {
            String[] cache;
            if (m_folder.charAt(m_folder.length() - 1) != '\\')
                m_folder += "\\";
             
            if ((m_flags & FLAG_SEARCH_IN_SUBFOLDERS) != 0)
            {
                ArrayList tmpStorage = ArrayList.Synchronized(new ArrayList(10));
                buildTmpStorage(tmpStorage,m_folder);
                cache = new String[tmpStorage.size()];
                for (int i = cache.length - 1;i >= 0;i--)
                    cache[i] = ((String)tmpStorage.get(i));
            }
            else
                cache = getFullFileList(m_folder,m_flags); 
            m_cache = cache;
            MLSUtil.sortStringsArray(m_cache);
        }
         
    }

    private int getMatch(String filterName, String filterExt, int index) throws Exception {
        if (m_cache == null)
            buildCache();
         
        if (index < 0)
            index = 0;
         
        int length = m_cache.length;
        for (int i = index;i < length;i++)
            if (fileMatch(m_cache[i],filterName,filterExt))
                return i;
             
        return length;
    }

    public int getSize() throws Exception {
        if (m_cache != null)
            return m_cache.length;
         
        return 0;
    }

    public String getFirstMatch(String filter) throws Exception {
        String filterName;
        String filterExt;
        int i = filter.lastIndexOf('.');
        if (i >= 0)
        {
            filterName = filter.Substring(0, (i)-(0));
            filterExt = filter.substring(i + 1);
        }
        else
        {
            filterName = filter;
            filterExt = null;
        } 
        i = getMatch(filterName,filterExt,0);
        if (i < 0 || i >= m_cache.length)
            return null;
         
        return m_cache[i];
    }

    /**
    * Returns array of filenames in folder by filter.
    * Filters are: *.*, *.jpg, ??123.* and so on. Filenames in the array
    * don't contain full path. Just file names.
    * 
    *  @param filter filter containing wildcards * and ?.
    * 
    *  @return  array of relative filenames (without path).
    */
    public String[] getFiles(String filter) throws Exception {
        String filterExt;
        String filterName;
        int i = filter.lastIndexOf('.');
        if (i >= 0)
        {
            filterName = filter.Substring(0, (i)-(0));
            filterExt = filter.substring(i + 1);
        }
        else
        {
            filterName = filter;
            filterExt = null;
        } 
        if (m_cache == null)
            buildCache();
         
        int length = m_cache.length;
        String[] tmp = new String[length];
        int size = 0;
        for (i = getMatch(filterName,filterExt,0);i < length;i = getMatch(filterName,filterExt,i + 1))
        {
            tmp[size] = m_cache[i];
            size++;
        }
        String[] files = new String[size];
        Array.Copy(tmp, 0, files, 0, size);
        return files;
    }

    private static int nonWildcardIndex(String str, int start) throws Exception {
        int length = str.length();
        while (start < length)
        {
            char c = str.charAt(start);
            if (c != WILDCARD_ALL && c != WILDCARD_ONE)
                break;
             
            start++;
        }
        return start;
    }

    private static int wildcardIndex(String str, int start) throws Exception {
        int length = str.length();
        while (start < length)
        {
            char c = str.charAt(start);
            if (c == WILDCARD_ALL || c == WILDCARD_ONE)
                return start;
             
            start++;
        }
        return start;
    }

    private static boolean fileMatch(String file, String filterName, String filterExt) throws Exception {
        String fileExt;
        String fileName;
        int beg = file.lastIndexOf('\\');
        int end = file.lastIndexOf('.');
        if (beg < 0)
            beg = 0;
        else
            beg++; 
        if (end > beg)
        {
            fileName = file.Substring(beg, (end)-(beg));
            fileExt = file.substring(end + 1);
        }
        else
        {
            fileName = file.substring(beg);
            fileExt = "";
        } 
        if (filterExt != null && !match(fileExt,filterExt))
            return false;
         
        return match(fileName,filterName);
    }

    public static boolean fileMatch(String file, String filter) throws Exception {
        String filterName;
        String filterExt;
        int i = filter.lastIndexOf('.');
        if (i >= 0)
        {
            filterName = filter.Substring(0, (i)-(0));
            filterExt = filter.substring(i + 1);
        }
        else
        {
            filterName = filter;
            filterExt = null;
        } 
        return fileMatch(file,filterName,filterExt);
    }

    public static boolean match(String str, String filter) throws Exception {
        if (str == null)
            return false;
         
        if (filter == null)
            return false;
         
        str = str.toLowerCase();
        filter = filter.toLowerCase();
        int ptr = 0;
        int filterLength = filter.length();
        int strLength = str.length();
        for (int i = 0;i < filterLength;i++)
        {
            char fchar = filter.charAt(i);
            switch(fchar)
            {
                case WILDCARD_ALL: 
                    {
                        // get substring after the *
                        int wildcard_index = wildcardIndex(filter,i + 1);
                        String substr = filter.Substring(i + 1, (wildcard_index)-(i + 1));
                        // if there is no substrinng after * - we match
                        if (substr.length() == 0)
                            return true;
                         
                        //search instance of the substring in the given string
                        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                        int index = SupportClass.getIndex(str,substr,ptr);
                        while (index >= 0)
                        {
                            // check if the rest of the strings match
                            if (match(str.substring(index + substr.length()),filter.substring(wildcard_index)))
                                return true;
                             
                            // try to find another instance of the substring
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            index = SupportClass.getIndex(str,substr,index + 1);
                        }
                    }
                    return false;
                case WILDCARD_ONE: 
                    // no matching instances - no match
                    ptr++;
                    break;
                default: 
                    if (ptr >= strLength || fchar != str.charAt(ptr))
                        return false;
                     
                    ptr++;
                    break;
            
            }
        }
        return ptr >= str.length();
    }

    public Iterator createIterator(String filter) throws Exception {
        return new Iterator(this,filter);
    }

    //UPGRADE_NOTE: Field 'EnclosingInstance' was added to class 'Iterator' to access its enclosing instance. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1019'"
    public static class Iterator   
    {
        private void initBlock(FileFilter enclosingInstance) throws Exception {
            this.enclosingInstance = enclosingInstance;
        }

        private FileFilter enclosingInstance;
        public FileFilter getEnclosing_Instance() throws Exception {
            return enclosingInstance;
        }

        private String m_filterName;
        private String m_filterExt;
        private int m_cursor = -1;
        public Iterator(FileFilter enclosingInstance, String filter) throws Exception {
            initBlock(enclosingInstance);
            int i = filter.lastIndexOf('.');
            if (i >= 0)
            {
                m_filterName = filter.Substring(0, (i)-(0));
                m_filterExt = filter.substring(i + 1);
            }
            else
            {
                m_filterName = filter;
                m_filterExt = null;
            } 
        }

        public String next() throws Exception {
            m_cursor = getEnclosing_Instance().getMatch(m_filterName,m_filterExt,m_cursor);
            if (m_cursor >= 0 && m_cursor < getEnclosing_Instance().m_cache.length)
                return getEnclosing_Instance().m_cache[m_cursor++];
             
            return null;
        }

        public String first() throws Exception {
            m_cursor = 0;
            return next();
        }
    
    }

}


