package how.wow.infrastructure.server;

import how.wow.domain.ports.MessageBroker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatService {
    private final MessageBroker broker;
    private final ServerSocket serverSocket;
    private final ExecutorService executor;
    private volatile boolean running = false;

    public ChatService(MessageBroker broker, int port, int maxUsers) throws IOException {
        this.broker = broker;
        this.serverSocket = new ServerSocket(port,50, InetAddress.getByName("0.0.0.0"));
        this.executor = Executors.newFixedThreadPool(maxUsers);
    }

    public void start() {
        running = true;
        System.out.println("Сервер запущен на порту " + serverSocket.getLocalPort());

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket, broker));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Ошибка принятия соединения: " + e.getMessage());
                }
            }
        }
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
            executor.shutdownNow();
            System.out.println("Сервер остановлен");
        } catch (IOException e) {
            System.err.println("Ошибка при остановке сервера: " + e.getMessage());
        }
    }
}