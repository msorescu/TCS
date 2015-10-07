//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:59 PM
//

package Tcs.Mls;

import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.TimeSpan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import Tcs.Mls.TimeZoneInfoEx;

// Container for time zone information
public class TimeZoneInfoEx   
{
    private String m_displayName;
    private String m_standardName;
    private String m_daylightName;
    private int m_index;
    private boolean m_supportsDst;
    private TimeSpan m_bias;
    private TimeSpan m_daylightBias;
    private Date m_standardTransitionTimeOfDay;
    private int m_standardTransitionMonth;
    private int m_standardTransitionWeek;
    private int m_standardTransitionDayOfWeek;
    private Date m_daylightTransitionTimeOfDay;
    private int m_daylightTransitionMonth;
    private int m_daylightTransitionWeek;
    private int m_daylightTransitionDayOfWeek;
    private static TimeZoneInfoEx[] m_timeZoneInfoList;
    private TimeZoneInfoEx() throws Exception {
    }

    public String toString() {
        try
        {
            return getDisplayName();
        }
        catch (RuntimeException __dummyCatchVar0)
        {
            throw __dummyCatchVar0;
        }
        catch (Exception __dummyCatchVar0)
        {
            throw new RuntimeException(__dummyCatchVar0);
        }
    
    }

    public String getDisplayName() throws Exception {
        return m_displayName;
    }

    public String getStandardName() throws Exception {
        return m_standardName;
    }

    public String getDaylightName() throws Exception {
        return m_daylightName;
    }

    public int getIndex() throws Exception {
        return m_index;
    }

    public boolean getSupportsDaylightSavings() throws Exception {
        return m_supportsDst;
    }

    public TimeSpan getBias() throws Exception {
        return m_bias;
    }

    public TimeSpan getDaylightBias() throws Exception {
        return m_daylightBias;
    }

    public Date getStandardTransitionTimeOfDay() throws Exception {
        return m_standardTransitionTimeOfDay;
    }

    public int getStandardTransitionMonth() throws Exception {
        return m_standardTransitionMonth;
    }

    public int getStandardTransitionWeek() throws Exception {
        return m_standardTransitionWeek;
    }

    public int getStandardTransitionDayOfWeek() throws Exception {
        return m_standardTransitionDayOfWeek;
    }

    public Date getDaylightTransitionTimeOfDay() throws Exception {
        return m_daylightTransitionTimeOfDay;
    }

    public int getDaylightTransitionMonth() throws Exception {
        return m_daylightTransitionMonth;
    }

    public int getDaylightTransitionWeek() throws Exception {
        return m_daylightTransitionWeek;
    }

    public int getDaylightTransitionDayOfWeek() throws Exception {
        return m_daylightTransitionDayOfWeek;
    }

    public static TimeZoneInfoEx findTimeZone(String timeZoneName) throws Exception {
        if (m_timeZoneInfoList == null)
            m_timeZoneInfoList = getTimeZonesFromRegistry();
         
        for (TimeZoneInfoEx zone : m_timeZoneInfoList)
        {
            if (zone.getDisplayName().indexOf(timeZoneName) > -1)
            {
                return zone;
            }
             
        }
        return null;
    }

    public static TimeZoneInfoEx[] getTimeZonesFromRegistry() throws Exception {
        ArrayList timeZoneList = new ArrayList();
        // Extract the information from the registry into an arraylist.
        String timeZoneKeyPath = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones";
        RegistryKey timeZonesKey = Registry.LocalMachine.OpenSubKey(timeZoneKeyPath);
        try
        {
            {
                String[] zoneKeys = timeZonesKey.GetSubKeyNames();
                int zoneKeyCount = zoneKeys.length;
                for (int i = 0;i < zoneKeyCount;i++)
                {
                    //StringBuilder sb = new StringBuilder();
                    RegistryKey timeZoneKey = timeZonesKey.OpenSubKey(zoneKeys[i]);
                    try
                    {
                        {
                            TimeZoneInfoEx newTimeZone = new TimeZoneInfoEx();
                            newTimeZone.m_displayName = (String)timeZoneKey.GetValue("Display");
                            newTimeZone.m_daylightName = (String)timeZoneKey.GetValue("Dlt");
                            newTimeZone.m_standardName = (String)timeZoneKey.GetValue("Std");
                            try
                            {
                                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ timeZoneIndex = timeZoneKey.GetValue("Index");
                                if (timeZoneIndex != null)
                                    newTimeZone.m_index = (int)timeZoneIndex;
                                 
                            }
                            catch (Exception __dummyCatchVar1)
                            {
                            }

                            byte[] bytes = (byte[])timeZoneKey.GetValue("TZI");
                            newTimeZone.m_bias = new TimeSpan(0, BitConverter.ToInt32(bytes, 0), 0);
                            newTimeZone.m_daylightBias = new TimeSpan(0, BitConverter.ToInt32(bytes, 8), 0);
                            newTimeZone.m_standardTransitionMonth = BitConverter.ToInt16(bytes, 14);
                            newTimeZone.m_standardTransitionDayOfWeek = BitConverter.ToInt16(bytes, 16);
                            newTimeZone.m_standardTransitionWeek = BitConverter.ToInt16(bytes, 18);
                            newTimeZone.m_standardTransitionTimeOfDay = new Date(1, 1, 1, BitConverter.ToInt16(bytes, 20), BitConverter.ToInt16(bytes, 22), BitConverter.ToInt16(bytes, 24), BitConverter.ToInt16(bytes, 26));
                            newTimeZone.m_daylightTransitionMonth = BitConverter.ToInt16(bytes, 30);
                            newTimeZone.m_daylightTransitionDayOfWeek = BitConverter.ToInt16(bytes, 32);
                            newTimeZone.m_daylightTransitionWeek = BitConverter.ToInt16(bytes, 34);
                            newTimeZone.m_daylightTransitionTimeOfDay = new Date(1, 1, 1, BitConverter.ToInt16(bytes, 36), BitConverter.ToInt16(bytes, 38), BitConverter.ToInt16(bytes, 40), BitConverter.ToInt16(bytes, 42));
                            newTimeZone.m_supportsDst = (newTimeZone.m_standardTransitionMonth != 0);
                            //sb.Append(newTimeZone.m_displayName);
                            //sb.Append("\t");
                            //sb.Append(newTimeZone.m_daylightName);
                            //sb.Append("\t");
                            //sb.Append(newTimeZone.m_standardName);
                            //sb.Append("\t");
                            //sb.Append(newTimeZone.m_supportsDst.ToString());
                            //sb.Append("\r\n");
                            timeZoneList.add(newTimeZone);
                        }
                    }
                    finally
                    {
                        if (timeZoneKey != null)
                            Disposable.mkDisposable(timeZoneKey).dispose();
                         
                    }
                }
            }
        }
        finally
        {
            if (timeZonesKey != null)
                Disposable.mkDisposable(timeZonesKey).dispose();
             
        }
        //System.IO.File.WriteAllText("c:\\TimeZoneList.txt", sb.ToString());
        // Put the time zone infos into an array and sort them by the Index Property
        TimeZoneInfoEx[] timeZoneInfos = new TimeZoneInfoEx[timeZoneList.size()];
        int[] timeZoneOrders = new int[timeZoneList.size()];
        for (int i = 0;i < timeZoneList.size();i++)
        {
            TimeZoneInfoEx zoneInfo = (TimeZoneInfoEx)timeZoneList.get(i);
            timeZoneInfos[i] = zoneInfo;
            timeZoneOrders[i] = zoneInfo.getIndex();
        }
        Array.Sort(timeZoneOrders, timeZoneInfos);
        return timeZoneInfos;
    }

    private static Date getRelativeDate(int year, int month, int targetDayOfWeek, int numberOfSundays) throws Exception {
        Date time;
        if (numberOfSundays <= 4)
        {
            //
            // Get the (numberOfSundays)th Sunday.
            //
            time = new GregorianCalendar(year, month-1, 1).getTime();
            int dayOfWeek = (int)time.DayOfWeek;
            int delta = targetDayOfWeek - dayOfWeek;
            if (delta < 0)
            {
                delta += 7;
            }
             
            delta += 7 * (numberOfSundays - 1);
            if (delta > 0)
            {
                time = DateTimeSupport.add(time,Calendar.DAY_OF_YEAR,delta);
            }
             
        }
        else
        {
            //
            // If numberOfSunday is greater than 4, we will get the last sunday.
            //
            int daysInMonth = Date.DaysInMonth(year, month);
            time = new GregorianCalendar(year, month-1, daysInMonth).getTime();
            // This is the day of week for the last day of the month.
            int dayOfWeek = (int)time.DayOfWeek;
            int delta = dayOfWeek - targetDayOfWeek;
            if (delta < 0)
            {
                delta += 7;
            }
             
            if (delta > 0)
            {
                time = DateTimeSupport.add(time,Calendar.DAY_OF_YEAR,-delta);
            }
             
        } 
        return time;
    }

    private static DaylightTime getDaylightTime(int year, TimeZoneInfoEx zone) throws Exception {
        TimeSpan delta = zone.getDaylightBias();
        Date startTime = getRelativeDate(year,zone.getDaylightTransitionMonth(),zone.getDaylightTransitionDayOfWeek(),zone.getDaylightTransitionWeek());
        startTime = startTime.AddTicks(zone.getDaylightTransitionTimeOfDay().Ticks);
        Date endTime = getRelativeDate(year,zone.getStandardTransitionMonth(),zone.getStandardTransitionDayOfWeek(),zone.getStandardTransitionWeek());
        endTime = endTime.AddTicks(zone.getStandardTransitionTimeOfDay().Ticks);
        return new DaylightTime(startTime, endTime, delta);
    }

    public static boolean getIsDalightSavingsFromLocal(Date time, TimeZoneInfoEx zone) throws Exception {
        if (!zone.getSupportsDaylightSavings())
        {
            return false;
        }
         
        DaylightTime daylightTime = GetDaylightTime(time.Year, zone);
        // startTime and endTime represent the period from either the start of DST to the end and includes the
        // potentially overlapped times
        Date startTime = daylightTime.Start - zone.getDaylightBias();
        Date endTime = daylightTime.End;
        boolean isDst = false;
        if (DateTimeSupport.lessthan(endTime, startTime))
        {
            // In southern hemisphere, the daylight saving time starts later in the year, and ends in the beginning of next year.
            // Note, the summer in the southern hemisphere begins late in the year.
            if (DateTimeSupport.lessthanorequal(startTime, time) || DateTimeSupport.lessthan(time, endTime))
            {
                isDst = true;
            }
             
        }
        else if (DateTimeSupport.lessthanorequal(startTime, time) && DateTimeSupport.lessthan(time, endTime))
        {
            // In northern hemisphere, the daylight saving time starts in the middle of the year.
            isDst = true;
        }
          
        return isDst;
    }

    public static boolean getIsDalightSavingsFromUtc(Date time, TimeZoneInfoEx zone) throws Exception {
        if (!zone.getSupportsDaylightSavings())
        {
            return false;
        }
         
        // Get the daylight changes for the year of the specified time.
        TimeSpan offset = -zone.getBias();
        DaylightTime daylightTime = GetDaylightTime(time.Year, zone);
        // The start and end times represent the range of universal times that are in DST for that year.
        // Within that there is an ambiguous hour, usually right at the end, but at the beginning in
        // the unusual case of a negative daylight savings delta.
        Date startTime = daylightTime.Start - offset;
        Date endTime = daylightTime.End - offset + zone.getDaylightBias();
        boolean isDst = false;
        if (DateTimeSupport.lessthan(endTime, startTime))
        {
            // In southern hemisphere, the daylight saving time starts later in the year, and ends in the beginning of next year.
            // Note, the summer in the southern hemisphere begins late in the year.
            isDst = (DateTimeSupport.lessthan(time, endTime) || DateTimeSupport.lessthanorequal(startTime, time));
        }
        else
        {
            // In northern hemisphere, the daylight saving time starts in the middle of the year.
            isDst = (DateTimeSupport.lessthanorequal(startTime, time) && DateTimeSupport.lessthan(time, endTime));
        } 
        return isDst;
    }

    public static TimeSpan getUtcOffsetFromLocal(Date time, TimeZoneInfoEx zone) throws Exception {
        TimeSpan baseOffset = -zone.getBias();
        boolean isDaylightSavings = getIsDalightSavingsFromLocal(time,zone);
        TimeSpan finalOffset = baseOffset -= (isDaylightSavings ? zone.getDaylightBias() : TimeSpan.Zero);
        return baseOffset;
    }

    public static TimeSpan getUtcOffsetFromUtc(Date time, TimeZoneInfoEx zone) throws Exception {
        TimeSpan baseOffset = -zone.getBias();
        boolean isDaylightSavings = getIsDalightSavingsFromUtc(time,zone);
        TimeSpan finalOffset = baseOffset -= (isDaylightSavings ? zone.getDaylightBias() : TimeSpan.Zero);
        return baseOffset;
    }

    public static Date convertTimeZoneToUtc(Date time, TimeZoneInfoEx zone) throws Exception {
        TimeSpan offset = getUtcOffsetFromLocal(time,zone);
        Date utcConverted = new Date(time.Ticks - offset.Ticks);
        return utcConverted;
    }

    public static Date convertUtcToTimeZone(Date time, TimeZoneInfoEx zone) throws Exception {
        TimeSpan offset = getUtcOffsetFromUtc(time,zone);
        Date localConverted = new Date(time.Ticks + offset.Ticks);
        return localConverted;
    }

    public static Date convertTimeZoneToTimeZone(Date time, TimeZoneInfoEx zoneSource, TimeZoneInfoEx zoneDestination) throws Exception {
        Date utcConverted = convertTimeZoneToUtc(time,zoneSource);
        Date localConverted = convertUtcToTimeZone(utcConverted,zoneDestination);
        return localConverted;
    }

}


