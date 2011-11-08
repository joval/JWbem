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

package com.h9labs.jwbem.msvm;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemObject;
import com.h9labs.jwbem.SWbemObjectSet;
import com.h9labs.jwbem.SWbemServices;
import com.h9labs.jwbem.SWbemSet;

/**
 * The base class for SWbemObjects in the msvm namespace.
 * 
 * @author akutz
 * 
 */
public class MsvmObject extends SWbemObject
{

    /**
     * Initializes a new instance of the MsvmObject class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public MsvmObject(IJIDispatch objectDispatcher, SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Gets an object by its ID.
     * 
     * @param <T> The type of object.
     * 
     * @param service The service connection.
     * @param clazz The class of the return object.
     * @param name The unique name of the object to get.
     * @return If successful the object; otherwise null.
     * @throws Exception When an error occurs.
     */
    public static <T extends MsvmObject> T getByName(
        final SWbemServices service,
        final Class<T> clazz,
        String name) throws Exception
    {
        String format = "SELECT * FROM %s WHERE Name='%s'";
        String wmiClass = clazz.getSimpleName().replace("Msvm", "Msvm_");
        String query = String.format(format, wmiClass, name);
        SWbemObjectSet<T> set = service.execQuery(query, clazz);
        int size = set.getSize();
        if (size == 0)
        {
            return null;
        }
        if (size > 1)
        {
            throw new Exception("More than one object found.");
        }
        T obj = set.iterator().next();
        return obj;
    }

    /**
     * Gets an object by its ID.
     * 
     * @param <T> The type of object.
     * 
     * @param service The service connection.
     * @param clazz The class of the return object.
     * @param elementName The friendly name of the object to get.
     * @return If successful the object; otherwise null.
     * @throws Exception When an error occurs.
     */
    public static <T extends MsvmObject> T getByElementName(
        final SWbemServices service,
        final Class<T> clazz,
        String elementName) throws Exception
    {
        String format = "SELECT * FROM %s WHERE ElementName='%s'";
        String wmiClass = clazz.getSimpleName().replace("Msvm", "Msvm_");
        String query = String.format(format, wmiClass, elementName);
        SWbemObjectSet<T> set = service.execQuery(query, clazz);
        int size = set.getSize();
        if (size == 0)
        {
            return null;
        }
        if (size > 1)
        {
            throw new Exception("More than one object found.");
        }
        T obj = set.iterator().next();
        return obj;
    }

    /**
     * Gets all of the objects of the specified type.
     * 
     * @param <T> The type of object.
     * @param service The service connection.
     * @param clazz The class of the return object.
     * @return A set of all of the objects.
     * @throws Exception When an error occurs.
     */
    public static <T extends MsvmObject> SWbemSet<T> getAll(
        final SWbemServices service,
        final Class<T> clazz) throws Exception
    {
        String format = "SELECT * FROM %s";
        String wmiClass = clazz.getSimpleName().replace("Msvm", "Msvm_");
        String query = String.format(format, wmiClass);
        SWbemObjectSet<T> set = service.execQuery(query, clazz);
        int size = set.getSize();
        if (size == 0)
        {
            return null;
        }
        return set;
    }

    /**
     * Gets the label by which the object is known. This property is inherited
     * from CIM_System and it is a GUID.
     * 
     * @return The label by which the object is known. This property is
     *         inherited from CIM_System and it is a GUID.
     */
    public String getName()
    {
        return this.getProperty("Name", String.class);
    }

    /**
     * Gets a user-friendly name for the object. This property is inherited from
     * CIM_ManagedElement and it is set to the display name of the computer for
     * a virtual system or the NetBIOS name of the host system.
     * 
     * @return A user-friendly name for the object. This property is inherited
     *         from CIM_ManagedElement and it is set to the display name of the
     *         computer for a virtual system or the NetBIOS name of the host
     *         system.
     */
    public String getElementName()
    {
        return this.getProperty("ElementName", String.class);
    }
}
