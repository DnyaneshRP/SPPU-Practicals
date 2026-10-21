
# Mapper Function
def mapper(text):
    result = []

    for char in text.lower():
        if char.isascii() and char.isalpha():
            result.append((char, 1))

    return result


# Reducer Function
def reducer(mapped_data):
    count = {}

    for char, value in mapped_data:
        count[char] = count.get(char, 0) + value

    return count


# Main Program
text = input("Enter the dataset: ")

mapped_data = mapper(text)
result = reducer(mapped_data)

print("\nCharacter Count:")
for char in sorted(result):
    print(char, ":", result[char])
