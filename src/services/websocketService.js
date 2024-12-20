// src/services/websocketService.js
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

// src/services/websocketService.js
// src/services/websocketService.js
// src/services/websocketService.js
export class WebSocketService {
    constructor(userId) {
        this.userId = userId;
        this.client = null;
        this.connected = false;
    }

    connect(onMessageReceived) {
        this.client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            connectHeaders: {
                userId: this.userId
            },
            onConnect: () => {
                console.log('WebSocket Connected with userId:', this.userId);
                this.connected = true;
                this.subscribeToAlerts(onMessageReceived);
            },
            onDisconnect: () => {
                console.log('WebSocket Disconnected');
                this.connected = false;
            }
        });

        this.client.activate();
    }

    disconnect() {
        if (this.client && this.connected) {
            console.log('Disconnecting WebSocket');
            this.client.deactivate();
            this.connected = false;
        }
    }

    subscribeToAlerts(onMessageReceived) {
        if (this.client && this.userId) {
            return this.client.subscribe(`/user/${this.userId}/topic/alerts`, message => {
                const alert = JSON.parse(message.body);
                onMessageReceived(alert);
            });
        }
    }
}