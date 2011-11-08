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

package com.h9labs.jwbem;

import java.net.UnknownHostException;
import org.jinterop.dcom.common.IJIAuthInfo;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIClsid;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;

/**
 * An SWbemLocator.
 * 
 * @author akutz
 * 
 */
public class SWbemLocator
{
    private static final String SWbemLocatorInterfaceId =
        "76a6415b-cb41-11d1-8b02-00600806d9b6";

    private static final String SWbemLocatorId =
        "76a64158-cb41-11d1-8b02-00600806d9b6";

    private IJIAuthInfo credentials = null;

    private JISession session = null;

    private JIComServer server = null;

    // The static constructor.
    static
    {
        JISystem.setAutoRegisteration(true);
        JISystem.setJavaCoClassAutoCollection(false);
    }

    /**
     * Connects to a remote Windows host.
     * 
     * @param hostName The name of the host to connect to.
     * @param swbemTarget The name of the host to send WMI calls to. This value
     *        should almost ALWAYS be set to 127.0.0.1, but there are instances
     *        where the wmiTarget may be a different machine than the one
     *        specified in the hostName parameter, such as if the machine
     *        specified by the hostName parameter is a WMI proxy in a DMZ.
     * @param namespace The namespace to connect to.
     * @param userName The username to connect as.
     * @param password The password for the username.
     * @return A SWbemService object.
     * @throws JIException When an error occurs.
     * @throws UnknownHostException When the host is unknown.
     */
    public SWbemServices connect(
        final String hostName,
        final String swbemTarget,
        final String namespace,
        final String userName,
        final String password) throws JIException, UnknownHostException
    {
        // if (session != null) close();
        this.credentials = new Credentials(userName, password);

        session = JISession.createSession(this.credentials);

        // Causes the use of ntlm2, key exchange, etc.
        session.useSessionSecurity(true);
        
        // Sets timeout for all connections to COM server
        session.setGlobalSocketTimeout(1000 * 60 * 15); // in milliseconds
        
        JISystem.setAutoRegisteration(true);

        // Connect to the SWbemLocator program
        IJIDispatch sWbemLocatorDispatch = getSWbemLocatorInterface(hostName);

        // Get the WbemServices interface
        IJIDispatch svcDispatch =
            getWbemServices(sWbemLocatorDispatch, swbemTarget, namespace);

        return new SWbemServices(svcDispatch, this);
    }

    /**
     * Disconnects and closes the connection to the server.
     */
    public void disconnect()
    {
        if (session != null)
        {
            try
            {
                JISession.destroySession(session);
            }
            catch (Exception ex)
            {
                // Swallow the exception
            }
            finally
            {
                session = null;
                server = null;
            }
        }
    }

    private IJIDispatch getSWbemLocatorInterface(String hostname)
        throws JIException,
        UnknownHostException
    {
        JIClsid classId = JIClsid.valueOf(SWbemLocatorId);
        server = new JIComServer(classId, hostname, session);

        IJIComObject unknown = server.createInstance();
        IJIComObject comObject =
            unknown.queryInterface(SWbemLocatorInterfaceId);
        return (IJIDispatch) JIObjectFactory.narrowObject(comObject
            .queryInterface(IJIDispatch.IID));
    }

    private IJIDispatch getWbemServices(
        IJIDispatch sWbemLocatorDispatch,
        String swbemTarget,
        String namespace) throws JIException
    {
        Object[] inParams =
            new Object[]
            {
                new JIString(swbemTarget), new JIString(namespace),
                JIVariant.OPTIONAL_PARAM(), JIVariant.OPTIONAL_PARAM(),
                JIVariant.OPTIONAL_PARAM(), JIVariant.OPTIONAL_PARAM(),
                new Integer(0), JIVariant.OPTIONAL_PARAM(),
            };
        JIVariant[] results =
            sWbemLocatorDispatch.callMethodA("ConnectServer", inParams);

        IJIComObject co = results[0].getObjectAsComObject();
        return (IJIDispatch) JIObjectFactory.narrowObject(co);
    }

    private class Credentials implements IJIAuthInfo
    {
        private String domain = "";
        final private String userName;
        final private String password;

        public Credentials(String userName, String password)
        {
            if (userName.contains("@"))
            {
                String[] parts = userName.split("@");
                this.userName = parts[0];
                this.domain = parts[1];
            }
            else if (userName.contains("\\"))
            {
                String[] parts = userName.split("\\\\");
                this.userName = parts[1];
                this.domain = parts[0];
            }
            else
            {
                this.userName = userName;
            }
            this.password = password;
        }

        public String getDomain()
        {
            return this.domain;
        }

        public String getPassword()
        {
            return this.password;
        }

        public String getUserName()
        {
            return this.userName;
        }
    }
}
