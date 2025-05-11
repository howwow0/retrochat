package how.wow;

import how.wow.config.NetworkProperties;
import how.wow.infrastructure.client.SocketChatClient;
import how.wow.presentation.ClientUI;

public class ClientApp {
    public static void main(String[] args) {
        String host = NetworkProperties.getHost();
        int port = NetworkProperties.getPort();

        ClientUI clientUI = new ClientUI();
        SocketChatClient client = new SocketChatClient(host, port);

        try {
            client.connect(clientUI);
            clientUI.startChatLoop(client::sendMessage);
        } catch (Exception e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        } finally {
            client.disconnect();
        }
    }
}