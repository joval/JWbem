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

package com.h9labs.jwbem.msvm.virtualsystemmanagement;

import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemMethod;
import com.h9labs.jwbem.SWbemObject;
import com.h9labs.jwbem.SWbemObjectSet;
import com.h9labs.jwbem.SWbemServices;
import com.h9labs.jwbem.msvm.MsvmObject;
import com.h9labs.jwbem.msvm.virtualsystem.MsvmSummaryInformation;
import com.h9labs.jwbem.msvm.virtualsystem.MsvmVirtualSystemSettingData;

/**
 * This class represents the Msvm_VirtualSystemManagementService class.
 * 
 * @author akutz
 * 
 */
public class MsvmVirtualSystemManagementService extends MsvmObject
{
    private SWbemMethod getSummaryInformation;

    /**
     * Initializes a new instance of the MsvmVirtualSystemManagementService
     * class.
     * 
     * @param dispatch The dispatch object.
     * @param service The service connection.
     */
    public MsvmVirtualSystemManagementService(
        IJIDispatch dispatch,
        SWbemServices service)
    {
        super(dispatch, service);
    }

    /**
     * Gets the hosts Msvm_VirtualSystemManagementService.
     * 
     * @param service The service connection.
     * @return The hosts Msvm_VirtualSystemManagementService.
     * @throws Exception When an error occurs.
     */
    static public MsvmVirtualSystemManagementService getManagementService(
        final SWbemServices service) throws Exception
    {
        // Get the management service.
        final String wql = "SELECT * FROM Msvm_VirtualSystemManagementService";
        final SWbemObjectSet<MsvmVirtualSystemManagementService> objSetMgmtSvc =
            service.execQuery(wql, MsvmVirtualSystemManagementService.class);
        final int size = objSetMgmtSvc.getSize();
        if (size != 1)
        {
            throw new Exception(
                "There should be exactly 1 Msvm_VirtualSystemManagementService");
        }
        final MsvmVirtualSystemManagementService mgmtSvc =
            objSetMgmtSvc.iterator().next();
        return mgmtSvc;
    }

    /**
     * Returns virtual system summary information.
     * 
     * @param settingData An array of CIM_VirtualSystemSettingData instances
     *        that specifies the virtual machines and/or snapshots for which
     *        information is to be retrieved. If this parameter is NULL,
     *        information for all virtual machines is retrieved.
     * @param requestedInformation An array of enumeration values (which
     *        correspond to the properties in the Msvm_SummaryInformation class)
     *        that specifies the data to retrieve for the virtual machines
     *        and/or snapshots specified in the SettingData array. Values in the
     *        0-99 range apply to both virtual machines and snapshots. Values in
     *        the 100-199 range apply to virtual machines only, and will be
     *        ignored for elements of SettingData which represent snapshots.
     *        Values in the 200-299 range apply to snapshots only, and will be
     *        ignored for elements of SettingData which represent virtual
     *        machines.
     * @return Virtual system summary information.
     * @throws Exception When an error occurs.
     */
    public MsvmSummaryInformation getSummaryInformation(
        MsvmVirtualSystemSettingData[] settingData,
        Integer[] requestedInformation) throws Exception
    {
        if (this.getSummaryInformation == null)
        {
            for (final SWbemMethod m : super.getMethods())
            {
                if (m.getName().equals("GetSummaryInformation"))
                {
                    this.getSummaryInformation = m;
                }
            }
        }

        JIString[] sdpaths = new JIString[settingData.length];
        for (int x = 0; x < sdpaths.length; ++x)
        {
            String path = settingData[x].getObjectPath().getPath();
            sdpaths[x] = new JIString(path);
        }

        // Get the IN parameters.
        SWbemObject inParams = this.getSummaryInformation.getInParameters();
        inParams.getObjectDispatcher().put(
            "SettingData",
            new JIVariant(new JIArray(sdpaths)));
        inParams.getObjectDispatcher().put(
            "RequestedInformation",
            new JIVariant(new JIArray(requestedInformation)));

        Object[] methodParams =
            new Object[]
            {
                new JIString("GetSummaryInformation"),
                new JIVariant(inParams.getObjectDispatcher()), new Integer(0),
                JIVariant.NULL(),
            };

        // Execute the method.
        JIVariant[] results =
            super.objectDispatcher.callMethodA("ExecMethod_", methodParams);

        // Get the out parameters.
        JIVariant outParamsVar = results[0];
        IJIComObject co = outParamsVar.getObjectAsComObject();
        IJIDispatch outParamsDisp =
            (IJIDispatch) JIObjectFactory.narrowObject(co);

        // Get the out parameter SummaryInformation and convert it into an
        // array of JIVariants.
        JIVariant summInfoVars = outParamsDisp.get("SummaryInformation");
        JIArray summInfoJIArr = summInfoVars.getObjectAsArray();
        JIVariant[] summInfoJIVarArr =
            (JIVariant[]) summInfoJIArr.getArrayInstance();

        if (summInfoJIVarArr.length != 1)
        {
            throw new UnsupportedOperationException("More than one summary.");
        }

        IJIComObject summInfoCo = summInfoJIVarArr[0].getObjectAsComObject();
        IJIDispatch summInfoDisp =
            (IJIDispatch) JIObjectFactory.narrowObject(summInfoCo);

        return new MsvmSummaryInformation(summInfoDisp, this.service);
    }
}
