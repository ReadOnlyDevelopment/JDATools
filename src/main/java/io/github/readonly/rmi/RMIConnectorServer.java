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
