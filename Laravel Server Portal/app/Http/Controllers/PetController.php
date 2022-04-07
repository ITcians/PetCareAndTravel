<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;
use App\Models\Cleaning;
use App\Models\Hygiene;
use App\Models\Master;
use App\Models\User;
use App\Models\Notifications;
use App\Models\Payments;
use App\Models\Pets;
use App\Models\Records;
use App\Models\Sales;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use SebastianBergmann\CodeCoverage\Driver\Driver;
use stdClass;

class PetController extends Controller
{
    //


    function getHygieneReports(Request $rq)
    {
        $petId = $rq->input("petId");

        return DryControl::okResponse([
            'message' => "Hygiene Reports",
            "reports" => Hygiene::where('petId', $petId)->latest()->get()
        ]);
    }

    function petList(Request $req)
    {
        return view('pets.list', ['pets' => Pets::latest()->paginate(15)]);
    }

    function getPetsInJSON(Request $req)
    {
        return Pets::latest()->simplePaginate(20);
    }
    function addPet(Request $req)
    {
        try {

            $user = DryControl::getUserViaToken($req->bearerToken());
            if (!$user)
                return DryControl::okResponse(["Error" => "Unauthenticated request!",]);

            $data = $req->input();
            $pet = new Pets();
            $pet->petName = $data['petName'];
            $pet->petAge = $data['petAge'];
            $pet->petSpeice = $data['petSpecie'];
            $pet->petGender = $data['petGender'];
            $pet->petPrice = $data['petPrice'];
            $pet->petHeight = $data['petHeight'];
            $pet->petWeight = $data['petWeight'];
            $pet->petLat = $data['petLat'];
            $pet->petAddress = $data['petAddress'];
            $pet->petLang = $data['petLang'];
            $pet->petContactNo = $data['petContactNo'];
            $pet->petPhoto = DryControl::uploadProfileImage(base64_decode($data['petImage']));
            $pet->save();
            $master = new Master();
            $master->userId = $user->id;
            $master->petId = $pet->id;
            $master->save();
            return DryControl::okResponse(['Message' => "Pet added successfully!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(["Error" => $ex->getMessage()]);
        }
    }

    public function update(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();


            $data = $req->input();
            if (isset($data['petImage'])) {
                //update image first
                $data['petImage'] = DryControl::uploadProfileImage(base64_decode($data['petImage']));
                Pets::where('petId', $data['petId'])->update([
                    'petPhoto' => $data['petImage']
                ]);
            }

            if (isset($data['petName'])) {
                Pets::where('petId', $data['petId'])->update([
                    'petName' => $data['petName']
                ]);
            }

            if (isset($data['petContactNo'])) {
                Pets::where('petId', $data['petId'])->update([
                    'petContactNo' => $data['petContactNo']
                ]);
            }

            if (isset($data['petWeight'])) {

                Pets::where('petId', $data['petId'])->update([
                    'petWeight' => $data['petWeight']
                ]);
            }

            if (isset($data['petHeight'])) {

                Pets::where('petId', $data['petId'])->update([
                    'petHeight' => $data['petHeight']
                ]);
            }
            if (isset($data['petPrice'])) {

                Pets::where('petId', $data['petId'])->update([
                    'petPrice' => $data['petPrice']
                ]);
            }
            if (isset($data['petAge'])) {

                Pets::where('petId', $data['petId'])->update([
                    'petAge' => $data['petAge']
                ]);
            }
            if (isset($data['petSpecie'])) {

                Pets::where('petId', $data['petId'])->update([
                    'petSpeice' => $data['petSpecie']
                ]);
            }

            // if (isset($data['petLat']) && isset($data['petLang'])) {

            //     Pets::where('petId', $data['petId'])->update([
            //     'petLat'=>$data['petLat'],
            //     'petLang'=>$data['petLang']
            //     ]);
            // }

            return DryControl::okResponse(['Message' => "Pet updated!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }

    public function delete(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();

            Pets::where('petId', $req->input('petId'))->delete();
            Master::where('petId', $req->input('petId'))->delete();
            return DryControl::okResponse(['Message' => "Pet deleted!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => "Server internal error try again later!"]);
        }
    }

    public function addOrder(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();

            $data = $req->input();
            $sale = new Sales();
            $sale->pickUpLat = $data['pickUpLat'];
            $sale->pickUpLang = $data['pickUpLang'];
            $sale->dropOfLat = $data['dropOfLat'];
            $sale->dropOfLang = $data['dropOfLang'];
            $sale->saleStatus = 1;
            $sale->saleFees = $data['saleFess'];
            $sale->save();

            $payment = new Payments();
            $payment->payment = doubleval($data["petPrice"]) + doubleval($data['saleFess']);
            $payment->paymentMethod = $data['paymentMethod'];
            //TODO: if payment method is selected online then charge
            $payment->paymentStatus = $data['paymentStatus'];
            $payment->save();

            $master = new Master();
            $master->userId = $user->id;
            $master->petId = $data['petId'];
            $master->paymentId = $payment->id;
            $master->saleId = $sale->id;
            $master->save();
            $auther = Master::select('userId')->where('petId', $data['petId'])->get()[0];
            $pet = Pets::select('petName')->where('petId', $data['petId'])->get()[0];
            //TODO : Send notification
            $not = new Notifications();
            $not->notification = "You have received and order for your pet " . $pet->petName;
            $not->notificationFor = $auther->userId;
            $not->notificationFrom = $user->id;
            $not->save();
            return DryControl::okResponse(['Message' => "Thank you! Your order has been placed!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }

    public function addHygiene(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            DryControl::sendNotification("Testing", "The body", $user->fcm);
            $data = $req->input();
            $hygiene = new Hygiene();
            $hygiene->hygieneType = $data['hygieneType'];
            $hygiene->petId = $data['petId'];
            $hygiene->save();

            return DryControl::okResponse(['Message' => "Hygiene updated successfully! " . $hygiene->id]);
        } catch (Exception $ex) {

            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }

    public function addRecord(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();
            $record = new Records();
            $record->recordType = $req->input('recordType');
            $record->save();
            $master = new Master();
            $master->petId = $req->input('petId');
            $master->recordId = $record->id;
            $master->save();
            return DryControl::okResponse(['Message' => "Recrods updated!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => "Server internal error try again later!"]);
        }
    }
    public function addCleaning(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();
            $clean = new Cleaning();
            $clean->cleanType = $req->input('cleanType');
            $clean->save();
            $master = new Master();
            $master->petId = $req->input('petId');
            $master->cleanId = $clean->id;
            $master->save();
            return DryControl::okResponse(['Message' => "Cleaning updated!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }
    public function sales(Request $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();

            $data = $req->input();
            $sale = new Sales();
            $sale->pickUpLat = $data['pickUpLat'];
            $sale->pickUpLang = $data['pickUpLang'];
            $sale->dropOfLat = $data['dropOfLat'];
            $sale->dropOfLang = $data['dropOfLang'];
            $sale->saleStatus = 0;
            $sale->saleFees = $data['saleFees'];
            $sale->save();

            return DryControl::okResponse(['Message' => "Sales Updated!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }
    public function payment(REQUEST $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();

            $data = $req->input();
            $payment = new Payments();
            $payment->payment = doubleval($data["petPrice"]) + doubleval($data['saleFees']);
            $payment->paymentMethod = $data['paymentMethod'];
            $payment->paymentStatus = $data['paymentStatus'];
            $payment->save();

            return DryControl::okResponse(['Message' => "Payment Updated!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }
    public function addnews(REQUEST $req)
    {
        try {
            $user = DryControl::getUserViaToken($req->bearerToken());
            if ($user == null)
                return DryControl::unAuthRequest();

            $data = $req->input();
            $payment = new News();
            // $payment->payment = doubleval($data["petPrice"]) + doubleval($data['saleFees']);
            // $payment->paymentMethod = $data['paymentMethod'];
            // $payment->paymentStatus = $data['paymentStatus'];
            // $payment->save();

            return DryControl::okResponse(['Message' => "Pet News Created!"]);
        } catch (Exception $ex) {
            return DryControl::okResponse(['Error' => $ex->getMessage()]);
        }
    }

    public function viewsales()
    {
        return view('pets.sales', ['sales' => Sales::latest()->paginate(15)]);
    }

    public function getPets(Request $req)
    {
        $user = DryControl::getUserViaToken($req->bearerToken());
        if ($user == null)
            return DryControl::unAuthRequest();

        $pets = DB::table("masters")
            ->select(
                "pets.petId",
                "petName",
                "petAddress",
                'petLat',
                'petLang',
                'petHeight',
                'petWeight',
                'petSpeice',
                'petAge',
                'petGender',
                'petPrice',
                'petPhoto',
                'petContactNo'
            )
            ->join("pets", 'pets.petId', 'masters.petId')
            ->where('petPrice','>',0)
            ->where('saleId',null)
            ->where('paymentId',null)
            ->where('hygieneId',null)
            ->where('recordId',null)
            ->where('cleanId',null)
            ->where("userId", "!=", $user->id)->get();
        return $pets;
    }

    public function getPet(Request $req)
    {

        $user = DryControl::getUserViaToken($req->bearerToken());

        if ($user == null)
            return DryControl::unAuthRequest();

        try {
            return DB::table("masters")->join('pets', 'pets.petId', 'masters.petId')
                ->where('userId', $user->id)
                ->where('paymentId', null)->get();
        } catch (Exception $ex) {
            return $ex->getMessage();
        }
    }

    public function getUserOrders(Request $req)
    {
        $user = DryControl::getUserViaToken($req->bearerToken());

        if ($user == null)
            return DryControl::unAuthRequest();

        return DB::table('masters')
            ->select(
                'petName',
                'paymentStatus',
                'saleFees',
                'payment',
                'sales.created_at',
                'saleStatus',
                'sales.saleId'
            )
            ->join("pets", "pets.petId", "=", "masters.petId")
            ->join("payments", "payments.paymentId", "=", "masters.paymentId")
            ->join("sales", "sales.saleId", "=", "masters.saleId")
            ->where('userId', $user->id)
            ->orderBy('sales.created_at')
            ->get();
    }

    public function orderComplete(Request $req)
    {

        $userId = Master::select('petId', 'userId')->where('saleId', $req->input('orderId'))->first();

        Sales::where('saleId', $req->input('orderId'))->update([
            'saleStatus' => 0
        ]);
        Payments::where('paymentId', $req->input('paymentId'))
            ->update(['paymentStatus' => "Approved"]);


        Master::where('petId', $userId->petId)->update(['userId' => $userId->userId]);
        DryControl::generateNotification(
            Auth::user()->id,
            $userId->userId,
            "Your order has been Accpet by Pet CAT team! It will be delivered soon"
        );
        return back()->with(['success' => "Order completed!"]);
    }

    public function orderCancel(Request $req)
    {
     
        $user = DryControl::getUserViaToken($req->bearerToken());

        if ($user == null)
            return DryControl::unAuthRequest();

            $payment=Master::select('paymentId')->where("saleId",$req->input('orderId'))->get()->first();
        Sales::where('saleId', $req->input('orderId'))->delete();
        Payments::where('paymentId', $payment->paymentId)->delete();

        DryControl::generateNotification(
            $user->id,
            1,
            "User cancel the order!"
        );
        Master::where('saleId', $req->input('orderId'))->delete();

        return DryControl::okResponse(["Message"=>"Order cancel!"]);
    }

    public function orderDelete(Request $req)
    {
        Sales::where('saleId', $req->input('orderId'))->delete();
        Payments::where('paymentId', $req->input('paymentId'))->delete();
        $userId = Master::where('saleId', $req->input('orderId'))->first();
        DryControl::generateNotification(
            Auth::user()->id,
            $userId->userId,
            "Your order has been reject by Pet CAT team!"
        );
        Master::where('saleId', $req->input('orderId'))->delete();

        return back()->with(['success' => "Order deleted!"]);
    }
    public function orders()
    {
        $data = DB::table('masters')
            ->join("users", "users.id", "=", "masters.userId")
            ->join("pets", "pets.petId", "=", "masters.petId")
            ->join("payments", "payments.paymentId", "=", "masters.paymentId")
            ->join("sales", "sales.saleId", "=", "masters.saleId")
            ->orderBy('saleStatus', 'desc')
            ->paginate(15);

        return view('pets.orders', ["orders" => $data]);
    }
}
