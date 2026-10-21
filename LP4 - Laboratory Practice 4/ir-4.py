
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse
from collections import deque

def web_crawler(start_url, max_pages=5):
    visited = set()
    queue = deque([start_url])
    domain = urlparse(start_url).netloc

    while queue and len(visited) < max_pages:
        url = queue.popleft()

        if url in visited:
            continue

        try:
            response = requests.get(
                url,
                timeout=10,
                headers={"User-Agent": "SimpleStudentCrawler/1.0"}
            )
            response.raise_for_status()

            # Process HTML pages only
            if "text/html" not in response.headers.get(
                "Content-Type", ""
            ):
                continue

            visited.add(url)
            soup = BeautifulSoup(response.text, "html.parser")

            title = soup.title.string.strip() if (
                soup.title and soup.title.string
            ) else "No title"

            print("\nURL:", url)
            print("Title:", title)

            links_found = 0

            for link in soup.find_all("a", href=True):
                new_url = urljoin(url, link["href"]).split("#")[0]

                # Crawl only pages from the same domain
                if (
                    urlparse(new_url).scheme in ("http", "https")
                    and urlparse(new_url).netloc == domain
                    and new_url not in visited
                    and new_url not in queue
                ):
                    queue.append(new_url)
                    links_found += 1

            print("New links found:", links_found)

        except requests.RequestException as error:
            print("\nError crawling", url, ":", error)

    print("\nCrawling completed.")
    print("Total pages crawled:", len(visited))


start_url = input("Enter starting URL: ").strip()
max_pages = int(input("Enter maximum pages to crawl: "))

if urlparse(start_url).scheme not in ("http", "https") or not urlparse(
    start_url
).netloc:
    print("Please enter a valid HTTP or HTTPS URL.")
elif not 1 <= max_pages <= 50:
    print("Maximum pages must be between 1 and 50.")
else:
    web_crawler(start_url, max_pages)
