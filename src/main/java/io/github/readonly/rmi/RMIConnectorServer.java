/*
 * This file is part of JDATools, licensed under the MIT License (MIT).
 *
 * Copyright (c) ROMVoid95
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.readonly.rmi;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;

import com.sun.management.OperatingSystemMXBean;

import io.github.readonly.Properties;
import io.github.readonly.api.rmi.RMIConnector;
import io.github.readonly.api.rmi.RamUsage;
import io.github.readonly.api.rmi.ThreadInfo;
import io.github.readonly.common.util.ProfilingUtil;
import io.github.readonly.discordbot.DiscordBot;

public class RMIConnectorServer implements RMIConnector
{

	@Override
	public ThreadInfo[] getThreads() throws RemoteException
	{
		return Thread.getAllStackTraces()
			.entrySet()
			.stream()
			.map(e -> ThreadInfo.fromThread(e.getKey(), e.getValue())).toArray(ThreadInfo[]::new);
	}

	@Override
	public double getCPULoad() throws RemoteException
	{
		return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getCpuLoad();
	}

	@Override
	public RamUsage getRamUsage() throws RemoteException
	{
		final Runtime runtime = Runtime.getRuntime();
		return new RamUsage(runtime.totalMemory(), runtime.freeMemory());
	}

	@Override
	public HashMap<String, Object> getProcessInfoProfiling() throws RemoteException
	{
		final HashMap<String, Object> map = new HashMap<>();
		map.put("agentVersion", DiscordBot.VERSION);

		final RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		final ProcessHandle handle = ProcessHandle.current();

		// App info
		map.put("name", runtimeBean.getName());
		map.put("epochMillis", System.currentTimeMillis());
		map.put("pid", handle.pid());
		try {
			map.put("host", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			map.put("host", "unknown");
		}

		final String jar = System.getProperty(Properties.JAR_PATH, null);
		if (jar != null) {
			map.put("appJar", jar);
			try (JarFile jarFile = new JarFile(jar)){
				map.put("appClass", jarFile.getManifest().getMainAttributes().getValue("Main-Class"));
			} catch (Exception e) {
				map.put("appClass", "unknown");
			}
		}

		map.put("jvmInputArguments", runtimeBean.getInputArguments() == null ? List.of() : new ArrayList<>(runtimeBean.getInputArguments()));
		map.put("xmxBytes", ProfilingUtil.getJvmXmxBytes(runtimeBean.getInputArguments()));
		map.put("command", handle.info().command().orElse(""));
		map.put("commandLine", handle.info().commandLine().orElse(""));
		map.put("start", handle.info().startInstant().map(Instant::toString).orElse("unknown"));
		map.put("classpath", runtimeBean.getClassPath());

		return map;
	}

	@Override
	public void onShutdown() throws RemoteException
	{
		// TODO Auto-generated method stub

	}

}
