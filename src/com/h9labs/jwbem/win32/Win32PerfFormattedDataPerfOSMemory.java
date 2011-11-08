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

package com.h9labs.jwbem.win32;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.jwbem.SWbemObject;
import com.h9labs.jwbem.SWbemServices;

/**
 * The Win32PerfFormattedDataPerfOSMemory formatted data class provides
 * pre-calculated performance data from performance counters that monitor the
 * physical and virtual memory on the computer.
 * 
 * @author akutz
 * @remarks <p>
 *          Physical memory is the amount of random access memory (RAM) on the
 *          computer. Virtual memory consists of space in physical memory and on
 *          disk. Many of the memory counters monitor paging, which is the
 *          movement of pages of code and data between disk and physical memory.
 *          Excessive paging, a symptom of a memory shortage, can cause delays
 *          which interfere with all system processes.
 *          </p>
 *          <p>
 *          This class represents the Memory object in System Monitor and
 *          returns the same data you see in System Monitor. The original data
 *          source is the PerfOS performance library. The corresponding raw data
 *          class is Win32PerfRawDataPerfOSMemory. Data is dynamically provided
 *          for this class from the performance library object by the
 *          WmiPerfInst provider.
 *          </p>
 */
public class Win32PerfFormattedDataPerfOSMemory extends SWbemObject
{

    /**
     * Initializes a new instance of the Win32PerfFormattedDataPerfOSMemory
     * class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public Win32PerfFormattedDataPerfOSMemory(
        IJIDispatch objectDispatcher,
        SWbemServices service)
    {
        super(objectDispatcher, service);
    }

    /**
     * Gets the amount of physical memory in bytes available to processes
     * running on the computer.
     * 
     * @return <p>
     *         This value is calculated by summing space on the Zeroed, Free,
     *         and Standby memory lists. Free memory is ready for use; Zeroed
     *         memory is pages of memory filled with zeros to prevent later
     *         processes from seeing data used by a previous process. Standby
     *         memory is memory removed from a process's working set (its
     *         physical memory) on route to disk, but is still available to be
     *         recalled. This property displays the last observed value only; it
     *         is not an average.
     *         </p>
     */
    public long getAvailableBytes()
    {
        return super.getProperty("AvailableBytes", Long.class);
    }

    /**
     * Gets the amount of physical memory available to processes running on the
     * computer, in kilobytes.
     * 
     * @return The amount of physical memory available to processes running on
     *         the computer, in kilobytes.
     * @remarks <p>
     *          <It is calculated by summing space on the Zeroed, Free, and
     *          Standby memory lists. Free memory is ready for use; Zeroed
     *          memory contains memory pages filled with zeros to prevent later
     *          processes from seeing data used by a previous Process. Standby
     *          memory is memory removed from a process' working set (its
     *          physical memory), but is still available to be recalled. This
     *          property displays the last observed value only; it is not an
     *          average.
     *          </p>
     */
    public long getAvailableKBytes()
    {
        return super.getProperty("AvailableKBytes", Long.class);
    }

    /**
     * Gets the amount of physical memory available to processes running on the
     * computer, in megabytes.
     * 
     * @return The amount of physical memory available to processes running on
     *         the computer, in megabytes.
     * @remarks <p>
     *          This value is calculated by summing space on the Zeroed, Free,
     *          and Standby memory lists. Free memory is ready for use; Zeroed
     *          memory contains memory pages filled with zeros to prevent later
     *          processes from seeing data used by a previous process. Standby
     *          memory is memory removed from a process' working set (its
     *          physical memory), but is still available to be recalled. This
     *          property displays the last observed value only; it is not an
     *          average.
     *          </p>
     */
    public long getAvailableMBytes()
    {
        return super.getProperty("AvailableMBytes", Long.class);
    }
}
