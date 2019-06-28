package edu.fema.jmessenger.jMessenger.locate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Starter {

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

	public void alterarArquivo(String ip) throws IOException {

		URL recurso = getClass().getClassLoader().getResource("jndi.properties");
		File arquivo = new File(recurso.getPath());
		Properties propriedades = new Properties();

		FileInputStream fileInputStream = new FileInputStream(arquivo);
		propriedades.load(fileInputStream);
		propriedades.setProperty("java.naming.provider.url", "tcp://" + ip + ":61616");
		FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
		propriedades.store(fileOutputStream,"");
		fileOutputStream.close();
	}

}
