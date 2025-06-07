/*
Implement the Heap sort algorithm demonstrating heap data structure with modularity of programming language
 */

#include <iostream>
using namespace std;

class MaxHeap {
private:
    int *heap;
    int size;

public:
    MaxHeap(int n) {
        size = 0;
        heap = new int[n + 1]; // 1-based indexing
    }

    void insert(int x) {
        int i = ++size;

        // Heapify up
        while (i > 1 && x > heap[i / 2]) {
            heap[i] = heap[i / 2];
            i = i / 2;
        }

        heap[i] = x;
    }

    int deleteMax() {
        if (size < 1) {
            cout << "Heap is empty" << endl;
            return -1;
        }

        int x = heap[1];
        int k = heap[size];
        heap[size] = x;
        size--;

        int i = 1, j = 2;

        // Heapify down
        while (j <= size) {
            if (j < size && heap[j] < heap[j + 1])
                j++;
            if (k >= heap[j])
                break;

            heap[i] = heap[j];
            i = j;
            j = j * 2;
        }

        heap[i] = k;
        return x;
    }

    void heapSort() {
        int originalSize = size;
        cout << "\nSorted elements:\n";
        while (size > 0) {
            int max = deleteMax();
            cout << max << " ";
        }
        cout << "\n";
        size = originalSize;
    }

    void displayHeap() {
        for (int i = 1; i <= size; i++) {
            cout << heap[i] << " ";
        }
        cout << "\n";
    }

    ~MaxHeap() {
        delete[] heap;
    }
};

// ---------------- Main Function ----------------
int main() {
    int num, value;

    cout << "Enter number of elements: ";
    cin >> num;

    MaxHeap h(num);

    cout << "Enter elements:\n";
    for (int i = 0; i < num; i++) {
        cin >> value;
        h.insert(value);
    }

    cout << "\nHeap array:\n";
    h.displayHeap();

    h.heapSort();

    cout << "\nSorted array:\n";
    h.displayHeap();

    return 0;
}
