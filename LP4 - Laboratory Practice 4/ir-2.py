
# PageRank Algorithm

def pagerank(graph, d=0.85, iterations=100):
    n = len(graph)
    ranks = {page: 1 / n for page in graph}

    for i in range(iterations):
        new_ranks = {}

        for page in graph:
            rank = (1 - d) / n

            for other_page in graph:
                if page in graph[other_page]:
                    rank += d * (ranks[other_page] / len(graph[other_page]))

            new_ranks[page] = rank

        ranks = new_ranks

    return ranks


# Web graph
graph = {
    'A': ['B', 'C'],
    'B': ['C'],
    'C': ['A'],
    'D': ['C']
}

# Calculate PageRank
result = pagerank(graph)

print("PageRank Results:")
for page, rank in result.items():
    print(f"Page {page}: {rank:.4f}")

# Sort pages by importance
print("\nPage Ranking:")
sorted_pages = sorted(result, key=result.get, reverse=True)

for i, page in enumerate(sorted_pages, 1):
    print(f"{i}. Page {page}")
