'''
Enter number of web pages: 4

Enter ID of page 1 (A, B, C...): A
Enter topic of page A: AI
Enter outgoing links from A (comma-separated, or none): B,C

Enter ID of page 2 (A, B, C...): B
Enter topic of page B: AI
Enter outgoing links from B (comma-separated, or none): C,D

Enter ID of page 3 (A, B, C...): C
Enter topic of page C: IR
Enter outgoing links from C (comma-separated, or none): A

Enter ID of page 4 (A, B, C...): D
Enter topic of page D: AI
Enter outgoing links from D (comma-separated, or none): A

Enter topic for PageRank: AI
'''


import xml.etree.ElementTree as ET

# Step 1: Accept web pages from the user
n = int(input("Enter number of web pages: "))

root = ET.Element("web")

for i in range(n):
    page_id = input(f"\nEnter ID of page {i+1} (A, B, C...): ")
    topic = input(f"Enter topic of page {page_id}: ")

    page = ET.SubElement(root, "page", id=page_id, topic=topic)

    links = input(
        f"Enter outgoing links from {page_id} (comma-separated, or none): "
    )

    if links.strip().lower() != "none" and links.strip():
        for link in links.split(","):
            ET.SubElement(page, "link").text = link.strip()

# Step 2: Parse the XML
graph = {}
topics = {}

for page in root.findall("page"):
    page_id = page.get("id")
    topics[page_id] = page.get("topic")
    graph[page_id] = [
        link.text for link in page.findall("link")
        if link.text in [p.get("id") for p in root.findall("page")]
    ]

print("\nWeb Graph:")
for page, links in graph.items():
    print(page, "->", links)

# Step 3: Accept the topic
selected_topic = input("\nEnter topic for PageRank: ")

topic_pages = [
    page for page in graph
    if topics[page].lower() == selected_topic.lower()
]

if not topic_pages:
    print("No pages found for this topic.")
else:
    # Step 4: Initialize topic-specific probabilities
    personalization = {
        page: 1 / len(topic_pages) if page in topic_pages else 0
        for page in graph
    }

    d = 0.85
    pagerank = personalization.copy()

    # Step 5: Calculate PageRank
    for _ in range(100):
        new_rank = {
            page: (1 - d) * personalization[page]
            for page in graph
        }

        for source, links in graph.items():
            if links:
                for destination in links:
                    new_rank[destination] += (
                        d * pagerank[source] / len(links)
                    )
            else:
                for page in graph:
                    new_rank[page] += (
                        d * pagerank[source] * personalization[page]
                    )

        pagerank = new_rank

    # Step 6: Display results
    print("\nTopic-Specific PageRank for:", selected_topic)

    for page, score in sorted(
        pagerank.items(), key=lambda item: item[1], reverse=True
    ):
        print(f"Page {page}: {score:.4f}")
