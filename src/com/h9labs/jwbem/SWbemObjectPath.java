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

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.impls.automation.IJIDispatch;

/**
 * An SWbemObjectPath class.
 * 
 * @author akutz
 * @see "http://msdn.microsoft.com/en-us/library/aa393746(VS.85).aspx"
 */
public class SWbemObjectPath extends SWbemDispatchObject
{
    /**
     * The object's path.
     */
    private String path;
    
    private String clazz;

    /**
     * Initializes a new instance of the SWbemObjectPath class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public SWbemObjectPath(IJIDispatch objectDispatcher, SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Gets the absolute path. This is the same as the __Path system property in
     * the COM API. This is the default property of this object.
     * 
     * @return The absolute path. This is the same as the __Path system property
     *         in the COM API. This is the default property of this object.
     * @throws JIException When an error occurs.
     */
    public String getPath() throws JIException
    {
        if (this.path != null)
        {
            return this.path;
        }

        this.path = super.objectDispatcher.get("Path").getObjectAsString2();
        return this.path;
    }
    
    /**
     * Gets the name of the class that is part of the object path.
     * @return The name of the class that is part of the object path.
     * @throws JIException When an error occurs.
     */
    public String getClazz() throws JIException
    {
        if (this.clazz != null)
        {
            return this.clazz;
        }
        
        this.clazz = super.objectDispatcher.get("Class").getObjectAsString2();
        return this.clazz;
    }
}