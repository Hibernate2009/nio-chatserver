package com.bssys.nio2.chat;

import java.nio.channels.CompletionHandler;

public class WriteCompletionHandler implements CompletionHandler<Integer, SessionState> {

	public void completed(Integer result, SessionState attachment) {

	}

	public void failed(Throwable exc, SessionState attachment) {

	}

}
