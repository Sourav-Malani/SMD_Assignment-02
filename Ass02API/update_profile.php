<?php
include 'db_connect.php'; // Include your database connection script

// Check if the request is a POST request
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get data from POST request
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password']; // Consider encrypting the password

    // Perform the database update operation here
    // Replace 'your_table_name' with the actual name of your user table
    $sql = "UPDATE users SET name=?, email=?, password=? WHERE user_id=?";

    // Prepare the SQL statement
    $stmt = $conn->prepare($sql);

    // Bind parameters
    $stmt->bind_param("sssi", $name, $email, $password, $user_id);

    // Execute the statement
    if ($stmt->execute()) {
        // Update successful
        echo json_encode(array("message" => "Profile updated successfully", "status" => true));
    } else {
        // Error in updating profile
        echo json_encode(array("message" => "Failed to update profile", "status" => false));
    }

    // Close the statement
    $stmt->close();
}

// Close the database connection
$conn->close();
?>