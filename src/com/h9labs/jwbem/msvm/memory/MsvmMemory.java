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

package com.h9labs.jwbem.msvm.memory;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemServices;
import com.h9labs.jwbem.msvm.MsvmObject;

/**
 * Represents the memory currently allocated to a virtual system.
 * 
 * @author akutz
 * 
 */
public class MsvmMemory extends MsvmObject
{
    /**
     * Initializes a new instance of the MsvmMemory class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public MsvmMemory(IJIDispatch objectDispatcher, SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Gets a calculated value that represents the total amount of memory
     * divided by the BlockSize.
     * 
     * @return A calculated value that represents the total amount of memory
     *         divided by the BlockSize.
     */
    public long getNumberOfBlocks()
    {
        return super.getProperty("NumberOfBlocks", Long.class);
    }

    /**
     * Gets the size, in bytes, of the blocks that form the storage extent.
     * 
     * @return The size, in bytes, of the blocks that form the storage extent.
     * @remarks If variable block size, then the maximum block size, in bytes,
     *          should be specified. If the block size is unknown, or if a block
     *          concept is not valid (for example, for aggregate extents,
     *          memory, or logical disks), enter a 1 (one).
     */
    public long getBlockSize()
    {
        return super.getProperty("BlockSize", Long.class);
    }
}
