package com.bssys.nio2.chat;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, SessionState> {

	private AsynchronousSocketChannel channel;
	private ByteBuffer inputBuffer;
	private App chatServer;

	public ReadCompletionHandler(AsynchronousSocketChannel channel, ByteBuffer inputBuffer, App chatServer) {
		this.channel = channel;
		this.inputBuffer = inputBuffer;
		this.chatServer = chatServer;
	}

	public void completed(Integer bytesRead, SessionState session) {

		if (bytesRead < 1) {
			System.out.println("Closing connection to " + channel);
			chatServer.removeClient(channel);
		} else {
			try {
				byte[] buffer = new byte[bytesRead];
				inputBuffer.flip();

				inputBuffer.get(buffer);
				String  message = new String(buffer, "UTF-8");
				System.out.println("Received message from "+ session.getProperty("client") + ":" + message);

				chatServer.writeMessageToClients(channel, session.getProperty("client")+":"+ message+"\r\n");

				inputBuffer.clear();
				channel.read(inputBuffer, session, this);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public void failed(Throwable exc, SessionState attachment) {
		// TODO Auto-generated method stub
		chatServer.removeClient(channel);

	}

}
