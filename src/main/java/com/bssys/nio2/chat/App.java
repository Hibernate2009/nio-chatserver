package com.bssys.nio2.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App {

	private final List<AsynchronousSocketChannel> connections = Collections.synchronizedList(new ArrayList<AsynchronousSocketChannel>());

	public void run() throws IOException {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(newFixedThreadPool);

		final AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(group);
		InetSocketAddress address = new InetSocketAddress("localhost", 3333);
		listener.bind(address);

		AcceptCompletionHandler acceptCompletionHandler = new AcceptCompletionHandler(listener, this);

		SessionState state = new SessionState();
		listener.accept(state, acceptCompletionHandler);

	}

	public void addClient(AsynchronousSocketChannel client) {
		connections.add(client);
	}
	public void removeClient(AsynchronousSocketChannel channel) {
		connections.remove(channel);
	}

	public void writeMessageToClients(AsynchronousSocketChannel channel, String message) {
		synchronized (connections) {
			for (AsynchronousSocketChannel clientConnection : connections) {
				if (clientConnection != channel) {
					ByteBuffer wrapBuffer = ByteBuffer.wrap(message.getBytes());
					SessionState sessionState = new SessionState();
					clientConnection.write(wrapBuffer, sessionState, new WriteCompletionHandler());
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Async server started");
		new App().run();
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	

}
