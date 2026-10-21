
import random
import string
from PIL import Image, ImageDraw, ImageFont

# Generate random CAPTCHA text
characters = string.ascii_uppercase + string.digits
captcha = ''.join(random.choices(characters, k=6))

# Create a blank image
img = Image.new("RGB", (300, 100), color="white")
draw = ImageDraw.Draw(img)

# Load font
try:
    font = ImageFont.truetype("arial.ttf", 40)
except OSError:
    font = ImageFont.load_default()

# Draw CAPTCHA text
draw.text((40, 25), captcha, fill="blue", font=font)

# Add random noise lines
for i in range(6):
    x1 = random.randint(0, 300)
    y1 = random.randint(0, 100)
    x2 = random.randint(0, 300)
    y2 = random.randint(0, 100)
    draw.line((x1, y1, x2, y2), fill="gray", width=1)

# Save and display CAPTCHA image
img.save("captcha.png")
img.show()

print("CAPTCHA image generated successfully!")

# Verify CAPTCHA
user_input = input("Enter the CAPTCHA shown in the image: ")

if user_input.strip().upper() == captcha:
    print("CAPTCHA Verified Successfully!")
else:
    print("Invalid CAPTCHA! Please try again.")

print("Correct CAPTCHA was:", captcha)
