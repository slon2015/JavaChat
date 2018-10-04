package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;

public class WebServer {

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();

		HttpServer server = vertx.createHttpServer();

		server.requestHandler(req -> {
			if(req.method() != HttpMethod.GET)
					return;
			
			try {
				req.response().end(getFileText("Hello.html"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}).listen(8080);
	}
	
	private static String getFileText(String fname) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("hello.html"));
		String ret = reader.lines().reduce((acc, cur) -> {
			return acc + cur;
		}).orElse(null);
		reader.close();
		return ret;
	}

}
