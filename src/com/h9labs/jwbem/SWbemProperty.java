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
import org.jinterop.dcom.core.JIUnsignedByte;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.automation.IJIDispatch;

/**
 * An SWbemProperty class.
 * 
 * @author akutz
 * @see "http://msdn.microsoft.com/en-us/library/aa393804(VS.85).aspx"
 */
public class SWbemProperty extends SWbemDispatchObject implements SWbemSetItem
{
    private JIVariant value;
    private String name;
    private Boolean isArray;

    /**
     * Initializes a new instance of the SWbemProperty class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     * 
     */
    public SWbemProperty(IJIDispatch objectDispatcher, SWbemServices service)
    {
        super(objectDispatcher, service);

        try
        {
            this.value = super.objectDispatcher.get("Value");
        }
        catch (JIException ex)
        {
            this.value = null;
        }
    }

    /**
     * Gets the name of the property.
     * 
     * @return The name of the property.
     * @throws JIException When an error occurs.
     */
    public String getName() throws JIException
    {
        if (this.name != null)
        {
            return this.name;
        }

        this.name = super.objectDispatcher.get("Name").getObjectAsString2();
        return this.name;
    }

    /**
     * Gets a value indicating whether or not the object is an array.
     * 
     * @return A value indicating whether or not the object is an array.
     * @throws JIException When an error occurs.
     */
    public boolean isArray() throws JIException
    {
        if (this.isArray != null)
        {
            return this.isArray;
        }

        this.isArray =
            super.objectDispatcher.get("IsArray").getObjectAsBoolean();
        return this.isArray;
    }

    /**
     * Gets the value of the object as an object.
     * 
     * @return The value of the object as an object.
     * @throws JIException When an error occurs.
     */
    public Object getValue() throws JIException
    {
        return this.value.getObject();
    }

    /**
     * Gets the value of the property as an integer.
     * 
     * @return The value of the property as an integer.
     * @throws JIException When an error occurs.
     */
    public Integer getValueAsInteger() throws JIException
    {
        switch (this.value.getType())
        {
            case JIVariant.VT_BSTR :
            {
                return Integer.parseInt(this.value.getObjectAsString2());
            }
            case JIVariant.VT_I4 :
            {
                return this.value.getObjectAsInt();
            }
            default:
            {
                return null;
            }
        }
    }
    
    /**
     * Gets the value of the property as a long.
     * 
     * @return The value of the property as an long.
     * @throws JIException When an error occurs.
     */
    public Long getValueAsLong() throws JIException
    {
        switch (this.value.getType())
        {
            case JIVariant.VT_BSTR :
            {
                return Long.parseLong(this.value.getObjectAsString2());
            }
            case JIVariant.VT_I4 :
            {
                return new Long(this.value.getObjectAsInt());
            }
            case JIVariant.VT_I8 :
            {
                return this.value.getObjectAsLong();
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Gets the value of the property as a boolean.
     * 
     * @return The value of the property as a boolean.
     * @throws JIException When an error occurs.
     */
    public Boolean getValueAsBoolean() throws JIException
    {
        return this.value.getObjectAsBoolean();
    }

    /**
     * Gets the value of the property as a string.
     * 
     * @return The value of the property as a string.
     * @throws JIException When an error occurs.
     */
    private String getValueAsString() throws JIException
    {
        switch (this.value.getType())
        {
            case JIVariant.VT_BSTR :
            {
                return this.value.getObjectAsString2();
            }
            case JIVariant.VT_BOOL :
            {
                return String.valueOf(this.value.getObjectAsBoolean());
            }
            case JIVariant.VT_I2 :
            {
                return String.valueOf(this.value.getObjectAsShort());
            }
            case JIVariant.VT_I4 :
            {
                return String.valueOf(this.value.getObjectAsInt());
            }
            case JIVariant.VT_NULL :
            {
                return null;
            }
            case JIVariant.VT_UI1 :
            {
                return ((JIUnsignedByte) this.value.getObject())
                    .getValue()
                    .toString();
            }
            default :
            {
                // Unhandled at the moment
                return "<type: " + String.valueOf(this.value.getType())
                    + ", flag: " + String.valueOf(this.value.getFlag())
                    + ", value: " + this.value.toString() + ">";
            }
        }
    }

    @Override
    public String toString()
    {
        try
        {
            return getValueAsString();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
