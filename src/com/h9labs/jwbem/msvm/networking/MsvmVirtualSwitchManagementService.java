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

package com.h9labs.jwbem.msvm.networking;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemMethod;
import com.h9labs.jwbem.SWbemObject;
import com.h9labs.jwbem.SWbemObjectSet;
import com.h9labs.jwbem.SWbemServices;
import com.h9labs.jwbem.msvm.MsvmObject;

/**
 * Controls the definition, modification, and destruction of global networking
 * resources such as virtual switches, switch ports, and internal Ethernet
 * ports.
 * 
 * @author akutz
 * 
 */
public class MsvmVirtualSwitchManagementService extends MsvmObject
{

    private SWbemMethod disconnectSwitchPort;
    private SWbemMethod connectSwitchPort;
    private SWbemMethod createSwitchPort;
    private SWbemMethod deleteSwitchPort;

    /**
     * Initializes a new instance of the MsvmVirtualSwitchManagementService
     * class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public MsvmVirtualSwitchManagementService(
        IJIDispatch objectDispatcher,
        SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Gets the host's MsvmVirtualSwitchManagementService.
     * 
     * @param service The service connection.
     * @return The host's MsvmVirtualSwitchManagementService.
     * @throws Exception When an error occurs.
     */
    static public MsvmVirtualSwitchManagementService getManagementService(
        final SWbemServices service) throws Exception
    {
        // Get the management service.
        final String wql = "SELECT * FROM Msvm_VirtualSwitchManagementService";
        final SWbemObjectSet<MsvmVirtualSwitchManagementService> objSetMgmtSvc =
            service.execQuery(wql, MsvmVirtualSwitchManagementService.class);
        final int size = objSetMgmtSvc.getSize();
        if (size != 1)
        {
            throw new Exception(
                "There should be exactly 1 Msvm_VirtualSwitchManagementService");
        }
        final MsvmVirtualSwitchManagementService mgmtSvc =
            objSetMgmtSvc.iterator().next();
        return mgmtSvc;
    }

    /**
     * Connects a switch port to a LAN endpoint.
     * 
     * @param switchPort The switch port to be connected to the LAN endpoint.
     * @param lanEndPoint The LAN endpoint to be connected to the switch port.
     * @return Upon success of the connection, an active connection instance
     *         will be created that associates the port with the LAN endpoint.
     * @throws JIException When an error occurs.
     */
    public MsvmActiveConnection connectSwitchPort(
        final MsvmSwitchPort switchPort,
        final MsvmVmLANEndpoint lanEndPoint) throws JIException
    {
        if (this.connectSwitchPort == null)
        {
            for (final SWbemMethod m : super.getMethods())
            {
                if (m.getName().equals("ConnectSwitchPort"))
                {
                    this.connectSwitchPort = m;
                }
            }
        }

        final String switchPortPath = switchPort.getObjectPath().getPath();
        final String lanEndPointPath = lanEndPoint.getObjectPath().getPath();

        // Get the IN parameters.
        SWbemObject inParams = this.connectSwitchPort.getInParameters();
        inParams.getObjectDispatcher().put(
            "SwitchPort",
            new JIVariant(new JIString(switchPortPath)));
        inParams.getObjectDispatcher().put(
            "LANEndpoint",
            new JIVariant(new JIString(lanEndPointPath)));

        Object[] methodParams =
            new Object[]
            {
                new JIString("ConnectSwitchPort"),
                new JIVariant(inParams.getObjectDispatcher()), new Integer(0),
                JIVariant.NULL(),
            };

        JIVariant[] results =
            super.objectDispatcher.callMethodA("ExecMethod_", methodParams);

        // Get the out parameters.
        JIVariant outParamsVar = results[0];
        IJIComObject co = outParamsVar.getObjectAsComObject();
        IJIDispatch outParamsDisp =
            (IJIDispatch) JIObjectFactory.narrowObject(co);

        // Get the out parameter ActiveConnection and convert it into an
        // array of JIVariants.
        JIVariant acVar = outParamsDisp.get("ActiveConnection");
        String acPath = acVar.getObjectAsString2();

        if (acPath.equals(""))
        {
            return null;
        }
        else
        {
            return new MsvmActiveConnection(null, this.service);
        }
    }

    /**
     * Disconnects a virtual switch port.
     * 
     * @param toDisconnect The switch port to be disconnected.
     * @throws JIException When an error occurs.
     */
    public void disconnectSwitchPort(final MsvmSwitchPort toDisconnect)
        throws JIException
    {
        if (this.disconnectSwitchPort == null)
        {
            for (final SWbemMethod m : super.getMethods())
            {
                if (m.getName().equals("DisconnectSwitchPort"))
                {
                    this.disconnectSwitchPort = m;
                }
            }
        }

        final String toDisconnectPath = toDisconnect.getObjectPath().getPath();

        // Get the IN parameters.
        final SWbemObject inParams =
            this.disconnectSwitchPort.getInParameters();
        inParams.getObjectDispatcher().put(
            "SwitchPort",
            new JIVariant(new JIString(toDisconnectPath)));

        Object[] methodParams =
            new Object[]
            {
                new JIString("DisconnectSwitchPort"),
                new JIVariant(inParams.getObjectDispatcher()), new Integer(0),
                JIVariant.NULL(),
            };

        super.objectDispatcher.callMethodA("ExecMethod_", methodParams);
    }

    /**
     * Deletes a virtual switch port.
     * 
     * @param toDelete A reference to the port to be deleted.
     * @throws JIException When an error occurs.
     */
    public void deleteSwitchPort(final MsvmSwitchPort toDelete)
        throws JIException
    {
        if (this.deleteSwitchPort == null)
        {
            for (final SWbemMethod m : super.getMethods())
            {
                if (m.getName().equals("DeleteSwitchPort"))
                {
                    this.deleteSwitchPort = m;
                }
            }
        }

        final String toDeletePath = toDelete.getObjectPath().getPath();

        SWbemObject inParams = this.deleteSwitchPort.getInParameters();
        inParams.getObjectDispatcher().put(
            "SwitchPort",
            new JIVariant(new JIString(toDeletePath)));

        Object[] methodParams =
            new Object[]
            {
                new JIString("DeleteSwitchPort"),
                new JIVariant(inParams.getObjectDispatcher()), new Integer(0),
                JIVariant.NULL(),
            };

        super.objectDispatcher.callMethodA("ExecMethod_", methodParams);
    }

    /**
     * Creates a new port on a virtual switch.
     * 
     * @param vswitch The switch on which the port is to be created.
     * @param name The name of the port. This name must be unique among all
     *        ports.
     * @param friendlyName A user-readable name for the port.
     * @param scope The authorization scope to be used for the access control
     *        policy of this virtual switch port.
     * 
     * @return The new switch port.
     * @throws Exception When an error occurs.
     */
    public MsvmSwitchPort createSwitchPort(
        final MsvmVirtualSwitch vswitch,
        final String name,
        final String friendlyName,
        final String scope) throws Exception
    {
        if (this.createSwitchPort == null)
        {
            for (final SWbemMethod m : super.getMethods())
            {
                if (m.getName().equals("CreateSwitchPort"))
                {
                    this.createSwitchPort = m;
                }
            }
        }

        final String vswitchPath = vswitch.getObjectPath().getPath();

        // Get the IN parameters.
        SWbemObject inParams = this.createSwitchPort.getInParameters();
        inParams.getObjectDispatcher().put(
            "VirtualSwitch",
            new JIVariant(new JIString(vswitchPath)));
        inParams.getObjectDispatcher().put(
            "Name",
            new JIVariant(new JIString(name)));
        inParams.getObjectDispatcher().put(
            "FriendlyName",
            new JIVariant(new JIString(friendlyName)));
        inParams.getObjectDispatcher().put(
            "ScopeOfResidence",
            new JIVariant(new JIString(scope)));

        Object[] methodParams =
            new Object[]
            {
                new JIString("CreateSwitchPort"),
                new JIVariant(inParams.getObjectDispatcher()), new Integer(0),
                JIVariant.NULL(),
            };

        JIVariant[] results =
            super.objectDispatcher.callMethodA("ExecMethod_", methodParams);

        // Get the out parameters.
        JIVariant outParamsVar = results[0];
        IJIComObject co = outParamsVar.getObjectAsComObject();
        IJIDispatch outParamsDisp =
            (IJIDispatch) JIObjectFactory.narrowObject(co);

        // Get the out parameter ActiveConnection and convert it into an
        // array of JIVariants.
        JIVariant cspVar = outParamsDisp.get("CreatedSwitchPort");
        String cspPath = cspVar.getObjectAsString2();

        if (cspPath.equals(""))
        {
            return null;
        }

        MsvmSwitchPort sp =
            MsvmSwitchPort.getByName(service, MsvmSwitchPort.class, name);
        return sp;
    }
}
