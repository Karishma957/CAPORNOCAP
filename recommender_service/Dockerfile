# Use Python 3.10 slim as base
FROM python:3.10-slim

# Set work directory
WORKDIR /app

# Install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy app code
COPY app.py .
COPY model.py .

# Expose Flask port
EXPOSE 8000

# Start Flask app
CMD ["python", "app.py"]
