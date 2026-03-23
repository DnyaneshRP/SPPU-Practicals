const knowledgeBase = {

    "hi": "Hello! Welcome to Spice Garden Restaurant.",
    "hello": "Hi! How can I help you today?",
    "hey": "Hey there! Looking for delicious Indian food?",
    "how are you": "I'm doing great! Ready to help you.",

    "menu": "Our menu includes Biryani, Butter Chicken, Paneer Butter Masala, Dosa and Naan.",

    "biryani": "We serve Chicken Biryani, Mutton Biryani and Veg Biryani.",
    "butter chicken": "Butter Chicken is one of our most popular dishes.",
    "paneer": "We have Paneer Butter Masala and Paneer Tikka.",
    "dosa": "Masala Dosa, Plain Dosa and Cheese Dosa are available.",
    "idli": "Yes, we serve Idli with Sambar.",

    "dal": "We serve Dal Tadka and Dal Makhani.",
    "naan": "Butter Naan, Garlic Naan and Plain Naan available.",
    "roti": "We have Tandoori Roti and Butter Roti.",
    "tandoori": "Tandoori Chicken and Paneer Tikka available.",
    "veg": "Yes, we have many vegetarian dishes.",

    "non veg": "Yes, we serve chicken and mutton dishes.",
    "dessert": "Desserts include Gulab Jamun and Rasmalai.",
    "gulab jamun": "Gulab Jamun is served hot.",
    "rasmalai": "Yes Rasmalai is available.",
    "drinks": "We serve soft drinks, lassi and juices.",

    "lassi": "Sweet Lassi, Mango Lassi and Salted Lassi available.",
    "juice": "Fresh juices like mango and orange are available.",

    "price": "Our food ranges from ₹120 to ₹450.",
    "cheap": "We have dishes under ₹200.",
    "expensive": "Premium dishes like Mutton Biryani are available.",

    "timing": "Restaurant is open from 10 AM to 11 PM.",
    "opening": "We open at 10 AM.",
    "closing": "We close at 11 PM.",
    "lunch": "Lunch time is 12 PM to 3 PM.",
    "dinner": "Dinner time is 7 PM to 11 PM.",

    "delivery": "Yes we offer home delivery within 5 km.",
    "home delivery": "Delivery is available through our website.",
    "takeaway": "Takeaway orders are available.",

    "reservation": "You can reserve a table by calling us.",
    "table booking": "Table booking is available.",

    "location": "We are located near MG Road Pune.",
    "address": "Our address is MG Road Pune.",

    "parking": "Parking facility is available.",
    "wifi": "Free WiFi available for customers.",
    "family": "Family seating is available.",

    "birthday": "Yes you can celebrate birthdays here.",
    "party": "We host small parties.",

    "spicy": "Spice level can be adjusted.",
    "mild": "Yes we can make food less spicy.",

    "payment": "We accept Cash, UPI and Cards.",
    "upi": "UPI like Google Pay and PhonePe accepted.",
    "card": "Debit and credit cards accepted.",

    "thanks": "You're welcome!",
    "thank you": "Happy to help!",
    "bye": "Goodbye! Visit again."

};



function addMessage(text, type) {

    const chat = document.getElementById("chat-area");

    const div = document.createElement("div");
    div.className = type;

    const msg = document.createElement("div");
    msg.className = "message";
    msg.innerText = text;

    div.appendChild(msg);
    chat.appendChild(div);

    chat.scrollTop = chat.scrollHeight;
}


function getResponse(input) {

    input = input.toLowerCase();

    for (let key in knowledgeBase) {
        if (input.includes(key)) {
            return knowledgeBase[key];
        }
    }

    const fallback = [
        "Sorry, I didn't understand that.",
        "Please ask about our menu or food.",
        "I'm still learning. Please ask something else.",
        "Sorry, I didn't get that..\nContact us: +91 9848408440",
        "Sorry, I didn't get that..\nYou can reach us at: spiceresturant12@gmail.com"
    ];

    return fallback[Math.floor(Math.random() * fallback.length)];
}


function sendMessage() {

    const input = document.getElementById("userInput");
    let text = input.value.trim();

    if (text === "") return;

    addMessage(text, "user");

    let response = getResponse(text);

    setTimeout(() => {
        addMessage(response, "bot");
    }, 400);

    input.value = "";
}



document.getElementById("sendBtn").onclick = sendMessage;


document.getElementById("userInput").addEventListener("keypress", function (e) {
    if (e.key === "Enter") {
        sendMessage();
    }
});



const chatButton = document.getElementById("chatButton");
const chatbot = document.getElementById("chatbot");
const closeChat = document.getElementById("closeChat");

chatButton.onclick = function () {

    if (chatbot.style.display === "flex") {
        chatbot.style.display = "none";
    }
    else {
        chatbot.style.display = "flex";
    }
}

closeChat.onclick = function () {
    chatbot.style.display = "none";
}



window.onload = function () {

    addMessage("Welcome to Spice Garden Restaurant!", "bot");
    addMessage("Ask me about menu, food, delivery, timings or reservations.", "bot");

}