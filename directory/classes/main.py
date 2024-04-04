import time

def crack_pin(target_pin):
    # Start timing
    start_time = time.time()

    # List of common PINs based on research or known defaults
    common_pins = ['1234', '1111', '0000', '1212', '7777', '1004', '2000', '4444', '2222', '6969']

    # First, try the common PINs
    for pin in common_pins:
        print(f"Trying common PIN: {pin}")
        if pin == target_pin:
            duration = time.time() - start_time
            return f"PIN cracked with common PIN: {pin} in {duration:.2f} seconds"

    # If common PINs don't work, proceed with brute force
    for i in range(10000):
        pin = str(i).zfill(4)  # Ensure the PIN has 4 digits
        if pin in common_pins:  # Skip if this PIN was already tried in common PINs
            continue
        print(f"Trying PIN: {pin}")
        if pin == target_pin:
            duration = time.time() - start_time
            return f"PIN cracked with brute force: {pin} in {duration:.2f} seconds"

    duration = time.time() - start_time
    return f"PIN not cracked. Total time: {duration:.2f} seconds."

# Replace '5687' with a variable or user input for different scenarios
print(crack_pin('5687'))
