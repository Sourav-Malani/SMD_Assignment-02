<?php
include 'db_connect.php'; // Make sure you have this file for database connection

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $password = $_POST['password'];

    // Check if the user exists in the database
    $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    $user = $result->fetch_assoc();

    if ($user) {
        // Verify the password (use password_verify if you're hashing passwords)
        if ($user['password'] === $password) {
            // Return user data without cover and profile photo URLs
            echo json_encode(array(
                "status" => "success",
                "name" => $user['name'],
                "city" => $user['city'],
                "country" => $user['country'],
                "email" => $user['email'],
                "phone" => $user['phone'],
                "profile_photo_url" => $user['profile_photo_url'], // Retrieve profile photo URL from the database
                "cover_photo_url" => $user['cover_photo_url'], // Retrieve cover photo URL from the database
                // Add any other user data you need to send
            ));
        } else {
            echo json_encode(array("status" => "error", "message" => "Invalid Credentials"));
        }
    } else {
        echo json_encode(array("status" => "error", "message" => "User does not exist"));
    }

    $stmt->close();
}
$conn->close();
?>
