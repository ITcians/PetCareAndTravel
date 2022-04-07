<?php

namespace App\Http\Controllers;

use App\Models\Notifications;
use App\Models\User;
use Exception;
use Illuminate\Http\Request;

class DryControl extends Controller
{

    public static function sendNotification($title, $notification, $token)
    {
        $url = "https://fcm.googleapis.com/fcm/send";
        $server = "AAAAUiFAG9M:APA91bG0Pehyc9AL-ceLy2MeQbsXSrswz5qrHx95d370S7hILYtxTaRhGSM59TSvqZEiveWGYE11GLs6kY8J6pCai29l6qDQC06I0uxYkX-9-fYdLvZnE3UQwtz6FvE0v7rE-C5V8gw4";

        // Compile headers in one variable
        $headers = array(
            'Authorization:key=' . $server,
            'Content-Type:application/json'
        );

        // Add notification content to a variable for easy reference
        $notifData = [
            'title' => $title,
            'body' => $notification,
            'click_action' => "android.intent.action.MAIN"
        ];

        // Create the api body
        $apiBody = [
            'notification' => $notifData,
            'data' => $notifData,
            'to' => "$token"
        ];

        // Initialize curl with the prepared headers and body
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($apiBody));

        // Execute call and save result
        $result = curl_exec($ch);

        // Close curl after call
        curl_close($ch);

        return $result;
    }
    public static function okResponse($response)
    {
        return response()->json($response, 200);
    }

    public static function errorResponse($response)
    {
        return response()->json($response, 401);
    }

    public static function uploadProfileImage($file)
    {
        if ($file == null)
            return null;
        try {
            //upload image
            $folder = 'profiles';
            $path = public_path("images") . "/$folder/";
            if (!file_exists($path))
                mkdir($path, 0777, true);

            $img = uniqid() . ".png";
            file_put_contents($path . "" . $img, $file);

            return $folder . "/" . $img;
        } catch (Exception $ex) {
            return null;
        }
    }

    public static function getUserViaToken($token)
    {
        $user = User::where('token', $token)->get();
        if (sizeof($user) > 0)
            return $user[0];

        return null;
    }
    public static function unAuthRequest()
    {
        return response()->json(['Error' => "Unauthenticated request!"]);
    }

    public static function generateNotification($from, $to, $msg)
    {
        $n = new Notifications();
        $n->notification = $msg;
        $n->notificationFor = $to;
        $n->notificationFrom = $from;
        $n->save();
        return true;
    }

}
