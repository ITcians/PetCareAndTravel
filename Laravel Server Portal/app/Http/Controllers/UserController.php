<?php

namespace App\Http\Controllers;

use App\Models\Notifications;
use App\Models\User;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use stdClass;

class UserController extends Controller
{
    function index()
    {
        return view('users.index', ['users' => User::all()]);
    }

    public function appSignUp(Request $req)
    {
        try {

            if (sizeof(User::where('email', $req->input('email'))->get()) > 0)
                return DryControl::okResponse("This email already exists!");

            $user = new User();
            $data = $req->input();
            $user->name = $data['name'];
            $user->email = $data['email'];
            $user->password = Hash::make($data['password']);
            $user->accountType = $data['accountType'];
            $user->fcm = $data['fcm'];
            $user->token = Hash::make(uniqid());
            //upload photo
            if (isset($data["photo"]))
                $user->photo = DryControl::uploadProfileImage(base64_decode($data["photo"]));

            $user->save();
            $res = new stdClass;
            $res->Token = $user->token;
            $res->Photo = $user->photo;
            $res->Message = "User has been created";
            return DryControl::okResponse($res);
        } catch (Exception $ex) {
            return DryControl::okResponse($ex->getMessage());
        }
    }

    public function appLogin(Request $req)
    {
        $data = User::where('email', $req->input('email'))->get();
        if (sizeof($data) > 0) {
            //user found 
            $user = $data[0];
            if (Hash::check($req->input('pass'), $user->password)) {
                unset($user->password);
                unset($user->id);
                return DryControl::okResponse($user);
            } else
                return DryControl::okResponse(["Error" => "Your password didn't matched!"]);
        } else
            return DryControl::okResponse(["Error" => "No user found with that email!"]);
    }

    public function update(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();

            $photo = null;
            $data = $req->input();
            //upload photo
            if (isset($data["photo"])) {
                $photo = DryControl::uploadProfileImage(base64_decode($data["photo"]));
                User::where('id', $user->id)->update([
                    'photo' => $photo
                ]);
            }


            if (isset($data["name"]))
                User::where('id', $user->id)->update([
                    'name' => $data['name'],
                ]);

            if (isset($data["password"]))
                User::where('id', $user->id)->update([
                    'pasword' => Hash::make($data['password']),
                ]);
            if ($photo != null)
                return DryControl::okResponse(["Photo" => $photo]);
            return DryControl::okResponse(['Message' => "User updated!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse($ex->getMessage());
        }
    }


    public function updateFCM(Request $req)
    {
        $user = DryControl::getUserViaToken($req->bearerToken());
        User::where('id', $user->id)->update(['fcm' => $req->input('fcm')]);
        return DryControl::okResponse(["message" => "FCM UPDATED"]);
    }

    public function delete(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();

            User::where('id', $req->input('id'))->delete();
            return DryControl::okResponse(['Message' => "User deleted!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }

    function getUserNotifications(Request $req)
    {
        $user = DryControl::getUserViaToken($req->bearerToken());
        if ($user == null)
            return DryControl::unAuthRequest();

        $not = DB::table("notifications")
            ->select(
                'notificationId',
                'notification',
                'name',
                'notifications.created_at'
            )
            ->join('users', 'users.id', 'notifications.notificationFrom')
            ->where("notificationStatus", 1)
            ->where('notificationFor', $user->id)
            ->get();
        return $not;
    }

    function readNotification(Request $req)
    {
        Notifications::where('notificationId', $req->input("notificationId"))->update(['notificationStatus' => 0]);
        return DryControl::okResponse(["Message" => "Notification status updated!"]);
    }
}
