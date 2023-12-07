<?php
include 'db_connect.php'; // Make sure you have this file for database connection

// Check if the request is a POST request
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get data from POST request
    $name = $_POST['name'];
    $email = $_POST['email'];
    $username = $_POST['username'];
    $password = $_POST['password']; // Consider encrypting the password
    $country = $_POST['country'];
    $city = $_POST['city'];
    $phone = $_POST['phone'];

    // Check if email already exists in the database
    $checkEmail = $conn->prepare("SELECT email FROM users WHERE email = ?");
    $checkEmail->bind_param("s", $email);
    $checkEmail->execute();
    $checkEmailResult = $checkEmail->get_result();

    if ($checkEmailResult->num_rows > 0) {
        echo "Email already exists";
    } else {
        // Insert the user data into the database without cover and profile photo URLs
        $stmt = $conn->prepare("INSERT INTO users (name, email, username, password, country, city, phone) VALUES (?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("sssssss", $name, $email, $username, $password, $country, $city, $phone);

        if ($stmt->execute()) {
            echo "User registered successfully";
        } else {
            echo "Error: " . $stmt->error;
        }

        $stmt->close();
    }

    $checkEmail->close();
}
$conn->close();
?>
