function appendValue(value) {
    const display = document.getElementById("display");
    display.value += value;
}

function clearDisplay() {
    document.getElementById("display").value = "";
}

function calculateResult() {
    const display = document.getElementById("display");
    let expression = display.value;

    if (expression === "") {
        alert("Please enter a value first.");
        return;
    }

    try {
        let result = eval(expression);

        if (!isFinite(result)) {
            alert("Invalid calculation (division by zero or undefined result).");
            clearDisplay();
            return;
        }

        display.value = result;
    } catch (error) {
        alert("Invalid expression. Please check your input.");
        clearDisplay();
    }
}

function square() {
    const display = document.getElementById("display");
    let value = display.value;

    if (value === "") {
        let userInput = prompt("Enter a number to square:");

        if (userInput === null || userInput.trim() === "") {
            alert("No input provided.");
            return;
        }

        if (isNaN(userInput)) {
            alert("Invalid input! Please enter a valid number.");
            return;
        }

        display.value = userInput * userInput;
    } else {
        if (isNaN(value)) {
            alert("Invalid input! Please enter a valid number.");
            clearDisplay();
            return;
        }

        display.value = value * value;
    }
}
