package com.bssys.nio2.chat;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, SessionState> {

	private AsynchronousServerSocketChannel socketChannel;
	private App chatServer;

	public AcceptCompletionHandler(AsynchronousServerSocketChannel socketChannel, App chatServer) {
		this.socketChannel = socketChannel;
		this.chatServer = chatServer;

	}

	public void completed(AsynchronousSocketChannel channel, SessionState session) {
		System.out.println("Client connected:" +channel);

		SessionState newSessionState = new SessionState();
		newSessionState.setProperty("client", channel.toString());
		socketChannel.accept(newSessionState, this);

		ByteBuffer inputBuffer = ByteBuffer.allocate(1024);
		channel.read(inputBuffer, newSessionState, new ReadCompletionHandler(channel, inputBuffer, chatServer));

		chatServer.addClient(channel);
	}

	public void failed(Throwable exc, SessionState session) {
	}
}