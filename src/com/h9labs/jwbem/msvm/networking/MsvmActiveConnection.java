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

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemServices;
import com.h9labs.jwbem.msvm.MsvmObject;

/**
 * Connects a switch port to the LAN endpoint to which the port is connected.
 * 
 * @author akutz
 * @remarks The existence of this object means that the switch port and the LAN
 *          endpoint are actively connected and the Ethernet port associated
 *          with the LAN endpoint can communicate with the network through the
 *          switch port.
 */
public class MsvmActiveConnection extends MsvmObject
{
    /**
     * Initializes a new instance of the MsvmVirtualSystemManagementService
     * class.
     * 
     * @param objectDispatcher The dispatch object.
     * @param service The service connection.
     */
    public MsvmActiveConnection(
        IJIDispatch objectDispatcher,
        SWbemServices service)
    {
        super(objectDispatcher, service);
    }
}
