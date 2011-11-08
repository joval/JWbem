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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;
import org.jinterop.dcom.impls.automation.IJIEnumVariant;

/**
 * An SWbemSet.
 * 
 * @author akutz
 * @param <T> The type of object in the set.
 * 
 */
public class SWbemSet<T extends SWbemSetItem> extends SWbemDispatchObject
    implements Iterable<T>
{
    private Integer size;

    /**
     * T's class type.
     */
    private Class<T> clazz;

    /**
     * Initializes a new instance of the SWbemSet class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    private SWbemSet(IJIDispatch objectDispatcher, SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Initializes a new instance of the SWbemSet class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     * @param clazz T's class type.
     */
    public SWbemSet(
        IJIDispatch objectDispatcher,
        SWbemServices service,
        Class<T> clazz)
    {
        this(objectDispatcher, service);
        this.clazz = clazz;
    }

    public Iterator<T> iterator()
    {
        try
        {
            return new SWbemSetIterator(super.objectDispatcher, this.service);
        }
        catch (Exception e)
        {
            // Swallow the exception.
            return null;
        }
    }

    /**
     * Gets the number of items in this set.
     * 
     * @return The number of items in this set.
     */
    public int getSize()
    {
        if (this.size != null)
        {
            return this.size;
        }

        try
        {
            JIVariant jiCount = super.objectDispatcher.get("Count");
            this.size = jiCount.getObjectAsInt();
        }
        catch (JIException ex)
        {
            this.size = 0;
        }

        return this.size;
    }

    /**
     * Creates an array from the set.
     * 
     * @return An array of the members of the set.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray()
    {
        T[] arr = (T[]) Array.newInstance(clazz, this.getSize());
        int x = 0;
        Iterator<T> iter = this.iterator();
        while (iter.hasNext())
        {
            arr[x] = iter.next();
            ++x;
        }
        return arr;
    }

    /**
     * Creates a native object array from the set.
     * 
     * @return An array of the underlying native objects of the members of the
     *         set.
     */
    public IJIDispatch[] toNativeArray()
    {
        IJIDispatch[] arr = new IJIDispatch[this.getSize()];
        int x = 0;
        Iterator<T> iter = this.iterator();
        while (iter.hasNext())
        {
            arr[x] = ((SWbemObject) iter.next()).getObjectDispatcher();
            ++x;
        }
        return arr;
    }

    /**
     * Gets an item of the set.
     * 
     * @param itemName The name of the item to get.
     * @return The item with the specified name.
     * @throws Exception When an error occurs.
     */
    public T getItem(final String itemName) throws Exception
    {
        try
        {
            // Invoke the Item method.
            Object[] inParams = new Object[]
            {
                new JIString(itemName), new Integer(0)
            };
            final JIVariant[] results =
                super.objectDispatcher.callMethodA("Item", inParams);
            final IJIComObject prop = results[0].getObjectAsComObject();
            final IJIDispatch dispatch =
                (IJIDispatch) JIObjectFactory.narrowObject(prop);

            // Create a new SWbemSetItem from the result.
            final Constructor<T> ctor =
                this.clazz.getConstructor(
                    IJIDispatch.class,
                    SWbemServices.class);
            final T item = ctor.newInstance(dispatch, this.service);

            // Return the SWbemSetItem.
            return item;
        }
        catch (JIException e)
        {
            if (e.getErrorCode() == 0x80020009)
            {
                return null;
            }
            else
            {
                throw e;
            }
        }
    }

    /**
     * An iterator used to iterate an SWbemSet object.
     * 
     * @author akutz
     * 
     */
    private class SWbemSetIterator extends SWbemDispatchObject implements
        Iterator<T>
    {
        /**
         * The count.
         */
        private int count = 0;

        /**
         * The enumerator.
         */
        private IJIEnumVariant enumerator = null;

        /**
         * Initializes a new instance of the SWbemSetIterator class.
         * 
         * @param objectDispatcher The underlying dispatch object used to
         *        communicate with the server.
         * @param service The service connection.
         * @throws JIException When an error occurs.
         * 
         */
        public SWbemSetIterator(
            final IJIDispatch objectDispatcher,
            final SWbemServices service) throws JIException
        {
            super(objectDispatcher, service);

            JIVariant jiCount = super.objectDispatcher.get("Count");
            this.count = jiCount.getObjectAsInt();

            JIVariant variant = super.objectDispatcher.get("_NewEnum");
            IJIComObject co = variant.getObjectAsComObject();
            co = co.queryInterface(IJIEnumVariant.IID);
            this.enumerator = (IJIEnumVariant) JIObjectFactory.narrowObject(co);
        }

        public boolean hasNext()
        {
            return this.count > 0;
        }

        public T next()
        {
            try
            {
                if (hasNext())
                {
                    count--;
                    Object[] values = this.enumerator.next(1);
                    JIArray array = (JIArray) values[0];
                    Object[] arrayObj = (Object[]) array.getArrayInstance();
                    IJIComObject co =
                        ((JIVariant) arrayObj[0]).getObjectAsComObject();
                    IJIDispatch dispatch =
                        (IJIDispatch) JIObjectFactory.narrowObject(co);

                    // Create a new SWbemSetItem from the result.
                    final Constructor<T> ctor =
                        clazz.getConstructor(
                            IJIDispatch.class,
                            SWbemServices.class);
                    final T item = ctor.newInstance(dispatch, this.service);
                    return item;
                }
                else
                {
                    throw new NoSuchElementException();
                }
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}