
import xml.etree.ElementTree as ET

# Step 1: Create sample XML data
xml_data = """
<web>
    <page id="A" topic="AI">
        <link>B</link>
        <link>C</link>
    </page>
    <page id="B" topic="AI">
        <link>C</link>
        <link>D</link>
    </page>
    <page id="C" topic="IR">
        <link>A</link>
    </page>
    <page id="D" topic="AI">
        <link>A</link>
    </page>
</web>
"""

# Step 2: Parse XML
root = ET.fromstring(xml_data)

graph = {}
topics = {}

for page in root.findall("page"):
    page_id = page.get("id")
    topics[page_id] = page.get("topic")
    graph[page_id] = [
        link.text for link in page.findall("link")
    ]

print("Web Graph (Adjacency List):")
for page, links in graph.items():
    print(page, "->", links)

# Step 3: Select the topic
selected_topic = "AI"

# Give higher initial importance to topic-related pages
n = len(graph)
personalization = {
    page: (1 / sum(t == selected_topic
                   for t in topics.values()))
    if topics[page] == selected_topic else 0
    for page in graph
}

# Step 4: Calculate Topic-Specific PageRank
d = 0.85
pagerank = personalization.copy()
iterations = 100

for _ in range(iterations):
    new_rank = {
        page: (1 - d) * personalization[page]
        for page in graph
    }

    for source in graph:
        outgoing = graph[source]

        if outgoing:
            for destination in outgoing:
                new_rank[destination] += (
                    d * pagerank[source] / len(outgoing)
                )
        else:
            # Handle pages with no outgoing links
            for page in graph:
                new_rank[page] += (
                    d * pagerank[source]
                    * personalization[page]
                )

    pagerank = new_rank

# Step 5: Display the final ranking
print("\nTopic-Specific PageRank for:", selected_topic)

ranked_pages = sorted(
    pagerank.items(),
    key=lambda item: item[1],
    reverse=True
)

for page, score in ranked_pages:
    print(f"Page {page}: {score:.4f}")
