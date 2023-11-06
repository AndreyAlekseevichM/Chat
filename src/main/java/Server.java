import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server extends Thread {

    public static LinkedList<Server> serverList = new LinkedList<>();
    private final Socket socket;
    public final BufferedReader in; // поток чтения из сокета
    private final BufferedWriter out; // поток записи в сокет

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(new File("settings.txt")));
        int PORT = Integer.parseInt(props.getProperty("PORT"));

        try (ServerSocket server = new ServerSocket(PORT)) {
            writeFileServer("Server is running");
            System.out.println("Server is running");
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new Server(socket));
                    writeFileServer("New connection to the server");
                    System.out.println("New connection to the server");
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }

    @Override
    public void run() {
        String message;
        try {
            message = in.readLine();
            out.write(message + "\n");
            out.flush();
            while (true) {
                message = in.readLine();
                try {
                    if (message.equals("/exit")) {
                        this.downSocket();
                        break;
                    }
                } catch (NullPointerException ignored) {
                }
                writeFileServer(message);
                System.out.println(message);
                for (Server vr : serverList) {
                    vr.sendMsg(message);
                }
            }
        } catch (IOException e) {
            this.downSocket();
        }
    }

    private void sendMsg(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }

    }

    protected void downSocket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (Server vr : serverList) {
                    if (vr.equals(this)) {
                        vr.interrupt();
                    }
                    serverList.remove(this);
                }
                writeFileServer("The socket is disabled");
                System.out.println("The socket is disabled");
            }
        } catch (IOException ignored) {
        }
    }

    protected static void writeFileServer(String msg) {
        try (FileWriter writer = new FileWriter("file.log", true)) {
            writer.append(new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Calendar.getInstance().getTime()))
                    .append(" ")
                    .append(msg)
                    .append('\n')
                    .flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}