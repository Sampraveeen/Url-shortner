import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

// URL Shortener Class
class URLShortener {
    Map<String, String> urlMap = new HashMap<>();
    private int idCounter = 1;

    public String shortenURL(String longURL) {
        if (longURL == null || longURL.isEmpty()) {
            return "Invalid URL";
        }
        String shortURL = "short.ly/" + idCounter++;
        urlMap.put(shortURL, longURL);
        return shortURL;
    }

    public String retrieveURL(String shortURL) {
        return urlMap.getOrDefault(shortURL, "URL not found");
    }
}

// URL Storage Class (Simulating persistent storage)
class URLStorage {
    // Simulate loading from storage
    public Map<String, String> load() {
        return new HashMap<>();  // Initially empty, can be connected to a real database
    }

    // Simulate saving to storage
    public void save(Map<String, String> urlMap) {
        // Here you could save the map to a file or database
        System.out.println("URLs saved to storage");
    }
}

public class URLShortenerGUI extends JFrame {
    private JTextField longUrlField;
    private JTextField shortUrlField;
    private JTextArea resultArea;
    private URLShortener urlShortener;
    private URLStorage urlStorage;

    public URLShortenerGUI() {
        urlShortener = new URLShortener();
        urlStorage = new URLStorage();
        urlShortener.urlMap.putAll(urlStorage.load()); // Load existing mappings (if any)

        setTitle("QuickLink Shortener");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create components
        longUrlField = new JTextField(20);
        shortUrlField = new JTextField(20);
        JButton shortenButton = new JButton("Shorten URL");
        JButton retrieveButton = new JButton("Retrieve URL");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Create panels
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Long URL:"));
        inputPanel.add(longUrlField);
        inputPanel.add(shortenButton);

        JPanel retrievePanel = new JPanel();
        retrievePanel.add(new JLabel("Shortened URL:"));
        retrievePanel.add(shortUrlField);
        retrievePanel.add(retrieveButton);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(retrievePanel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        // Add action listeners
        shortenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String longURL = longUrlField.getText().trim();
                if (longURL.isEmpty()) {
                    resultArea.append("Please enter a valid URL\n");
                } else {
                    String shortURL = urlShortener.shortenURL(longURL);
                    resultArea.append("Shortened URL: " + shortURL + "\n");
                    urlStorage.save(urlShortener.urlMap); // Save mappings after shortening
                }
            }
        });

        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shortURL = shortUrlField.getText().trim();
                if (shortURL.isEmpty()) {
                    resultArea.append("Please enter a valid shortened URL\n");
                } else {
                    String originalURL = urlShortener.retrieveURL(shortURL);
                    resultArea.append("Original URL: " + originalURL + "\n");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            URLShortenerGUI gui = new URLShortenerGUI();
            gui.setVisible(true);
        });
    }
}
