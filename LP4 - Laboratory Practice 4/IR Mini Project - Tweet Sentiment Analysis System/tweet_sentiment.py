'''
Test cases
Try these inputs to verify the system.
Test	Input Tweet	Expected Result
1	I love this amazing product!	Positive
2	This is the worst experience ever.	Negative
3	I am going to college today.	Neutral
4	The movie was fantastic!	Positive
5	I hate this terrible service.	Negative
6	(Empty input)	Warning message
'''


import tkinter as tk
from tkinter import messagebox
from textblob import TextBlob
import re


# Function to clean tweet
def clean_tweet(tweet):
    tweet = re.sub(r"http\S+|www\S+", "", tweet)
    tweet = re.sub(r"@\w+", "", tweet)
    tweet = re.sub(r"#", "", tweet)
    tweet = re.sub(r"[^a-zA-Z\s']", "", tweet)
    tweet = re.sub(r"\s+", " ", tweet)
    return tweet.strip()


# Function to analyze sentiment
def analyze_sentiment():
    tweet = tweet_input.get("1.0", tk.END).strip()

    if not tweet:
        messagebox.showwarning("Warning", "Please enter a tweet!")
        return

    cleaned_tweet = clean_tweet(tweet)

    if not cleaned_tweet:
        messagebox.showwarning(
            "Warning", "Please enter some meaningful text!"
        )
        return

    analysis = TextBlob(cleaned_tweet)

    polarity = analysis.sentiment.polarity
    subjectivity = analysis.sentiment.subjectivity

    if polarity > 0:
        sentiment = "POSITIVE 😊"
        color = "green"
    elif polarity < 0:
        sentiment = "NEGATIVE 😞"
        color = "red"
    else:
        sentiment = "NEUTRAL 😐"
        color = "gray"

    sentiment_label.config(text=sentiment, fg=color)
    polarity_label.config(text=f"Polarity: {polarity:.2f}")
    subjectivity_label.config(
        text=f"Subjectivity: {subjectivity:.2f}"
    )
    cleaned_label.config(
        text=f"Cleaned Tweet: {cleaned_tweet}"
    )


# Function to clear fields
def clear_fields():
    tweet_input.delete("1.0", tk.END)
    sentiment_label.config(text="")
    polarity_label.config(text="")
    subjectivity_label.config(text="")
    cleaned_label.config(text="")


# Create main window
root = tk.Tk()
root.title("Tweet Sentiment Analysis System")
root.geometry("650x550")
root.configure(bg="#f0f4f8")

# Heading
heading = tk.Label(
    root,
    text="TWEET SENTIMENT ANALYSIS",
    font=("Arial", 20, "bold"),
    bg="#f0f4f8",
    fg="#1e3a5f"
)
heading.pack(pady=25)

# Input label
input_label = tk.Label(
    root,
    text="Enter Your Tweet:",
    font=("Arial", 13),
    bg="#f0f4f8"
)
input_label.pack()

# Tweet input box
tweet_input = tk.Text(
    root,
    height=5,
    width=60,
    font=("Arial", 12),
    wrap=tk.WORD
)
tweet_input.pack(pady=10, padx=20)

# Button frame
button_frame = tk.Frame(root, bg="#f0f4f8")
button_frame.pack(pady=10)

analyze_button = tk.Button(
    button_frame,
    text="Analyze Sentiment",
    command=analyze_sentiment,
    font=("Arial", 12, "bold"),
    bg="#007bff",
    fg="white",
    padx=15,
    pady=8
)
analyze_button.pack(side=tk.LEFT, padx=10)

clear_button = tk.Button(
    button_frame,
    text="Clear",
    command=clear_fields,
    font=("Arial", 12),
    bg="#dc3545",
    fg="white",
    padx=20,
    pady=8
)
clear_button.pack(side=tk.LEFT, padx=10)

# Result heading
result_heading = tk.Label(
    root,
    text="Analysis Result",
    font=("Arial", 16, "bold"),
    bg="#f0f4f8"
)
result_heading.pack(pady=15)

# Sentiment result
sentiment_label = tk.Label(
    root,
    text="",
    font=("Arial", 18, "bold"),
    bg="#f0f4f8"
)
sentiment_label.pack(pady=5)

# Polarity
polarity_label = tk.Label(
    root,
    text="",
    font=("Arial", 12),
    bg="#f0f4f8"
)
polarity_label.pack(pady=5)

# Subjectivity
subjectivity_label = tk.Label(
    root,
    text="",
    font=("Arial", 12),
    bg="#f0f4f8"
)
subjectivity_label.pack(pady=5)

# Cleaned tweet
cleaned_label = tk.Label(
    root,
    text="",
    font=("Arial", 10),
    bg="#f0f4f8",
    wraplength=580
)
cleaned_label.pack(pady=10)

# Start GUI
root.mainloop()
