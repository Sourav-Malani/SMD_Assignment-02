<?php
include 'db_connect.php';

$uploadPath = 'uploads/';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_FILES['image']['name']) && isset($_POST['imageType']) && isset($_POST['userEmail'])) {
        $imageName = $_FILES['image']['name'];
        $imageType = $_POST['imageType'];
        $userEmail = $_POST['userEmail'];
        $tempName = $_FILES['image']['tmp_name'];

        $imageExt = strtolower(pathinfo($imageName, PATHINFO_EXTENSION));
        $newImageName = time() . '_' . uniqid() . '.' . $imageExt;

        if (move_uploaded_file($tempName, $uploadPath . $newImageName)) {
            $imageUrl = 'http://192.168.18.114/Ass02API/' . $uploadPath . $newImageName;

            $stmt = $conn->prepare("UPDATE users SET profile_photo_url = ? WHERE email = ?");
            $stmt->bind_param("ss", $imageUrl, $userEmail);
            $stmt->execute();

            $response['status'] = 'success';
            $response['message'] = 'File uploaded successfully';
            $response['imageUrl'] = $imageUrl;
        } else {
            $response['status'] = 'error';
            $response['message'] = 'Failed to upload file';
        }
    } else {
        $response['status'] = 'error';
        $response['message'] = 'Not all parameters are set';
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Invalid request';
}

echo json_encode($response);
?>
