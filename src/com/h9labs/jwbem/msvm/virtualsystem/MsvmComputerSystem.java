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

package com.h9labs.jwbem.msvm.virtualsystem;

import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemMethod;
import com.h9labs.jwbem.SWbemObject;
import com.h9labs.jwbem.SWbemObjectSet;
import com.h9labs.jwbem.SWbemServices;
import com.h9labs.jwbem.SWbemSet;
import com.h9labs.jwbem.msvm.MsvmObject;
import com.h9labs.jwbem.msvm.networking.MsvmSyntheticEthernetPort;

/**
 * Represents a hosting computer system or virtual computer system.
 * 
 * @author akutz
 * 
 */
public class MsvmComputerSystem extends MsvmObject
{
    private SWbemMethod requestStateChange;

    /**
     * Initializes a new instance of the MsvmComputerSystem class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public MsvmComputerSystem(
        IJIDispatch objectDispatcher,
        SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Requests that the state of the computer system be changed to the value
     * specified in the RequestedState parameter. Invoking the
     * RequestStateChange method multiple times could result in earlier requests
     * being overwritten or lost. If 0 is returned, then the task completed
     * successfully. Any other return code indicates an error condition.
     * 
     * @param requestedState The new state
     * @return A status code.
     * @throws Exception When an error occurs.
     */
    public int requestStateChange(final int requestedState) throws Exception
    {
        if (this.requestStateChange == null)
        {
            for (final SWbemMethod m : super.getMethods())
            {
                if (m.getName().equals("RequestStateChange"))
                {
                    this.requestStateChange = m;
                }
            }
        }

        SWbemObject inParams = this.requestStateChange.getInParameters();
        inParams.getObjectDispatcher().put(
            "RequestedState",
            new JIVariant(requestedState));
        inParams.getObjectDispatcher().put("TimeoutPeriod", JIVariant.NULL());

        Object[] methodParams =
            new Object[]
            {
                new JIString("RequestStateChange"),
                new JIVariant(inParams.getObjectDispatcher()), new Integer(0),
                JIVariant.NULL(),
            };

        super.objectDispatcher.callMethodA("ExecMethod_", methodParams);

        return 0;
    }

    /**
     * Gets the associated MsvmVirtualSystemSettingData for this virtual
     * computer system.
     * 
     * @return The associated virtual system data for this virtual computer
     *         system.
     * @throws Exception When an error occurs.
     * @remarks This method will fail for host computer systems; it is only
     *          valid for virtual computer systems.
     */
    public MsvmVirtualSystemSettingData getSettingData() throws Exception
    {
        // Get the settings data.
        String path = super.getObjectPath().getPath();
        String wmiClass = "Msvm_VirtualSystemSettingData";
        String format = "ASSOCIATORS OF {%s} WHERE ResultClass=%s";
        String wql = String.format(format, path, wmiClass);
        SWbemObjectSet<MsvmVirtualSystemSettingData> objSetSD =
            super.getService().execQuery(
                wql,
                MsvmVirtualSystemSettingData.class);

        if (objSetSD.getSize() != 1)
        {
            throw new Exception("More than one Msvm_VirtualSystemSettingData");
        }

        return objSetSD.iterator().next();
    }

    /**
     * Gets the enabled and disabled states of an element.
     * 
     * @return The enabled and disabled states of an element.
     */
    public int getEnabledState()
    {
        return super.getProperty("EnabledState", Integer.class);
    }

    /**
     * Gets the associated MsvmSyntheticEthernetPorts for this virtual computer
     * system.
     * 
     * @return The associated MsvmSyntheticEthernetPorts for this virtual
     *         computer system.
     * @throws Exception When an error occurs.
     */
    public SWbemSet<MsvmSyntheticEthernetPort> getSyntheticEthernetPorts()
        throws Exception
    {
        // Get the settings data.
        String path = super.getObjectPath().getPath();
        String wmiClass = "Msvm_SyntheticEthernetPort";
        String format = "ASSOCIATORS OF {%s} WHERE ResultClass=%s";
        String wql = String.format(format, path, wmiClass);
        SWbemObjectSet<MsvmSyntheticEthernetPort> objSetSEP =
            super.getService().execQuery(wql, MsvmSyntheticEthernetPort.class);
        return objSetSEP;
    }

    /**
     * <p>
     * The current health of the element.
     * </p>
     * <p>
     * This attribute expresses the health of this element but not necessarily
     * that of its subcomponents. The possible values are 5 and 25, where 5
     * means the element is entirely healthy and 25 means there is a critical
     * error.
     * </p>
     * <p>
     * When a critical error occurs, check the event log for details. The
     * EnabledState property can also contain more information. For example,
     * when disk space is critically low, HealthState is set to 25, the VM
     * pauses, and EnabledState is set to 32768 (Paused).
     * </p>
     * 
     * @return <p>
     *         The current health of the element.
     *         </p>
     *         <p>
     *         <ul>
     *         <li>OK - 5 - The VM is fully functional and is operating within
     *         normal operational parameters and without error.</li>
     *         <li>Major Failure - 20 - The VM has suffered a major failure.
     *         This value is used when one or more disks containing the VM's
     *         VHDs is low on disk space and the VM has been paused. This value
     *         is not supported before Windows ManagementServerBean 2008 R2.</li>
     *         <li>Critical failure - 25 - The element is nonfunctional, and
     *         recovery might not be possible. This can indicate that the worker
     *         process for the VM (Vmwp.exe) is not responding to control or
     *         information requests, or that one or more disks containing the
     *         VHDs for the VM are low on disk space.</li>
     *         </ul>
     *         </p>
     */
    public int getHealthState()
    {
        return super.getProperty("HealthState", Integer.class);
    }
}
