package io.github.readonly.api.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface RMIConnector extends Remote
{
	int PORT = 6291;
	String BASE_NAME = "RMIConnector";

	ThreadInfo[] getThreads() throws RemoteException;

	double getCPULoad() throws RemoteException;

	RamUsage getRamUsage() throws RemoteException;

	HashMap<String, Object> getProcessInfoProfiling() throws RemoteException;

	void onShutdown() throws RemoteException;
}
