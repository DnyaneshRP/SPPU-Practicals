import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FoodChatbot extends JFrame {

    JPanel chatPanel;
    JScrollPane scrollPane;
    JTextField inputField;
    JButton sendButton;

    HashMap<String,String> knowledgeBase = new HashMap<>();

    public FoodChatbot(){

        setTitle("Spice Garden Restaurant Chatbot");
        setSize(500,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Spice Garden Restaurant Assistant", JLabel.CENTER);
        header.setFont(new Font("Segoe UI",Font.BOLD,18));
        header.setOpaque(true);
        header.setBackground(new Color(0,150,136));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100,50));
        add(header,BorderLayout.NORTH);

        // Chat area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel,BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(245,245,245));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        add(scrollPane,BorderLayout.CENTER);

        // Bottom input area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI",Font.PLAIN,14));

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(0,150,136));
        sendButton.setForeground(Color.WHITE);

        bottomPanel.add(inputField,BorderLayout.CENTER);
        bottomPanel.add(sendButton,BorderLayout.EAST);

        add(bottomPanel,BorderLayout.SOUTH);

        loadKnowledgeBase();

        addBotMessage("Welcome to Spice Garden Restaurant!");
        addBotMessage("Ask me about menu, food, delivery, timings or reservations.");

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    // Send message
    void sendMessage(){

        String userText = inputField.getText().trim();
        if(userText.equals("")) return;

        addUserMessage(userText);

        String response = getResponse(userText);

        addBotMessage(response);

        inputField.setText("");

        SwingUtilities.invokeLater(() ->
            scrollPane.getVerticalScrollBar().setValue(
            scrollPane.getVerticalScrollBar().getMaximum()));
    }

    // User bubble
    void addUserMessage(String message){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel label = new JLabel("<html><p style='width:200px'>"+message+"</p></html>");
        label.setOpaque(true);
        label.setBackground(new Color(220,248,198));
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(label);
        panel.setBackground(new Color(245,245,245));

        chatPanel.add(panel);
        chatPanel.revalidate();
    }

    // Bot bubble
    void addBotMessage(String message){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("<html><p style='width:200px'>"+message+"</p></html>");
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(label);
        panel.setBackground(new Color(245,245,245));

        chatPanel.add(panel);
        chatPanel.revalidate();
    }

    // Knowledge base (50 questions)
    void loadKnowledgeBase(){

        knowledgeBase.put("hi","Hello! Welcome to Spice Garden Restaurant.");
        knowledgeBase.put("hello","Hi! How can I help you today?");
        knowledgeBase.put("hey","Hey there! Looking for delicious Indian food?");
        knowledgeBase.put("how are you","I'm doing great! Ready to help you.");

        knowledgeBase.put("menu","Our menu includes Biryani, Butter Chicken, Paneer Butter Masala, Dosa and Naan.");

        knowledgeBase.put("biryani","We serve Chicken Biryani, Mutton Biryani and Veg Biryani.");
        knowledgeBase.put("butter chicken","Butter Chicken is one of our most popular dishes.");
        knowledgeBase.put("paneer","We have Paneer Butter Masala and Paneer Tikka.");
        knowledgeBase.put("dosa","Masala Dosa, Plain Dosa and Cheese Dosa are available.");
        knowledgeBase.put("idli","Yes, we serve Idli with Sambar.");

        knowledgeBase.put("dal","We serve Dal Tadka and Dal Makhani.");
        knowledgeBase.put("naan","Butter Naan, Garlic Naan and Plain Naan available.");
        knowledgeBase.put("roti","We have Tandoori Roti and Butter Roti.");
        knowledgeBase.put("tandoori","Tandoori Chicken and Paneer Tikka available.");
        knowledgeBase.put("veg","Yes, we have many vegetarian dishes.");

        knowledgeBase.put("non veg","Yes, we serve chicken and mutton dishes.");
        knowledgeBase.put("dessert","Desserts include Gulab Jamun and Rasmalai.");
        knowledgeBase.put("gulab jamun","Gulab Jamun is served hot.");
        knowledgeBase.put("rasmalai","Yes Rasmalai is available.");
        knowledgeBase.put("drinks","We serve soft drinks, lassi and juices.");

        knowledgeBase.put("lassi","Sweet Lassi, Mango Lassi and Salted Lassi available.");
        knowledgeBase.put("juice","Fresh juices like mango and orange are available.");

        knowledgeBase.put("price","Our food ranges from ₹120 to ₹450.");
        knowledgeBase.put("cheap","We have dishes under ₹200.");
        knowledgeBase.put("expensive","Premium dishes like Mutton Biryani are available.");

        knowledgeBase.put("timing","Restaurant is open from 10 AM to 11 PM.");
        knowledgeBase.put("opening","We open at 10 AM.");
        knowledgeBase.put("closing","We close at 11 PM.");
        knowledgeBase.put("lunch","Lunch time is 12 PM to 3 PM.");
        knowledgeBase.put("dinner","Dinner time is 7 PM to 11 PM.");

        knowledgeBase.put("delivery","Yes we offer home delivery within 5 km.");
        knowledgeBase.put("home delivery","Delivery is available through our website.");
        knowledgeBase.put("takeaway","Takeaway orders are available.");

        knowledgeBase.put("reservation","You can reserve a table by calling us.");
        knowledgeBase.put("table booking","Table booking is available.");

        knowledgeBase.put("location","We are located near MG Road Pune.");
        knowledgeBase.put("address","Our address is MG Road Pune.");

        knowledgeBase.put("parking","Parking facility is available.");
        knowledgeBase.put("wifi","Free WiFi available for customers.");
        knowledgeBase.put("family","Family seating is available.");

        knowledgeBase.put("birthday","Yes you can celebrate birthdays here.");
        knowledgeBase.put("party","We host small parties.");

        knowledgeBase.put("spicy","Spice level can be adjusted.");
        knowledgeBase.put("mild","Yes we can make food less spicy.");

        knowledgeBase.put("payment","We accept Cash, UPI and Cards.");
        knowledgeBase.put("upi","UPI like Google Pay and PhonePe accepted.");
        knowledgeBase.put("card","Debit and credit cards accepted.");

        knowledgeBase.put("thanks","You're welcome!");
        knowledgeBase.put("thank you","Happy to help!");
        knowledgeBase.put("bye","Goodbye! Visit again.");
    }

    // Response logic
    String getResponse(String input){

        input = input.toLowerCase();

        for(Map.Entry<String,String> entry : knowledgeBase.entrySet()){
            if(input.contains(entry.getKey()))
                return entry.getValue();
        }

        String[] fallback = {
                "Sorry, I didn't understand that.",
                "Please ask about our menu or food.",
                "Try asking about biryani, dosa or timings.",
                "I'm still learning. Please ask something else."
        };

        Random r = new Random();
        return fallback[r.nextInt(fallback.length)];
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new FoodChatbot());
    }
}