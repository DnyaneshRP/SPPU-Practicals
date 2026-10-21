
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# Input two documents
doc1 = input("Enter Document 1: ")
doc2 = input("Enter Document 2: ")

# Convert documents into TF-IDF vectors
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform([doc1, doc2])

# Calculate cosine similarity
similarity = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:2])[0][0]

# Display result
print("\nCosine Similarity:", round(similarity, 4))
print("Similarity Percentage:", round(similarity * 100, 2), "%")
