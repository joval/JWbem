/*
 * Copyright (c) 2009, Hyper9 All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of Hyper9 nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.h9labs.jwbem.win32;

import java.util.Date;
import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemObject;
import com.h9labs.jwbem.SWbemServices;

/**
 * The Win32NTLogEvent WMI class is used to translate instances from the Windows
 * NT event log. An application must have SeSecurityPrivilege to receive events
 * from the security event log, otherwise "Access Denied" is returned to the
 * application.
 * 
 * @author akutz
 * 
 */
public class Win32NTLogEvent extends SWbemObject
{
    /**
     * Initializes a new instance of the Win32NTLogEvent class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public Win32NTLogEvent(IJIDispatch objectDispatcher, SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Gets the sub-category for this event. This sub-category is
     * source-specific.
     * 
     * @return The sub-category for this event. This sub-category is
     *         source-specific.
     */
    public int getCategory()
    {
        return super.getProperty("Category", int.class);
    }

    /**
     * Gets the translation of the sub-category. The translation is
     * source-specific.
     * 
     * @return The translation of the sub-category. The translation is
     *         source-specific
     */
    public String getCategoryString()
    {
        return super.getProperty("CategoryString", String.class);
    }

    /**
     * Gets the name of the computer that generated this event.
     * 
     * @return The name of the computer that generated this event.
     */
    public String getComputerName()
    {
        return super.getProperty("ComputerName", String.class);
    }

    /**
     * Gets the user name of the logged-on user when the event occurred. If the
     * user name cannot be determined, this will be NULL.
     * 
     * @return The user name of the logged-on user when the event occurred. If
     *         the user name cannot be determined, this will be NULL.
     */
    public String getUser()
    {
        return super.getProperty("UserBeanImpl", String.class);
    }

    /**
     * Gets the list of the binary data that accompanied the report of the
     * Windows NT event.
     * 
     * @return The list of the binary data that accompanied the report of the
     *         Windows NT event.
     */
    public short[] getData()
    {
        return super.getProperty("Data", short[].class);
    }

    /**
     * <p>
     * Gets the value of the lower 16-bits of the EventIndentifier property. It
     * is present to match the value displayed in the WindowsNT Event viewer.
     * </p>
     * <p>
     * Note: Two events from the same source may have the same value for this
     * property but may have different severity and EventIdentifier values.
     * </p>
     * 
     * @return The value of the lower 16-bits of the EventIndentifier property.
     *         It is present to match the value displayed in the WindowsNT Event
     *         viewer.
     */
    public long getEventCode()
    {
        return super.getProperty("EventCode", long.class);
    }

    /**
     * Gets the identifier of the event. This is specific to the source that
     * generated the event log entry and is used, together with SourceName, to
     * uniquely identify a Windows NT event type.
     * 
     * @return The identifier of the event. This is specific to the source that
     *         generated the event log entry and is used, together with
     *         SourceName, to uniquely identify a Windows NT event type.
     */
    public long getEventIdentifier()
    {
        return super.getProperty("EventIdentifier", long.class);
    }

    /**
     * Gets the list of the insertion strings that accompanied the report of the
     * Windows NT event.
     * 
     * @return The list of the insertion strings that accompanied the report of
     *         the Windows NT event.
     */
    public String[] getInsertionStrings()
    {
        return super.getProperty("InsertionStrings", String[].class);
    }

    /**
     * Gets the name of Windows NT event log file. Together with RecordNumber,
     * this is used to uniquely identify an instance of this class.
     * 
     * @return The name of Windows NT event log file. Together with
     *         RecordNumber, this is used to uniquely identify an instance of
     *         this class.
     */
    public String getLogFile()
    {
        return super.getProperty("LogFile", String.class);
    }

    /**
     * The event message as it appears in the Windows NT event log. This is a
     * standard message with zero or more insertion strings supplied by the
     * source of the Windows NT event. The insertion strings are inserted into
     * the standard message in a predefined format. If there are no insertion
     * strings or there is a problem inserting the insertion strings, only the
     * standard message will be present in this field.
     * 
     * @return The event message as it appears in the Windows NT event log. This
     *         is a standard message with zero or more insertion strings
     *         supplied by the source of the Windows NT event. The insertion
     *         strings are inserted into the standard message in a predefined
     *         format. If there are no insertion strings or there is a problem
     *         inserting the insertion strings, only the standard message will
     *         be present in this field.
     */
    public String getMessage()
    {
        return super.getProperty("MessageBeanImpl", String.class);
    }

    /**
     * <p>
     * Gets the type of event.
     * <p>
     * <ul>
     * <li>1 - Error</li>
     * <li>2 - Warning</li>
     * <li>3 - Information</li>
     * <li>4 - Security Audit Success</li>
     * <li>5 - Security Audit Failure</li>
     * </ul>
     * 
     * @return The type of event.
     */
    public short getEventType()
    {
        return super.getProperty("EventType", short.class);
    }

    /**
     * Gets the record number that identifies the event within the Windows NT
     * event log file. This is specific to the log file and is used together
     * with the log file name to uniquely identify an instance of this class.
     * 
     * @return The record number that identifies the event within the Windows NT
     *         event log file. This is specific to the log file and is used
     *         together with the log file name to uniquely identify an instance
     *         of this class.
     */
    public long getRecordNumber()
    {
        return super.getProperty("RecordNumber", long.class);
    }

    /**
     * Gets the name of the source (application, service, driver, or subsystem)
     * that generated the entry. It is used, together with EventIdentifier to
     * uniquely identify a Windows NT event type.
     * 
     * @return The name of the source (application, service, driver, or
     *         subsystem) that generated the entry. It is used, together with
     *         EventIdentifier to uniquely identify a Windows NT event type.
     */
    public String getSourceName()
    {
        return super.getProperty("SourceName", String.class);
    }

    /**
     * Gets the time that the source generated the event.
     * 
     * @return The time that the source generated the event.
     */
    public Date getTimeGenerated()
    {
        return super.getProperty("TimeGenerated", Date.class);
    }

    /**
     * Gets the time the event was written to the log file.
     * 
     * @return The time the event was written to the log file.
     */
    public Date getTimeWritten()
    {
        return super.getProperty("TimeWritten", Date.class);
    }
}
